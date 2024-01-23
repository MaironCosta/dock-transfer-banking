# Dock Transfer Banking

Este projeto tem como objetivo realizar ações citadas no arquivo readme_instrucoes.md.

## Stack

* [Github](https://github.com/MaironCosta/dock-transfer-banking)
* Docker
* Docker Compose
* Git
* [Hexagonal Arch](https://dzone.com/articles/hexagonal-architecture-what-is-it-and-how-does-it)
* JAVA 17
* Lombok
* Makefile
* Maven 3.9.6
* Redis
* SpringBoot 3.2.1

Obs.: A aplicação desenvolvida visa promover uma alta coesão e manter um baixo acomplamento,
desta forma, tentando manter a possibilidade de evolução sem interferir em outras features
existentes.

## Endpoint

### Portador

Endpoints relativos a solicitações do portador.

Base path: `/dock/portador/`.

| Verbo HTTP  |          Resource path           | Status Code |                              Descrição |
|-------------|:--------------------------------:|------------:|---------------------------------------:|
| POST        |             v1/, v1              |         201 | Realiza o cadastro de um novo portador |
| DELETE      |        v1/{id}/, v1/{id}         |         202 |       Realiza a remoção de um portador | 

Parametros:

* id: identificado do portador.

Exemplo cadastro novo portador:

``` 
curl -vL -X POST 'http://localhost:8080/dock/portador/v1' \
--header 'Content-Type: application/json' \
--data '{
    "nome": "Ciclano2",
    "cpf": "64732624669"
}'
```

### Conta

Endpoints relativos a solicitações de contas.

Base path: `/dock/conta/`.

| Verbo HTTP |                                                                   Resource path                                                                   | Status Code |                                         Descrição |
|------------|:-------------------------------------------------------------------------------------------------------------------------------------------------:|------------:|--------------------------------------------------:|
| POST       |                                                                     v1\/, v1                                                                      |         201 |              Realiza o cadastro de uma nova conta |
| PUT        | v1/agencia/{agencia}/numero/{numero}/bloquear/?bloquear=[false, true], </br> v1/agencia/{agencia}/numero/{numero}/bloquear?bloquear=[true, false] |         202 |                   Realiza o bloqueio de uma conta |
| GET        |                                 v1/agencia/{agencia}/numero/{numero}/, </br> v1/agencia/{agencia}/numero/{numero}                                 |         200 | Realiza a busca de uma conta por número e agência |
| PUT        |                          v1/agencia/{agencia}/numero/{numero}/fechar/, </br> v1/agencia/{agencia}/numero/{numero}/fechar                          |         202 |                 Realiza o fechamendo de uma conta |

Parametros:

* agencia: agencia na qual a conta pertence;
* numero: número da conta;
* bloquear: 
  * true: para realizar o bloqueio da conta;
  * false: para realizar o desbloqueio da conta;

Exemplo cadastro nova conta:

``` 
curl -vL -X POST 'http://localhost:8080/dock/conta/v1/' \
--header 'Content-Type: application/json' \
--data '{
    "cpf": "60825916402",
    "agencia": "976",
    "numero": "123-0"
}'
```

### Operações

Endpoints relativos a solicitações de operações.

Base path: `/dock/operacao/`.

| Verbo HTTP |                                                      Resource path                                                       | Status Code |                                 Descrição |
|------------|:------------------------------------------------------------------------------------------------------------------------:|------------:|------------------------------------------:|
| POST       |                                                         v1/, v1                                                          |         201 |   Realiza o registro de uma nova operação |
| GET        | /v1/agencia/{agencia}/numero/{numero}/?mes={mes}&ano={ano}, </br> /v1/agencia/{agencia}/numero/{numero}?{mes}&ano={ano}  |         200 | Realiza a busca das operações por período |

Parametros:

* agencia: agencia na qual a conta pertence;
* numero: número da conta;
* mes: mês da consulta do extrato;
* ano : ano da consulta do extrato;

Exemplo realizar operação:

``` 
curl -vL -X POST 'http://localhost:8080/dock/operacao/v1' \
--header 'Content-Type: application/json' \
--data '{
    "agencia": "0001",
    "numero": "1234-4",
    "tipo": "saque",
    "valor": 200.56
}'
```

## Dockerfile

É possível executar a solução usando docker. Para tal, estando na raíz do projeto, 
deve-se executar no terminal o comando `$ make docker-compose-up`.
