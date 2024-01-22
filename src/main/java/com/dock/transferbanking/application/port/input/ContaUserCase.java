package com.dock.transferbanking.application.port.input;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;

public interface ContaUserCase {

    ContaDomain create (ContaDomain contaDomain, PortadorDomain portadorDomain) throws InternalServerErrorException;

    ContaDomain getByAgenciaAndNumero(String agencia, String numero) throws InternalServerErrorException, NotFoundException;

    ContaDomain bloquear(ContaDomain contaDomain, boolean isBloqueio) throws InternalServerErrorException;

    ContaDomain fechar(ContaDomain contaDomain) throws InternalServerErrorException;

}
