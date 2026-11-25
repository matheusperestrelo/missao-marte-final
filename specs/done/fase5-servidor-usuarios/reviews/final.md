# Verificação final — fase5-servidor-usuarios

> **Nova verificação (2ª).** A anterior deu PENDENCIAS: faltava teste para
> o 404 e o 500 de R7.1, e a frase das linhas 3-4 do `servidor/README.md`
> estava desatualizada. O usuário aprovou um ajuste que acrescentou a
> tarefa 5 ao `tasks.md`. Ela foi implementada e aprovada (`reviews/05-r1.md`).
> Este relatório substitui o anterior.

**Veredito:** APROVADA
**Branch:** feat/fase5-servidor-usuarios
**Commits da spec:** 2 (fd6591c..8bca6f3). Por decisão do usuário (commit
único no fim da spec), a tarefa 1 rodada 2, as tarefas 2 a 5, a pasta
`reviews/` e a tarefa 5 do `tasks.md` estão só na working tree, sem commit.
Esta verificação foi feita sobre a working tree atual. Todas as 5 tarefas
estão `[x]` com `status: done`.

## Cobertura

Testes em `fases/fase-05-servidor-ranking/servidor/src/test/java/missaomarte/servidor/`.
No `check.sh`: 49 testes, 0 falhas, 0 erros, 0 pulados (47 da verificação anterior mais os 2 da tarefa 5).

