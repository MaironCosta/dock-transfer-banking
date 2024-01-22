package com.dock.transferbanking.adapter.input.http.conta;

import com.dock.transferbanking.adapter.input.http.ResponseHTTP;
import com.dock.transferbanking.adapter.input.http.portador.PortadorRequest;
import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.PortadorUserCase;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import jakarta.websocket.server.PathParam;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/conta")
public class ContaController {

    private static final String SUCCESS_TO_CREATE = "sucesso para criar uma nova conta";

    private static final String SUCCESS_TO_EXECUTE = "sucesso executar";

    private static final String ERROR_TO_BLOCK = "error ao bloquear conta";

    private final ContaUserCase contaUserCase;

    private final PortadorUserCase portadorUserCase;

    ContaController (ContaUserCase contaUserCase, PortadorUserCase portadorUserCase) {
        this.contaUserCase = contaUserCase;
        this.portadorUserCase = portadorUserCase;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(path = {"/v1/", "/v1"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP create (@RequestBody ContaRequest contaRequest) throws InternalServerErrorException, NotFoundException {

        PortadorDomain portadorDomain = portadorUserCase.getByCpf(contaRequest.cpf());
        ContaDomain contaDomain = contaRequest.toContaDomain();
        contaDomain = contaUserCase.create(contaDomain, portadorDomain);

        return new ResponseHTTP(ContaController.SUCCESS_TO_CREATE,
                getContaResponse(contaDomain),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @PutMapping(path = {"/v1/agencia/{agencia}/numero/{numero}/bloquear/",
            "/v1/agencia/{agencia}/numero/{numero}/bloquear"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP bloquear (@PathVariable(name = "agencia") String agencia,
                                  @PathVariable(name = "numero") String numero,
                                  @RequestParam(value = "bloquear") Boolean isBloqueio) throws InternalServerErrorException, NotFoundException {

        if (ObjectUtils.isEmpty(isBloqueio)) {
            throw new InternalServerErrorException(ContaController.ERROR_TO_BLOCK);
        }

        ContaDomain contaDomain = contaUserCase.getByAgenciaAndNumero(agencia, numero);
        contaUserCase.bloquear(contaDomain, isBloqueio);

        return new ResponseHTTP(ContaController.SUCCESS_TO_EXECUTE,
                null,
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping(path = {"/v1/agencia/{agencia}/numero/{numero}/",
            "/v1/agencia/{agencia}/numero/{numero}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP get (@PathVariable(name = "agencia") String agencia,
                             @PathVariable(name = "numero") String numero) throws InternalServerErrorException, NotFoundException {

        ContaDomain contaDomain = contaUserCase.getByAgenciaAndNumero(agencia, numero);

        return new ResponseHTTP(ContaController.SUCCESS_TO_EXECUTE,
                getContaResponse(contaDomain),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    @PutMapping(path = {"/v1/agencia/{agencia}/numero/{numero}/fechar/",
            "/v1/agencia/{agencia}/numero/{numero}/fechar"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP fechar (@PathVariable(name = "agencia") String agencia,
                             @PathVariable(name = "numero") String numero) throws InternalServerErrorException, NotFoundException {

        ContaDomain contaDomain = contaUserCase.getByAgenciaAndNumero(agencia, numero);
        contaDomain = contaUserCase.fechar(contaDomain);

        return new ResponseHTTP(ContaController.SUCCESS_TO_EXECUTE,
                contaDomain,
                null,
                LocalDateTime.now());
    }

    private ContaResponse getContaResponse(ContaDomain contaDomain) {
        return new ContaResponse(contaDomain.getId(), contaDomain.getAgencia(), contaDomain.getNumero(),
                contaDomain.isAtiva(), contaDomain.isBloqueada(), contaDomain.getPortadorDomain(),
                contaDomain.getSaldo(), null);
    }

}
