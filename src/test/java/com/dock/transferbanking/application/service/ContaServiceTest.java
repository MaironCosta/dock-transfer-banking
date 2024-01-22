package com.dock.transferbanking.application.service;


import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.port.output.IContaRepositoryCache;
import com.dock.transferbanking.application.port.output.IContaRepositoryDatabase;
import com.dock.transferbanking.application.service.conta.ContaService;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class ContaServiceTest {

    @Mock
    IContaRepositoryCache contaRepositoryCache;

    @Mock
    IContaRepositoryDatabase contaRepositoryDatabase;

    @Mock
    OperacoaoUserCase operacoaoUserCase;

    ContaUserCase getContaUserCase() {
        return new ContaService(contaRepositoryCache, contaRepositoryDatabase, operacoaoUserCase);
    }

    ContaDomain getDefaultContaDomain() {
        ContaDomain conta = new ContaDomain();
        conta.setAtiva(true);
        conta.setBloqueada(false);
        conta.setAgencia("123a");
        conta.setNumero("987-0");

        return conta;
    }

    @Test
    public void createNovaContaShouldReturnSuccess() throws InternalServerErrorException {

        ContaUserCase contaUserCase = this.getContaUserCase();

        PortadorDomain portadorDomain = new PortadorDomain("Fulano", "60825916402");
        portadorDomain.setId(1L);

        ContaDomain newConta = new ContaDomain();
        newConta.setNumero("123-0");
        newConta.setAgencia("234");
        newConta.setPortadorDomain(portadorDomain);

        ContaDomain savedConta = new ContaDomain();
        savedConta.setNumero("123-0");
        savedConta.setAgencia("234");
        savedConta.setId(1L);
        savedConta.setPortadorDomain(portadorDomain);

        Mockito.when(contaRepositoryDatabase.SaveOrUpdate(newConta))
                .thenReturn(savedConta);

        ContaDomain contaDomain = contaUserCase.create(newConta, portadorDomain);

        Assertions.assertEquals(newConta.getAgencia(), contaDomain.getAgencia());
        Assertions.assertEquals(newConta.getNumero(), contaDomain.getNumero());
        Assertions.assertFalse(contaDomain.isAtiva());
        Assertions.assertFalse(contaDomain.isBloqueada());
        Assertions.assertFalse(ObjectUtils.isEmpty(contaDomain.getPortadorDomain()));
    }

    @Test
    public void createNovaContaShouldThrowInternalServerErrorException() throws InternalServerErrorException {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {

            ContaDomain newConta = new ContaDomain();
            newConta.setNumero("123-0");
            newConta.setAgencia("");
            PortadorDomain portadorDomain = new PortadorDomain("Rex", "098");
            portadorDomain.setId(1L);

            this.getContaUserCase().create(newConta, portadorDomain);

        });

        Assertions.assertEquals(ex.getMessage(), ContaService.INVALID_DATA);
    }

    @Test
    public void getByAgenciaAndNumeroShouldThrowInternalServerErrorException() {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {
            this.getContaUserCase().getByAgenciaAndNumero("456", "");
        });

        Assertions.assertEquals(ex.getMessage(), ContaService.INVALID_DATA);
    }

    @Test
    public void getByAgenciaAndNumeroShouldConta() throws InternalServerErrorException, NotFoundException {

        ContaDomain defaultContaDomain = this.getDefaultContaDomain();
        defaultContaDomain.setId(1L);
        defaultContaDomain.setPortadorDomain(new PortadorDomain("Fulano", "123"));
        Mockito.when(contaRepositoryDatabase.getByAgenciaAndNumero("123a", "987-0")).
                thenReturn(defaultContaDomain);
        Mockito.when(operacoaoUserCase.getSaldo(1L)).
                thenReturn(BigDecimal.TEN);

        ContaDomain contaDomain = this.getContaUserCase().getByAgenciaAndNumero("123a", "987-0");

        Assertions.assertEquals("123a", contaDomain.getAgencia());
        Assertions.assertEquals("987-0", contaDomain.getNumero());
        Assertions.assertEquals(BigDecimal.TEN, contaDomain.getSaldo());
    }

}
