# Verificação final — fase5-servidor-partidas-ranking

**Veredito:** APROVADA
**Branch:** feat/fase5-servidor-partidas-ranking
**Commits da spec:** 9 (aceebf0..c4351f1)

As 4 tarefas estão `[x]` com `status: done`. Nenhum teste da spec 13 foi
alterado na branch (o diff de testes contra `main` só traz arquivos novos).

## Cobertura

Testes em `fases/fase-05-servidor-ranking/servidor/src/test/java/missaomarte/servidor/`.

| Requisito | Critério | Teste(s) | Situação |
|---|---|---|---|
| R1 | 1 | `RegistroPartidaApiTest.r1_1_registroValidoDa201ComOsCamposDaPartida` | coberto |
| R1 | 2 | `RegistroPartidaApiTest.r1_2_partidaFicaComODonoDoTokenEIgnoraCamposAMais` | coberto |
| R1 | 3 | `RegistroPartidaApiTest.r1_3_dataHoraEDoServidorEmUtcIso8601` | coberto |
| R1 | 4 | `r1_4_primeiraVitoriaNumRankingVazioFicaEmPrimeiro`, `r1_4_vitoriaQueEntraEmTerceiroDevolvePosicaoTres` | coberto |
| R1 | 5 | `r1_5_derrota…`, `r1_5_vitoriaComPontuacaoZero…`, `r1_5_vitoriaComPontuacaoNegativa…`, `r1_5_vitoriaPiorQueAMelhorDoProprioJogador…`, `r1_5_vitoriaEmDecimoPrimeiro…` | coberto |
| R1 | 6 | `r1_6_empateComAPropriaAnteriorMantemAAnteriorEDevolveNulo` (+ desempate por `dataHora` em `RankingGlobalTest`) | coberto |
| R1 | 7 | `r1_7_semTokenDa401ENaoRegistra`, `r1_7_tokenInvalidoDa401ENaoRegistra` | coberto |
| R2 | 1 | `ValidacaoPartidaApiTest.r2_1_dificuldadeInvalidaDa400`, `r2_1_resultadoInvalidoDa400`, `r2_1_todasAsDificuldadesEResultadosValidosSaoAceitos` | coberto |
| R2 | 2 | `r2_2_pontuacaoNosLimites…`, `r2_2_duracaoNosLimites…`, `r2_2_movimentosNosLimites…` | coberto |
| R2 | 3 | `r2_3_campoAusenteDa400`, `r2_3_campoNuloDa400` | coberto |
| R2 | 4 | `r2_4_textoNumCampoNumericoDa400`, `r2_4_numeroComCasasDecimaisDa400`, `r2_4_corpoQueNaoEJsonDa400` | coberto |
| R2 | 5 | `r2_5_doisCamposInvalidosDaoDoisItensEmErros` | coberto |
| R3 | 1 | `HistoricoApiTest.r3_1_respondeComPaginaEItensNoFormato` | coberto |
| R3 | 2 | `r3_2_devolveSoAsPartidasDoDono` | coberto |
| R3 | 3 | `r3_3_ordenaPorDataHoraDecrescenteEIdDecrescenteNoEmpate` | coberto |
| R3 | 4 | `r3_4_semParametrosUsaPagina0Tamanho10` | coberto |
| R3 | 5 | `r3_5_devolveAFatiaECertaEOsTotais` | coberto |
| R3 | 6 | `r3_6_paginaAlemDoFimDa200ComItensVazios` | coberto |
| R3 | 7 | `r3_7_paginaNegativa…`, `r3_7_tamanhoForaDaFaixa…`, `r3_7_parametroNaoInteiro…`, `r3_7_limitesDeTamanhoSaoAceitos` | coberto |
| R3 | 8 | `r3_8_semPartidasDa200Vazio` | coberto |
| R3 | 9 | `r3_9_semTokenDa401`, `r3_9_tokenInvalidoDa401` | coberto |
| R4 | 1 | `RankingApiTest.r4_1_linhaTemOsCamposEOsValoresDaPartida` | coberto |
| R4 | 2 | `RankingApiTest.r4_2_…`, `RankingGlobalTest.r4_2_…` | coberto |
| R4 | 3 | `RankingApiTest.r4_3_…`, `RankingGlobalTest.r4_3_…` | coberto |
| R4 | 4 | `RankingGlobalTest.r4_4_*` (pontuação, duração, `dataHora`, `id`, melhor partida do jogador), `RankingApiTest.r4_4_…` | coberto |
| R4 | 5 | `RankingApiTest.r4_5_…`, `RankingGlobalTest.r4_5_cortaEmDez…`, `r4_5_empateTotalNaoRepetePosicao` | coberto |
| R4 | 6 | `RankingApiTest.r4_6_…`, `RankingGlobalTest.r4_6_…` | coberto |
| R4 | 7 | `RankingApiTest.r4_7_linhaTemSomenteAsChavesEsperadas` | coberto |
| R4 | 8 | `r4_8_semTokenDa401`, `r4_8_tokenInvalidoDa401` | coberto |
| R5 | 1 | `RegistroPartidaApiTest.r5_1_partidaSalvaPodeSerLidaComTodosOsCampos` + inspeção (`application.properties`: `jdbc:h2:file:${MISSAOMARTE_DADOS:./dados}/missaomarte`, o mesmo banco da spec 13) | coberto |
| R5 | 2 | verificação manual (QA, passo 9) | coberto (manual) |
| R5 | 3 | inspeção: os controllers devolvem só `record`s de `api/dto` (`PartidaRegistradaResponse`, `PaginaPartidasResponse`, `PartidaResponse`, `LinhaRankingResponse`) | coberto |
| R6 | 1 | inspeção: `servidor/README.md`, tabela da API com as três rotas; corpo e faixas no texto logo abaixo | coberto |
| R6 | 2 | inspeção: `servidor/requests.http` com vitória, derrota, inválido, histórico padrão, página 1, `tamanho=51` e ranking, usando `{{token}}` | coberto |
| R6 | 3 | inspeção: `servidor/README.md`, seção "Limitações" | coberto |
| R6 | 4 | verificação manual (QA, passos 1 a 8), **ainda não executada** | pendente de QA |

