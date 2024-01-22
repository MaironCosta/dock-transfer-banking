package com.dock.transferbanking.adapter.output.repository.database.portador;

import com.dock.transferbanking.exception.NotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IPortadorDatabase extends CrudRepository<PortadorDatabase, Long> {

    Optional<PortadorDatabase> getByCpf(String cpf) throws NotFoundException;
}
