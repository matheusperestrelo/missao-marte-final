# Tarefas: fase2-testes-junit

- [x] 1. Configurar o JUnit 5 e testar `Posicao`, `Mapa` e `Nave`
  - status: done
  - Fatia vertical: dependências e `useJUnitPlatform()` no `build.gradle` (conforme o design); remover `src/test/java/.gitkeep`; `PosicaoTest`, `MapaTest` e `NaveTest`. Conferir que `./gradlew test` roda os testes e que o relatório aparece em `build/reports/tests/test/`. Rodar o `comparar-hash.sh` e o `.workflow/check.sh`.
  - _Requisitos: R1, R2_
  - notas: (Revisor, regularização) implementada fora do workflow no PR #33 (commit 27e4b67), sem passar por `review`; revisada em `reviews/01-r1.md`, sem alterar código. Evidência do Revisor (2026-11-21) numa cópia temporária no scratchpad, com o Gradle 9.7.1 do cache e o JDK 25.0.2, porque o Wrapper 8.7 não roda no JDK 25: `gradle clean build --offline` → BUILD SUCCESSFUL, 75 testes, 0 falhas (PosicaoTest 2, MapaTest 2, NaveTest 4); relatório em `build/reports/tests/test/index.html`; `runtimeClasspath` sem dependências; `comparar-hash.sh` → 33 idênticos, exit 0. O `src/test/java/.gitkeep` nunca foi versionado (nada a remover). Pendente para o QA: `./gradlew clean build` e `check.sh` com JDK 17/Wrapper 8.7.

- [ ] 2. Testar `Acao` e `Dificuldade`
  - status: changes_requested
  - `AcaoTest` e `DificuldadeTest`, com testes parametrizados (`@ParameterizedTest`) onde houver tabela.
  - _Requisitos: R3_
  - notas: (Revisor, regularização) implementada fora do workflow no PR #33 (commit 27e4b67), sem passar por `review`; revisada em `reviews/02-r1.md`, sem alterar código. Evidência do Revisor (mesma cópia temporária, Gradle 9.7.1/JDK 25): AcaoTest 20 e DificuldadeTest 15 passam. Bloqueante: `DificuldadeTest.java:29` não testa espaços, porque o `@CsvSource` remove os espaços de `  FACIL  ` sem aspas (o relatório mostra `"FACIL" vira FACIL`). Numa segunda cópia temporária, tirar o `trim()` de `Dificuldade.deString` não quebra nenhum teste. Pendente para o QA: JDK 17/Wrapper 8.7.

- [x] 3. Testar a `MissaoFactory`
  - status: done
  - Sementes fixas; quantidades por dificuldade; sem sobreposição e fora da base para várias sementes e tamanhos ≥ 2; sequência de nomes; mesma semente, mesmo cenário. Nunca tamanho 1 no Médio/Difícil.
  - _Requisitos: R4_
  - notas: (Revisor, regularização) implementada fora do workflow no PR #33 (commit 27e4b67), sem passar por `review`; revisada em `reviews/03-r1.md`, sem alterar código. Evidência do Revisor (mesma cópia temporária, Gradle 9.7.1/JDK 25): os 6 testes de MissaoFactoryTest passam (R4.2 com sementes 0–49 × tamanhos 2–6 × 3 dificuldades); nenhum tamanho 1 com a fábrica real. Pendente para o QA: JDK 17/Wrapper 8.7.

- [x] 4. Testar o turno da `GameSession`: início, movimento, embarque, entrega e vitória
  - status: done
  - Criar os dublês `FabricaFixa` e `RelogioControlado`. Cobrir início e piloto anônimo, movimento e borda, embarque e recusas (sem passageiro e nave cheia), entrega automática, indicadores fechando, vitória só com todos entregues, inválido e sair, duração congelada e resumo.
  - _Requisitos: R5.1, R5.2, R5.3, R5.4, R5.5, R5.6, R5.10, R5.12, R5.13, R5.14_
  - notas: (Revisor, regularização) implementada fora do workflow no PR #33 (commit 27e4b67), sem passar por `review`; revisada em `reviews/04-r1.md`, sem alterar código. Dublês `FabricaFixa`, `RelogioControlado` e `RandomSequencia` em `src/test/java/missaomarte/application/Dubles.java`. Evidência do Revisor (mesma cópia temporária, Gradle 9.7.1/JDK 25): os 16 testes de GameSessionTest passam. R5.6 é provado dentro de `r5_5_entregaAutomaticaNaBase` (o ID está no `@DisplayName`). Pendente para o QA: JDK 17/Wrapper 8.7.

- [x] 5. Testar colisão, derrotas e a ordem dos eventos na `GameSession`
  - status: done
  - Colisão com asteroide (e, se precisar de inimigo, `Random` com sequência fixa); perda de vida e nave destruída; as duas derrotas no mesmo turno, na ordem; pontuação zerada mesmo após a última entrega (ponto *h*); ordem ação → entrega → colisão → derrota → vitória.
  - _Requisitos: R5.7, R5.8, R5.9, R5.11_
  - notas: (Revisor, regularização) implementada fora do workflow no PR #33 (commit 27e4b67), sem passar por `review`; revisada em `reviews/05-r1.md`, sem alterar código. Colisões com asteroides parados. Evidência do Revisor (mesma cópia temporária, Gradle 9.7.1/JDK 25): `r5_7`, `r5_8`, `r5_9` e `r5_11` passam. Pendente para o QA: JDK 17/Wrapper 8.7.

- [x] 6. Testar o `RankingTop5` e o `FileRankingRepository`
  - status: done
  - Dublês `RankingEmMemoria` e `RankingQueFalha`; `Clock` fixo. `FileRankingRepository` só com `@TempDir`, incluindo a leitura de um arquivo no formato da Fase 0. Acrescentar `./gradlew test` e o caminho do relatório no README da fase. Colar nestas notas o resumo da execução (quantos testes, tempo), para o tutorial da #10.
  - _Requisitos: R6, R1.2_
  - notas: (Revisor, regularização) implementada fora do workflow no PR #33 (testes no commit 27e4b67; README da fase no 074a200, da #10), sem passar por `review`; revisada em `reviews/06-r1.md`, sem alterar código. Evidência do Revisor (mesma cópia temporária, Gradle 9.7.1/JDK 25). Resumo da execução: 75 testes, 0 falhas, 0 ignorados, 0.508s no relatório `build/reports/tests/test/index.html`; `BUILD SUCCESSFUL in 1s` (RankingTop5Test 6, FileRankingRepositoryTest 4). Pendente para o QA: JDK 17/Wrapper 8.7.
