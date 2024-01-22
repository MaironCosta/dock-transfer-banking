package com.dock.transferbanking.adapter.input.http.portador;

import com.dock.transferbanking.application.domain.PortadorDomain;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public record PortadorRequest (
        @JsonProperty(value = "nome")String nome,
        @JsonProperty(value = "cpf") String cpf) {

    PortadorDomain toPortadorDomain() {
        PortadorDomain portadorDomain = new PortadorDomain(nome, cpf);
        return portadorDomain;
    }
}
