package com.dock.transferbanking.adapter.input.http;


import com.dock.transferbanking.adapter.input.http.conta.ContaController;
import com.dock.transferbanking.adapter.input.http.conta.ContaRequest;
import com.dock.transferbanking.adapter.input.http.portador.PortadorController;
import com.dock.transferbanking.adapter.input.http.portador.PortadorRequest;
import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.PortadorUserCase;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
@WebMvcTest(ContaController.class)
@ExtendWith(SpringExtension.class)
@EnableWebMvc
public class ContaControllerTest {

    private final String baseURL = "/conta/v1/";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PortadorUserCase portadorUserCase;

    @MockBean
    ContaUserCase contaUserCase;

    @Test
    public void givenContaRequest_thenInternalServerErrorInternalServerErrorException () throws Exception {

        ContaRequest contaRequest = new ContaRequest("123", null, "098-1");
        Mockito.when(portadorUserCase.getByCpf(contaRequest.cpf())).
                thenReturn(new PortadorDomain("fulano", "123"));

        Mockito.when(contaUserCase.create(Mockito.any(ContaDomain.class), Mockito.any(PortadorDomain.class))).
                thenThrow(new InternalServerErrorException("test"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReqBody = objectMapper.writeValueAsString(contaRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(baseURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReqBody);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError())
                .andReturn();

    }

    @Test
    public void givenContaRequest_thenthenReturnSuccess () throws Exception {

        ContaRequest contaRequest = new ContaRequest("123", "46465", "098-1");
        Mockito.when(portadorUserCase.getByCpf(contaRequest.cpf())).
                thenReturn(new PortadorDomain("fulano", "123"));

        Mockito.when(contaUserCase.create(Mockito.any(ContaDomain.class), Mockito.any(PortadorDomain.class))).
                thenReturn(new ContaDomain());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReqBody = objectMapper.writeValueAsString(contaRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(baseURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReqBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

    }
}
