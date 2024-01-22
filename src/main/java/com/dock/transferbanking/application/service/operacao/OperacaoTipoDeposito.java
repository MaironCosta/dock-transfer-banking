package com.dock.transferbanking.application.service.operacao;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacaoTipoDeposito extends OperacaoTipo {

    @Override
    public OperacaoDomain executar(BigDecimal valor,
                                   ContaDomain contaDomain, BigDecimal montanteSaqueDiario) throws InternalServerErrorException {

        if (!isStatusContaValido(contaDomain))
            throw new InternalServerErrorException(OperacaoTipo.INVALID_OPERATION);

        OperacaoDomain op = new OperacaoDomain("deposito",
                valor, contaDomain);
        op.setData(LocalDateTime.now());
        return op;
    }
}
