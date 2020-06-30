package br.com.dbserver.eleicao.service.repository;

import br.com.dbserver.eleicao.service.entity.Apuracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ApuracaoRepository extends JpaRepository<Apuracao, Long> {

    List<Apuracao> findByDataBetween(LocalDate startData, LocalDate endData);
}
