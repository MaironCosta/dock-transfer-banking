package com.dock.transferbanking.adapter.output.repository.database.operacao;

import com.dock.transferbanking.adapter.output.repository.database.conta.ContaDatabase;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "operacao")
@Table(name = "operacao", schema = "dock")
public class OperacaoDatabase implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data")
    private LocalDateTime data;

    @JoinColumn(name = "fk_id_conta")
    @ManyToOne
    private ContaDatabase conta;

    public OperacaoDatabase (OperacaoDomain domain) {
        this.setId(domain.getId());
        this.setTipo(domain.getTipo());
        this.setValor(domain.getValor());
        this.setData(domain.getData());
        this.setConta(new ContaDatabase(domain.getContaDomain()));
    }

    public OperacaoDomain toOperacaoDomain() {
        OperacaoDomain operacaoDomain = new OperacaoDomain();
        operacaoDomain.setId(this.getId());
        operacaoDomain.setTipo(this.getTipo());
        operacaoDomain.setValor(this.getValor());
        operacaoDomain.setData(this.getData());
        operacaoDomain.setContaDomain(this.getConta().toContaDomain());

        return operacaoDomain;
    }

}
