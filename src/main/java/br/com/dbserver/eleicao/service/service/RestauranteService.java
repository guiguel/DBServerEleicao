package br.com.dbserver.eleicao.service.service;

import br.com.dbserver.eleicao.service.entity.Restaurante;
import br.com.dbserver.eleicao.service.repository.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteService {

    private RestauranteRepository restauranteRepository;

    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante save(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public List<Restaurante> findAll() {
        return restauranteRepository.findAll();
    }

}
