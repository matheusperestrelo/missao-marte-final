# Tarefas: fase2-testes-junit

- [ ] 1. Configurar o JUnit 5 e testar `Posicao`, `Mapa` e `Nave`
  - status: todo
  - Fatia vertical: dependências e `useJUnitPlatform()` no `build.gradle` (conforme o design); remover `src/test/java/.gitkeep`; `PosicaoTest`, `MapaTest` e `NaveTest`. Conferir que `./gradlew test` roda os testes e que o relatório aparece em `build/reports/tests/test/`. Rodar o `comparar-hash.sh` e o `.workflow/check.sh`.
  - _Requisitos: R1, R2_
  - notas:

- [ ] 2. Testar `Acao` e `Dificuldade`
  - status: todo
  - `AcaoTest` e `DificuldadeTest`, com testes parametrizados (`@ParameterizedTest`) onde houver tabela.
  - _Requisitos: R3_
  - notas:

- [ ] 3. Testar a `MissaoFactory`
  - status: todo
  - Sementes fixas; quantidades por dificuldade; sem sobreposição e fora da base para várias sementes e tamanhos ≥ 2; sequência de nomes; mesma semente, mesmo cenário. Nunca tamanho 1 no Médio/Difícil.
  - _Requisitos: R4_
  - notas:

- [ ] 4. Testar o turno da `GameSession`: início, movimento, embarque, entrega e vitória
  - status: todo
  - Criar os dublês `FabricaFixa` e `RelogioControlado`. Cobrir início e piloto anônimo, movimento e borda, embarque e recusas (sem passageiro e nave cheia), entrega automática, indicadores fechando, vitória só com todos entregues, inválido e sair, duração congelada e resumo.
  - _Requisitos: R5.1, R5.2, R5.3, R5.4, R5.5, R5.6, R5.10, R5.12, R5.13, R5.14_
  - notas:

- [ ] 5. Testar colisão, derrotas e a ordem dos eventos na `GameSession`
  - status: todo
  - Colisão com asteroide (e, se precisar de inimigo, `Random` com sequência fixa); perda de vida e nave destruída; as duas derrotas no mesmo turno, na ordem; pontuação zerada mesmo após a última entrega (ponto *h*); ordem ação → entrega → colisão → derrota → vitória.
  - _Requisitos: R5.7, R5.8, R5.9, R5.11_
  - notas:

- [ ] 6. Testar o `RankingTop5` e o `FileRankingRepository`
  - status: todo
  - Dublês `RankingEmMemoria` e `RankingQueFalha`; `Clock` fixo. `FileRankingRepository` só com `@TempDir`, incluindo a leitura de um arquivo no formato da Fase 0. Acrescentar `./gradlew test` e o caminho do relatório no README da fase. Colar nestas notas o resumo da execução (quantos testes, tempo), para o tutorial da #10.
  - _Requisitos: R6, R1.2_
  - notas:
