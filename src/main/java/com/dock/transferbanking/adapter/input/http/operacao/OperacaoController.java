package com.dock.transferbanking.adapter.input.http.operacao;

import com.dock.transferbanking.adapter.input.http.ResponseHTTP;
import com.dock.transferbanking.adapter.input.http.conta.ContaResponse;
import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.OperacaoDomain;
import com.dock.transferbanking.application.port.input.ContaUserCase;
import com.dock.transferbanking.application.port.input.OperacoaoUserCase;
import com.dock.transferbanking.application.service.operacao.OperacaoTipo;
import com.dock.transferbanking.exception.InternalServerErrorException;
import com.dock.transferbanking.exception.NotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/operacao")
public class OperacaoController {

    private static final String SUCCESS_TO_EXECUTE = "sucesso ao executar";

    private final OperacoaoUserCase operacoaoUserCase;

    private final ContaUserCase contaUserCase;

    public OperacaoController(OperacoaoUserCase operacoaoUserCase, ContaUserCase contaUserCase) {
        this.operacoaoUserCase = operacoaoUserCase;
        this.contaUserCase = contaUserCase;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(path = {"/v1/", "/v1"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP executar (@RequestBody OperacaoRequest operacaoRequest)
            throws InternalServerErrorException, NotFoundException {

        ContaDomain contaDomain = contaUserCase.getByAgenciaAndNumero(operacaoRequest.agencia(),
                operacaoRequest.numero());

        OperacaoTipo operacaoTipo = operacoaoUserCase.getOperacaoTipo(operacaoRequest.tipo());
        operacoaoUserCase.executar(operacaoTipo, operacaoRequest.valor(), contaDomain);

        return new ResponseHTTP(OperacaoController.SUCCESS_TO_EXECUTE,
                null,
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping(path = {"/v1/agencia/{agencia}/numero/{numero}/",
            "/v1/agencia/{agencia}/numero/{numero}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseHTTP getByPeriodo (@PathVariable(name = "agencia") String agencia,
                                      @PathVariable(name = "numero") String numero,
                                      @RequestParam(value = "mes") Long mes,
                                      @RequestParam(value = "ano") Long ano)
            throws InternalServerErrorException, NotFoundException {

        ContaDomain contaDomain = contaUserCase.getByAgenciaAndNumero(agencia, numero);

        List<OperacaoDomain> operacaoByMesAno = operacoaoUserCase.getOperacaoByMesAno(contaDomain.getId(),
                mes, ano);

        List<OperacaoExtrato> operacoes = new ArrayList<>();
        operacaoByMesAno.forEach((v) -> {
            operacoes.add(new OperacaoExtrato(v.getId(), v.getValor(), v.getTipo(), v.getData()));
        });

        ContaResponse contaResponse = new ContaResponse(contaDomain.getId(), contaDomain.getAgencia(), contaDomain.getNumero(),
                contaDomain.isAtiva(), contaDomain.isBloqueada(), contaDomain.getPortadorDomain(),
                contaDomain.getSaldo(), operacoes);

        return new ResponseHTTP(OperacaoController.SUCCESS_TO_EXECUTE,
                contaResponse,
                null,
                LocalDateTime.now());
    }


}
