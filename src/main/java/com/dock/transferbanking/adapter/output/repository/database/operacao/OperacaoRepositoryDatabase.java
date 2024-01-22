package com.dock.transferbanking.adapter.output.repository.database.operacao;

import com.dock.transferbanking.adapter.output.repository.database.portador.PortadorRepositoryDatabase;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.port.output.IOperacaoRepositoryDatabase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OperacaoRepositoryDatabase implements IOperacaoRepositoryDatabase {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ERROR_TO_SAVE = "error ao salvar operação";

    private final IOperacaoDatabase operacaoDatabase;

    public OperacaoRepositoryDatabase(IOperacaoDatabase operacaoDatabase) {
        this.operacaoDatabase = operacaoDatabase;
    }

    @Override
    public OperacaoDomain save(OperacaoDomain operacaoDomain) throws InternalServerErrorException {

        try {

            OperacaoDatabase database = new OperacaoDatabase(operacaoDomain);
            database = operacaoDatabase.save(database);

            return database.toOperacaoDomain();

        } catch (Exception e) {

            logger.error(OperacaoRepositoryDatabase.ERROR_TO_SAVE,
                    "agencia", operacaoDomain.getContaDomain().getAgencia(),
                    "numero", operacaoDomain.getContaDomain().getNumero(),
                    "tipo", operacaoDomain.getTipo(),
                    "valor", operacaoDomain.getValor(),
                    "error", e.getMessage());

            throw new InternalServerErrorException(PortadorRepositoryDatabase.ERROR_TO_SAVE);
        }

    }

    public BigDecimal getMontanteSaqueDiario(Long contaID) {

        LocalDateTime now = LocalDateTime.now();
        Optional<BigDecimal> saque = operacaoDatabase.getMontanteOperacaoTipoDiario(now.getDayOfMonth(), now.getMonthValue(),
                now.getYear(), contaID, "saque");

        return saque.orElse(BigDecimal.ZERO);
    }

    public BigDecimal getSaldo(Long contaID) {
        Optional<BigDecimal> saldo = operacaoDatabase.getSaldo(contaID);
        return saldo.orElse(BigDecimal.ZERO);
    }

    @Override
    public List<OperacaoDomain> getOperacaoByMesAno(Long contaID, Long mes, Long ano) {

        List<OperacaoDatabase> operacoes = operacaoDatabase.getOperacaoByMesAno(contaID, mes, ano)
                .orElse(new ArrayList<>());

        List<OperacaoDomain> result = new ArrayList<>();
        operacoes.forEach((o) -> result.add(o.toOperacaoDomain()));

        return result;
    }
}
