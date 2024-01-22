package com.dock.transferbanking.application.service;


import com.dock.transferbanking.adapter.output.repository.database.portador.PortadorRepositoryDatabase;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.PortadorUserCase;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryCache;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryDatabase;
import com.dock.transferbanking.application.service.portador.PortadorService;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PortadorServiceTest {

    @Mock
    IPortadorRepositoryCache portadorRepositoryCache;

    @Mock
    IPortadorRepositoryDatabase portadorRepositoryDatabase;

    PortadorUserCase getPortadorUserCase() {
        return new PortadorService(portadorRepositoryCache, portadorRepositoryDatabase);
    }

    @Test
    public void createShouldReturnPortadorDomainWithID() throws InternalServerErrorException {

        PortadorDomain p = new PortadorDomain("Fulando", "95751461908");
        PortadorDomain result = new PortadorDomain("Fulando", "95751461908");
        result.setId(1L);

        Mockito.when(portadorRepositoryDatabase.
                        saveOrUpdate(p)).
                thenReturn(result);

        PortadorDomain expectedResult = this.getPortadorUserCase().saveOrUpdate(p);

        Assertions.assertTrue(!ObjectUtils.isEmpty(expectedResult.getId()));

    }

    @Test
    public void createSemNomePortadorShouldThrowInternalServerErrorException() {

        try {

            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            PortadorDomain pass = new PortadorDomain("", "123");
            portadorUserCase.saveOrUpdate(pass);

        } catch (Exception e) {
            Assertions.assertInstanceOf(InternalServerErrorException.class, e);
        }

    }

    @Test
    public void createInvalidoCPFPortadorShouldThrowInternalServerErrorException() {

        try {

            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            PortadorDomain pass = new PortadorDomain("Teste", "123");
            portadorUserCase.saveOrUpdate(pass);

        } catch (Exception e) {
            Assertions.assertInstanceOf(InternalServerErrorException.class, e);
        }

    }

    @Test
    public void removeIDPortadorNullShouldThrowInternalServerErrorException() {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {
            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            portadorUserCase.remove(null);
        });

        Assertions.assertEquals(PortadorService.NOT_INFORMED, ex.getMessage());
    }

    @Test
    public void removePortadorNotExistsByIDShouldThrowNotFoundException() {

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> {
            Mockito.when(portadorRepositoryDatabase.getById(Mockito.anyLong()))
                    .thenThrow(new NotFoundException(PortadorRepositoryDatabase.PORTADOR_NOT_FOUND));
            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            portadorUserCase.remove(1L);
        });

        Assertions.assertEquals(PortadorRepositoryDatabase.PORTADOR_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void removeCPFPortadorNullShouldThrowInternalServerErrorException() {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {
            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            portadorUserCase.getByCpf(null);
        });

        Assertions.assertEquals(PortadorService.NOT_INFORMED, ex.getMessage());
    }

    @Test
    public void removePortadorNotExistsByCPFShouldThrowNotFoundException() {

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> {
            Mockito.when(portadorRepositoryDatabase.getByCpf(Mockito.anyString()))
                    .thenThrow(new NotFoundException(PortadorRepositoryDatabase.PORTADOR_NOT_FOUND));
            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            portadorUserCase.getByCpf("123");
        });

        Assertions.assertEquals(PortadorRepositoryDatabase.PORTADOR_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void getPortadorByCPFShouldReturnPortadorFromDatabase() throws NotFoundException, InternalServerErrorException {

        PortadorDomain returnDB = new PortadorDomain("Fulano", "123");
        returnDB.setId(1L);
        Mockito.when(portadorRepositoryDatabase.getByCpf(Mockito.anyString()))
                .thenReturn(returnDB);
        PortadorUserCase portadorUserCase = this.getPortadorUserCase();
        PortadorDomain byCpf = portadorUserCase.getByCpf("123");

        Assertions.assertEquals(returnDB.getCpf(), byCpf.getCpf());
        Assertions.assertEquals(returnDB.getNome(), byCpf.getNome());
    }

    @Test
    public void getPortadorByCPFShouldReturnPortadorFromCache() throws NotFoundException, InternalServerErrorException {

        PortadorUserCase portadorUserCase = this.getPortadorUserCase();
        PortadorDomain returnDB = new PortadorDomain("Fulano", "123");
        returnDB.setId(1L);
        Mockito.when(portadorRepositoryCache.get("PORTADOR_123"))
                .thenReturn(returnDB);
        PortadorDomain byCpf = portadorUserCase.getByCpf("123");

        Assertions.assertEquals(returnDB.getCpf(), byCpf.getCpf());
        Assertions.assertEquals(returnDB.getNome(), byCpf.getNome());
    }



    @Test
    public void getPortadorByCPFShouldThrowInternalServerErrorException() {

        InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class, () -> {
            PortadorUserCase portadorUserCase = this.getPortadorUserCase();
            portadorUserCase.getByCpf("");
        });

        Assertions.assertEquals(PortadorService.NOT_INFORMED, ex.getMessage());
    }

}
