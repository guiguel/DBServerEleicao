package br.com.dbserver.eleicao.service.service;

import br.com.dbserver.eleicao.service.entity.Profissional;
import br.com.dbserver.eleicao.service.repository.ColaboradorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfissionalService {

    private ColaboradorRepository colaboradorRepository;

    public ProfissionalService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    public Profissional save(Profissional profissional) {
        return colaboradorRepository.save(profissional);
    }
}
