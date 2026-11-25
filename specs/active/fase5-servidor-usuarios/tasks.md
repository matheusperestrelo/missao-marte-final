# Tarefas: fase5-servidor-usuarios

- [ ] 1. Criar o servidor Spring Boot com `GET /api/saude` público e todas as outras rotas protegidas com 401 em `ProblemDetail`
  - status: review
  - Fatia vertical: `fases/fase-05-servidor-ranking/` com `README.md` (R8.1) e `.gitignore`; `servidor/` com o Wrapper 9.8.2 (gerar **antes** do `build.gradle`, ver design, "Riscos"), `settings.gradle`, `build.gradle` (Boot 4.1.2, Java 17, dependências do design), `application.properties` de produção e de teste, `MissaoMarteServidorApplication`.
  - `ConfiguracaoSeguranca` (sem estado, CSRF desligado, liberando só `GET /api/saude` e `/error` por enquanto, mais as duas rotas `POST` que já podem ficar na lista), `EntradaNaoAutenticada` (401 `ProblemDetail` com `detail` fixo e `WWW-Authenticate: Bearer`), `SaudeController` e `TratadorDeErros` com o 500 genérico.
  - O resource server precisa de um `JwtDecoder` para subir: nesta tarefa, criar já o `SegredoJwt` e o `ConfiguracaoJwt` (decoder com HS256 e `Clock`), sem emissor. Cobrir o `SegredoJwt` com testes (R6.2, R6.3, R6.4) aqui.
  - Testes: `SaudeApiTest` (R2), rota inexistente sem token → 401 em `ProblemDetail` com o cabeçalho (R5.2, R5.6 no caso sem token), `SegredoJwtTest`.
  - `servidor/README.md` com como rodar/testar, variáveis, endereço e a tabela da API só com a saúde; `requests.http` com a saúde.
  - Conferir com JDK 17: `./gradlew clean build` em `servidor/`, `./gradlew bootRun` + `curl http://127.0.0.1:8080/api/saude`, arquivo H2 criado em `./dados` (ou `MISSAOMARTE_DADOS`). Rodar o `.workflow/check.sh`.
  - Não alterar nada fora de `fases/fase-05-servidor-ranking/`.
  - _Requisitos: R1, R2, R5, R6, R7, R8_
  - notas: Servidor Spring Boot criado (Wrapper 9.8.2, Boot 4.1.2, Java 17) com saúde pública, segurança sem estado, 401 em ProblemDetail, SegredoJwt/ConfiguracaoJwt (decoder HS256 com Clock), READMEs e requests.http. Desvio pequeno do design: o filtro Bearer recusa token inválido até em rota liberada (R2.2), então a ConfiguracaoSeguranca usa um BearerTokenResolver que ignora o token nas 4 rotas públicas. O aviso no log do segredo aleatório (parte de R6.2/R6.4) só foi conferido manualmente no bootRun; não há teste de log, só da resolução pura (sugestão: cobrir na tarefa 3 com o teste de log). Manual com JDK 17: saúde 200, rota inexistente 401 problem+json com WWW-Authenticate, arquivo H2 criado em ./dados.

- [ ] 2. Implementar o cadastro `POST /api/usuarios` com validação, hash BCrypt e 409 por nome normalizado
  - status: todo
  - `Usuario`, `UsuarioRepository`, `NomeUsuario`, `CadastroService`, `NomeJaExisteException`, `UsuarioController`, `CadastroRequest` (com `toString` mascarado), `UsuarioResponse`, `@TamanhoMaximoEmBytes` e o validador; bean `PasswordEncoder` `BCryptPasswordEncoder(10)` e bean `Clock`.
  - `TratadorDeErros`: 400 de validação com `erros` `[{campo, mensagem}]` sem o valor rejeitado, 400 de JSON malformado, 409.
  - Testes `CadastroApiTest` com todos os casos de R3 e os de R7.1–R7.4 que tocam o cadastro (dois campos inválidos → dois itens; senha não aparece no corpo; `toString`).
  - Acrescentar o cadastro à tabela do `servidor/README.md` e ao `requests.http` (válido, repetido com outra caixa, inválido).
  - _Requisitos: R3, R7, R8_
  - notas:

- [ ] 3. Implementar o login `POST /api/auth/login` com emissão do JWT e a mesma resposta para usuário inexistente e senha errada
  - status: todo
  - `EmissorDeToken`, `TokenEmitido`, `JwtEmissorDeToken` (`sub` = id, `iat`, `exp` = +2 h, HS256), bean `JwtEncoder` no `ConfiguracaoJwt`, `AutenticacaoService` com o hash falso gerado no construtor, `CredenciaisInvalidasException` → 401 "Nome ou senha inválidos." no `TratadorDeErros`, `AutenticacaoController`, `LoginRequest` e `LoginResponse` (com `toString` mascarados).
  - Testes `LoginApiTest` (R4.1–R4.5, R4.7) e `AutenticacaoServiceTest` (R4.6, com `PasswordEncoder` simulado). Teste de R6.1: o token emitido é aceito pelo `JwtDecoder` com o segredo configurado. Teste de R7.5: log capturado de um cadastro e um login não contém a senha nem o token. Teste de R5.7 no login (sem `Set-Cookie`).
  - Acrescentar o login à tabela do README e ao `requests.http`, guardando o token numa variável do arquivo (ex.: `@token = {{login.response.body.token}}`).
  - _Requisitos: R4, R5, R6, R7, R8_
  - notas:

- [ ] 4. Entregar o usuário autenticado às rotas protegidas e cobrir token inválido, expirado e de usuário inexistente
  - status: todo
  - `UsuarioAutenticado` e `ResolvedorUsuarioAutenticado` (lê o `sub` do `Jwt`, confere `existsById`, senão 401 com o mesmo `ProblemDetail` da `EntradaNaoAutenticada`), registrado num `WebMvcConfigurer`. A spec 14 usa esse parâmetro nos controllers de partidas e ranking.
  - Teste `ProtecaoApiTest` com a `RotaProtegidaDeTeste` só em `src/test` (`GET /api/teste/protegida`): malformado, assinatura de outro segredo, expirado (emitido com `Clock` fixo 3 h antes), `sub` inexistente → 401 com `ProblemDetail`, `detail` fixo e `WWW-Authenticate`; válido → 200 com o `id`; sem `Set-Cookie` (R5.7).
  - No `servidor/README.md`, explicar em poucas linhas por que a API é sem estado e por que o CSRF pode ficar desligado, e listar as rotas liberadas.
  - _Requisitos: R5, R8_
  - notas:
