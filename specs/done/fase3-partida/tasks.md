# Tarefas: fase3-partida

- [x] 1. Jogar turnos na janela: tecla → `Acao`, mapa 11×11 com a geometria invertida e painel com o estado
  - status: done
  - Fatia vertical: `MapeadorTeclas`, `MapaGeometria` e `TextoPainel` com TDD; `ControlePartida` só com `tecla(...)` (sem ranking e sem fim ainda); reescrever a `TelaPartida` desenhando grade, elementos coloridos com símbolo (nave por último) e o painel de estado; `iniciarPartida` criando o controle.
  - Conferir no `:lwjgl3:run`: `W/↑` sobe na tela, `C` e outras teclas não gastam turno, nave sobre a base no início, redimensionar sem distorcer.
  - _Requisitos: R1, R2, R3, R4.1, R4.5, R6_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.

- [x] 2. Mostrar as mensagens dos eventos do turno e o alerta de passageiros resgatados no painel
  - status: done
  - `MensagensPainel` com TDD (frases de R4.2, sem emoji nem separador); o `ControlePartida` guarda as mensagens do último turno; a tela desenha mensagens e alerta com quebra de linha.
  - _Requisitos: R4.2, R4.3, R4.4, R4.5_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.

- [x] 3. Encerrar a partida com `Enter`: vitória registra uma vez e vai ao Ranking com resumo, derrota vai ao Menu, abandono volta direto
  - status: done
  - `DestinoFim` e o resto do `ControlePartida` com TDD (`ControlePartidaTest` com `RankingService` em memória e com repositório que falha): registro único no turno da vitória, teclas ignoradas depois do fim, `enter()` por resultado, abandono.
  - A tela mostra "Pressione Enter para continuar" e navega para `irParaRanking(resumo, entrou)` ou `irParaMenu()`.
  - Roteiro manual completo: vencer no Fácil (entra no Top 10 e aparece no Ranking), perder por colisão e por pontuação, abandonar com `Q` e com `Esc`, segunda partida seguida.
  - _Requisitos: R5, R6_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.
