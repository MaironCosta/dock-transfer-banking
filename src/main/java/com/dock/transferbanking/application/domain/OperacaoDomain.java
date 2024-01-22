package com.dock.transferbanking.application.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacaoDomain {

    private Long id;

    private String tipo;

    private BigDecimal valor;

    private LocalDateTime data;

    private ContaDomain contaDomain;

    public OperacaoDomain() {
    }

    public OperacaoDomain(String tipo, BigDecimal valor, ContaDomain contaDomain) {
        this.tipo = tipo;
        this.valor = valor;
        this.contaDomain = contaDomain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ContaDomain getContaDomain() {
        return contaDomain;
    }

    public void setContaDomain(ContaDomain contaDomain) {
        this.contaDomain = contaDomain;
    }
}
