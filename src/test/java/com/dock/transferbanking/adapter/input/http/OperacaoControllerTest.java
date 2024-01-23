package com.dock.transferbanking.adapter.input.http;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.dock.transferbanking.adapter.input.http.operacao.OperacaoController;
import com.dock.transferbanking.adapter.input.http.operacao.OperacaoRequest;
import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.service.operacao.OperacaoTipoDeposito;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
@WebMvcTest(OperacaoController.class)
@ExtendWith(SpringExtension.class)
@EnableWebMvc
public class OperacaoControllerTest {

    private final String baseURL = "/operacao/v1/";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OperacoaoUserCase operacoaoUserCase;

    @MockBean
    ContaUserCase contaUserCase;

    @Test
    public void givenOperacaoRequest_thenSuccess () throws Exception {

        OperacaoRequest operacaoRequest = new OperacaoRequest("123", "098-1", "deposito", BigDecimal.TEN);
        ContaDomain contaDomain = new ContaDomain();
        contaDomain.setSaldo(BigDecimal.ONE);
        Mockito.when(contaUserCase.getByAgenciaAndNumero(operacaoRequest.agencia(), operacaoRequest.numero()))
                        .thenReturn(contaDomain);

        Mockito.when(operacoaoUserCase.getOperacaoTipo(operacaoRequest.tipo()))
                .thenReturn(new OperacaoTipoDeposito());

        Mockito.when(operacoaoUserCase.executar(new OperacaoTipoDeposito(), operacaoRequest.valor(), contaDomain))
                .thenReturn(new OperacaoDomain());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReqBody = objectMapper.writeValueAsString(operacaoRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(baseURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReqBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

    }
}
