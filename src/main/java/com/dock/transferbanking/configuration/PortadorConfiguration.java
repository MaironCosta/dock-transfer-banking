package com.dock.transferbanking.configuration;

import com.dock.transferbanking.adapter.output.repository.cache.portador.IPortadorCache;
import com.dock.transferbanking.adapter.output.repository.cache.portador.PortadorRepositoryCache;
import com.dock.transferbanking.adapter.output.repository.database.portador.IPortadorDatabase;
import com.dock.transferbanking.adapter.output.repository.database.portador.PortadorRepositoryDatabase;
import com.dock.transferbanking.application.port.input.PortadorUserCase;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryCache;
import com.dock.transferbanking.application.port.output.IPortadorRepositoryDatabase;
import com.dock.transferbanking.application.service.portador.PortadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortadorConfiguration {

    @Bean
    public PortadorUserCase portadorUserCase (IPortadorRepositoryCache portadorRepositoryCache,
                                              IPortadorRepositoryDatabase portadorRepositoryDatabase ) {
        return new PortadorService(portadorRepositoryCache, portadorRepositoryDatabase);
    }

    @Bean
    public IPortadorRepositoryCache portadorRepositoryCache(IPortadorCache portadorCache,
                                                            ObjectMapper objectMapper) {
        return new PortadorRepositoryCache(portadorCache, objectMapper);
    }

    @Bean
    public IPortadorRepositoryDatabase portadorRepositoryDatabase(IPortadorDatabase portadorDatabase){
        return new PortadorRepositoryDatabase(portadorDatabase);
    }


}