## check.sh

passou (`check.sh: tudo passou`; fase 0 intacta, fases 1, 2, 3 e 5 compiladas,
regras de camadas ok; servidor com 107 testes, 0 falhas, 0 pulados).

## Pendências

- Notas da tarefa 4 — o fluxo manual com `bootRun` (R6.4) não foi executado
  por nenhuma sessão; entra no roteiro de QA abaixo, assim como R5.2.
- Notas das tarefas 1 e 2 — desvios do `design.md`, justificados: não houve
  handler novo no `TratadorDeErros` para enum/tipo inválidos no corpo (sai o
  400 padrão do `ResponseEntityExceptionHandler`, sem `erros` e com `detail`
  em inglês); `PartidaController` sem `@Validated` (para receber
  `HandlerMethodValidationException`).
- Revisão 01-r1, sugestão 1 — o 400 de corpo ilegível (enum inválido,
  decimal, JSON malformado) não diz qual campo falhou nem tem mensagem em
  português, diferente do resto da API. Atende R2.1/R2.4, mas o cliente
  online pode precisar disso.
- Revisão 03-r1, sugestão 2 (não aplicada) — `servidor/README.md:4`, "…o
  histórico e o ranking; as demais rotas exigem token" dá a entender que
  histórico e ranking são públicos. Texto sugerido: "Estão no ar a saúde, o
  cadastro, o login, o registro de partidas, o histórico e o ranking. Só
  saúde, cadastro e login são públicos."
- Nova (esta verificação) — `servidor/requests.http`, bloco "Rota protegida
  com o token (exemplo; ainda não existe rota protegida real)": o comentário
  ficou desatualizado; agora existem três rotas protegidas.
- Revisões 01-r1 (3), 02-r1 (2) — sugestões repetidas: os testes de 401/400
  conferem o status, mas nem sempre `application/problem+json` e
  `WWW-Authenticate`. O formato vem da spec 13 e é o mesmo filtro.
- Revisão 01-r1, sugestão 2 — `RegistroPartidaApiTest` emite o token com
  `Instant.now()` e depois fixa o relógio em 2026-03-04; depende da data
  real.
