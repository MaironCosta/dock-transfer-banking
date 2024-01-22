package com.dock.transferbanking.adapter.input.http.operacao;

import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record OperacaoRequest(
        @JsonProperty(required = true, value = "agencia")
        String agencia,

        @JsonProperty(required = true, value = "numero")
        String numero,

        @JsonProperty(required = true, value = "tipo")
        String tipo,

        @JsonProperty(required = true, value = "valor")
        BigDecimal valor) {

}
