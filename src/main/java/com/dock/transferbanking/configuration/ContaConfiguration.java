package com.dock.transferbanking.configuration;

import com.dock.transferbanking.adapter.output.repository.cache.conta.ContaRepositoryCache;
import com.dock.transferbanking.adapter.output.repository.cache.conta.IContaCache;
import com.dock.transferbanking.adapter.output.repository.database.conta.ContaRepositoryDatabase;
import com.dock.transferbanking.adapter.output.repository.database.conta.IContaDatabase;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.port.output.IContaRepositoryCache;
import com.dock.transferbanking.application.port.output.IContaRepositoryDatabase;
import com.dock.transferbanking.application.service.conta.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContaConfiguration {

    @Bean
    public IContaRepositoryDatabase contaRepositoryDatabase(IContaDatabase contaDatabase){
        return new ContaRepositoryDatabase(contaDatabase);
    }

    @Bean
    public IContaRepositoryCache contaCache(IContaCache contaCache, ObjectMapper objectMapper) {
        return new ContaRepositoryCache(contaCache, objectMapper);
    }

    @Bean
    public ContaUserCase contaUserCase(IContaRepositoryCache contaRepositoryCache,
                                       IContaRepositoryDatabase contaRepositoryDatabase,
                                       OperacoaoUserCase operacoaoUserCase) {
        return new ContaService(contaRepositoryCache, contaRepositoryDatabase, operacoaoUserCase);
    }
}
