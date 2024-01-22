package com.dock.transferbanking.application.service.conta;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.port.output.IContaRepositoryCache;
import com.dock.transferbanking.application.port.output.IContaRepositoryDatabase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;

public class ContaService implements ContaUserCase {

    public static String INVALID_DATA = "agência ou número inválido";

    private final String CACHE_KEY = "CONTA";

    private final IContaRepositoryCache contaRepositoryCache;

    private final IContaRepositoryDatabase contaRepositoryDatabase;

    private final OperacoaoUserCase operacoaoUserCase;

    public ContaService(IContaRepositoryCache contaRepositoryCache,
                        IContaRepositoryDatabase contaRepositoryDatabase,
                        OperacoaoUserCase operacoaoUserCase) {
        this.contaRepositoryCache = contaRepositoryCache;
        this.contaRepositoryDatabase = contaRepositoryDatabase;
        this.operacoaoUserCase = operacoaoUserCase;
    }

    public String getCacheKey(String v) {
        return String.format("%s_%s", CACHE_KEY, v);
    }

    @Override
    public ContaDomain create(ContaDomain contaDomain, PortadorDomain portadorDomain) throws InternalServerErrorException {

        contaDomain.setPortadorDomain(portadorDomain);

        if (!this.isContaValida(contaDomain)) {
            throw new InternalServerErrorException(ContaService.INVALID_DATA);
        }

        contaDomain = contaRepositoryDatabase.SaveOrUpdate(contaDomain);

        this.putInCache(contaDomain);

        return contaDomain;
    }

    @Override
    public ContaDomain getByAgenciaAndNumero(String agencia, String numero) throws InternalServerErrorException, NotFoundException {

        if (ObjectUtils.isEmpty(agencia) || ObjectUtils.isEmpty(numero))
            throw new InternalServerErrorException(ContaService.INVALID_DATA);

        String partCacheKey = String.format("%s_%s", agencia, numero);
        ContaDomain contaDomain = contaRepositoryCache.get(partCacheKey);
        if (!ObjectUtils.isEmpty(contaDomain)) {
            contaDomain.setSaldo(operacoaoUserCase.getSaldo(contaDomain.getId()));
            return contaDomain;
        }

        contaDomain = contaRepositoryDatabase.getByAgenciaAndNumero(agencia, numero);
        contaDomain.setSaldo(operacoaoUserCase.getSaldo(contaDomain.getId()));

        this.putInCache(contaDomain);

        return contaDomain;
    }

    @Override
    public ContaDomain bloquear(ContaDomain contaDomain, boolean isBloqueio) throws InternalServerErrorException {

        contaDomain.setBloqueada(isBloqueio);
        contaDomain = contaRepositoryDatabase.SaveOrUpdate(contaDomain);
        this.putInCache(contaDomain);

        return contaDomain;
    }

    @Override
    public ContaDomain fechar(ContaDomain contaDomain) throws InternalServerErrorException {

        contaDomain.setAtiva(false);
        contaDomain = contaRepositoryDatabase.SaveOrUpdate(contaDomain);
        this.putInCache(contaDomain);

        return contaDomain;
    }

    private void putInCache(ContaDomain contaDomain) {
        contaRepositoryCache.set(this.getCacheKey(contaDomain.getId().toString()), contaDomain);
        String partCacheKey = String.format("%s_%s", contaDomain.getAgencia(), contaDomain.getNumero());
        contaRepositoryCache.set(this.getCacheKey(partCacheKey), contaDomain);
    }

    private boolean isContaValida (ContaDomain conta) {
        if (ObjectUtils.isEmpty(conta.getNumero()) || ObjectUtils.isEmpty(conta.getAgencia())) {
            return false;
        }

        if (ObjectUtils.isEmpty(conta.getPortadorDomain()) ||
                ObjectUtils.isEmpty(conta.getPortadorDomain().getId())) {
            return false;
        }

        return true;
    }

}
