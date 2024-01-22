package com.dock.transferbanking.application.port.output;

import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;

import java.math.BigDecimal;
import java.util.List;

public interface IOperacaoRepositoryDatabase {

    OperacaoDomain save (OperacaoDomain operacaoDomain) throws InternalServerErrorException;

    BigDecimal getMontanteSaqueDiario(Long contaID);

    BigDecimal getSaldo(Long contaID);

    List<OperacaoDomain> getOperacaoByMesAno(Long contaID, Long mes, Long ano);

}
