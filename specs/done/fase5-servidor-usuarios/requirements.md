> **Concluída em 2026-11-24.** Diferenças do planejado: a `ConfiguracaoSeguranca` usa um `BearerTokenResolver` que ignora o token nas 4 rotas públicas (sem isso, token inválido dava 401 na saúde e quebrava R2.2), e o `TratadorDeErros` ganhou um handler de `AuthenticationException` para o `sub` inexistente dar 401 e não 500; a tarefa 5 foi acrescentada por ajuste aprovado na verificação final para testar o 404 e o 500 de R7.1 e corrigir o README. Ficaram como sugestões abertas (ver `reviews/final.md`): exemplos com `/api/qualquer-coisa` no `requests.http` (para a spec 14), teste `r5_2` duplicado, 401 de token montado em dois lugares e testes extras não exigidos (409 por concorrência, `toString` de `TokenEmitido`, R6.1 pelo valor configurado). Por decisão do usuário, a partir da rodada 2 da tarefa 1 tudo foi commitado num único commit no fim da spec.

# Requisitos: fase5-servidor-usuarios

**Issue:** #20 · **Trilha:** C · **Fase:** 5 · **Depende de:** —
**Branch sugerida:** `feat/issue-20-fase5-servidor-usuarios`

## Contexto

A Fase 5 adiciona um ranking global com cadastro e login. Esta é a
primeira spec da fase: ela cria `fases/fase-05-servidor-ranking/` só com o
`servidor/`, um projeto Spring Boot independente do cliente, com cadastro
de usuário, login por token JWT, verificação de saúde e todas as outras
rotas protegidas por padrão. A spec 14 (`fase5-servidor-partidas-ranking`)
acrescenta partidas, histórico e ranking em cima desta base. O cliente
vem depois da Fase 4 (backlog, `briefing-fase5`).

Ela não depende da Fase 3 nem da Fase 4 e pode ser implementada em
paralelo a elas.

Base: `ESCOPO-PROJETO.md`, seções 3, 5, 9 ("Servidor") e 14;
`specs/planned/briefing-fase5/`; enunciado (Sprint 3); resumo do
alinhamento da Fase 5 (decisões registradas em `design.md`).

## Fora de escopo

- Partidas, histórico e ranking global: spec 14
  (`fase5-servidor-partidas-ranking`).
- Pasta `cliente/` e qualquer mudança no jogo: specs do cliente, ainda no
  backlog (`briefing-fase5`), depois da Fase 4.
- Alterar qualquer arquivo das Fases 0 a 4.
- Tutorial `docs/fase-05-tutorial.md`: vira um ajuste de documentação
  depois que o cliente estiver pronto.
- Swagger/springdoc, Docker, `DEPLOY.md`: cortes já decididos.
- Endereço e porta por variável de ambiente, nome fixo do JAR e
  encerramento gracioso: Fase 6 (`briefing-fase6`, `fase6-android-build`).
- Troca de senha, exclusão de conta, recuperação de senha, papéis/perfis,
  renovação de token (refresh) e limite de tentativas de login.
- Banco ou autenticação externos (ex.: Supabase): descartado no
  alinhamento, porque o enunciado pede JPA + H2 com hash e token próprios.
- Trocar o gate (`.workflow/check.sh`): ele já detecta `servidor/` sozinho.

## Requisitos

### R1 — Projeto do servidor independente

**História:** Como integrante da equipe, quero o servidor num build
Gradle próprio, para que ele evolua e seja testado sem depender do código
do cliente.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `fases/fase-05-servidor-ranking/` com `README.md`,
   `.gitignore` (`build/`, `.gradle/`, `out/`, `ranking.json`,
   `local.properties`, `dados/`) e a pasta `servidor/`, **sem** a pasta
   `cliente/`. _(verificação: inspeção)_
2. O SISTEMA DEVE ter em `servidor/` um build Gradle com o Wrapper
   **9.8.2**, Spring Boot **4.1.2** e Java **17**, que não referencia
   nenhum arquivo fora de `servidor/`. _(verificação: inspeção de
   `gradle/wrapper/gradle-wrapper.properties` e do `build.gradle`)_
3. QUANDO o `.workflow/check.sh` for executado O SISTEMA DEVE rodar
   `./gradlew clean build` em `servidor/` com JDK 17 e passar, junto com a
   regra de camadas. _(verificação: gate)_
4. O SISTEMA NÃO DEVE alterar nada em `fases/fase-00-*` a `fases/fase-04-*`.
   _(verificação: `git diff main --stat` da branch)_
5. QUANDO o servidor for iniciado sem variáveis de ambiente O SISTEMA DEVE
   escutar só em `127.0.0.1`, na porta `8080`. _(verificação: inspeção da
   configuração + execução manual)_
6. O SISTEMA DEVE guardar o banco H2 em arquivo dentro da pasta indicada
   por `MISSAOMARTE_DADOS` e, sem a variável, em `./dados` relativo à
   pasta de onde o servidor é iniciado. _(verificação: inspeção +
   execução manual: o arquivo aparece na pasta)_
7. QUANDO o servidor for reiniciado O SISTEMA DEVE manter os usuários já
   cadastrados. _(verificação: manual)_

### R2 — Verificação de saúde

**História:** Como quem opera o servidor, quero uma rota pública de
saúde, para saber se ele está no ar sem precisar de conta.

**Critérios de aceitação:**

1. QUANDO `GET /api/saude` for chamado sem token O SISTEMA DEVE responder
   200 com o corpo `{"status":"ok"}`. _(verificação: teste MockMvc)_
2. QUANDO `GET /api/saude` for chamado com um token inválido no cabeçalho
   O SISTEMA DEVE responder 200 do mesmo jeito. _(verificação: teste
   MockMvc)_

### R3 — Cadastro de usuário

**História:** Como jogador, quero criar uma conta com nome e senha, para
aparecer no ranking global.

**Critérios de aceitação:**

1. QUANDO `POST /api/usuarios` receber `{"nome","senha"}` válidos O
   SISTEMA DEVE responder 201 com `{"id","nome"}`, onde `nome` é o nome
   exatamente como foi enviado. _(verificação: teste MockMvc)_
2. O SISTEMA NÃO DEVE devolver a senha nem o hash em nenhuma resposta do
   cadastro. _(verificação: teste MockMvc confere que o corpo só tem `id`
   e `nome`)_
3. QUANDO um usuário for cadastrado O SISTEMA DEVE guardar a senha só
   como hash BCrypt de custo ≥ 10, nunca o texto. _(verificação: teste lê
   o usuário salvo e confere o prefixo `$2a$`/`$2b$`/`$2y$`, o custo e que
   o hash confere com a senha)_
4. QUANDO dois usuários forem cadastrados com a mesma senha O SISTEMA DEVE
   guardar hashes diferentes. _(verificação: teste)_
5. O SISTEMA DEVE guardar também o nome normalizado (o nome em
   minúsculas) e a data de criação do usuário. _(verificação: teste lê o
   usuário salvo)_
6. SE já existir um usuário com o mesmo nome normalizado (ex.: `Matheus`
   já existe e chega `matheus` ou `MATHEUS`) ENTÃO O SISTEMA DEVE
   responder 409 em `ProblemDetail` e não criar outro usuário.
   _(verificação: teste MockMvc + contagem no banco)_
7. SE o nome não tiver de 3 a 20 caracteres formados só por letras sem
   acento (`A–Z`, `a–z`), números, `_` e `-` ENTÃO O SISTEMA DEVE responder
   400. Casos que precisam de teste: 2 e 21 caracteres (recusados), 3 e 20
   (aceitos), espaço, acento (`João`), caractere especial, vazio e
   ausente. _(verificação: testes MockMvc)_
8. SE a senha tiver menos de 8 caracteres ENTÃO O SISTEMA DEVE responder
   400. _(verificação: testes com 7 e 8 caracteres)_
9. SE a senha tiver mais de 72 bytes em UTF-8 ENTÃO O SISTEMA DEVE
   responder 400, mesmo que tenha 72 caracteres ou menos. _(verificação:
   testes com 72 bytes (aceita), 73 bytes (recusa) e uma senha com
   caracteres acentuados de até 72 caracteres e mais de 72 bytes (recusa))_
10. SE a senha estiver ausente ou vazia, ou o corpo não for um JSON
    válido ENTÃO O SISTEMA DEVE responder 400. _(verificação: testes
    MockMvc)_

### R4 — Login

**História:** Como jogador cadastrado, quero entrar com nome e senha e
receber um token, para usar as rotas protegidas.

**Critérios de aceitação:**

1. QUANDO `POST /api/auth/login` receber nome e senha corretos O SISTEMA
   DEVE responder 200 com `{"token","tipo":"Bearer","expiraEmSegundos":7200}`.
   _(verificação: teste MockMvc)_
