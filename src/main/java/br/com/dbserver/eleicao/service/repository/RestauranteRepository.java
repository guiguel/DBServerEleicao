package br.com.dbserver.eleicao.service.repository;

import br.com.dbserver.eleicao.service.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long>{

}
