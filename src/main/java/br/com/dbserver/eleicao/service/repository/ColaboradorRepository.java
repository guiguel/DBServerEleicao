package br.com.dbserver.eleicao.service.repository;

import br.com.dbserver.eleicao.service.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Profissional, Long>{

}
