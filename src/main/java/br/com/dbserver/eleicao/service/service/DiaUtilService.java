package br.com.dbserver.eleicao.service.service;

import br.com.dbserver.eleicao.service.exception.BusinessException;
import br.com.dbserver.eleicao.service.exception.Message;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalQuery;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class DiaUtilService {

    public void validar(LocalDate localDate) {
        TemporalQuery<Boolean> fds = temporal -> {
            DayOfWeek dayOfWeek = DayOfWeek.from(temporal);
            return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        };
        Optional.of(fds)
                .filter(Predicate.not(localDate::query))
                .orElseThrow(() -> new BusinessException(Message.DIA_NAO_UTIL));

    }
}
