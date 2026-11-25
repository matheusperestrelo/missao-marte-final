# Servidor — Missão Marte

API REST (Spring Boot 4.1.2, Java 17, H2 em arquivo). Nesta etapa só a
saúde está no ar; todas as outras rotas respondem 401.

## Requisitos

JDK 17 (o JDK do `PATH` pode ser outro, por exemplo o 25: aponte
`JAVA_HOME` para um JDK 17 antes de rodar o Gradle).

## Como rodar e testar

```bash
export JAVA_HOME=/caminho/do/jdk-17
./gradlew bootRun     # sobe em 127.0.0.1:8080
./gradlew test        # testes (H2 em memória, sem rede)
./gradlew clean build # o que o gate executa
```

O servidor escuta só em `127.0.0.1:8080`. Para conferir:

```bash
curl http://127.0.0.1:8080/api/saude
```

## Variáveis de ambiente

| Variável | Efeito | Se ausente |
|---|---|---|
| `MISSAOMARTE_DADOS` | Pasta do arquivo H2 (`missaomarte.mv.db`) | `./dados`, relativo à pasta de onde o servidor é iniciado |
| `MISSAOMARTE_JWT_SEGREDO` | Segredo de assinatura dos tokens, mínimo de **32 bytes** em UTF-8 | O servidor sobe com um segredo aleatório e avisa no log: os tokens deixam de valer ao reiniciar |

Um segredo com 1 a 31 bytes impede o servidor de subir. Nunca coloque o
segredo no repositório.

## Banco de dados

Os dados ficam num arquivo H2. Dois servidores não podem usar o mesmo
arquivo ao mesmo tempo (o banco trava). `spring.jpa.hibernate.ddl-auto=update`
é cômodo para estudo, mas não serve para produção: ele altera o esquema
sozinho, sem controle de versões nem reversão. Em produção, use migrações
(Flyway ou Liquibase) e `validate`.

## API

| Método | Rota | Token | Respostas |
|---|---|---|---|
| GET | `/api/saude` | não | 200 `{"status":"ok"}` |
| qualquer outra | qualquer outra | sim | 401 `ProblemDetail` ("Autenticação necessária ou token inválido.") com `WWW-Authenticate: Bearer` |

Todo erro sai como `ProblemDetail` (`application/problem+json`).

Exemplos prontos em [`requests.http`](requests.http).
