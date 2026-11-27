> **Concluída em 2026-11-26.** Diferenças do planejado: o `PartidaController` valida `pagina`/`tamanho` com `@Min`/`@Max` direto nos `@RequestParam`, sem `@Validated` na classe (o `TratadorDeErros` trata a `HandlerMethodValidationException` e o `TypeMismatch` com 400 e `erros`); o Jackson passou a recusar decimal como inteiro (`accept-float-as-int=false` no `application.properties` principal e no de teste); por ajuste aprovado pelo usuário na tarefa 4, `RegistroPartidaApiTest` e `ValidacaoPartidaApiTest` ganharam `@AfterEach` limpando partidas e a asserção de `r1_1` passou de `posicaoRanking` null (provisório da tarefa 1) para 1. Ficaram como pendências abertas (ver `reviews/final.md`): a frase da linha 4 do `servidor/README.md` sugere que histórico e ranking são públicos, comentário desatualizado no `requests.http` ("ainda não existe rota protegida real"), 400 de corpo ilegível (enum inválido, decimal, JSON malformado) sem lista `erros` e com `detail` em inglês, testes de erro que só conferem o status, token de teste dependente da data real e falta de `@Transactional` em `PartidaService.registrar`.

# Requisitos: fase5-servidor-partidas-ranking

**Issue:** #22 · **Trilha:** C · **Fase:** 5 · **Depende de:** 13 (`fase5-servidor-usuarios`)
**Branch sugerida:** `feat/issue-22-fase5-servidor-partidas-ranking`

## Contexto

A spec 13 criou o servidor com cadastro, login e rotas protegidas. Esta
spec completa a API do servidor da Fase 5: o jogador logado registra as
partidas, consulta o próprio histórico paginado e vê o ranking global
Top 10 (uma linha por jogador, com a melhor vitória). Na resposta do
registro, o servidor já diz se a partida entrou no Top 10, para que o
cliente (no backlog, depois da Fase 4) mostre isso com uma chamada só.

Base: `ESCOPO-PROJETO.md`, seção 9 ("Servidor"); enunciado (Sprint 3);
`specs/active/fase5-servidor-usuarios/`; resumo do alinhamento da Fase 5
(decisões registradas em `design.md`).

## Fora de escopo

- Cadastro, login, saúde, segredo do JWT e formato do 401: spec 13.
- Cliente (porta `ServidorDoJogo`, adaptador HTTP, telas online): specs do
  cliente no backlog (`briefing-fase5`).
- Tela "Meu histórico" no cliente: corte já decidido. A rota existe e é
  testada aqui.
- Validar se a pontuação é legítima (reexecutar a partida no servidor):
  limitação obrigatória, só documentada (R6).
- Partida abortada: não é enviada pelo cliente e não é um valor aceito.
- Editar ou apagar partidas, ranking por dificuldade, ranking paginado,
  ranking público sem token.
- Swagger, Docker, `DEPLOY.md`, tutorial da fase.
- Alterar qualquer arquivo fora de `fases/fase-05-servidor-ranking/servidor/`,
  exceto o `README.md` da fase se precisar citar as rotas novas.

## Requisitos

### R1 — Registrar uma partida

**História:** Como jogador logado, quero enviar o resultado de cada
partida, para que ela conte no meu histórico e no ranking global.

**Critérios de aceitação:**

1. QUANDO `POST /api/partidas` receber, com token válido,
   `{"dificuldade","resultado","pontuacao","duracaoSegundos","movimentos"}`
   válidos O SISTEMA DEVE responder 201 com
   `{"id","dificuldade","resultado","pontuacao","duracaoSegundos","movimentos","dataHora","posicaoRanking"}`.
   _(verificação: teste MockMvc)_
2. O SISTEMA DEVE associar a partida ao usuário do token, e NÃO a nenhum
   usuário ou nome enviado no corpo. QUANDO o corpo trouxer campos a mais
   (ex.: `usuarioId`, `nome`, `id`, `dataHora`) O SISTEMA DEVE ignorá-los e
   registrar normalmente. _(verificação: teste envia `usuarioId` de outro
   usuário e confere que a partida ficou com o dono do token)_
3. O SISTEMA DEVE preencher `dataHora` com o momento do registro no
   servidor, em UTC no formato ISO-8601. _(verificação: teste com `Clock`
   fixo)_
