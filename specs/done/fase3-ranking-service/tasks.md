# Tarefas: fase3-ranking-service

- [x] 1. Criar o `RankingService` com Top 10 em `application.ranking`, ligado ao jogo pelo launcher, e remover o `RankingTop5`
  - status: done
  - Fatia vertical: mover `RankingRepository`, `RankingEntry` e `RankingException` para `missaomarte.application.ranking` (só `package` e `import` mudam); criar `RankingService` (`top10`, `registrar`, `recorde`, `limpar`, `MAXIMO = 10`) com TDD; trocar o parâmetro da `MissaoMarteGame` e a composição no `Lwjgl3Launcher`.
  - Remover `RankingTop5.java` e `RankingTop5Test.java`. Mover `RankingEmMemoria` e `RankingQueFalha` para `core/src/test/java/missaomarte/application/ranking/`. Cobrir no `RankingServiceTest` todos os casos que o `RankingTop5Test` cobria, com o limite 10.
  - Não mudar asserções de nenhum outro teste vindo da Fase 2. Só `import`.
  - Conferir: `./gradlew test`, `./gradlew :lwjgl3:run` ainda abrindo a janela e `.workflow/check.sh`.
  - _Requisitos: R1, R2, R3, R4, R5, R6_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.
