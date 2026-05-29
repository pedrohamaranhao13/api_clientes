# API Clientes (`api_clientes`)

Este é um projeto de API REST monolítica desenvolvido em **Java 21** utilizando o ecossistema **Spring Boot 4.x**. O principal objetivo deste projeto é demonstrar a criação de uma arquitetura em camadas limpa, realizando persistência nativa por meio de **JDBC tradicional (Java Database Connectivity)** conectado a um banco de dados **PostgreSQL**.

O projeto gerencia informações estruturadas de clientes e seus respectivos múltiplos endereços.

---

## 🛠️ Tecnologias e Dependências Utilizadas

- **Java 21**: Versão moderna da linguagem aproveitando recursos como *Local Variable Type Inference* (`var`) e melhorias de performance.
- **Spring Boot 4.0.6**: Framework base para subida rápida da aplicação, mapeamento de rotas e injeção de dependências web.
- **Spring Web MVC**: Módulo do Spring usado para a criação dos endpoints REST e tratamento de requisições e parâmetros HTTP.
- **Driver JDBC PostgreSQL**: Driver nativo que permite a comunicação da aplicação Java com o servidor do banco de dados PostgreSQL.
- **Project Lombok**: Biblioteca focada em produtividade que remove o código boilerplate gerando automaticamente `getters`, `setters`, `toString`, `equals` e `hashCode` através da anotação `@Data`.
- **Springdoc OpenAPI (Swagger UI v3.0.3)**: Geração automatizada da documentação interativa dos endpoints da API.
- **Maven**: Gerenciador de dependências e automatizador de builds do ecossistema Java.

---

## 🏛️ Arquitetura e Organização de Camadas

O sistema divide as responsabilidades de software de forma isolada, seguindo o padrão clássico de camadas:

1. **`entities`**: Classes puras de domínio (`Cliente` e `Endereco`) que representam a abstração orientada a objetos das tabelas do banco de dados. Elas mapeiam um relacionamento **1:N (Um para Muitos)**, onde um cliente pode ter vários endereços cadastrados.
2. **`controllers`**: Camada de entrada HTTP (`ClienteController`). Recebe as requisições web, valida a chegada de parâmetros via `@RequestParam` e devolve respostas de texto ou status HTTP para o cliente.
3. **`services`**: Camada de regras e lógica de negócio (`ClienteService`). Centraliza as validações obrigatórias (comprimento de strings, nulidades) e orquestra se a operação pode ou não avançar para o banco de dados com base em lógica de validação (como checagem de CPF existente).
4. **`repositories`**: Camada de infraestrutura e acesso a dados (`ClienteRepository`). Isola o código SQL nativo, abrindo conexões através do padrão Factory e executando instruções `INSERT` e `SELECT` usando proteção contra SQL Injection.
5. **`factories`**: Abstração de infraestrutura para geração de conexões de banco de dados (`ConnectionFactory`), concentrando strings JDBC, credenciais e gerenciando o fechamento seguro de conexões por meio da técnica do Java *try-with-resources*.

---

## 🗄️ Modelagem de Banco de Dados (PostgreSQL)

O schema do banco de dados é composto por duas tabelas com integridade referencial estrita:

```sql
CREATE TABLE CLIENTES(
    ID      SERIAL          PRIMARY KEY,
    NOME    VARCHAR(150)    NOT NULL,
    CPF     CHAR(14)        NOT NULL        UNIQUE
);

CREATE TABLE ENDERECOS(
    ID          SERIAL          PRIMARY KEY,
    LOGRADOURO  VARCHAR(200)    NOT NULL,
    NUMERO      VARCHAR(25)     NOT NULL,
    COMPLEMENTO VARCHAR(150)    NOT NULL,
    BAIRRO      VARCHAR(100)    NOT NULL,
    CIDADE      VARCHAR(50)     NOT NULL,
    UF          CHAR(2)         NOT NULL,
    CEP         CHAR(9)         NOT NULL,
    CLIENTE_ID  INTEGER         NOT NULL,
    FOREIGN KEY (CLIENTE_ID) REFERENCES CLIENTES(ID)
);