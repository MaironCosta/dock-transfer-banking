package com.dock.transferbanking.adapter.output.repository.database.conta;

import com.dock.transferbanking.exception.NotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IContaDatabase extends CrudRepository<ContaDatabase, Long> {

    Optional<ContaDatabase> getByAgenciaAndNumero(String agencia, String numero) throws NotFoundException;

    Optional<ContaDatabase> getByPortadorId(Long idPortador) throws NotFoundException;
}
