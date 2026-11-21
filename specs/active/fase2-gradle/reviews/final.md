# Verificação final — fase2-gradle

> Nova verificação (2026-11-21). Ela substitui a anterior (f527465), que
> deu PENDENCIAS só porque a máquina não tinha JDK 17 e a etapa Gradle do
> `check.sh` não rodava. O JDK 17 foi instalado e o `check.sh` passou
> inteiro. O usuário executou o roteiro de QA e mandou seguir.

**Veredito:** APROVADA
**Branch:** feat/issue-8-fase2-gradle (PR #33)
**Commits da spec:** 2 de implementação (49a51b1, 074a200; o 074a200 é da #10 e traz o README da fase) e 3 de revisão (efd3e4b, 341a0ea, f527465)

Regularização: a spec foi implementada fora do workflow, no PR #33. As
tarefas 1 e 2 foram revisadas a partir dos commits existentes
(`reviews/01-r1.md` e `reviews/02-r1.md`). As duas foram `approved`, sem
alteração de código.

Ambiente desta verificação: macOS, OpenJDK 17.0.21.1 (Homebrew), com
`JAVA_HOME="$(brew --prefix openjdk@17)/libexec/openjdk.jdk/Contents/Home"`.
HEAD em 033f25c.

## Cobertura

Por decisão do `design.md` ("Estratégia de testes"), a spec não tem testes
JUnit. A coluna "Teste(s)" mostra a verificação que o `requirements.md`
prevê e a evidência obtida.

| Requisito | Critério | Teste(s) / evidência | Situação |
|---|---|---|---|
| R1 | 1 | inspeção: `src/main/java/` e `src/test/java/` versionadas (a segunda pelos testes da #9) | coberto |
| R1 | 2 | `comparar-hash.sh` → "33 arquivos comparados: todos idênticos", exit 0 | coberto |
| R1 | 3 | `check.sh` "fase 0 imutavel" ok; diff das Fases 0 e 1 contra `main` vazio | coberto |
| R2 | 1 | inspeção de `settings.gradle`/`build.gradle` | coberto |
| R2 | 2 | inspeção (`application`, `missaomarte.Main`); `./gradlew run` abre o menu | coberto |
| R2 | 3 | inspeção (`release = 17`, UTF-8 em `JavaCompile`); compila no JDK 17 | coberto |
| R2 | 4 | inspeção; `dependencies --configuration runtimeClasspath` → `No dependencies` | coberto |
| R2 | 5 | inspeção (`standardInput = System.in`); a entrada por pipe chegou ao menu (R3.3) | coberto |
| R2 | 6 | `gradlew` 100755, `gradlew.bat`, jar, `gradle-8.7-bin.zip`; `--version` → Gradle 8.7 | coberto |
| R2 | 7 | `.gitignore` + `git check-ignore -v` (`build/`, `.gradle/`, `ranking.json`) | coberto |
| R3 | 1 | `./gradlew --version` → "Gradle 8.7", "JVM: 17.0.21.1 (Homebrew 17.0.21.1+0)" | coberto |
| R3 | 2 | `check.sh`, etapa `gradlew clean build` → `BUILD SUCCESSFUL` | coberto |
| R3 | 3 | `./gradlew run --console=plain`: banner e menu da Fase 1; as opções `2` (ranking) e `4` (sair) responderam ao teclado. Partida completa feita no QA do usuário | coberto |
| R3 | 4 | `./gradlew installDist` → `build/install/missao-marte/bin/missao-marte` e `.bat` | coberto |
| R3 | 5 | `Main.java:21` grava `Paths.get("ranking.json")` (relativo à pasta de execução); `.gitignore:4` ignora o arquivo; `git status` limpo depois das execuções; gravação/leitura em `FileRankingRepositoryTest` | coberto (sem gravação manual depois de uma vitória) |
| R4 | 1 | modo 100755; executado a partir de outra pasta | coberto |
| R4 | 2 | saída por arquivo + resumo (hash com 12 caracteres, não bloqueante) | coberto |
| R4 | 3 | exit 0 com as árvores idênticas | coberto |
| R4 | 4 | cópia temporária: alterado / só na Fase 2 / só na Fase 1 → exit 1 (`reviews/02-r1.md`) | coberto |
| R5 | 1 | inspeção do `fases/fase-02-gradle/README.md` | coberto |
| R5 | 2 | `.workflow/check.sh` → "check.sh: tudo passou", exit 0 | coberto |

Lacunas: nenhuma.

## check.sh

Passou (exit 0, "check.sh: tudo passou"), com JDK 17:

- fase 0 imutavel: ok
- `fases/fase-01-refatoracao`: javac ok, regras de camadas ok
- `fases/fase-02-gradle`: `gradlew clean build` ok (`BUILD SUCCESSFUL`, 8
  tarefas executadas), regras de camadas ok

Evidência complementar: `build/test-results` deu 75 testes, 0 falhas, 0
erros e 0 ignorados. `git status --short` ficou limpo depois do build, do
`installDist` e do `run`.

## Pendências

Nenhuma bloqueia. Ficam registradas:

- Revisão 01-r1, sugestão 1: não existe `src/test/java/.gitkeep` (desvio
  do `design.md`). A pasta fica versionada pelos testes da #9, no mesmo PR.
- Revisão 02-r1, sugestão 1: `comparar-hash.sh:35` imprime o hash cortado
  em 12 caracteres. A comparação usa o hash completo.
- Revisão 02-r1, sugestão 2: `comparar-hash.sh` diz "todos idênticos" com
  exit 0 quando as duas árvores estão vazias ou ausentes.
- Revisão 02-r1, sugestão 4: `docs/fase-02-tutorial.md:198` e `:205` (#10)
  citam hashes que não batem com o checkout atual. A conclusão (33
  idênticos) se mantém.
- R3.5: o `ranking.json` não foi gravado manualmente depois de uma
  vitória nesta verificação. A gravação está coberta por
  `FileRankingRepositoryTest` e o caminho relativo foi conferido no código.

## Roteiro de QA

O usuário já executou este roteiro e mandou seguir. Ele fica aqui para
referência. Antes, em cada terminal:
`export JAVA_HOME="$(brew --prefix openjdk@17)/libexec/openjdk.jdk/Contents/Home"`
e `cd fases/fase-02-gradle`.

1. `./gradlew --version` → **esperado:** "Gradle 8.7" e "JVM: 17.x" (R3.1, R2.6).
2. Na raiz do repositório, `.workflow/check.sh` → **esperado:** todas as
   etapas `ok` e a linha final `check.sh: tudo passou` (R3.2, R5.2).
3. `./gradlew run --console=plain` → **esperado:** o menu da Fase 1 com os
   mesmos textos. Uma partida até o fim com `w`/`a`/`s`/`d` e `c`, com as
   teclas chegando ao jogo (R2.2, R2.5, R3.3).
4. Depois de uma vitória, `ls ranking.json` e `git status --short` →
   **esperado:** o arquivo existe em `fases/fase-02-gradle/` e não aparece
   no `git status` (R3.5).
5. `./gradlew installDist` e depois `build/install/missao-marte/bin/missao-marte`
   → **esperado:** inicia o mesmo jogo. Saia com `4` (R3.4).
6. `git status --short` → **esperado:** `build/` e `.gradle/` não aparecem (R2.7).
7. `bash scripts/comparar-hash.sh; echo $?` → **esperado:** "33 arquivos
   comparados: todos idênticos" e `0` (R1.2, R4.1–R4.3).
8. Opcional, caso negativo: acrescente uma linha a
   `src/main/java/missaomarte/domain/Mapa.java` e rode o script →
   **esperado:** linha `DIFERENTE` e exit `1`. Desfaça com
   `git checkout -- src/main/java/missaomarte/domain/Mapa.java` (R4.4).

## Próximos passos

- QA feito pelo usuário. Falta o merge e a conclusão da spec (active/ →
  done/), que ficam com o orquestrador.
- Nenhuma tarefa nova para o Planejador.
- As sugestões (hash completo no script, guarda para `total = 0`, hashes
  do tutorial) ficam a critério do usuário: viram um `ajuste-`/`fix-`
  futuro ou são ignoradas.
