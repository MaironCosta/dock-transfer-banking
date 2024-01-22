package com.dock.transferbanking.application.port.input;

import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import com.dock.transferbanking.exception.ValidationException;

public interface PortadorUserCase {

    PortadorDomain saveOrUpdate(PortadorDomain portador) throws InternalServerErrorException;
    void remove(Long id) throws InternalServerErrorException, NotFoundException;

    PortadorDomain getByCpf(String cpf) throws InternalServerErrorException, NotFoundException;

}
