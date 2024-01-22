package com.dock.transferbanking.configuration;

import com.dock.transferbanking.adapter.output.repository.database.operacao.IOperacaoDatabase;
import com.dock.transferbanking.adapter.output.repository.database.operacao.OperacaoRepositoryDatabase;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.port.output.IOperacaoRepositoryDatabase;
import com.dock.transferbanking.application.service.operacao.OperacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperacaoConfiguration {


    @Bean
    public IOperacaoRepositoryDatabase operacaoRepositoryDatabase(IOperacaoDatabase operacaoDatabase) {
        return new OperacaoRepositoryDatabase(operacaoDatabase);
    }

    @Bean
    public OperacoaoUserCase operacoaoUserCase (IOperacaoRepositoryDatabase operacaoRepositoryDatabase) {
        return new OperacaoService(operacaoRepositoryDatabase);
    }

}
