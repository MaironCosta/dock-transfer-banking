package com.dock.transferbanking.application.port.output;

import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;

public interface IPortadorRepositoryDatabase {

    PortadorDomain saveOrUpdate (PortadorDomain domain) throws InternalServerErrorException;

    PortadorDomain getById (Long id) throws NotFoundException;

    PortadorDomain getByCpf (String cpf) throws NotFoundException;

}