| Requisito | Critério | Teste(s) / verificação | Situação |
|---|---|---|---|
| R1 | 1 | inspeção: `README.md`, `.gitignore` (as 6 entradas) e `servidor/`, sem `cliente/` | coberto |
| R1 | 2 | inspeção: `gradle-wrapper.properties` 9.8.2, `build.gradle` Boot 4.1.2, `options.release = 17`, nada fora de `servidor/` | coberto |
| R1 | 3 | gate: `check.sh` passou (`gradlew clean build` com JDK 17 + camadas) | coberto |
| R1 | 4 | `git diff main --stat -- fases` + `git status`: só `fases/fase-05-servidor-ranking/` | coberto |
| R1 | 5 | inspeção (`server.address=127.0.0.1`, `server.port=8080`) + execução manual na 1ª verificação | coberto |
| R1 | 6 | inspeção (`${MISSAOMARTE_DADOS:./dados}`) + manual: `missaomarte.mv.db` criado na pasta de `MISSAOMARTE_DADOS` | coberto |
| R1 | 7 | manual (na 1ª verificação: cadastro, reinício e login com sucesso; o código de produção não mudou desde então). Vai para o QA | coberto (manual) |
| R2 | 1 | `SaudeApiTest.r2_1_saudeSemTokenResponde200ComStatusOk` | coberto |
| R2 | 2 | `SaudeApiTest.r2_2_saudeComTokenInvalidoResponde200` | coberto |
| R3 | 1 | `CadastroApiTest.r3_1_cadastroValidoDa201ComIdENomeComoEnviado` | coberto |
| R3 | 2 | `CadastroApiTest.r3_2_respostaTemSoIdENome` | coberto |
| R3 | 3 | `CadastroApiTest.r3_3_senhaGuardadaComoHashBcryptDeCustoDez` | coberto |
| R3 | 4 | `CadastroApiTest.r3_4_mesmaSenhaGeraHashesDiferentes` | coberto |
| R3 | 5 | `CadastroApiTest.r3_5_guardaNomeNormalizadoEDataDeCriacao` | coberto |
| R3 | 6 | `CadastroApiTest.r3_6_nomeRepetidoComOutraCaixaDa409SemCriarOutro` | coberto |
| R3 | 7 | `CadastroApiTest.r3_7_nomeNosLimitesDeTamanho`, `r3_7_nomeComCaracteresInvalidosOuAusenteDa400` | coberto |
| R3 | 8 | `CadastroApiTest.r3_8_senhaCom7Recusa8Aceita` | coberto |
| R3 | 9 | `CadastroApiTest.r3_9_senhaMaximaDe72BytesEmUtf8` | coberto |
| R3 | 10 | `CadastroApiTest.r3_10_senhaAusenteVaziaOuJsonMalformadoDa400` | coberto |
| R4 | 1 | `LoginApiTest.r4_1_loginCorretoDa200ComTokenTipoEExpiracao` | coberto |
| R4 | 2 | `LoginApiTest.r4_2_loginComOutraCaixaNoNomeEAceito` | coberto |
| R4 | 3 | `LoginApiTest.r4_3_senhaComOutraCaixaERecusada` | coberto |
| R4 | 4 | `LoginApiTest.r4_4_tokenEJwtHs256ComSubIatExpESemDadosSensiveis` | coberto |
| R4 | 5 | `LoginApiTest.r4_5_usuarioInexistenteESenhaErradaDaoOMesmo401` | coberto |
| R4 | 6 | `AutenticacaoServiceTest.r4_6_usuarioInexistenteAindaConfereASenhaContraUmHashFalso` | coberto |
| R4 | 7 | `LoginApiTest.r4_7_nomeOuSenhaAusentesOuVaziosDao400EmProblemDetail` | coberto |
| R5 | 1 | inspeção de `ConfiguracaoSeguranca` (4 rotas `permitAll`, `anyRequest().authenticated()`) + testes de saúde, cadastro e login sem token | coberto |
| R5 | 2 | `ProtecaoApiTest.r5_2_rotaProtegidaSemTokenDa401`, `r5_2_rotaInexistenteSemTokenDa401`; `SaudeApiTest.r5_2_rotaInexistenteSemTokenDa401EmProblemDetail` | coberto |
| R5 | 3 | `ProtecaoApiTest.r5_3_tokenMalformadoDa401`, `r5_3_assinaturaDeOutroSegredoDa401`, `r5_3_tokenExpiradoDa401` | coberto |
| R5 | 4 | `ProtecaoApiTest.r5_4_subInexistenteDa401` | coberto |
| R5 | 5 | `ProtecaoApiTest.r5_5_tokenValidoChegaARotaComOId` | coberto |
| R5 | 6 | auxiliar `esperar401` em `ProtecaoApiTest` (sem token, 3 casos de token, `sub` inexistente); `SaudeApiTest.r5_2_...`; login errado em `LoginApiTest.r4_5_...` | coberto |
| R5 | 7 | `LoginApiTest.r5_7_loginNaoEnviaCookie`, `ProtecaoApiTest.r5_7_respostaProtegidaNaoUsaCookie` | coberto |
| R5 | 8 | testes de cadastro e login (POST sem token CSRF) | coberto |
| R6 | 1 | `LoginApiTest.r6_1_tokenEmitidoEAceitoPeloDecoderComOSegredoConfigurado`, `SegredoJwtTest.r6_1_com32BytesUsaOsBytesDoValor` | coberto |
| R6 | 2 | `SegredoJwtTest.r6_2_ausente...`, `r6_2_vazio...`; `ConfiguracaoJwtLogTest.r6_2_segredoAleatorioAvisaNoLog...` | coberto |
| R6 | 3 | `SegredoJwtTest.r6_3_com31BytesImpedeAInicializacaoSemMostrarOValor`, `r6_3_contaBytesEmUtf8NaoCaracteres` | coberto |
| R6 | 4 | `SegredoJwtTest.r6_3_com31Bytes...`, `r6_4_toStringNaoMostraOsBytes`; `ConfiguracaoJwtLogTest.r6_4_segredoValidoNaoGeraAvisoNemApareceNoLog` | coberto |
| R6 | 5 | inspeção: `application.properties` de produção sem valor; valor visivelmente de teste só em `src/test/resources` e em `ConfiguracaoJwtLogTest`; `requests.http` sem segredo | coberto |
| R7 | 1 | 400: `CadastroApiTest.r7_1_jsonMalformadoEhProblemJson` e os 400 de validação; 401: `ProtecaoApiTest`, `LoginApiTest.r4_5_...`; 404: `ProtecaoApiTest.r7_1_rotaInexistenteComTokenValidoDa404EmProblemDetail`; 409: `r3_6_...`; 500: `ProtecaoApiTest.r7_7_erroInternoDa500GenericoSemPilhaNemMensagem` (todos conferem `application/problem+json`) | coberto |
| R7 | 2 | `CadastroApiTest.r7_2_doisCamposInvalidosDaoDoisItensEmErros`, `r7_2_campoComVariasViolacoesGeraUmItemSo` | coberto |
| R7 | 3 | `CadastroApiTest.r7_3_senhaInvalidaNaoApareceNoCorpoDoErro`, `r7_3_nomeInvalidoNaoRepeteValorRejeitadoNoCorpo` | coberto |
| R7 | 4 | `CadastroApiTest.r7_4_toStringDoCadastroNaoMostraASenha`, `LoginApiTest.r7_4_toStringDeLoginRequestELoginResponseNaoMostraSenhaNemToken` | coberto |
| R7 | 5 | `LoginApiTest.r7_5_logDeCadastroELoginNaoTemSenhaNemToken` + inspeção | coberto |
| R7 | 6 | inspeção: controllers devolvem `UsuarioResponse`, `LoginResponse` e o `record` da saúde, nunca `Usuario` | coberto |
| R7 | 7 | `ProtecaoApiTest.r7_7_erroInternoDa500GenericoSemPilhaNemMensagem` (500, `detail` "Erro interno no servidor.", sem `trace`, sem a mensagem da exceção, sem `java.`/`missaomarte.`) + inspeção de `TratadorDeErros.java:74-81` e `server.error.include-*=never` | coberto |
| R8 | 1 | inspeção do `fases/fase-05-servidor-ranking/README.md` | coberto |
| R8 | 2 | inspeção do `servidor/README.md` (JDK 17, `bootRun`, `test`, as duas variáveis, 32 bytes, endereço, `ddl-auto`, tabela com saúde, cadastro e login, rotas liberadas, sem estado e CSRF; linhas 3-5 agora dizem que saúde, cadastro e login estão no ar) | coberto |
| R8 | 3 | inspeção do `servidor/requests.http` (saúde; cadastro válido, repetido e inválido; login certo com `@token = {{login.response.body.token}}` e errado) | coberto |
| R8 | 4 | QA manual | vai para o QA |

