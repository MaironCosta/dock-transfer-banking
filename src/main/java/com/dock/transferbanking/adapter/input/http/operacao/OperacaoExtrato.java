package com.dock.transferbanking.adapter.input.http.operacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperacaoExtrato(

        @JsonProperty(value = "id", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long id,

        @JsonProperty(value = "valor", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal valor,

        @JsonProperty(value = "tipo", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String tipo,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "data", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime data) {
}
