# Specs

## Fila de implementação (active/)

| Ordem | Spec | Issue | Trilha | Depende de | Status | Resumo |
|---|---|---|---|---|---|---|
| 6 | ajuste-fase1-contratos | #13 | — | — | na fila | `docs/contratos.md`: contratos da Fase 1 como implementados e proposta da API/porta da Fase 5 (só documentação) |
| 7 | ajuste-fase1-tutorial | #7 | — | 6 | na fila | `fases/fase-01-refatoracao/docs/fase-01-tutorial.md`: SOLID, GRASP, padrões, diagramas, limitações e roteiro manual (só documentação) |

## Backlog (planned/)

Um briefing por fase: decisões já tomadas, specs previstas e perguntas em
aberto. Cada briefing vira specs em `active/` quando a fase for planejada.

| Spec | Resumo |
|---|---|
| briefing-fase3 | libGDX: specs `fase3-estrutura-libgdx` (C), `fase3-ranking-service` (A) e `fase3-telas` (B) |
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