2. QUANDO o login for feito com o nome em outra caixa (cadastrado
   `Matheus`, login `MATHEUS`) e a senha correta O SISTEMA DEVE aceitar o
   login. _(verificação: teste MockMvc)_
3. A senha DEVE diferenciar maiúsculas de minúsculas: SE a senha vier com
   outra caixa ENTÃO O SISTEMA DEVE recusar o login. _(verificação: teste)_
4. O token emitido DEVE ser um JWT assinado com HS256, com `sub` igual ao
   `id` do usuário, `iat` e `exp` = `iat` + 2 h, e **sem** senha, hash ou
   nome normalizado nas declarações. _(verificação: teste decodifica o
   token e confere as declarações)_
5. SE o usuário não existir ou a senha estiver errada ENTÃO O SISTEMA DEVE
   responder 401 em `ProblemDetail` com **exatamente o mesmo corpo** nos
   dois casos, com o `detail` "Nome ou senha inválidos." _(verificação:
   teste MockMvc compara os dois corpos)_
6. QUANDO o usuário não existir O SISTEMA DEVE ainda assim conferir a
   senha contra um hash BCrypt falso, para o tempo de resposta ser
   parecido com o de senha errada. _(verificação: teste de unidade do
   serviço confere que o verificador de senha foi chamado no caso de
   usuário inexistente)_
7. SE nome ou senha estiverem ausentes ou vazios ENTÃO O SISTEMA DEVE
   responder 400 em `ProblemDetail`. _(verificação: teste MockMvc)_

### R5 — Rotas protegidas por padrão

**História:** Como equipe, quero que toda rota exija token, salvo as
liberadas de propósito, para que uma rota nova nunca nasça aberta por
esquecimento.

**Critérios de aceitação:**

1. O SISTEMA DEVE liberar sem token só `POST /api/usuarios`,
   `POST /api/auth/login`, `GET /api/saude` e `/error`. _(verificação:
   inspeção da configuração + testes)_
2. QUANDO qualquer outra rota (existente ou não) for chamada sem token O
   SISTEMA DEVE responder 401. _(verificação: teste MockMvc numa rota
   protegida de teste e numa rota inexistente)_
3. SE o token for malformado, tiver assinatura errada ou estiver expirado
   ENTÃO O SISTEMA DEVE responder 401. _(verificação: um teste MockMvc por
   caso; o expirado é gerado com relógio no passado)_
4. SE o token for válido mas o `sub` não corresponder a nenhum usuário
   existente ENTÃO O SISTEMA DEVE responder 401. _(verificação: teste
   MockMvc)_
5. QUANDO o token for válido e o usuário existir O SISTEMA DEVE deixar a
   requisição chegar à rota, que recebe o `id` do usuário autenticado.
   _(verificação: teste MockMvc numa rota protegida que só existe nos
   testes e devolve o `id` recebido)_
6. Todo 401 (sem token, token ruim, usuário inexistente, login errado)
   DEVE vir como `ProblemDetail` (`Content-Type:
   application/problem+json`, com `status` e `title`). Nos 401 de token, o
   `detail` DEVE ser sempre o mesmo, "Autenticação necessária ou token
   inválido.", sem dizer o motivo, e a resposta DEVE trazer o cabeçalho
   `WWW-Authenticate` começando por `Bearer`. _(verificação: testes
   MockMvc conferem o tipo, o `detail` e o cabeçalho nos três casos de
   token do critério 3 e no caso sem token)_
7. O SISTEMA NÃO DEVE criar sessão HTTP nem enviar cookie em nenhuma
   resposta. _(verificação: teste MockMvc confere a ausência de
   `Set-Cookie` no login e numa rota protegida)_
8. QUANDO um `POST` público for feito sem token CSRF O SISTEMA DEVE
   aceitá-lo (CSRF desligado, porque a API não usa cookie). _(verificação:
   os testes de cadastro e login já cobrem)_

### R6 — Segredo do JWT

**História:** Como quem opera o servidor, quero definir o segredo do
token por variável de ambiente, para que ele nunca fique no repositório e
os tokens continuem válidos depois de reiniciar.

**Critérios de aceitação:**

1. QUANDO `MISSAOMARTE_JWT_SEGREDO` tiver 32 bytes ou mais em UTF-8 O
   SISTEMA DEVE assinar e validar os tokens com esse segredo.
   _(verificação: teste: token assinado com o segredo configurado é
   aceito)_
