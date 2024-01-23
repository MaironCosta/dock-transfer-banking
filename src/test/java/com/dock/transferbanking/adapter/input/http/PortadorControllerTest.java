package com.dock.transferbanking.adapter.input.http;


import com.dock.transferbanking.adapter.input.http.portador.PortadorController;
import com.dock.transferbanking.adapter.input.http.portador.PortadorRequest;
import com.dock.transferbanking.application.domain.PortadorDomain;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
@WebMvcTest(PortadorController.class)
@ExtendWith(SpringExtension.class)
@EnableWebMvc
public class PortadorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PortadorUserCase portadorUserCase;

    @Test
    public void givenPortadorRequest_thenInternalServerErrorInternalServerErrorException () throws Exception {

        Mockito.when(portadorUserCase.saveOrUpdate(Mockito.any(PortadorDomain.class))).
                thenThrow(new InternalServerErrorException("test"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReqBody = objectMapper.writeValueAsString(new PortadorRequest("test", "123"));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/portador/v1/", "/portador/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReqBody);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError())
                .andReturn();

    }

    @Test
    public void givenPortadorRequest_thenReturnSuccess () throws Exception {

        PortadorRequest portadorRequest = new PortadorRequest("test", "60825916402");
        Mockito.when(portadorUserCase.saveOrUpdate(Mockito.any(PortadorDomain.class))).
                thenReturn(portadorRequest.toPortadorDomain());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReqBody = objectMapper.writeValueAsString(portadorRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/portador/v1/", "/portador/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReqBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

    }

}
