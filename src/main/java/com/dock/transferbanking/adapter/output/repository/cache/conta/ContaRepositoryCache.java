package com.dock.transferbanking.adapter.output.repository.cache.conta;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.port.output.IContaRepositoryCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ContaRepositoryCache implements IContaRepositoryCache {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public final static String ERROR_INSERT_IN_REDIS = "error ao inserir conta no redis";

    public final static String ERROR_GET_IN_REDIS = "error ao pegar conta no redis";

    private final IContaCache contaCache;

    private final ObjectMapper objectMapper;

    public ContaRepositoryCache(IContaCache contaCache, ObjectMapper objectMapper) {
        this.contaCache = contaCache;
        this.objectMapper = objectMapper;
    }

    @Override
    public void set(String key, ContaDomain conta) {

        try {
            String json = objectMapper.writeValueAsString(conta);
            ContaCache cache = new ContaCache(key, json);
            contaCache.save(cache);
        } catch (JsonProcessingException e) {
            logger.error(ContaRepositoryCache.ERROR_INSERT_IN_REDIS, "key", key);
        }
    }

    @Override
    public ContaDomain get(String key) {

        try {

            Optional<ContaCache> byId = contaCache.findById(key);
            ContaCache cache = byId.orElse(null);
            if (ObjectUtils.isEmpty(cache)) return null;

            return objectMapper.readValue(cache.getResult(), ContaDomain.class);

        } catch (JsonProcessingException e) {
            logger.error(ContaRepositoryCache.ERROR_GET_IN_REDIS, "key", key);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        this.contaCache.deleteById(key);
    }
}
