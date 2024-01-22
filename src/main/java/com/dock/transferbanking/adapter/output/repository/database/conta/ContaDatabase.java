package com.dock.transferbanking.adapter.output.repository.database.conta;

import com.dock.transferbanking.adapter.output.repository.database.portador.PortadorDatabase;
import com.dock.transferbanking.application.domain.ContaDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "conta")
@Table(name = "conta", schema = "dock")
public class ContaDatabase implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agencia")
    private String agencia;

    @Column(name = "numero")
    private String numero;

    @Column(name = "ativa")
    private Boolean ativa;

    @Column(name = "bloqueada")
    private Boolean bloqueada;

    @JoinColumn(name = "fk_id_portador")
    @ManyToOne
    private PortadorDatabase portador;

    public ContaDatabase (ContaDomain domain) {

        this.setNumero(domain.getNumero());
        this.setAgencia(domain.getAgencia());
        this.setPortador(new PortadorDatabase(domain.getPortadorDomain()));
        this.setId(domain.getId());
        this.setAtiva(domain.isAtiva());
        this.setBloqueada(domain.isBloqueada());
    }

    public ContaDomain toContaDomain() {

        ContaDomain domain = new ContaDomain();
        domain.setNumero(this.getNumero());
        domain.setAgencia(this.getAgencia());
        domain.setPortadorDomain(this.getPortador().toPortadorDomain());
        domain.setId(this.getId());
        domain.setAtiva(this.getAtiva());
        domain.setBloqueada(this.getBloqueada());

        return domain;
    }

}
