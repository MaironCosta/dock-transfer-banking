package com.dock.transferbanking.application.domain;

import java.math.BigDecimal;

public class ContaDomain {

    private Long id;

    private String agencia, numero;

    private boolean ativa, bloqueada;

    private PortadorDomain portadorDomain;

    private BigDecimal saldo;

    public ContaDomain() {
    }

    public ContaDomain(Long id, String agencia, String numero, boolean ativa, boolean bloqueada, PortadorDomain portadorDomain) {
        this.id = id;
        this.agencia = agencia;
        this.numero = numero;
        this.ativa = ativa;
        this.bloqueada = bloqueada;
        this.portadorDomain = portadorDomain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    public PortadorDomain getPortadorDomain() {
        return portadorDomain;
    }

    public void setPortadorDomain(PortadorDomain portadorDomain) {
        this.portadorDomain = portadorDomain;
    }

    public BigDecimal getSaldo() {
        return saldo == null?BigDecimal.ZERO:saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
