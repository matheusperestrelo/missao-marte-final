# Specs

## Fila de implementação (active/)

| Ordem | Spec | Issue | Trilha | Depende de | Status | Resumo |
|---|---|---|---|---|---|---|
| 2 | fase1-application | #5 | A | 1 | na fila | `Acao`, `Dificuldade` com parâmetros, `MissaoFactory`, `GameSession` com eventos do turno, regra do Top 5 |
| 3 | fase1-console-infra | #6 | B | 2 | na fila | Console com as frases da Fase 0, ranking em arquivo JSON, `Main` de composição e `build.sh` |

## Backlog (planned/)

Um briefing por fase: decisões já tomadas, specs previstas e perguntas em
aberto. Cada briefing vira specs em `active/` quando a fase for planejada.

| Spec | Resumo |
|---|---|
| briefing-fase2 | Gradle 8.7 e JUnit 5: specs `fase2-gradle` (#8, C) e `fase2-testes-junit` (#9, C) |
| briefing-fase3 | libGDX: specs `fase3-estrutura-libgdx` (C), `fase3-ranking-service` (A) e `fase3-telas` (B) |
| briefing-fase4 | Interface gráfica: specs `fase4-assets-recursos` (A) e `fase4-interface-grafica` (B) |
| briefing-fase5 | Servidor e cliente online: specs `fase5-servidor-usuarios` (C), `fase5-servidor-partidas-ranking` (C), `fase5-cliente-rede` (A) e `fase5-cliente-telas-online` (B) |
| briefing-fase6 | Android: specs `fase6-android-build` (C), `fase6-toque` (B) e `fase6-config-servidor` (A) |
| briefing-fase7 | Melhoria: spec `fase7-escudo` (A), mecânica ainda a alinhar |

## Concluídas (done/)

| Ordem | Spec | Concluída em | Resumo |
|---|---|---|---|
| 1 | fase1-domain | 2026-11-20 | Cria a pasta da Fase 1 e a camada `domain` (posição, mapa, nave, passageiros, perigos, missão) |
