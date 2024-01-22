package com.dock.transferbanking.application.service.operacao;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;

import java.math.BigDecimal;

public abstract class OperacaoTipo {

    public static String INVALID_OPERATION = "operação inválida";

    public abstract OperacaoDomain executar (BigDecimal valor,
                                             ContaDomain contaDomain, BigDecimal montanteSaqueDiario) throws InternalServerErrorException;

    public boolean isStatusContaValido(ContaDomain contaDomain) {
        return (contaDomain.isAtiva() || !contaDomain.isBloqueada());
    }
}
