package com.dock.transferbanking.application.service.operacao;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.port.output.IOperacaoRepositoryDatabase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperacaoService implements OperacoaoUserCase {

    public static String INVALID_DATA = "tipo operação ou valor número inválido";

    private final IOperacaoRepositoryDatabase operacaoRepositoryDatabase;

    private final Map<String, OperacaoTipo> tipo;

    public OperacaoService(IOperacaoRepositoryDatabase operacaoRepositoryDatabase) {
        this.operacaoRepositoryDatabase = operacaoRepositoryDatabase;

        tipo = new HashMap<>();
        tipo.put("deposito", new OperacaoTipoDeposito());
        tipo.put("saque", new OperacaoTIpoSaque());
    }

    @Override
    public OperacaoDomain executar(OperacaoTipo operacao,
                                   BigDecimal valor,
                                   ContaDomain contaDomain) throws InternalServerErrorException {

        BigDecimal montanteSaqueDiario = operacaoRepositoryDatabase.getMontanteSaqueDiario(contaDomain.getId());
        OperacaoDomain operacaoDomain = operacao.executar(valor, contaDomain, montanteSaqueDiario);

        operacaoDomain = operacaoRepositoryDatabase.save(operacaoDomain);

        return operacaoDomain;
    }

    @Override
    public OperacaoTipo getOperacaoTipo(String tipo) throws InternalServerErrorException {

        OperacaoTipo operacaoTipo = this.tipo.get(tipo);
        if (ObjectUtils.isEmpty(operacaoTipo))
            throw new InternalServerErrorException(OperacaoService.INVALID_DATA);

        return operacaoTipo;
    }

    @Override
    public BigDecimal getSaldo(Long contaID) {
        return operacaoRepositoryDatabase.getSaldo(contaID);
    }

    @Override
    public List<OperacaoDomain> getOperacaoByMesAno(Long contaID, Long mes, Long ano) {
        return operacaoRepositoryDatabase.getOperacaoByMesAno(contaID, mes, ano);
    }

}
