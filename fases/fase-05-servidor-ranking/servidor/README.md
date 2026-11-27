# Servidor — Missão Marte

API REST (Spring Boot 4.1.2, Java 17, H2 em arquivo). Estão no ar a saúde,
o cadastro, o login e o registro de partidas; as demais rotas exigem token.

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
| POST | `/api/usuarios` | não | 201 `{"id","nome"}` (com `Location`); 400 dados inválidos; 409 nome já existe |
| POST | `/api/auth/login` | não | 200 `{"token","tipo":"Bearer","expiraEmSegundos":7200}`; 400 dados inválidos; 401 "Nome ou senha inválidos." (o mesmo corpo para usuário inexistente e senha errada) |
| POST | `/api/partidas` | sim | 201 `{"id","dificuldade","resultado","pontuacao","duracaoSegundos","movimentos","dataHora","posicaoRanking"}` (com `Location`; `posicaoRanking` é `null` por enquanto); 400 dados inválidos; 401 |
| qualquer outra | qualquer outra | sim | 401 `ProblemDetail` ("Autenticação necessária ou token inválido.") com `WWW-Authenticate: Bearer` |

Cadastro: `{"nome","senha"}`. O nome tem de 3 a 20 caracteres (letras sem
acento, números, `_` e `-`); a senha, de 8 caracteres a 72 bytes em UTF-8.
O nome é único sem diferenciar maiúsculas (`Matheus` e `matheus` são o
mesmo). A senha é guardada só como hash BCrypt e nunca volta nas respostas.

Login: `{"nome","senha"}`; o nome não diferencia maiúsculas, a senha sim. O
token é um JWT HS256 válido por 2 horas e vai nas rotas protegidas no
cabeçalho `Authorization: Bearer <token>`.

Rotas liberadas sem token (todas as outras exigem `Authorization: Bearer`):
`POST /api/usuarios`, `POST /api/auth/login`, `GET /api/saude` e `/error`.
Nelas o cabeçalho `Authorization` é ignorado, mesmo inválido. Token
malformado, de outro segredo, expirado, ou de um usuário que não existe
mais, devolve sempre o mesmo 401 (sem dizer o motivo).

A API é sem estado: não há sessão nem cookie (`Set-Cookie` nunca é
enviado); cada requisição prova quem é só pelo token. Por isso o CSRF pode
ficar desligado: esse ataque depende de o navegador anexar sozinho um
cookie de sessão, e aqui o token precisa ser colocado à mão no cabeçalho.

Todo erro sai como `ProblemDetail` (`application/problem+json`). O 400 de
validação traz `erros`, uma lista de `{"campo","mensagem"}` (um item por
campo, sem o valor enviado).

Registro de partida: `{"dificuldade","resultado","pontuacao","duracaoSegundos","movimentos"}`,
todos obrigatórios. `dificuldade` é `FACIL`, `MEDIO` ou `DIFICIL`; `resultado` é
`VITORIA` ou `DERROTA` (em maiúsculas; `ABORTADA` não é aceito). `pontuacao` vai de
-100000 a 100000, `duracaoSegundos` de 0 a 86400 e `movimentos` de 0 a 100000, números
inteiros (decimal é recusado com 400). A partida pertence ao usuário do token: campos a
mais no corpo (`usuarioId`, `nome`, `id`, `dataHora`) são ignorados, e a `dataHora` é do
servidor, em UTC (ISO-8601).

Exemplos prontos em [`requests.http`](requests.http).
