# Specs

## Fila de implementação (active/)

> **Divisão do trabalho:** as specs 8 a 12 (Fase 3), do André Felipe, já
> foram entregues (PR #38). As specs 13 e 14 (servidor da Fase 5) são do
> Matheus, assim como as specs 15 e 16 (Fase 4). Cada orquestrador só
> pega as specs do próprio integrante, mesmo que outra esteja elegível
> antes na fila.

| Ordem | Spec | Issue | Trilha | Depende de | Status | Resumo |
|---|---|---|---|---|---|---|
| 16 | fase4-interface-grafica | a criar | C | 15 | na fila | `MundoRenderer` com animações e nave girando, movimento suave (~12 células/s), um som por turno e explosão por cima, música em laço (pausa no fim, volta no Menu), caixa final, HUD (barra, painel, rodapé), fundo e "selecionar" nas outras telas, semente `-Dmissaomarte.semente=N`; README e `docs/fase-04-tutorial.md` |

## Backlog (planned/)

Um briefing por fase: decisões já tomadas, specs previstas e perguntas em
aberto. Cada briefing vira specs em `active/` quando a fase for planejada.

| Spec | Resumo |
|---|---|
| briefing-fase5 | Cliente online (o servidor já está na fila, Ordens 13 e 14): specs `fase5-cliente-rede` (A) e `fase5-cliente-telas-online` (B), depois da Fase 4 |
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
| 7 | ajuste-fase1-tutorial | 2026-11-22 | `fases/fase-01-refatoracao/docs/fase-01-tutorial.md`: SOLID, GRASP, padrões, diagramas, limitações e roteiro manual (só documentação) |
| 8 | fase3-estrutura-libgdx | 2026-11-26 | Fase 3 multi-módulo (`core` + `lwjgl3`, libGDX 1.12.1): regras e testes da Fase 2 em `core` (sem console), `Game`, `TelaBase` e janela 800×600 abrindo |
| 9 | fase3-ranking-service | 2026-11-26 | `application.ranking` com `RankingService` Top 10 (só vitória, > 0, lista cheia só aceita maior que o 10º); remove o `RankingTop5` e migra os testes |
| 10 | fase3-telas-menu | 2026-11-26 | Menu (1 Jogar, 2 Ranking, 3 Limpar com confirmação, 4 Sair), Nova partida (nome até 20, dificuldade por setas, mapa fixo 11×11) e Ranking Top 10 com resumo |
| 11 | fase3-partida | 2026-11-26 | Partida na janela: tecla → `Acao` (só `E` embarca), mapa 11×11 com `MapaGeometria`, painel e mensagens, fim com `Enter` e registro único no Top 10 |
| 12 | ajuste-fase3-tutorial | 2026-11-26 | README da Fase 3 e `docs/fase-03-tutorial.md`: módulos, ciclo de vida, telas, composição, mudanças (Top 10, mapa fixo, `E`, sem console) e limitações (só documentação) |
| 13 | fase5-servidor-usuarios | 2026-11-24 | Servidor da Fase 5 (Spring Boot 4.1.2, Wrapper 9.8.2, H2 em arquivo): `/api/saude` pública, cadastro com BCrypt e 409 por nome normalizado, login JWT HS256 de 2 h, demais rotas protegidas com 401 em `ProblemDetail`, segredo < 32 bytes não sobe |
| 14 | fase5-servidor-partidas-ranking | 2026-11-26 | `POST /api/partidas` (usuário do token, validação, `posicaoRanking`), histórico paginado (`pagina` 0, `tamanho` 1–50) e ranking global Top 10 (melhor vitória por jogador, desempates); limitação da pontuação no README |
| 15 | fase4-assets-recursos | 2026-11-27 | Cria a Fase 4 copiando a Fase 3; módulo `tools` gera e commita imagens (PNG, animações em faixa) e sons (WAV sintetizados, música de ~8 s) em `core/assets`; catálogo `Imagem`/`Som` sem libGDX, símbolo → imagem (genérica se desconhecido), `Recursos` com `AssetManager` carregado uma vez; partida desenhada com imagens |
