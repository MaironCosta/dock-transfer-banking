package com.dock.transferbanking.adapter.output.repository.database.conta;

import com.dock.transferbanking.adapter.output.repository.database.portador.PortadorDatabase;
import com.dock.transferbanking.adapter.output.repository.database.portador.PortadorRepositoryDatabase;
import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.port.output.IContaRepositoryDatabase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContaRepositoryDatabase implements IContaRepositoryDatabase {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String NOT_FOUND = "conta desconhecido";

    public static final String ERROR_TO_SAVE = "error ao salvar conta";

    private final IContaDatabase contaDatabase;

    public ContaRepositoryDatabase(IContaDatabase contaDatabase) {
        this.contaDatabase = contaDatabase;
    }

    @Override
    public ContaDomain getByAgenciaAndNumero(String agencia, String numero) throws NotFoundException {
        return contaDatabase.getByAgenciaAndNumero(agencia, numero).
                orElseThrow(() -> new NotFoundException(ContaRepositoryDatabase.NOT_FOUND)).
                toContaDomain();
    }

    @Override
    public ContaDomain getByPortadorID(Long idPortador) throws NotFoundException {
        return contaDatabase.getByPortadorId(idPortador).
                orElseThrow(() -> new NotFoundException(ContaRepositoryDatabase.NOT_FOUND)).
                toContaDomain();
    }

    @Override
    public ContaDomain SaveOrUpdate(ContaDomain domain) throws InternalServerErrorException {

        try {

            ContaDatabase db = new ContaDatabase(domain);
            ContaDatabase save = contaDatabase.save(db);
            return save.toContaDomain();

        } catch (Exception e) {
            logger.error(ContaRepositoryDatabase.ERROR_TO_SAVE, "agencia", domain.getAgencia(),
                    "numero", domain.getNumero(), "portador", domain.getPortadorDomain().getCpf(),
                    "error", e.getMessage());
            throw new InternalServerErrorException(ContaRepositoryDatabase.ERROR_TO_SAVE);
        }
    }
}
