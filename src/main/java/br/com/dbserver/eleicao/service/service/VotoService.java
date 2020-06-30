package br.com.dbserver.eleicao.service.service;

import br.com.dbserver.eleicao.service.entity.Voto;
import br.com.dbserver.eleicao.service.exception.BusinessException;
import br.com.dbserver.eleicao.service.exception.Message;
import br.com.dbserver.eleicao.service.repository.VotoRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class VotoService {

    private VotoRepository votoRepository;
    private DiaUtilService diaUtilService;

    public VotoService(VotoRepository votoRepository, DiaUtilService diaUtilService) {
        this.votoRepository = votoRepository;
        this.diaUtilService = diaUtilService;
    }

    public VotoRepository getVotoRepository() {
        return votoRepository;
    }

    public void setVotoRepository(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    public DiaUtilService getDiaUtilService() {
        return diaUtilService;
    }

    public void setDiaUtilService(DiaUtilService diaUtilService) {
        this.diaUtilService = diaUtilService;
    }

    public Voto vote(Voto voto) {
        diaUtilService.validar(voto.getDataVoto());
        validarVotoRepetido(voto);
        return votoRepository.save(voto);
    }

    private void validarVotoRepetido(Voto voto) {
        final Example<Voto> example = Example.of(Voto.builder().profissional(voto.getProfissional()).dataVoto(voto.getDataVoto()).build());
        Optional.of(example)
                .filter(Predicate.not(votoRepository::exists))
                .orElseThrow(() -> new BusinessException(Message.PROFISSIONAL_JA_VOTOU_NESTE_DIA));
    }

    public List<Voto> findAll() {
        return votoRepository.findAll();
    }

    public List<Voto> findByDataVoto(LocalDate dataVoto) {
        return votoRepository.findByDataVoto(dataVoto);
    }

}