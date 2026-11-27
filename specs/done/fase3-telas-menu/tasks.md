# Tarefas: fase3-telas-menu

- [x] 1. Criar o Menu com a confirmação de limpar o ranking, abrindo como primeira tela do jogo
  - status: done
  - Fatia vertical: `ComandoMenu`, `ControleMenu` (com TDD, `ControleMenuTest` usando `RankingService` com repositório em memória e um que falha), `TelaMenu` e a navegação na `MissaoMarteGame` (`irParaMenu`, `sair`, `trocarTela`). Remover a `TelaInicial`.
  - Opções `1` e `2` ainda sem destino nesta tarefa: o controle já devolve `NOVA_PARTIDA` e `RANKING`, e a tela ignora esses comandos até as tarefas 2 e 3.
  - Conferir no `:lwjgl3:run`: Menu, `3` com `S`, `N` e `Esc`, `4` fechando com `dispose` no log.
  - _Requisitos: R1.1, R1.4, R1.5, R2, R7_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.

- [x] 2. Criar a tela de Ranking com o Top 10 e o bloco de resumo, ligada à opção `2` do Menu
  - status: done
  - `TextosRanking` com TDD (`TextosRankingTest`), `TelaRanking` (com e sem resumo) e `irParaRanking(...)` na classe do jogo. Ligar `RANKING` do Menu.
  - O bloco de resumo é só mostrado. Quem registra e informa `entrou` é a spec 11.
  - _Requisitos: R1.3, R6, R7_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.

- [x] 3. Criar a tela Nova partida (nome e dificuldade) iniciando a sessão na tela da Partida provisória
  - status: done
  - `ResultadoFormulario`, `FormularioNovaPartida` com TDD (`FormularioNovaPartidaTest`), `TelaNovaPartida`, `TelaPartida` provisória e `iniciarPartida(...)` na classe do jogo com `TAMANHO_MAPA = 5`. Ligar `NOVA_PARTIDA` do Menu.
  - Conferir no `:lwjgl3:run`: nome com 20+ caracteres, backspace, nome vazio (aparece "Piloto Anônimo" na tela provisória), setas com bordas, `Esc` nas duas etapas.
  - _Requisitos: R1.2, R3, R4, R5, R7_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.
