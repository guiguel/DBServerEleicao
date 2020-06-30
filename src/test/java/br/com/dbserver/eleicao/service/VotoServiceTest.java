package br.com.dbserver.eleicao.service;

import br.com.dbserver.eleicao.service.entity.Profissional;
import br.com.dbserver.eleicao.service.entity.Restaurante;
import br.com.dbserver.eleicao.service.entity.Voto;
import br.com.dbserver.eleicao.service.exception.BusinessException;
import br.com.dbserver.eleicao.service.exception.Message;
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


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DbserverEleicaoApplication.class)
public class VotoServiceTest {

    @Autowired
    private VotoService votoService;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private RestauranteService restauranteService;

    private List<Restaurante> restaurantes;

    private List<Profissional> profissionais;


    @BeforeEach
    public void setUp() {
        restaurantes = new ArrayList<>();
        restaurantes.add(restauranteService.save(Restaurante.builder().nome("Restaurante 1").build()));
        profissionais = new ArrayList<>();
        profissionais.add(profissionalService.save(Profissional.builder().nome("Restaurante 1").build()));

        Voto voto = Voto.builder()
                .dataVoto(LocalDate.of(2020, Month.JUNE, 1))
                .profissional(profissionais.get(0))
                .restaurante(restaurantes.get(0))
                .build();

        votoService.vote(voto);
    }

    @Test
    public void testarVotarDiaNaoUtil() {
        final Voto voto = Voto.builder()
                .dataVoto(LocalDate.of(2020, Month.MAY, 30))
                .profissional(profissionais.get(0))
                .restaurante(restaurantes.get(0))
                .build();

        Assertions.assertThrows(BusinessException.class, () -> votoService.vote(voto), Message.DIA_NAO_UTIL.name());
    }

    @Test
    public void testarRepetirVoto() {
        final Voto voto = Voto.builder()
                .dataVoto(LocalDate.of(2020, Month.JUNE, 1))
                .profissional(profissionais.get(0))
                .restaurante(restaurantes.get(0))
                .build();

        Assertions.assertThrows(BusinessException.class, () -> votoService.vote(voto), Message.PROFISSIONAL_JA_VOTOU_NESTE_DIA.name());
    }

}
