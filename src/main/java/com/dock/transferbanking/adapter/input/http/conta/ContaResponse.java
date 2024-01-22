package com.dock.transferbanking.adapter.input.http.conta;

import com.dock.transferbanking.adapter.input.http.operacao.OperacaoExtrato;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record ContaResponse(

        @JsonProperty(value = "id", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long id,

        @JsonProperty(value = "agencia", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String agencia,

        @JsonProperty(value = "numero", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String numero,

        @JsonProperty(value = "ativa", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean ativa,

        @JsonProperty(value = "bloqueada", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean bloqueada,

        @JsonProperty(value = "portador", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        PortadorDomain portador,

        @JsonProperty(value = "saldo", required = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal saldo,

        @JsonProperty(value = "extrato")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<OperacaoExtrato> extrato) {

}
