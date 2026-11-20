# Requisitos: fase2-gradle

**Issue:** #8 · **Trilha:** C · **Fase:** 2 · **Depende de:** 3 (`fase1-console-infra`)
**Branch sugerida:** `feat/issue-8-fase2-gradle` · **PR:** "Closes #8"

## Contexto

A Fase 1 compila com um `build.sh` que chama o `javac` direto e não tem
onde rodar testes nem resolver dependências. Esta spec cria a Fase 2 em
`fases/fase-02-gradle/`: o mesmo código da Fase 1, só **movido** para o
layout Gradle, com o Wrapper 8.7 versionado. Assim o jogo passa a ter um
build padrão, e a spec seguinte (`fase2-testes-junit`, #9) pode acrescentar
os testes.

Base: issue #8 e `ESCOPO-PROJETO.md`, seção 7, "Fase 2".

## Fora de escopo

- JUnit e qualquer teste automático: spec `fase2-testes-junit` (#9).
- Alterar qualquer `.java` vindo da Fase 1, mesmo para corrigir bug ou
  estilo: quebra a comparação por hash. Bug encontrado vai para a Fase 1
  como `fix-` na fila, e a Fase 2 copia de novo.
- Mudar regras de jogo ou textos do console.
- Alterar a pasta da Fase 1 (ou da Fase 0).
- DSL Kotlin (`build.gradle.kts`): o escopo pede Groovy.
- `build.sh` na Fase 2: o Gradle o substitui.
- Tutorial da Fase 2 (tasks e DAG, escopos, wrapper, ciclo de vida) e
  README da raiz do repositório: issue #10, sem spec. Esta spec só entrega
  a saída da comparação por hash, que o tutorial vai citar.

## Requisitos

### R1 — Layout Gradle com o código da Fase 1

**História:** Como integrante da equipe, quero o código da Fase 1 no layout
padrão do Gradle, para usar um build conhecido sem mudar o jogo.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter a pasta `fases/fase-02-gradle/` com os fontes em
   `src/main/java/` e uma pasta `src/test/java/` versionada.
   _(verificação: inspeção)_
2. O SISTEMA DEVE ter em `src/main/java/` exatamente os mesmos arquivos
   `.java` de `fases/fase-01-refatoracao/src/`, com os mesmos caminhos
   relativos (pacotes) e o mesmo conteúdo byte a byte. Nenhum arquivo a
   mais e nenhum a menos. _(verificação: script de hash, R4)_
3. O SISTEMA NÃO DEVE alterar nada em `fases/fase-00-console/` nem em
   `fases/fase-01-refatoracao/`. _(verificação: `git diff` da branch)_

### R2 — Configuração do build

**História:** Como integrante, quero um build reprodutível em qualquer
máquina com JDK 17, para não depender do Gradle instalado.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `settings.gradle` e `build.gradle` em DSL Groovy,
   com o nome do projeto definido no `settings.gradle`.
   _(verificação: inspeção)_
2. O SISTEMA DEVE usar o plugin `application` com a classe principal
   `missaomarte.Main`. _(verificação: inspeção + `./gradlew run`)_
3. O SISTEMA DEVE compilar para Java 17, com codificação UTF-8 em toda
   tarefa de compilação Java. _(verificação: inspeção)_
4. O SISTEMA DEVE declarar só o repositório `mavenCentral()` e nenhuma
   dependência de execução (`implementation`/`runtimeOnly`).
   _(verificação: inspeção)_
5. O SISTEMA DEVE configurar a tarefa `run` com
   `standardInput = System.in`, para o teclado chegar ao jogo.
   _(verificação: manual, R3.3)_
6. O SISTEMA DEVE ter o Wrapper do Gradle **8.7** versionado: `gradlew`
   (executável), `gradlew.bat` e `gradle/wrapper/gradle-wrapper.jar` e
   `gradle-wrapper.properties`. _(verificação: inspeção +
   `./gradlew --version`)_
7. O SISTEMA DEVE ter um `.gitignore` na pasta da fase com `build/`,
   `.gradle/`, `out/` e `ranking.json`. _(verificação: inspeção)_

### R3 — Comandos que funcionam

**História:** Como jogador e como avaliador, quero compilar, rodar e
instalar o jogo com os comandos padrão do Gradle.

**Critérios de aceitação:**

1. QUANDO `./gradlew --version` for executado na pasta da fase com JDK 17
   O SISTEMA DEVE mostrar Gradle 8.7 e JVM 17. _(verificação: manual)_
2. QUANDO `./gradlew clean build` for executado O SISTEMA DEVE terminar com
   `BUILD SUCCESSFUL`. _(verificação: gate)_
3. QUANDO `./gradlew run --console=plain` for executado O SISTEMA DEVE
   abrir o jogo de console da Fase 1, aceitar o teclado e permitir jogar uma
   partida até o fim, com os mesmos textos da Fase 1.
   _(verificação: manual)_
4. QUANDO `./gradlew installDist` for executado O SISTEMA DEVE gerar
   `build/install/<nome>/bin/` com um script que inicia o jogo.
   _(verificação: manual)_
5. QUANDO o jogo rodar pelo Gradle O SISTEMA DEVE gravar o `ranking.json`
   na pasta de onde o jogo foi executado, e esse arquivo não deve aparecer
   no `git status`. _(verificação: manual)_

### R4 — Comparação por hash

**História:** Como avaliador, quero uma prova de que os fontes da Fase 2
são idênticos aos da Fase 1, para confiar que a migração não mudou o jogo.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter o script `fases/fase-02-gradle/scripts/comparar-hash.sh`,
   executável a partir de qualquer pasta. _(verificação: inspeção)_
2. QUANDO o script for executado O SISTEMA DEVE listar, para cada `.java`,
   o caminho relativo e o hash SHA-256 nas duas fases, e terminar com uma
   linha de resumo (quantos arquivos, todos iguais ou não).
   _(verificação: manual)_
3. QUANDO todos os arquivos forem iguais e nenhum faltar ou sobrar O
   SISTEMA DEVE sair com código 0. _(verificação: manual)_
4. SE um arquivo tiver hash diferente, existir só numa das fases ou faltar
   ENTÃO O SISTEMA DEVE apontar o arquivo e sair com código diferente de
   zero. _(verificação: manual, alterando uma cópia temporária)_

### R5 — README da fase

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `fases/fase-02-gradle/README.md` mínimo com:
   requisito (JDK 17, com o aviso de que o Gradle 8.7 não roda no JDK 25 e
   o `brew install openjdk@17`), os comandos de R3, como rodar o
   `comparar-hash.sh` e a observação de que o código é idêntico ao da
   Fase 1. _(verificação: inspeção)_
2. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar, incluindo
   `fases/fase-02-gradle: gradlew clean build`, as regras de camadas e a
   Fase 0 intacta. _(verificação: gate)_