4. QUANDO a partida registrada for, logo depois de salva, a linha do
   jogador no ranking global (R4) O SISTEMA DEVE devolver em
   `posicaoRanking` a posição dela (1 a 10). _(verificação: testes:
   primeira vitória num ranking vazio → 1; vitória que entra em 3º → 3)_
5. QUANDO a partida não for a linha do jogador no ranking O SISTEMA DEVE
   devolver `posicaoRanking: null`. Casos que precisam de teste: derrota;
   vitória com pontuação 0 ou negativa; vitória pior que a melhor vitória
   do próprio jogador; vitória que fica em 11º ou abaixo. _(verificação:
   um teste por caso)_
6. QUANDO a partida empatar com a melhor vitória anterior do próprio
   jogador (mesma pontuação e mesma duração) O SISTEMA DEVE manter a
   anterior no ranking (a mais antiga) e devolver `posicaoRanking: null`.
   _(verificação: teste)_
7. SE `POST /api/partidas` for chamado sem token ou com token inválido
   ENTÃO O SISTEMA DEVE responder 401 (formato da spec 13) e não registrar
   nada. _(verificação: teste MockMvc + contagem no banco)_

### R2 — Validar a partida

**História:** Como equipe, quero recusar dados absurdos ou incompletos,
para que o histórico e o ranking não guardem lixo.

**Critérios de aceitação:**

1. SE `dificuldade` não for `FACIL`, `MEDIO` ou `DIFICIL`, ou
   `resultado` não for `VITORIA` ou `DERROTA` (incluindo `ABORTADA`,
   minúsculas e texto vazio) ENTÃO O SISTEMA DEVE responder 400 em
   `ProblemDetail`. _(verificação: testes MockMvc)_
2. SE `pontuacao` estiver fora de −100000 a 100000, `duracaoSegundos` fora
   de 0 a 86400 ou `movimentos` fora de 0 a 100000 ENTÃO O SISTEMA DEVE
   responder 400. Os limites são aceitos. _(verificação: testes com cada
   limite e um valor logo fora de cada lado)_
3. SE qualquer um dos cinco campos estiver ausente ou `null` ENTÃO O
   SISTEMA DEVE responder 400, e nunca registrar o campo como 0.
   _(verificação: um teste por campo)_
4. SE um campo numérico vier com tipo errado (ex.: texto, número com
   casas decimais) ou o corpo não for JSON válido ENTÃO O SISTEMA DEVE
   responder 400. _(verificação: testes MockMvc)_
5. QUANDO a validação falhar O SISTEMA DEVE usar o mesmo formato de erro
   da spec 13 (`ProblemDetail` com `erros` `[{campo, mensagem}]` para
   campos inválidos) e não registrar nada. _(verificação: teste com dois
   campos inválidos espera dois itens + contagem no banco)_

### R3 — Histórico do jogador

**História:** Como jogador logado, quero ver as minhas partidas, da mais
recente para a mais antiga, em páginas.

**Critérios de aceitação:**

1. QUANDO `GET /api/partidas/minhas` for chamado com token válido O
   SISTEMA DEVE responder 200 com
   `{"itens":[...],"pagina","tamanho","totalItens","totalPaginas"}`, onde
   cada item tem `id`, `dificuldade`, `resultado`, `pontuacao`,
   `duracaoSegundos`, `movimentos` e `dataHora`. _(verificação: teste
   MockMvc)_
2. O SISTEMA DEVE devolver só as partidas do dono do token, nunca as de
   outros usuários. _(verificação: teste com dois usuários)_
3. O SISTEMA DEVE ordenar da mais recente para a mais antiga pela
   `dataHora`; com `dataHora` igual, a de maior `id` primeiro.
   _(verificação: teste com `Clock` controlado)_
4. QUANDO `pagina` e `tamanho` forem omitidos O SISTEMA DEVE usar
   `pagina=0` e `tamanho=10`. _(verificação: teste)_
5. QUANDO `pagina` e `tamanho` forem informados O SISTEMA DEVE devolver a
   fatia certa e os totais certos. A primeira página é a 0.
   _(verificação: teste com 23 partidas e `tamanho=10`: páginas 0, 1 e 2
   com 10, 10 e 3 itens, `totalPaginas=3`)_
6. QUANDO `pagina` passar do fim O SISTEMA DEVE responder 200 com
   `itens: []` e os totais certos. _(verificação: teste)_
