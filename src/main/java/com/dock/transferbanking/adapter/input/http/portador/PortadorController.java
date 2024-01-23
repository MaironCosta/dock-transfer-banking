package com.dock.transferbanking.adapter.input.http.portador;

import com.dock.transferbanking.adapter.input.http.ResponseHTTP;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.PortadorUserCase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import com.dock.transferbanking.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/portador")
public class PortadorController {

    private static final String SUCCESS_TO_CREATE = "sucesso para criar um novo portador";

    private static final String SUCCESS_TO_REMOVE = "sucesso para remover o portador";

    private final PortadorUserCase portadorUserCase;

    public PortadorController (PortadorUserCase portadorUserCase){
        this.portadorUserCase = portadorUserCase;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(path = {"/v1/", "/v1"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP create (@RequestBody PortadorRequest portadorRequest) throws InternalServerErrorException {

        PortadorDomain portadorDomain = portadorUserCase.saveOrUpdate(portadorRequest.toPortadorDomain());

        return new ResponseHTTP(PortadorController.SUCCESS_TO_CREATE,
                portadorDomain,
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @DeleteMapping(path = {"/v1/{id}/", "/v1/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP remove (@PathVariable Long id) throws InternalServerErrorException, NotFoundException {

        portadorUserCase.remove(id);

        return new ResponseHTTP(PortadorController.SUCCESS_TO_REMOVE,
                null,
                null,
                LocalDateTime.now());
    }
}
