package com.dock.transferbanking.adapter.output.repository.cache.portador;

import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PortadorRepositoryCache implements IPortadorRepositoryCache {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public final static String ERROR_INSERT_IN_REDIS = "error ao inserir portador no redis";

    public final static String ERROR_GET_IN_REDIS = "error ao pegar portador no redis";

    private final IPortadorCache portadorCache;

    private final ObjectMapper objectMapper;

    public PortadorRepositoryCache(IPortadorCache portadorCache, ObjectMapper objectMapper) {
        this.portadorCache = portadorCache;
        this.objectMapper = objectMapper;
    }

    @Override
    public void set(String key, PortadorDomain portadorDomain) {

        try {
            String json = objectMapper.writeValueAsString(portadorDomain);
            PortadorCache portador = new PortadorCache(key, json);
            portadorCache.save(portador);
        } catch (JsonProcessingException e) {
            logger.error(PortadorRepositoryCache.ERROR_INSERT_IN_REDIS, "key", key);
        }

    }

    @Override
    public PortadorDomain get(String key) {

        try {

            Optional<PortadorCache> byId = portadorCache.findById(key);
            PortadorCache cache = byId.orElse(null);
            if (ObjectUtils.isEmpty(cache)) return null;

            return objectMapper.readValue(cache.getResult(), PortadorDomain.class);

        } catch (JsonProcessingException e) {
            logger.error(PortadorRepositoryCache.ERROR_GET_IN_REDIS, "key", key);
        }
        return null;
    }

    @Override
    public void remove (String key) {
        this.portadorCache.deleteById(key);
    }
}
