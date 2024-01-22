create schema dock

    create table portador
    (
        id         serial       not null
            constraint dock_portador_pkey primary key,
        nome       varchar(255) not null,
        cpf        varchar(13)  not null unique,
        deleted_at timestamp default timezone('BRT'::text, null)
    )

    create
        unique index dock_portador_uindex
        on portador (id)

    create table conta
    (
        id             serial      not null
            constraint dock_conta_pkey primary key,
        agencia        varchar(20) not null,
        numero         varchar(50) not null,
--         saldo     number      not null default 0,
        ativa          bool        not null default true,
        bloqueada      bool        not null default false,
        fk_id_portador int         not null,
        FOREIGN KEY (fk_id_portador) references portador (id)
    )

    create
        unique index dock_conta_uindex
        on conta (id)

    create table operacao
    (
        id          serial      not null
            constraint dock_operacao_pkey primary key,
        valor       decimal     not null,
        tipo        varchar(50) not null,
        data        timestamp default timezone('BRT'::text, now()),
        fk_id_conta int         not null,
        FOREIGN KEY (fk_id_conta) references conta (id),
        CONSTRAINT chk_operacao_tipo
            CHECK (tipo IN ('saque', 'deposito'))
    )

    create
        unique index dock_operacao_uindex
        on operacao (id);

--- INSERTS DEFAULT

INSERT INTO dock.portador(nome, cpf)
VALUES ('Fulano', 39467306204);

INSERT INTO dock.conta(agencia, numero, fk_id_portador)
VALUES ('0001', '1234-4', 1);

INSERT INTO dock.operacao(valor, tipo, fk_id_conta)
VALUES (10.57, 'deposito', 1);

INSERT INTO dock.operacao(valor, tipo, fk_id_conta)
VALUES (10.56, 'saque', 1);
