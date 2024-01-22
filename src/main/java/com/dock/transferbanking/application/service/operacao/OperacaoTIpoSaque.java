package com.dock.transferbanking.application.service.operacao;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacaoTIpoSaque extends OperacaoTipo {

    private final BigDecimal DAILY_LIMIT = new BigDecimal(2000);

    @Override
    public OperacaoDomain executar(BigDecimal valor, ContaDomain contaDomain, BigDecimal montanteSaqueDiarioEfetuado) throws InternalServerErrorException {

        if (!super.isStatusContaValido(contaDomain))
            throw new InternalServerErrorException(OperacaoTipo.INVALID_OPERATION);

        if (ObjectUtils.isEmpty(montanteSaqueDiarioEfetuado)) {
            montanteSaqueDiarioEfetuado = BigDecimal.ZERO;
        }

        BigDecimal newTotalSaqueDiario = montanteSaqueDiarioEfetuado.add(valor);
        if (newTotalSaqueDiario.compareTo(DAILY_LIMIT) > 0)
            throw new InternalServerErrorException(OperacaoTipo.INVALID_OPERATION);

        BigDecimal newSaldo = contaDomain.getSaldo().subtract(valor);
        if (newSaldo.doubleValue() < 0) {
            throw new InternalServerErrorException(OperacaoTipo.INVALID_OPERATION);
        }

        OperacaoDomain operacaoDomain = new OperacaoDomain("saque",
                valor, contaDomain);
        operacaoDomain.setData(LocalDateTime.now());

        return operacaoDomain;
    }
}