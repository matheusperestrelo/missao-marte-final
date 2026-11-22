# Specs

## Fila de implementação (active/)

| Ordem | Spec | Issue | Trilha | Depende de | Status | Resumo |
|---|---|---|---|---|---|---|
| 7 | ajuste-fase1-tutorial | #7 | — | 6 | na fila | `fases/fase-01-refatoracao/docs/fase-01-tutorial.md`: SOLID, GRASP, padrões, diagramas, limitações e roteiro manual (só documentação) |
| 8 | fase3-estrutura-libgdx | #14 | C | 5 | na fila | Fase 3 multi-módulo (`core` + `lwjgl3`, libGDX 1.12.1): regras e testes da Fase 2 em `core` (sem console), `Game`, `TelaBase` e janela 800×600 abrindo |
| 9 | fase3-ranking-service | #15 | A | 8 | na fila | `application.ranking` com `RankingService` Top 10 (só vitória, > 0, lista cheia só aceita maior que o 10º); remove o `RankingTop5` e migra os testes |
| 10 | fase3-telas-menu | #14 | C | 8, 9 | na fila | Menu (1 Jogar, 2 Ranking, 3 Limpar com confirmação, 4 Sair), Nova partida (nome até 20, dificuldade por setas, mapa fixo 11×11) e Ranking Top 10 com resumo |
| 11 | fase3-partida | #16 | B | 8, 9, 10 | na fila | Partida na janela: tecla → `Acao` (só `E` embarca), mapa 11×11 com `MapaGeometria`, painel e mensagens, fim com `Enter` e registro único no Top 10 |
| 12 | ajuste-fase3-tutorial | a criar | — | 10, 11 | na fila | README da Fase 3 e `docs/fase-03-tutorial.md`: módulos, ciclo de vida, telas, composição, mudanças (Top 10, mapa fixo, `E`, sem console) e limitações (só documentação) |

## Backlog (planned/)

Um briefing por fase: decisões já tomadas, specs previstas e perguntas em
aberto. Cada briefing vira specs em `active/` quando a fase for planejada.

| Spec | Resumo |
|---|---|
| briefing-fase4 | Interface gráfica: specs `fase4-assets-recursos` (A) e `fase4-interface-grafica` (B) |
| briefing-fase5 | Servidor e cliente online: specs `fase5-servidor-usuarios` (C), `fase5-servidor-partidas-ranking` (C), `fase5-cliente-rede` (A) e `fase5-cliente-telas-online` (B) |
| briefing-fase6 | Android: specs `fase6-android-build` (C), `fase6-toque` (B) e `fase6-config-servidor` (A) |
| briefing-fase7 | Melhoria: spec `fase7-escudo` (A), mecânica ainda a alinhar |

## Concluídas (done/)

| Ordem | Spec | Concluída em | Resumo |
|---|---|---|---|
| 1 | fase1-domain | 2026-11-20 | Cria a pasta da Fase 1 e a camada `domain` (posição, mapa, nave, passageiros, perigos, missão) |
| 2 | fase1-application | 2026-11-20 | `Acao`, `Dificuldade` com parâmetros, `MissaoFactory`, `GameSession` com eventos do turno, regra do Top 5 |
| 3 | fase1-console-infra | 2026-11-20 | Console com as frases da Fase 0, ranking em arquivo JSON, `Main` de composição e `build.sh` |
| 4 | fase2-gradle | 2026-11-21 | Fase 1 movida para o layout Gradle (Wrapper 8.7, plugin `application`, Java 17), fontes idênticos conferidos por `comparar-hash.sh` |
| 5 | fase2-testes-junit | 2026-11-21 | JUnit 5 (BOM 5.10.x) e testes de domínio, ação, dificuldade, fábrica, sessão e ranking, sem alterar `src/main/java` |
| 6 | ajuste-fase1-contratos | 2026-11-22 | `docs/contratos.md`: contratos da Fase 1 como implementados e proposta da API/porta da Fase 5 (só documentação) |
