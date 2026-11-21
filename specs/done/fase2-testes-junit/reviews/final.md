# Verificação final — fase2-testes-junit

> Nova verificação (2026-11-21). Ela substitui a anterior (e50bfab), que
> deu PENDENCIAS só porque a máquina não tinha JDK 17 e a etapa Gradle do
> `check.sh` não rodava. O JDK 17 foi instalado e o `check.sh` passou
> inteiro. O usuário executou o roteiro de QA e mandou seguir.

**Veredito:** APROVADA
**Branch:** feat/issue-8-fase2-gradle (PR #33, junto com a #8 e a #10)
**Commits da spec:** 2 de implementação (27e4b67, testes da #9; a575d23, rodada 2 da tarefa 2) e 4 de revisão (1f64def, 4ba197c, 91f4645, e50bfab). O README da fase veio no 074a200 (#10).

Regularização: a spec foi implementada fora do workflow, no PR #33. As
tarefas 1 a 6 foram revisadas a partir dos commits existentes. A tarefa 2
voltou uma vez (`reviews/02-r1.md`, caso de espaços do R3.3) e foi
aprovada na rodada 2 (`reviews/02-r2.md`). As demais foram aprovadas na
rodada 1.

## Como a evidência foi obtida

No repositório, em HEAD 033f25c, com OpenJDK 17.0.21.1 (Homebrew) e
`JAVA_HOME="$(brew --prefix openjdk@17)/libexec/openjdk.jdk/Contents/Home"`:

- `.workflow/check.sh` → exit 0, "check.sh: tudo passou". A etapa
  `fases/fase-02-gradle: gradlew clean build` deu `BUILD SUCCESSFUL`.
- `./gradlew --version` → Gradle 8.7, JVM 17.0.21.1.
- `build/test-results/test/*.xml` → **75 testes, 0 falhas, 0 erros, 0
  ignorados**: AcaoTest 20, DificuldadeTest 15, GameSessionTest 16,
  MissaoFactoryTest 6, RankingTop5Test 6, MapaTest 2, NaveTest 4,
  PosicaoTest 2, FileRankingRepositoryTest 4.
- `./gradlew dependencies --configuration runtimeClasspath` → `No dependencies`.
- `bash scripts/comparar-hash.sh` → "33 arquivos comparados: todos
  idênticos", exit 0.
- `git status --short` limpo depois do build: nenhum `ranking.json` nem
  outro artefato de teste apareceu.

## Cobertura

| Requisito | Critério | Teste(s) / verificação | Situação |
|---|---|---|---|
| R1 | 1 | inspeção de `build.gradle:20-23,35-37` (BOM 5.10.3, `junit-jupiter` sem versão, launcher em `testRuntimeOnly`, `useJUnitPlatform()`) | coberto |
| R1 | 2 | `check.sh`, etapa `gradlew clean build` (Wrapper 8.7, JDK 17) → `BUILD SUCCESSFUL`; `build/test-results`: 75 testes, 0 falhas, 0 erros, 0 ignorados | coberto |
| R1 | 3 | `runtimeClasspath` → `No dependencies` | coberto |
| R1 | 4 | `comparar-hash.sh` → 33 idênticos, exit 0 | coberto |
| R1 | 5 | inspeção de `src/test/java`: sem `System.in`, todo `Random` com semente, relógio só `RelogioControlado`/`Clock.fixed`, a única gravação em disco é feita em `@TempDir` (`FileRankingRepositoryTest`) | coberto |
| R2 | 1 | `PosicaoTest.r2_1_moverDevolveNovaInstanciaEMantemAOriginal` | coberto |
| R2 | 2 | `PosicaoTest.r2_2_igualdadeEHashCodePelasCoordenadas` | coberto |
| R2 | 3 | `MapaTest.r2_3_bordasEstaoContidas`, `r2_3_foraDaBordaNaoEstaContido` | coberto |
| R2 | 4 | `NaveTest.r2_4_naveFicaParadaNaBorda` | coberto |
| R2 | 5 | `NaveTest.r2_5_embarcaAteACapacidadeERecusaCheia` | coberto |
| R2 | 6 | `NaveTest.r2_6_desembarcarTodosLiberaCapacidade` | coberto |
| R3 | 1 | `AcaoTest.r3_1_letrasDeComando`, `r3_1_soOPrimeiroCaractereConta`, `r3_1_invalidas` | coberto |
| R3 | 2 | `DificuldadeTest.r3_2_parametros` | coberto |
| R3 | 3 | `DificuldadeTest.r3_3_deString` (com `'  FACIL  '`; a mutação sem `trim()` falha), `r3_3_nuloViraMedio`, `r3_3_nomesDeExibicao` | coberto |
| R4 | 1 | `MissaoFactoryTest.r4_1_quantidadesDaDificuldade` | coberto |
| R4 | 2 | `MissaoFactoryTest.r4_2_semSobreposicaoEForaDaBase` (sementes 0–49 × tamanhos 2–6 × 3 dificuldades) | coberto |
| R4 | 3 | `MissaoFactoryTest.r4_3_sequenciaDePassageiros` | coberto |
| R4 | 4 | `MissaoFactoryTest.r4_4_mesmaSementeMesmoCenario` | coberto |
| R4 | 5 | inspeção: nenhuma chamada da fábrica real com tamanho 1 no Médio/Difícil | coberto |
| R5 | 1 | `GameSessionTest.r5_1_inicioDaPartida`, `r5_1_pilotoAnonimoETamanhoPadrao` | coberto |
| R5 | 2 | `r5_2_movimentoCustaUmPontoInclusiveNaBorda` | coberto |
| R5 | 3 | `r5_3_embarque` | coberto |
| R5 | 4 | `r5_4_semPassageiro`, `r5_4_naveCheia` | coberto |
| R5 | 5 | `r5_5_entregaAutomaticaNaBase` | coberto |
| R5 | 6 | dentro de `r5_5_entregaAutomaticaNaBase` (`@DisplayName` "R5.5 e R5.6", `GameSessionTest.java:147`) | coberto (ID só no `@DisplayName`) |
| R5 | 7 | `r5_7_colisaoENaveDestruida` | coberto |
| R5 | 8 | `r5_8_duasDerrotasNaOrdem` | coberto |
| R5 | 9 | `r5_9_pontuacaoZeradaAoEntregarOUltimo` | coberto |
| R5 | 10 | `r5_10_vitoriaSoComTodosEntregues` | coberto |
| R5 | 11 | `r5_11_ordemDosEventos`, junto com `r5_8_duasDerrotasNaOrdem` | coberto |
| R5 | 12 | `r5_12_invalidaESair` | coberto |
| R5 | 13 | `r5_13_duracaoCongelada` | coberto |
| R5 | 14 | `r5_14_resumo` | coberto |
| R6 | 1 | inspeção: `RankingEmMemoria`/`RankingQueFalha` em `Dubles.java`; `Clock.fixed` em `RankingTop5Test` | coberto |
| R6 | 2 | `RankingTop5Test.r6_2_recusaDerrotaEPontuacaoNaoPositiva` | coberto |
| R6 | 3 | `r6_3_limiteDeCinco` | coberto |
| R6 | 4 | `r6_4_ordemEmpateEData` (o máximo de 5 é provado em `r6_3`) | coberto |
| R6 | 5 | `r6_5_recorde` | coberto |
| R6 | 6 | `r6_6_falhaDoRepositorio` | coberto |
| R6 | 7 | `FileRankingRepositoryTest.r6_7_salvarELer`, `r6_7_arquivoInexistente`, `r6_7_limparApagaOArquivo` | coberto |
| R6 | 8 | `FileRankingRepositoryTest.r6_8_formatoDaFase0` | coberto |

Lacunas: nenhuma.

## check.sh

Passou (exit 0, "check.sh: tudo passou"), com JDK 17:

- fase 0 imutavel: ok
- `fases/fase-01-refatoracao`: javac ok, regras de camadas ok
- `fases/fase-02-gradle`: `gradlew clean build` ok (`BUILD SUCCESSFUL`),
  regras de camadas ok

## Pendências

Nenhuma bloqueia. Ficam registradas:

- Revisões 01-r1, 02-r1/02-r2, 04-r1 e 06-r1: testes extras sem ID de
  critério no nome (`NaveTest.perderVidaNaoFicaNegativa`,
  `AcaoTest.direcaoDasAcoes`, `GameSessionTest.inimigoAndaNaDirecaoSorteada`,
  `RankingTop5Test.listarELimpar`). A sugestão aparece em quatro revisões
  e não bloqueia.
- Revisão 04-r1, sugestão 1: R5.1 cobre "só espaços" (`"   "`), mas não o
  nome vazio (`""`).
- Revisão 04-r1: R5.6 é provado dentro do teste de R5.5, com o ID só no
  `@DisplayName`.
- Revisão 01-r1, sugestão 3: R2.4 só testa a borda direita.
- Revisão 04-r1, sugestão 3: `Dubles.java:61-64`, onde
  `RelogioControlado.withZone` ignora a zona (sem efeito hoje).
- Revisão 05-r1, sugestão 1: o `@DisplayName` de R5.11 não menciona que a
  posição da derrota é provada em `r5_8`.
- Desvio do `design.md`/tarefa 1: o `src/test/java/.gitkeep` nunca foi
  versionado, então não havia o que remover.
- Revisões 01-r1 e 06-r1: as seis tarefas foram implementadas num único
  commit (27e4b67), e o README da fase veio no commit da #10 (074a200).
  Isso vem da implementação fora do workflow e não pede ação.
- Revisão 02-r2, sugestão 2: a nota da rodada 2 da tarefa 2 está num item
  separado, fora da linha `notas:`.

## Roteiro de QA

O usuário já executou este roteiro e mandou seguir. Ele fica aqui para
referência. Antes, em cada terminal:
`export JAVA_HOME="$(brew --prefix openjdk@17)/libexec/openjdk.jdk/Contents/Home"`.

1. `cd fases/fase-02-gradle && ./gradlew --version` → **esperado:**
   "Gradle 8.7" e "JVM: 17.x" (R1.2).
2. Na raiz do repositório, `.workflow/check.sh` → **esperado:** todas as
   etapas `ok`, inclusive `fases/fase-02-gradle: gradlew clean build`, e a
   linha final `check.sh: tudo passou` (R1.2).
3. `cd fases/fase-02-gradle && ./gradlew clean build` → **esperado:**
   `BUILD SUCCESSFUL`, sem falhas de teste (R1.2).
4. Abra `fases/fase-02-gradle/build/reports/tests/test/index.html` no
   navegador → **esperado:** 75 testes, 0 falhas, 0 ignorados, 100% de
   sucesso. As classes `PosicaoTest`, `MapaTest`, `NaveTest`, `AcaoTest`,
   `DificuldadeTest`, `MissaoFactoryTest`, `GameSessionTest`,
   `RankingTop5Test` e `FileRankingRepositoryTest` aparecem (R1.2, R2–R6).
5. No relatório, abra `DificuldadeTest` → **esperado:** existe o caso
   `"  FACIL  " vira FACIL`, com os espaços visíveis (R3.3, correção da
   rodada 2).
6. `./gradlew test` duas vezes seguidas, a segunda com `--rerun-tasks` →
   **esperado:** os mesmos 75 testes passam nas duas, o que mostra que são
   determinísticos (R1.5).
7. `./gradlew dependencies --configuration runtimeClasspath` →
   **esperado:** `No dependencies`. Com `--configuration
   testRuntimeClasspath`, aparecem `junit-jupiter` 5.10.3 e
   `junit-platform-launcher` (R1.1, R1.3).
8. `bash scripts/comparar-hash.sh; echo $?` → **esperado:** "33 arquivos
   comparados: todos idênticos" e `0` (R1.4).
9. Depois dos testes, `git status --short` → **esperado:** nada novo. Nem
   `ranking.json` nem outros arquivos de teste aparecem no repositório
   (R1.5, R6.7).
10. Opcional, caso negativo: numa cópia descartável, troque
    `s.trim().toLowerCase()` por `s.toLowerCase()` em
    `src/main/java/missaomarte/application/Dificuldade.java:33` e rode
    `./gradlew test` → **esperado:** 1 falha (`"  FACIL  " vira FACIL`).
    Descarte a cópia depois. Não altere o repositório.

## Próximos passos

- QA feito pelo usuário. Falta o merge e a conclusão da spec (active/ →
  done/), que ficam com o orquestrador.
- Nenhuma tarefa nova para o Planejador.
- As sugestões acumuladas (IDs nos testes extras, caso `""` em R5.1, borda
  negativa em R2.4) ficam a critério do usuário: viram um `ajuste-` futuro
  ou são ignoradas.
