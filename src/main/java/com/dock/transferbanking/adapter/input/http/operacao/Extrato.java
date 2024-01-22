package com.dock.transferbanking.adapter.input.http.operacao;

import com.dock.transferbanking.adapter.input.http.conta.ContaResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Extrato(

        @JsonProperty(value = "conta", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ContaResponse contaResponse,

        @JsonProperty(value = "extrato", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<OperacaoExtrato> extrato
        ) {
}
