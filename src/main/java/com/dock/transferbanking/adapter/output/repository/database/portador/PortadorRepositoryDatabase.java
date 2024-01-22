package com.dock.transferbanking.adapter.output.repository.database.portador;

import com.dock.transferbanking.adapter.output.repository.cache.portador.PortadorRepositoryCache;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryDatabase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import com.dock.transferbanking.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PortadorRepositoryDatabase implements IPortadorRepositoryDatabase {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String PORTADOR_NOT_FOUND = "portador desconhecido";

    public static final String ERROR_TO_SAVE = "error ao salvar portador";

    private final IPortadorDatabase portadorDatabase;

    public PortadorRepositoryDatabase(IPortadorDatabase portadorDatabase) {
        this.portadorDatabase = portadorDatabase;
    }

    @Override
    public PortadorDomain saveOrUpdate(PortadorDomain domain) throws InternalServerErrorException {

        try {

            PortadorDatabase db = new PortadorDatabase(domain);
            PortadorDatabase save = portadorDatabase.save(db);
            return save.toPortadorDomain();

        } catch (Exception e) {
            logger.error(PortadorRepositoryDatabase.ERROR_TO_SAVE, "cpf", domain.getCpf(),
                    "error", e.getMessage());
            throw new InternalServerErrorException(PortadorRepositoryDatabase.ERROR_TO_SAVE);
        }
    }

    @Override
    public PortadorDomain getById(Long id) throws NotFoundException {
        return portadorDatabase.findById(id).
                orElseThrow(() -> new NotFoundException(PortadorRepositoryDatabase.PORTADOR_NOT_FOUND)).
                toPortadorDomain();
    }

    @Override
    public PortadorDomain getByCpf(String cpf) throws NotFoundException {
        return portadorDatabase.getByCpf(cpf).
                orElseThrow(() -> new NotFoundException(PortadorRepositoryDatabase.PORTADOR_NOT_FOUND)).
                toPortadorDomain();
    }
}