7. SE `pagina < 0`, `tamanho < 1`, `tamanho > 50` ou um deles não for um
   número inteiro ENTÃO O SISTEMA DEVE responder 400 em `ProblemDetail`
   dizendo qual parâmetro está errado. `tamanho=1` e `tamanho=50` são
   aceitos. _(verificação: testes MockMvc)_
8. QUANDO o jogador não tiver partidas O SISTEMA DEVE responder 200 com
   `itens: []`, `totalItens: 0` e `totalPaginas: 0`. _(verificação:
   teste)_
9. SE for chamado sem token ou com token inválido ENTÃO O SISTEMA DEVE
   responder 401. _(verificação: teste)_

### R4 — Ranking global

**História:** Como jogador logado, quero ver os 10 melhores jogadores, para
comparar a minha melhor vitória com a dos outros.

**Critérios de aceitação:**

1. QUANDO `GET /api/ranking` for chamado com token válido O SISTEMA DEVE
   responder 200 com uma lista de linhas
   `{"posicao","nome","pontuacao","dificuldade","duracaoSegundos","movimentos","dataHora"}`,
   onde `nome` é o nome como foi cadastrado. _(verificação: teste MockMvc)_
2. O SISTEMA DEVE considerar só partidas com `resultado = VITORIA` e
   `pontuacao > 0`. _(verificação: testes: derrota com pontuação alta e
   vitória com 0 ficam fora)_
3. O SISTEMA DEVE ter no máximo **uma linha por jogador**, com a melhor
   partida dele pelo critério do item 4. _(verificação: teste com um
   jogador com três vitórias)_
4. O SISTEMA DEVE ordenar por pontuação decrescente; empate por menor
   `duracaoSegundos`; depois pela partida mais antiga (`dataHora`
   crescente e, se igual, menor `id`). O mesmo critério escolhe a melhor
   partida de cada jogador. _(verificação: um teste para cada nível de
   desempate)_
5. O SISTEMA DEVE devolver no máximo 10 linhas, com `posicao` de 1 a N,
   sem repetir posição mesmo em empate total. _(verificação: teste com 12
   jogadores)_
6. QUANDO não houver nenhuma vitória com pontuação > 0 O SISTEMA DEVE
   responder 200 com `[]`. _(verificação: teste)_
7. O SISTEMA NÃO DEVE incluir no ranking `id` de usuário, `id` de
   partida, nome normalizado, hash ou qualquer outro campo além dos do
   item 1. _(verificação: teste confere as chaves exatas de uma linha)_
8. SE for chamado sem token ou com token inválido ENTÃO O SISTEMA DEVE
   responder 401. _(verificação: teste)_

### R5 — Persistência das partidas

**História:** Como jogador, quero que as minhas partidas continuem lá
depois de o servidor reiniciar.

**Critérios de aceitação:**

1. O SISTEMA DEVE guardar cada partida com usuário, dificuldade,
   resultado, pontuação, duração em segundos, movimentos e data/hora, no
   mesmo banco H2 em arquivo da spec 13. _(verificação: inspeção +
   teste lê a partida salva)_
2. QUANDO o servidor for reiniciado O SISTEMA DEVE manter as partidas, o
   histórico e o ranking. _(verificação: manual)_
3. Nenhuma resposta DEVE ser uma entidade JPA: sempre `record`s de
   resposta. _(verificação: inspeção)_

### R6 — Documentação e limitação

**História:** Como integrante ou professor, quero ver todas as rotas
documentadas e a limitação de segurança declarada.

**Critérios de aceitação:**

1. O SISTEMA DEVE acrescentar à tabela da API do `servidor/README.md` as
   rotas `POST /api/partidas`, `GET /api/partidas/minhas` e
   `GET /api/ranking`, com corpo, parâmetros, faixas válidas e respostas.
   _(verificação: inspeção)_
2. O SISTEMA DEVE acrescentar ao `servidor/requests.http` exemplos de
   registro (vitória, derrota, inválido), histórico (padrão, página 1,
   `tamanho=51`) e ranking, usando o token guardado do login.
   _(verificação: inspeção)_
3. O `servidor/README.md` DEVE ter uma seção "Limitações" dizendo que o
   servidor não executa a lógica do jogo e não consegue provar que a
   pontuação enviada é legítima, e que as validações só barram valores
   absurdos. _(verificação: inspeção)_
4. QUANDO o fluxo cadastro → login → registrar partidas → histórico →
   ranking for seguido pelo `requests.http` O SISTEMA DEVE responder como
   a tabela descreve. _(verificação: QA manual)_
