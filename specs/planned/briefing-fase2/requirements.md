# Briefing: Fase 2 — Gradle 8.7 e JUnit 5

> **Briefing, não spec.** Ele guarda o que já foi decidido para a Fase 2 até
> o planejamento desta fase. Na hora de planejá-la: grill só das perguntas
> em aberto (`design.md`), criação das specs abaixo em `active/` e remoção
> deste briefing do backlog.

## Contexto

Migrar o código da Fase 1 para o layout Gradle (Wrapper 8.7), com os fontes
idênticos, e cobrir as regras com JUnit 5. Pasta: `fases/fase-02-gradle/`.

## Specs previstas (fila aprovada)

| Ordem prevista | Spec | Issue | Trilha | Depende de |
|---|---|---|---|---|
| 4 | `fase2-gradle` | #8 | C | 3 (`fase1-console-infra`) |
| 5 | `fase2-testes-junit` | #9 | C | 4 |

Sem spec: #10 (documentação da migração).

## Base dos requisitos

Os critérios de aceite das issues #8 e #9 são a base, junto com a seção 7
("Fase 2") do `ESCOPO-PROJETO.md`:

- **#8:** layout `src/main/java` e `src/test/java`; `settings.gradle` e
  `build.gradle` (Groovy); plugin `application` com `mainClass`
  (`missaomarte.Main`); Java 17; UTF-8; `mavenCentral()`;
  `run { standardInput = System.in }`; Wrapper 8.7 versionado; `clean
  build`, `run --console=plain` e `installDist` funcionando; fontes `.java`
  idênticos aos da Fase 1 (comparação por hash).
- **#9:** JUnit 5 com `junit-platform-launcher` em `testRuntimeOnly` e
  `useJUnitPlatform()`; testes de `Posicao`, `Mapa`, `Nave`, `Acao`,
  `Dificuldade` (30/20/45 e quantidades), Top 5 (com repositório em
  memória), `MissaoFactory` (semente fixa, mapa ≥ 2), `GameSession`
  (semente fixa: embarque, desembarque, colisão e vitória, conferindo o
  resultado do turno) e `FileRankingRepository` com `@TempDir`. Sem teclado
  e sem gravar fora de pasta temporária.

Devem entrar também os testes das decisões da Fase 1: capacidade 4/3/2 e
"nave cheia"; desembarque automático e sem pontos; indicadores fechando;
vitória só com todos entregues; eventos na ordem, inclusive
DERROTA_NAVE_DESTRUIDA seguida de DERROTA_PONTUACAO_ZERADA no mesmo turno; comando inválido sem consumir turno; duração
congelada com `Clock` fixo.

## Fora de escopo (já decidido)

- Alterar qualquer `.java` vindo da Fase 1 dentro da Fase 2: quebra o hash.
- Mudar regras de jogo.
- Tutorial da Fase 2 e README da raiz: issue #10, sem spec.
