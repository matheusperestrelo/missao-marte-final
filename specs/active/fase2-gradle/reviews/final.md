# Verificação final — fase2-gradle

**Veredito:** PENDENCIAS (só por ambiente: o `check.sh` não executou a etapa Gradle por falta de JDK 17; não há lacuna de cobertura nem bloqueante de código)
**Branch:** feat/issue-8-fase2-gradle (PR #33)
**Commits da spec:** 2 de implementação (49a51b1, 074a200; 074a200 é da #10 e traz o README da fase) e 2 de revisão (efd3e4b, 341a0ea)

Regularização: a spec foi implementada fora do workflow no PR #33. As
tarefas 1 e 2 foram revisadas a partir dos commits existentes
(`reviews/01-r1.md`, `reviews/02-r1.md`), ambas `approved`, sem alteração
de código. Ambiente da verificação: macOS, só JDK 25.0.2 (nenhum JDK 17).

## Cobertura

A spec não tem testes JUnit por decisão do `design.md` ("Estratégia de
testes"). A coluna "Teste(s)" indica a verificação prevista no
`requirements.md` e a evidência obtida.

| Requisito | Critério | Teste(s) / evidência | Situação |
|---|---|---|---|
| R1 | 1 | inspeção: `src/main/java/` e `src/test/java/` versionadas (esta última pelos testes da #9) | coberto |
| R1 | 2 | `comparar-hash.sh` exit 0, 33 idênticos; SHA-256 completos idênticos | coberto |
| R1 | 3 | `git diff main --stat` das Fases 0 e 1 vazio | coberto |
| R2 | 1 | inspeção de `settings.gradle`/`build.gradle` | coberto |
| R2 | 2 | inspeção (`application`, `missaomarte.Main`) | coberto; `run` no QA |
| R2 | 3 | inspeção (`release = 17`, UTF-8 em `JavaCompile`) | coberto |
| R2 | 4 | inspeção (só `mavenCentral()`, sem `implementation`/`runtimeOnly`) | coberto |
| R2 | 5 | inspeção (`standardInput = System.in`) | coberto; comportamento no QA |
| R2 | 6 | `gradlew` 100755, `gradlew.bat`, jar válido, `gradle-8.7-bin.zip` | coberto; `--version` no QA |
| R2 | 7 | `.gitignore` + `git check-ignore -v` | coberto |
| R3 | 1 | manual com JDK 17 | **pendente de QA (JDK 17)** |
| R3 | 2 | gate com JDK 17 | **pendente de QA (JDK 17)** |
| R3 | 3 | manual com JDK 17 | **pendente de QA (JDK 17)** |
| R3 | 4 | manual com JDK 17 | **pendente de QA (JDK 17)** |
| R3 | 5 | `.gitignore` conferido; gravação real com JDK 17 | parcial; **pendente de QA (JDK 17)** |
| R4 | 1 | modo 100755; executado a partir de outra pasta | coberto |
| R4 | 2 | saída por arquivo + resumo (hash com 12 caracteres, não bloqueante) | coberto |
| R4 | 3 | exit 0 com árvores idênticas | coberto |
| R4 | 4 | cópia temporária: alterado / só na Fase 2 / só na Fase 1 → exit 1 | coberto |
| R5 | 1 | inspeção do `fases/fase-02-gradle/README.md` | coberto |
| R5 | 2 | `check.sh` | **pendente de QA (JDK 17)** |

## check.sh

Falhou em `fases/fase-02-gradle: gradlew clean build`, porque "JDK 17 nao
encontrado". A etapa não chegou a executar o Gradle. As demais passaram:
fase 0 imutável, `fases/fase-01-refatoracao: javac`, regras de camadas das
Fases 1 e 2. Como evidência complementar, `javac --release 17 -Xlint:all`
dos fontes da Fase 2 compilou sem avisos no JDK 25. Isso não substitui o
gate.

## Pendências

- revisões 01-r1 e 02-r1 — os critérios R3.1–R3.5 e R5.2 dependem de JDK 17
  e Wrapper 8.7 e não puderam ser reproduzidos aqui. São verificação manual
  obrigatória no QA (passos 1 a 6 abaixo).
- revisão 01-r1, sugestão 1 — não existe `src/test/java/.gitkeep` (desvio
  do `design.md`). A pasta fica versionada pelos testes da #9, no mesmo PR.
- revisão 02-r1, sugestão 1 — `comparar-hash.sh:35` imprime o hash cortado
  em 12 caracteres. Não bloqueante (a comparação usa o hash completo).
- revisão 02-r1, sugestão 2 — `comparar-hash.sh` diz "todos idênticos" com
  exit 0 se as duas árvores estiverem vazias ou ausentes.
- revisão 02-r1, sugestão 4 — `docs/fase-02-tutorial.md:198` e `:205` (#10)
  citam hashes que não batem com o checkout atual. A conclusão (33
  idênticos) se mantém; a saída de `reviews/02-r1.md` pode substituir a do
  tutorial.

## Roteiro de QA

Pré-requisito: JDK 17 (`brew install openjdk@17`). Em cada terminal, rode
`export JAVA_HOME=$(/usr/libexec/java_home -v 17)` (ou
`export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home`)
e `cd fases/fase-02-gradle`.

1. `./gradlew --version` → **esperado:** "Gradle 8.7" e "JVM: 17.x" (R3.1, R2.6).
2. Na raiz do repositório, `.workflow/check.sh` → **esperado:** todas as
   etapas `ok`, inclusive `fases/fase-02-gradle: gradlew clean build`, e a
   linha final `check.sh: tudo passou` (R3.2, R5.2). Este é o passo que
   resolve a pendência do veredito.
3. `./gradlew run --console=plain` → **esperado:** abre o menu da Fase 1
   com os mesmos textos. Jogue uma partida até o fim com `w`/`a`/`s`/`d` e
   `c`, e confira que as teclas chegam ao jogo, sem "Entrada encerrada"
   logo no início (R2.2, R2.5, R3.3). Para comparar os textos, rode a Fase
   1 pelo `build.sh` dela.
4. Depois da partida, `ls ranking.json` na pasta da fase e
   `git status --short` → **esperado:** o arquivo existe na
   `fases/fase-02-gradle/` e não aparece no `git status` (R3.5).
5. `./gradlew installDist` e depois `build/install/missao-marte/bin/missao-marte`
   → **esperado:** o script existe (também o `.bat`) e inicia o mesmo jogo.
   Saia com `q` (R3.4).
6. `git status --short` → **esperado:** `build/` e `.gradle/` não aparecem
   (R2.7).
7. Sem JDK 17, a partir de qualquer pasta:
   `bash <repo>/fases/fase-02-gradle/scripts/comparar-hash.sh; echo $?` →
   **esperado:** 33 linhas `OK`, "33 arquivos comparados: todos idênticos"
   e `0` (R1.2, R4.1–R4.3).
8. Opcional, caso negativo: acrescente uma linha a
   `src/main/java/missaomarte/domain/Mapa.java`, rode o script → **esperado:**
   linha `DIFERENTE` e exit `1`. Depois desfaça com
   `git checkout -- src/main/java/missaomarte/domain/Mapa.java` (R4.4).
9. Opcional, erro mais provável: rode `./gradlew --version` com o JDK 25 no
   `JAVA_HOME` → **esperado:** falha do Gradle 8.7, o que confirma o aviso
   do README (R5.1).

## Próximos passos

- Nenhuma tarefa nova para o Planejador: não há lacuna de cobertura nem
  bloqueante de código.
- Se os passos 1 a 6 do QA passarem com JDK 17 (principalmente o
  `check.sh` completo no passo 2), a spec fica em condição equivalente a
  APROVADA e segue para merge e conclusão (active/ → done/).
- As sugestões (hash completo no script, guarda para `total = 0`, hashes
  do tutorial) ficam a critério do usuário, como `ajuste-`/`fix-` futuro ou
  ignoradas.
