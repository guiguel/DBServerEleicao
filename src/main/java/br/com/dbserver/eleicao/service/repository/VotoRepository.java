package br.com.dbserver.eleicao.service.repository;

import br.com.dbserver.eleicao.service.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    List<Voto> findByDataVoto(LocalDate dataVoto);

}
