package com.dock.transferbanking.application.service;


import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.port.output.IOperacaoRepositoryDatabase;
import com.dock.transferbanking.application.service.operacao.OperacaoService;
import com.dock.transferbanking.application.service.operacao.OperacaoTIpoSaque;
import com.dock.transferbanking.application.service.operacao.OperacaoTipo;
import com.dock.transferbanking.application.service.operacao.OperacaoTipoDeposito;
import com.dock.transferbanking.exception.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class OperacaoServiceTest {

    @Mock
    IOperacaoRepositoryDatabase operacaoRepositoryDatabase;

    @Mock
    OperacaoTipoDeposito operacaoTipoDeposito;

    OperacoaoUserCase operacoaoUserCase () {
        return new OperacaoService(operacaoRepositoryDatabase);
    }

    @Test
    public void executarDepositoShouldThrowInternalServerErrorException () {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {
            OperacoaoUserCase operacoaoUserCase = this.operacoaoUserCase();
            ContaDomain conta = new ContaDomain();
            conta.setAtiva(false);
            conta.setBloqueada(true);
            operacoaoUserCase.executar(new OperacaoTipoDeposito(), BigDecimal.TEN, conta);
        });

        Assertions.assertEquals(ex.getMessage(), OperacaoTipo.INVALID_OPERATION);

    }

    @Test
    public void executarDepositoShouldReturnSuccess () throws InternalServerErrorException {

        ContaDomain conta = new ContaDomain();
        conta.setAtiva(true);
        conta.setBloqueada(false);

        LocalDateTime now = LocalDateTime.now();

        OperacaoDomain fromExec = new OperacaoDomain("deposito",
                BigDecimal.TEN, conta);
        fromExec.setData(now);

        Mockito.when(operacaoTipoDeposito.executar(BigDecimal.TEN, conta, null)).
                thenReturn(fromExec);

        OperacaoDomain returnDB = new OperacaoDomain("deposito",
                BigDecimal.TEN, conta);
        returnDB.setId(1L);
        returnDB.setData(now);
        Mockito.when(operacaoRepositoryDatabase.save(fromExec)).thenReturn(returnDB);

        OperacoaoUserCase operacoaoUserCase = this.operacoaoUserCase();
        OperacaoDomain result = operacoaoUserCase.executar(operacaoTipoDeposito, BigDecimal.TEN, conta);

        Assertions.assertEquals(1L, result.getId());

    }

    @Test
    public void executarSaqueExcedeeSaqueDiarioShouldThrowInternalServerErrorException () {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {

            ContaDomain conta = new ContaDomain();
            conta.setAtiva(true);
            conta.setBloqueada(false);
            conta.setId(1L);

            Mockito.when(operacaoRepositoryDatabase.getMontanteSaqueDiario(1L)).thenReturn(new BigDecimal(1999));
            OperacoaoUserCase operacoaoUserCase = this.operacoaoUserCase();
            operacoaoUserCase.executar(new OperacaoTIpoSaque(), BigDecimal.TEN, conta);

        });

        Assertions.assertEquals(OperacaoTipo.INVALID_OPERATION, ex.getMessage());

    }

    @Test
    public void executarSaqueContaSaldoNegativoDiarioShouldThrowInternalServerErrorException () {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {

            ContaDomain conta = new ContaDomain();
            conta.setAtiva(true);
            conta.setBloqueada(false);
            conta.setId(1L);
            conta.setSaldo(BigDecimal.ONE);

            Mockito.when(operacaoRepositoryDatabase.getMontanteSaqueDiario(1L)).thenReturn(new BigDecimal(BigInteger.TEN));
            OperacoaoUserCase operacoaoUserCase = this.operacoaoUserCase();
            operacoaoUserCase.executar(new OperacaoTIpoSaque(), BigDecimal.TEN, conta);

        });

        Assertions.assertEquals(OperacaoTipo.INVALID_OPERATION, ex.getMessage());

    }

}