- Revisão 04-r1, sugestão 2 — `PartidaService.registrar` sem
  `@Transactional` entre o `save` e o cálculo do ranking.
- Revisão 02-r1 (5) — isolamento entre classes de teste: resolvido na
  tarefa 4 (`@AfterEach` em `RegistroPartidaApiTest` e
  `ValidacaoPartidaApiTest`, aprovado pelo usuário).

## Roteiro de QA

Pré: JDK 17 em `JAVA_HOME`; exporte `MISSAOMARTE_JWT_SEGREDO` com 32+ bytes
(senão o token deixa de valer ao reiniciar no passo 9) e, para começar
limpo, `MISSAOMARTE_DADOS` apontando para uma pasta nova. Em
`fases/fase-05-servidor-ranking/servidor/`, rode `./gradlew bootRun` e use o
`requests.http` (cliente REST da IDE) ou `curl`.

1. "Cadastro válido" e depois "Login válido" → **esperado:** 201
   `{"id","nome":"Matheus"}`; 200 com `token`, que fica em `@token`.
2. "Registrar vitória" (1200 pontos) → **esperado:** 201 com os 8 campos,
   `dataHora` em UTC terminando em `Z` e `posicaoRanking: 1` (R1.1, R1.3, R1.4).
3. "Registrar derrota" → **esperado:** 201 com `posicaoRanking: null` (R1.5).
4. Repita "Registrar vitória" (mesma pontuação e duração) → **esperado:**
   201 com `posicaoRanking: null`, porque a anterior empata e é mais
   antiga (R1.6).
5. "Registrar partida inválida" (`ABORTADA`, duração −1) → **esperado:**
   400 `application/problem+json`. Depois envie um corpo com
   `"pontuacao": 1.5` e outro com `"duracaoSegundos": 86401` → 400 nos
   dois; e um com `"pontuacao": 100000, "duracaoSegundos": 86400` → 201
   (R2.1, R2.2, R2.4). Envie também `{"usuarioId": 999, "nome": "outro", ...}`
   válido → 201, e a partida aparece no histórico do Matheus (R1.2).
6. "Histórico: padrão" → **esperado:** 200 com as partidas registradas
   (inválidas não aparecem), da mais recente para a mais antiga,
   `pagina 0`, `tamanho 10`, totais certos. "página 1 com 5 itens" → fatia
   certa ou `itens: []` se passar do fim. "tamanho 51" → 400 com `erros`
   citando `tamanho` (R3.1, R3.3–R3.7).
7. Cadastre e faça login com um segundo usuário (ex.: `Bia`), registre uma
   vitória de 1500 → **esperado:** `posicaoRanking: 1`; o histórico da Bia
   mostra só a partida dela (R3.2, R1.4).
8. "Ranking global" → **esperado:** 200 com Bia em 1º (1500) e Matheus em 2º
   (1200), uma linha cada, só com `posicao, nome, pontuacao, dificuldade,
   duracaoSegundos, movimentos, dataHora`; `nome` com a caixa do cadastro
   (R4.1, R4.3, R4.7). Sem o cabeçalho `Authorization` (ou com token
   adulterado) nas três rotas → 401 `ProblemDetail` (R1.7, R3.9, R4.8).
9. Pare o servidor (Ctrl+C), suba de novo com o mesmo
   `MISSAOMARTE_DADOS`/segredo, faça login e repita histórico e ranking →
   **esperado:** as mesmas partidas e o mesmo ranking (R5.2).
10. Leia o `servidor/README.md`: tabela com as três rotas e seção
    "Limitações" (R6.1, R6.3); confira se a frase da linha 4 sobre rotas
    públicas está aceitável (ver Pendências).

Os passos 1 a 8 cobrem R6.4.

## Próximos passos

- APROVADA: QA pelo usuário (sobretudo R6.4 e R5.2, que nenhum teste
  automático cobre), merge, e conclusão da spec (active/ → done/).
- Opcional, a critério do usuário: ajuste pequeno no `README.md:4` e no
  comentário do `requests.http`, e decidir se o 400 de corpo ilegível
  precisa de `erros` em português antes do cliente online (Planejador).
