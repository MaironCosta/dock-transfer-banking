package com.dock.transferbanking.adapter.output.repository.database.portador;

import com.dock.transferbanking.application.domain.PortadorDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "portador")
@Table(name = "portador", schema = "dock")
public class PortadorDatabase implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public PortadorDomain toPortadorDomain() {
        PortadorDomain portadorDomain = new PortadorDomain(this.getNome(), this.getCpf());
        portadorDomain.setId(this.getId());
        portadorDomain.setDeletedAt(this.getDeletedAt());
        return portadorDomain;
    }

    public PortadorDatabase(PortadorDomain domain) {
        this.setId(domain.getId());
        this.setCpf(domain.getCpf().replaceAll("[^a-zA-Z0-9]+", ""));
        this.setNome(domain.getNome());
        this.setDeletedAt(domain.getDeletedAt());
    }

}
