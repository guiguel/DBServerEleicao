package br.com.dbserver.eleicao.service;

import br.com.dbserver.eleicao.service.entity.Apuracao;
import br.com.dbserver.eleicao.service.entity.Profissional;
import br.com.dbserver.eleicao.service.entity.Restaurante;
import br.com.dbserver.eleicao.service.entity.Voto;
import br.com.dbserver.eleicao.service.exception.BusinessException;
import br.com.dbserver.eleicao.service.exception.Message;
import br.com.dbserver.eleicao.service.service.ApuracaoService;
import br.com.dbserver.eleicao.service.service.ProfissionalService;
import br.com.dbserver.eleicao.service.service.RestauranteService;
import br.com.dbserver.eleicao.service.service.VotoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DbserverEleicaoApplication.class)
public class ApuracaoServiceTest {

    @Autowired
    private VotoService votoService;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ApuracaoService apuracaoService;

    private List<Restaurante> restaurantes;

    private List<Profissional> profissionais;


    @BeforeEach
    public void setUp() {
        restaurantes = new ArrayList<>();
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 1").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 2").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 3").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 4").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 5").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 6").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 7").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 8").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 9").build()));
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 10").build()));
        profissionais = new ArrayList<>();
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 1").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 2").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 3").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 4").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 5").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 6").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 7").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 8").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 9").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 10").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 11").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 12").build()));
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 13").build()));

        //Carrega todos os votos da semana.
        Random rand = new Random();
        for (int day = 1; day <= 5; day++) {
            final LocalDate now = LocalDate.of(2020, Month.JUNE, day);
            profissionais.forEach(profissional -> {
                votoService.vote(Voto.builder()
                        .dataVoto(now)
                        .profissional(profissional)
                        //escolhe um restaurante randomicamente entre os 10 cadastrados
                        .restaurante(restaurantes.get(rand.nextInt(9)))
                        .build());
            });
        }
    }

    @Test
    public void testarDiaNaoApuradoUtil() {
        Assertions.assertThrows(BusinessException.class, () -> apuracaoService.buscarApuracao(LocalDate.of(2020, Month.DECEMBER, 30)), Message.APURACAO_NAO_CONCLUIDA.name());
    }

    @Test
    public void testarApuracaoDiaRepetido() {
        final LocalDate monday = LocalDate.of(2020, Month.AUGUST, 3);
        votoService.vote(Voto.builder()
                .dataVoto(monday)
                .profissional(profissionais.get(0))
                //escolhe um restaurante randomicamente entre os 10 cadastrados
                .restaurante(restaurantes.get(0))
                .build());
        apuracaoService.apurar(monday);
        Assertions.assertThrows(BusinessException.class, () -> apuracaoService.apurar(monday), Message.APURACAO_JA_CONCLUIDA.name());
    }

    @Test
    public void testarApuracaoDaSemanaInteiraSemNenhumRestauranteRepetido() {
        for (int day = 1; day <= 5; day++) {
            final LocalDate now = LocalDate.of(2020, Month.JUNE, day);
            apuracaoService.apurar(now);
        }
        final LocalDate startData = LocalDate.of(2020, Month.JUNE, 1);
        final LocalDate endData = LocalDate.of(2020, Month.JUNE, 5);
        final List<Apuracao> apuracoes = apuracaoService.findByDataBetween(startData, endData);

        Assertions.assertNotNull(apuracoes);
        Assertions.assertEquals(5, apuracoes
                .stream()
                .map(Apuracao::getRestaurante)
                .map(Restaurante::getId)
                .distinct().count());
    }

}
