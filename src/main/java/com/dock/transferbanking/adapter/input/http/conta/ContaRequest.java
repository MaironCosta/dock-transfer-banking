package com.dock.transferbanking.adapter.input.http.conta;

import com.dock.transferbanking.application.domain.ContaDomain;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ContaRequest (
        @JsonProperty(value = "cpf")
        String cpf,

        @JsonProperty(value = "agencia")
        String agencia,

        @JsonProperty(value = "numero")
        String numero) {

    ContaDomain toContaDomain() {
        ContaDomain domain = new ContaDomain();
        domain.setAgencia(this.agencia);
        domain.setNumero(this.numero);
        return domain;
    }
}
