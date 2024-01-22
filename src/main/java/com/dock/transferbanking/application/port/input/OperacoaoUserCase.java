package com.dock.transferbanking.application.port.input;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.service.operacao.OperacaoTipo;
import com.dock.transferbanking.exception.InternalServerErrorException;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OperacoaoUserCase {

    OperacaoDomain executar(OperacaoTipo operacaoTipo,
                            BigDecimal valor,
                            ContaDomain contaDomain) throws InternalServerErrorException;

    OperacaoTipo getOperacaoTipo(String tipo) throws InternalServerErrorException;

    BigDecimal getSaldo(Long contaID);

    List<OperacaoDomain> getOperacaoByMesAno(Long contaID, Long mes, Long ano);

}
