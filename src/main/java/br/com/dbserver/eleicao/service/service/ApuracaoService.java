package br.com.dbserver.eleicao.service.service;

import br.com.dbserver.eleicao.service.entity.Apuracao;
import br.com.dbserver.eleicao.service.entity.Restaurante;
import br.com.dbserver.eleicao.service.entity.Voto;
import br.com.dbserver.eleicao.service.exception.BusinessException;
import br.com.dbserver.eleicao.service.exception.Message;
import br.com.dbserver.eleicao.service.repository.ApuracaoRepository;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ApuracaoService {

    private ApuracaoRepository apuracaoRepository;
    private VotoService votoService;

    public ApuracaoService(ApuracaoRepository apuracaoRepository, VotoService votoService) {
        this.apuracaoRepository = apuracaoRepository;
        this.votoService = votoService;
    }

    public Apuracao buscarApuracao(LocalDate localDate) {
        final Example<Apuracao> example = Example.of(Apuracao.builder().data(localDate).build());
        return Optional.of(example)
                .filter(apuracaoRepository::exists)
                .map(apuracaoRepository::findOne)
                .orElseThrow(() -> new BusinessException(Message.APURACAO_NAO_CONCLUIDA))
                .get();
    }

    public List<Apuracao> findByDataBetween(LocalDate startData, LocalDate endData) {
        return apuracaoRepository.findByDataBetween(startData, endData);
    }

    /**
     * Todos os dias da semana às 11:40 é executado a apuração do dia
     */
    @Scheduled(cron = "40 11 * * 1-5 ?")
    public void apurar() {
        apurar(LocalDate.now());
    }

    public void apurar(LocalDate localDate) {
        final Example<Apuracao> example = Example.of(Apuracao.builder().data(localDate).build());
        Optional.of(example)
                .filter(Predicate.not(apuracaoRepository::exists))
                .orElseThrow(() -> new BusinessException(Message.APURACAO_JA_CONCLUIDA));
        final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        LocalDate monday = localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        final List<Apuracao> apurados = apuracaoRepository.findByDataBetween(monday, localDate);

        final List<Voto> votos = votoService.findByDataVoto(localDate);

        final Map<Restaurante, Long> collect = votos.stream()
                .collect(Collectors.groupingBy(Voto::getRestaurante, Collectors.counting()));

        //Remove os restaurantes apurados na semana
        apurados.forEach(apuracao -> collect.remove(apuracao.getRestaurante()));

        //Ordena pelo mais votado.
        final LinkedHashMap<Restaurante, Long> collectOrder = collect
                .entrySet()
                .stream()
                .sorted((Map.Entry.<Restaurante, Long>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        apuracaoRepository.save(Apuracao.builder().data(localDate).restaurante(collectOrder.keySet().stream().findFirst().orElseThrow()).build());
    }

}