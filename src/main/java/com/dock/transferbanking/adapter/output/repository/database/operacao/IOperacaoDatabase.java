package com.dock.transferbanking.adapter.output.repository.database.operacao;

import com.dock.transferbanking.application.domain.OperacaoDomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IOperacaoDatabase extends CrudRepository<OperacaoDatabase, Long> {

    @Query(value = "select SUM (o.valor) " +
            "from operacao o " +
            "where  o.fk_id_conta = :contaID " +
            "and o.tipo = :operacaoTipo " +
            "and extract('day' from o.data) = :day " +
            "and extract('month' from o.data) = :month " +
            "and extract('year' from o.data) = :year ", nativeQuery = true)
    Optional<BigDecimal> getMontanteOperacaoTipoDiario(@Param("day") int day, @Param("month") int month,
                                                       @Param("year") int year,
                                                       @Param("contaID") Long contaID,
                                                       @Param("operacaoTipo") String operacaoTipo);

    @Query(value = "select SUM (" +
            "       CASE " +
            "           WHEN o.tipo = 'deposito' THEN o.valor " +
            "           WHEN o.tipo = 'saque' THEN o.valor * - 1 " +
            "       END " +
            "       ) " +
            "from operacao o " +
            "where o.fk_id_conta = :contaID ", nativeQuery = true)
    Optional<BigDecimal> getSaldo(@Param("contaID") Long contaID);

    @Query(value = "select o.* from operacao o " +
            "where 1 = 1 " +
            "and o.fk_id_conta = :contaID " +
            "and extract('month' from o.data) = :month " +
            "and extract('year' from o.data) = :year " +
            "order by o.data desc", nativeQuery = true)
    Optional<List<OperacaoDatabase>> getOperacaoByMesAno(@Param("contaID") Long contaID,
                                             @Param("month") Long month,
                                             @Param("year") Long year);
}
