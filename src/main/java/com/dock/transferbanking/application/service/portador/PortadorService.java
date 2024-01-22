package com.dock.transferbanking.application.service.portador;

import br.com.caelum.stella.validation.CPFValidator;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.PortadorUserCase;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryCache;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryDatabase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

public class PortadorService implements PortadorUserCase {

    public static String NOT_INFORMED = "portador não informado";

    public static String INVALID_DATA = "nome ou cpf inválido";

    private final String CACHE_KEY = "PORTADOR";

    private final IPortadorRepositoryCache portadorRepositoryCache;

    private final IPortadorRepositoryDatabase portadorRepositoryDatabase;

    public PortadorService (IPortadorRepositoryCache portadorRepositoryCache,
                            IPortadorRepositoryDatabase portadorRepositoryDatabase) {
        this.portadorRepositoryCache = portadorRepositoryCache;
        this.portadorRepositoryDatabase = portadorRepositoryDatabase;
    }

    public String getCacheKey(String v) {
        return String.format("%s_%s", CACHE_KEY, v);
    }

    @Override
    public PortadorDomain saveOrUpdate(PortadorDomain portador) throws InternalServerErrorException {

        if (!isPortadorValido(portador))
            throw new InternalServerErrorException(PortadorService.INVALID_DATA);

        portador = portadorRepositoryDatabase.saveOrUpdate(portador);
        this.putInCache(portador);

        return portador;
    }

    private boolean isPortadorValido(PortadorDomain portador) {

        if (ObjectUtils.isEmpty(portador.getNome())) {
            return false;
        }

        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.assertValid(portador.getCpf());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void remove(Long id) throws InternalServerErrorException, NotFoundException {

        if (ObjectUtils.isEmpty(id)) {
            throw new InternalServerErrorException(PortadorService.NOT_INFORMED);
        }

        PortadorDomain portadorDomain = this.portadorRepositoryDatabase.getById(id);
        portadorDomain.setDeletedAt(LocalDateTime.now());

        this.portadorRepositoryDatabase.saveOrUpdate(portadorDomain);
        this.putInCache(portadorDomain);
    }

    @Override
    public PortadorDomain getByCpf(String cpf) throws InternalServerErrorException, NotFoundException {

        if (ObjectUtils.isEmpty(cpf)) {
            throw new InternalServerErrorException(PortadorService.NOT_INFORMED);
        }

        PortadorDomain portadorDomain = this.portadorRepositoryCache.get(this.getCacheKey(cpf));
        if (!ObjectUtils.isEmpty(portadorDomain))
            return portadorDomain;

        portadorDomain = this.portadorRepositoryDatabase.getByCpf(cpf);
        this.putInCache(portadorDomain);

        return portadorDomain;
    }

    private void putInCache(PortadorDomain portadorDomain) {
        this.portadorRepositoryCache.set(this.getCacheKey(portadorDomain.getId().toString()), portadorDomain);
        this.portadorRepositoryCache.set(this.getCacheKey(portadorDomain.getCpf()), portadorDomain);
    }

}