## check.sh

passou (fase 0 imutável; `fases/fase-05-servidor-ranking/servidor: gradlew clean build` com JDK 17; regras de camadas da fase 5; demais fases sem falha).

## Pendências

Nenhuma impede a aprovação. As pendências 1 e 2 da verificação anterior
(lacuna de R7.1 e frase do README) foram fechadas pela tarefa 5.

1. `servidor/requests.http:7-8` e `:45-46` (observação da `03-r1` e da `04-r1`): os exemplos chamam `/api/qualquer-coisa`. Sem token isso dá 401. Com token dá 404 (`detail` "No static resource api/qualquer-coisa."), e não um 200. O comentário avisa que ainda não há rota protegida real. Fica para a spec 14.
2. Teste duplicado (sugestão 5 da `01-r1` e 6 da `04-r1`): o `r5_2` de rota inexistente existe em `SaudeApiTest` e em `ProtecaoApiTest`. Remover a cópia é decisão do usuário.
3. Desvio do design registrado nas notas da tarefa 1: um `BearerTokenResolver` ignora o token nas 4 rotas públicas, para R2.2 funcionar. A lista de rotas públicas está repetida em `ConfiguracaoSeguranca` (sugestão 6 da `01-r1`), e a spec 14 vai acrescentar rotas lá.
4. O 401 de token é montado em dois lugares, `EntradaNaoAutenticada` e `TratadorDeErros.naoAutenticado` (sugestão 5 da `04-r1`). O risco é os dois se afastarem com o tempo.
5. Sugestões da `05-r1` (tarefa 5): a string `"segredo-interno-da-excecao"` está escrita em `ProtecaoApiTest.java:150` e em `RotaProtegidaDeTeste.java:21` (uma constante evitaria que a asserção negativa deixasse de provar algo); o nome do teste do 500 cita só R7.7, embora prove também o 500 de R7.1; o teste do 404 não confere `$.status` no corpo.
6. Sugestões de teste que não viraram tarefa: o ramo `DataIntegrityViolationException` → 409 no `CadastroService` sem teste (`02-r1` s2); o `toString` de `TokenEmitido` sem teste, embora o design o liste (`03-r1` s1); R6.1 provado por ida e volta com o mesmo bean `SecretKey`, e não com o valor configurado (`03-r1` s2); o `title` do 401 de login não é conferido (`03-r1` s3).
7. Outras sugestões menores ainda abertas: o log do 500 sem a pilha (`TratadorDeErros.java:76`); a API depreciada `content().json(String, boolean)` em `SaudeApiTest` (o build avisa); a mensagem de validação instável quando um campo tem duas violações (`putIfAbsent`, `02-r1` s1).
8. Versionamento: hoje só a tarefa 1 rodada 1 tem commit. O commit único do fim da spec precisa incluir os arquivos modificados (`README.md`, `requests.http`, `TratadorDeErros.java`, `ConfiguracaoJwt.java`, `tasks.md`) e todos os novos sem versionamento (`api/AutenticacaoController.java`, `ConfiguracaoWeb.java`, `ResolvedorUsuarioAutenticado.java`, `UsuarioController.java`, `UsuarioNaoAutenticadoException.java`, `api/dto/`, `api/validacao/`, `application/`, `domain/`, `infrastructure/JwtEmissorDeToken.java`, os testes `CadastroApiTest`, `LoginApiTest`, `ProtecaoApiTest`, `RotaProtegidaDeTeste`, `application/`, `ConfiguracaoJwtLogTest`) e a pasta `specs/active/fase5-servidor-usuarios/reviews/`. O ajuste que criou a tarefa 5 também não tem commit `docs:` próprio; ele entra no mesmo commit.

