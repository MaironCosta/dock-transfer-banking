package com.dock.transferbanking.application.port.output;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;

public interface IContaRepositoryDatabase {

    ContaDomain getByAgenciaAndNumero(String agencia, String numero) throws NotFoundException;
    ContaDomain getByPortadorID(Long idPortador) throws NotFoundException;

    ContaDomain SaveOrUpdate (ContaDomain domain) throws InternalServerErrorException;

}