2. SE `MISSAOMARTE_JWT_SEGREDO` estiver ausente ou vazia ENTÃO O SISTEMA
   DEVE subir com um segredo aleatório de pelo menos 32 bytes e registrar
   um aviso no log dizendo que os tokens deixam de valer ao reiniciar.
   _(verificação: teste de unidade da resolução do segredo + teste
   captura o log)_
3. SE `MISSAOMARTE_JWT_SEGREDO` tiver de 1 a 31 bytes ENTÃO O SISTEMA NÃO
   DEVE subir, e a falha DEVE trazer a mensagem
   "MISSAOMARTE_JWT_SEGREDO precisa ter pelo menos 32 bytes."
   _(verificação: teste de unidade da resolução do segredo; teste com 31
   e 32 bytes)_
4. O SISTEMA NÃO DEVE escrever o segredo, nem parte dele, em log, mensagem
   de erro ou resposta. _(verificação: os testes dos critérios 2 e 3
   conferem que o valor não aparece na mensagem nem no log capturado)_
5. O repositório NÃO DEVE conter nenhum segredo de JWT: nem em
   `application.properties`, nem nos testes, nem no `requests.http`. Os
   testes usam um segredo gerado na hora ou um valor visivelmente de
   teste só em `src/test/resources`. _(verificação: inspeção)_

### R7 — Erros e dados sensíveis

**História:** Como desenvolvedor do cliente, quero erros num formato
único e previsível, para mostrar ao jogador uma mensagem útil sem expor
dados.

**Critérios de aceitação:**

1. Toda resposta de erro (400, 401, 404, 409, 500) DEVE ser um
   `ProblemDetail` (RFC 9457) com `Content-Type:
   application/problem+json`. _(verificação: testes MockMvc dos erros
   acima conferem o tipo)_
2. QUANDO a validação falhar O SISTEMA DEVE responder 400 com uma
   propriedade `erros`, uma lista de `{"campo","mensagem"}`, com um item
   por campo inválido. _(verificação: teste com nome e senha inválidos ao
   mesmo tempo espera dois itens)_
3. O SISTEMA NÃO DEVE incluir o valor enviado da senha em nenhuma
   mensagem de erro. _(verificação: teste de senha inválida confere que o
   valor não aparece no corpo)_
4. O `toString()` dos dados de cadastro e de login NÃO DEVE mostrar a
   senha. _(verificação: teste de unidade)_
5. O SISTEMA NÃO DEVE registrar senha, hash, token ou segredo em log.
   _(verificação: inspeção + teste captura o log de um cadastro e um login
   e confere que a senha e o token não aparecem)_
6. Nenhuma resposta da API DEVE ser uma entidade JPA: sempre um `record`
   de resposta. _(verificação: inspeção)_
7. QUANDO ocorrer um erro inesperado O SISTEMA DEVE responder 500 em
   `ProblemDetail` com `detail` genérico, sem pilha de chamadas nem
   mensagem interna. _(verificação: inspeção da configuração)_

### R8 — Documentação de uso

**História:** Como integrante ou professor, quero saber como rodar e
chamar o servidor, para testar sem ler o código.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `fases/fase-05-servidor-ranking/README.md`
   explicando o que a fase contém hoje (só o servidor; o cliente chega
   depois) e por que cliente e servidor são dois builds independentes.
   _(verificação: inspeção)_
2. O SISTEMA DEVE ter `servidor/README.md` com: requisitos (JDK 17), como
   rodar (`./gradlew bootRun`) e testar (`./gradlew test`), as variáveis
   `MISSAOMARTE_DADOS` e `MISSAOMARTE_JWT_SEGREDO` (com o efeito da
   ausência e o mínimo de 32 bytes), o endereço `127.0.0.1:8080`, por que
   `ddl-auto=update` não serve para produção, e uma tabela da API com as
   rotas desta spec (método, rota, token, respostas). _(verificação:
   inspeção)_
3. O SISTEMA DEVE ter `servidor/requests.http` com chamadas de exemplo
   para saúde, cadastro (válido, repetido e inválido) e login (certo e
   errado), usando só dados de teste e guardando o token do login numa
   variável do arquivo, nunca escrito à mão. _(verificação: inspeção)_
4. QUANDO os passos do `servidor/README.md` forem seguidos numa máquina
   com JDK 17 O SISTEMA DEVE subir e responder às chamadas do
   `requests.http` como a tabela descreve. _(verificação: QA manual)_