## Roteiro de QA

Em `fases/fase-05-servidor-ranking/servidor/`, com JDK 17 (`JAVA_HOME` apontando para ele):

1. `./gradlew test` → **esperado:** `BUILD SUCCESSFUL`, 49 testes sem falha (R1.3).
2. Sem variáveis de ambiente, rode `./gradlew bootRun` → **esperado:** o log mostra um WARN "MISSAOMARTE_JWT_SEGREDO não definido: usando um segredo aleatório. Os tokens deixam de valer quando o servidor reiniciar.", o servidor escuta em `127.0.0.1:8080` e o arquivo `./dados/missaomarte.mv.db` aparece (R1.5, R1.6, R6.2).
3. `curl -i http://127.0.0.1:8080/api/saude` → **esperado:** `200` e `{"status":"ok"}`. Repita com `-H "Authorization: Bearer lixo"` → **esperado:** `200` do mesmo jeito (R2.1, R2.2).
4. Pelo `requests.http` (ou `curl`), cadastre `{"nome":"Matheus","senha":"senha-teste-1"}` → **esperado:** `201`, corpo só com `id` e `nome` igual a "Matheus", sem `Set-Cookie` (R3.1, R3.2, R5.7).
5. Cadastre `{"nome":"MATHEUS","senha":"outra-senha"}` → **esperado:** `409` em `application/problem+json` (R3.6).
6. Cadastre `{"nome":"Jo ão","senha":"123"}` → **esperado:** `400` em problem+json com `erros` de dois itens (`nome` e `senha`), sem "123" no corpo (R3.7, R3.8, R7.2, R7.3). Envie também um corpo que não é JSON (`{nome:`) → **esperado:** `400` problem+json (R3.10).
7. Faça login com `{"nome":"matheus","senha":"senha-teste-1"}` → **esperado:** `200` com `token`, `"tipo":"Bearer"` e `"expiraEmSegundos":7200`. Cole o token em jwt.io → **esperado:** `alg` HS256 e só `sub` (o id), `iat` e `exp` = `iat` + 7200 (R4.1, R4.2, R4.4).
8. Faça login com a senha `SENHA-TESTE-1` e depois com o usuário `ninguem` → **esperado:** os dois `401` com o corpo idêntico e `detail` "Nome ou senha inválidos." (R4.3, R4.5). Login com `{"nome":"","senha":""}` → **esperado:** `400` problem+json (R4.7).
9. `curl -i http://127.0.0.1:8080/api/qualquer-coisa` sem token → **esperado:** `401`, problem+json, `detail` "Autenticação necessária ou token inválido." e `WWW-Authenticate: Bearer` (R5.2, R5.6). Repita com `Authorization: Bearer abc.def.ghi` → **esperado:** o mesmo 401 (R5.3).
10. Pare o servidor (Ctrl+C) e suba de novo sem variáveis. Use o token do passo 7 → **esperado:** `401`, porque o segredo aleatório mudou (R6.2). Faça login de novo com Matheus → **esperado:** `200`, porque o usuário continua no banco (R1.7).
11. Suba com `MISSAOMARTE_JWT_SEGREDO=curto` → **esperado:** o servidor não sobe, a mensagem é "MISSAOMARTE_JWT_SEGREDO precisa ter pelo menos 32 bytes." e "curto" não aparece no log (R6.3, R6.4).
12. Suba com `MISSAOMARTE_JWT_SEGREDO` de 32 caracteres ou mais e `MISSAOMARTE_DADOS=/tmp/marte-qa` → **esperado:** sem o WARN do segredo, banco criado em `/tmp/marte-qa`. Faça login e reinicie com o mesmo segredo: o token continua aceito, ou seja, chamar uma rota inexistente com ele dá `404` e não `401` (R1.6, R6.1, R7.1). Confira que esse 404 vem em `application/problem+json`.
13. Procure no terminal a senha e o token usados → **esperado:** nenhum dos dois aparece no log (R7.5).
14. Leia `fases/fase-05-servidor-ranking/README.md` e `servidor/README.md` e siga-os do zero → **esperado:** tudo o que a tabela da API descreve bate com o que foi observado (R8.1, R8.2, R8.4). As linhas 3-5 do `servidor/README.md` devem dizer que saúde, cadastro e login estão no ar.

## Próximos passos

- APROVADA: QA pelo usuário com o roteiro acima. Depois, o commit único da spec (pendência 8), a conclusão (active/ → done/) e o merge. As pendências 1 a 7 são sugestões: o usuário decide se viram ajuste agora ou se ficam para a spec 14.
