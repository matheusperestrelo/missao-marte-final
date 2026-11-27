> **Concluída em 2026-11-26.** Diferenças do planejado: implementada fora do workflow no PR #38, sem `reviews/`; regularizada depois. `RankingTop5` e `RankingTop5Test` removidos da Fase 3 como previsto; o `RankingService` fica em `application.ranking` com 12 testes em `RankingServiceTest`.

# Requisitos: fase3-ranking-service

**Issue:** #15 · **Trilha:** A · **Fase:** 3 · **Depende de:** 8 (`fase3-estrutura-libgdx`)
**Branch sugerida:** `feat/issue-15-fase3-ranking-service` · **PR:** "Closes #15"

## Contexto

Na Fase 2, a regra do ranking é o `RankingTop5`, que guarda as 5 melhores
vitórias. A Fase 3 muda o limite para **10** (mudança intencional,
`ESCOPO-PROJETO.md`, seção 8) e organiza o ranking num subpacote
`application.ranking` com um `RankingService`. As telas da Fase 3
(specs 10 e 11) consultam, registram e limpam o ranking só por esse
serviço, sem decidir nada.

Base: issue #15, `ESCOPO-PROJETO.md`, seção 8, "Ranking vai para a
aplicação", e o resumo do alinhamento da Fase 3.

## Fora de escopo

- Telas que mostram, registram ou limpam o ranking: specs 10 e 11.
- Mudar o formato do `ranking.json` ou o `FileRankingRepository` além do
  `import` dos tipos movidos: o arquivo continua compatível com as Fases 0
  a 2.
- "Seria novo recorde" (`seriaNovoRecorde`): só o console usava, e o
  console não existe na Fase 3. Decisão do alinhamento (remoção do console).
- Manter o `RankingTop5` ao lado do serviço: decidido no alinhamento que
  ele é removido, para não haver duas regras de ranking no mesmo módulo.
- Alterar a Fase 2: o Top 5 continua lá.
- Ranking global (servidor): Fase 5.

## Requisitos

### R1 — Ranking em `application.ranking`

**História:** Como integrante, quero os tipos do ranking agrupados num
subpacote da aplicação, para separar a regra do ranking da regra da
partida.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter o repositório do ranking (porta), a entrada do
   ranking e a exceção do ranking no pacote `missaomarte.application.ranking`,
   e nenhum deles em `missaomarte.application`. _(verificação: inspeção)_
2. O SISTEMA DEVE manter a implementação em arquivo em
   `missaomarte.infrastructure.ranking`, lendo e gravando o mesmo formato
   de antes. _(verificação: `FileRankingRepositoryTest` passando sem mudar
   as asserções)_
3. O SISTEMA NÃO DEVE ter o `RankingTop5` nem o `RankingTop5Test` na
   Fase 3. _(verificação: inspeção)_

### R2 — Listar o Top 10

**Critérios de aceitação:**

1. QUANDO o Top 10 for pedido O SISTEMA DEVE devolver as entradas
   gravadas em ordem decrescente de pontuação, com no máximo 10.
   _(verificação: teste)_
2. QUANDO não houver entradas O SISTEMA DEVE devolver uma lista vazia.
   _(verificação: teste)_
3. QUANDO o recorde for pedido O SISTEMA DEVE devolver a primeira entrada
   do Top 10, ou nada se o ranking estiver vazio. _(verificação: teste)_

### R3 — Registrar uma partida

**História:** Como jogador, quero que minha vitória entre no Top 10 quando
eu fizer pontos suficientes, e saber se entrei.

**Critérios de aceitação:**

1. SE a partida não for vitória ENTÃO O SISTEMA DEVE recusar o registro,
   informar que não entrou e não gravar nada. _(verificação: teste)_
2. SE a pontuação for menor ou igual a 0 ENTÃO O SISTEMA DEVE recusar o
   registro, informar que não entrou e não gravar nada.
   _(verificação: teste)_
3. QUANDO houver menos de 10 entradas e a partida for uma vitória com
   pontuação positiva O SISTEMA DEVE gravar a entrada e informar que
   entrou. _(verificação: teste)_
4. QUANDO houver 10 entradas O SISTEMA DEVE aceitar só uma pontuação
   **maior** que a da 10ª. Pontuação igual ou menor é recusada, sem gravar.
   Ao aceitar, a 10ª sai e a lista continua com 10. _(verificação: teste)_
5. QUANDO a nova entrada empatar em pontos com entradas existentes O
   SISTEMA DEVE colocá-la **depois** delas (as antigas ficam na frente),
   como no Top 5 da Fase 2. _(verificação: teste)_
6. QUANDO a entrada for gravada O SISTEMA DEVE preencher nome do piloto,
   pontuação, dificuldade, passageiros entregues, tempo de jogo e a data e
   hora do relógio injetado, no formato `aaaa-MM-dd HH:mm:ss`.
   _(verificação: teste com relógio fixo)_

### R4 — Limpar o ranking

**Critérios de aceitação:**

1. QUANDO o ranking for limpo O SISTEMA DEVE apagar todas as entradas, e o
   Top 10 seguinte deve vir vazio. _(verificação: teste)_

### R5 — Falha da persistência

**Critérios de aceitação:**

1. SE o repositório falhar ao gravar ou ao limpar ENTÃO O SISTEMA DEVE
   propagar a exceção do ranking com a mensagem original, para a tela
   mostrar o erro. _(verificação: teste com repositório que falha)_

### R6 — Composição e regressão

**Critérios de aceitação:**

1. O SISTEMA DEVE criar o serviço de ranking no launcher (com o
   repositório em arquivo e o relógio) e entregá-lo à classe do jogo pelo
   construtor, no lugar do repositório. _(verificação: inspeção)_
2. O SISTEMA DEVE ter testes com repositório **em memória** para todos os
   critérios de R2 a R5. Eles levam para o limite de 10 todos os casos que
   o `RankingTop5Test` cobria: recusa de derrota e de pontuação ≤ 0,
   limite, ordem e empate, data do relógio, recorde, listar e limpar, e
   falha do repositório. _(verificação: inspeção + gate)_
3. QUANDO `./gradlew test` rodar O SISTEMA DEVE passar todos os demais
   testes vindos da Fase 2 sem mudar as asserções deles. Só os `import`
   dos tipos movidos podem mudar. _(verificação: gate + `git diff`)_
4. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar.
   _(verificação: gate)_
