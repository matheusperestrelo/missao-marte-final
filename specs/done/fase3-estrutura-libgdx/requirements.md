> **Concluída em 2026-11-26.** Diferenças do planejado: implementada fora do workflow no PR #38 (mergeado em 2026-11-25), sem `reviews/` por tarefa nem `final.md`, e regularizada depois; a `TelaInicial` provisória foi substituída pelo `TelaMenu` da spec 10.

# Requisitos: fase3-estrutura-libgdx

**Issue:** #14 (etapa 1) · **Trilha:** C · **Fase:** 3 · **Depende de:** 5 (`fase2-testes-junit`)
**Branch sugerida:** `feat/issue-14-fase3-estrutura-libgdx` · **PR:** "Part of #14" (a #14 só fecha com a spec 10, `fase3-telas-menu`)

## Contexto

A Fase 2 é um jogo de terminal num módulo Gradle único. A Fase 3 leva o
jogo para uma janela desktop com libGDX 1.12.1. Esta spec cria a pasta
`fases/fase-03-libgdx/` com dois módulos, `core` (regras e telas, sem
plataforma) e `lwjgl3` (só o launcher desktop). Ela copia as regras e os
testes da Fase 2 para `core` e abre uma janela vazia com o ciclo de vida
do libGDX funcionando. As specs seguintes (9, 10 e 11) constroem o ranking
e as telas em cima desta base.

Base: issue #14, `ESCOPO-PROJETO.md`, seção 8, "Fase 3", e o resumo do
alinhamento da Fase 3 (decisões registradas em `design.md`).

## Fora de escopo

- `RankingService` e Top 10: spec 9 (`fase3-ranking-service`, #15). Aqui o
  `RankingTop5` e o `RankingTop5Test` são copiados como estão.
- Telas Menu, Nova partida, Ranking e Partida: specs 10 e 11. Aqui só
  existe uma tela provisória com o título.
- Alterar qualquer arquivo de `fases/fase-02-gradle/` (ou das Fases 0 e 1).
- Alterar regras, textos ou testes vindos da Fase 2 (exceto o pacote, que
  não muda: continua `missaomarte.*`).
- O console (`missaomarte.console`) e o `Main` do terminal: **não** vêm
  para a Fase 3. A versão de terminal continua na Fase 2. Decisão do
  alinhamento; nenhum teste da Fase 2 cobre o console.
- Módulo Android, assets, sons e fonte personalizada: Fases 4 e 6.
- DSL Kotlin: o projeto usa Groovy, como na Fase 2.
- Trocar o gate (`.workflow/check.sh`): ele já detecta a fase nova.
- README e tutorial da fase: spec 12 (`ajuste-fase3-tutorial`).

## Requisitos

### R1 — Pasta da fase com dois módulos

**História:** Como integrante da equipe, quero a Fase 3 separada em `core`
e `lwjgl3`, para que as regras e as telas não dependam da plataforma
desktop e a Fase 6 possa acrescentar o Android.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `fases/fase-03-libgdx/` com `settings.gradle`
   incluindo os módulos `core` e `lwjgl3`, `build.gradle` raiz, o Wrapper
   do Gradle 8.7 (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`) e um
   `.gitignore` com `build/`, `.gradle/`, `out/` e `ranking.json`.
   _(verificação: inspeção)_
2. O SISTEMA DEVE definir a versão do libGDX (`1.12.1`) num **único
   lugar**, no `build.gradle` raiz, e os módulos devem usá-la por
   referência. _(verificação: inspeção, `grep -rn "1.12.1"` na fase devolve
   uma única linha de build)_
3. O SISTEMA DEVE fazer `core` depender só de `com.badlogicgames.gdx:gdx`
   (execução) e do JUnit 5 (testes), sem nenhum backend de plataforma.
   _(verificação: inspeção)_
4. O SISTEMA DEVE fazer `lwjgl3` depender de `core`,
   `gdx-backend-lwjgl3` e `gdx-platform` com o classificador
   `natives-desktop`. _(verificação: inspeção)_
5. O SISTEMA NÃO DEVE alterar nada em `fases/fase-00-console/`,
   `fases/fase-01-refatoracao/` ou `fases/fase-02-gradle/`.
   _(verificação: `git diff main --stat` da branch)_

### R2 — Regras e testes da Fase 2 em `core`

**História:** Como integrante, quero reaproveitar as regras da Fase 2 sem
mudança, para que a janela use exatamente o mesmo jogo testado.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter em `core/src/main/java/missaomarte/` os pacotes
   `domain`, `application` e `infrastructure` da Fase 2, com o mesmo
   conteúdo byte a byte, e **sem** `console/` e sem `Main.java`.
   _(verificação: inspeção + comparação por hash dos arquivos copiados)_
2. O SISTEMA DEVE ter em `core/src/test/java/missaomarte/` todos os testes
   da Fase 2, com o mesmo conteúdo. _(verificação: inspeção)_
3. QUANDO `./gradlew test` for executado na raiz da fase O SISTEMA DEVE
   rodar e passar todos os testes copiados, sem nenhum teste a menos que
   na Fase 2. _(verificação: gate + contagem no relatório do Gradle)_
4. O SISTEMA NÃO DEVE ter import de `com.badlogic` em `domain`,
   `application` ou `infrastructure`. _(verificação: gate, regra de
   camadas)_

### R3 — Classe do jogo e recursos compartilhados

**História:** Como integrante, quero uma classe de jogo que crie e libere
os recursos gráficos num só lugar, para que as telas só os usem.

**Critérios de aceitação:**

1. QUANDO o jogo for criado (`create()`) O SISTEMA DEVE criar uma câmera
   ortográfica, um `FitViewport` de 800×600 ligado a ela, um
   `SpriteBatch`, um `ShapeRenderer` e uma fonte padrão do libGDX, e
   mostrar a tela inicial. _(verificação: inspeção + manual)_
2. QUANDO o jogo for encerrado (`dispose()`) O SISTEMA DEVE liberar a tela
   atual, o `SpriteBatch`, o `ShapeRenderer` e a fonte.
   _(verificação: inspeção + log manual)_
3. O SISTEMA DEVE receber as dependências do jogo (fábrica de missão,
   `Random`, `Clock` e repositório de ranking) **pelo construtor** da
   classe do jogo, sem criá-las dentro dela. _(verificação: inspeção)_
4. O SISTEMA DEVE expor às telas os recursos compartilhados (viewport,
   câmera, batch, shape renderer, fonte) sem que cada tela crie os seus.
   _(verificação: inspeção)_

### R4 — Classe base de tela

**História:** Como integrante, quero uma classe base para as telas, para
não repetir o registro do ciclo de vida, o viewport e a entrada em cada
uma.

**Critérios de aceitação:**

1. QUANDO uma tela receber `show`, `resize`, `pause`, `resume`, `hide` ou
   `dispose` O SISTEMA DEVE registrar no log do libGDX o nome da tela e o
   evento, sem que a tela concreta repita esse código.
   _(verificação: manual, saída do `:lwjgl3:run`)_
2. QUANDO a janela for redimensionada O SISTEMA DEVE atualizar o viewport
   com o novo tamanho, mantendo a área de 800×600 proporcional (com
   faixas, sem distorcer). _(verificação: manual)_
3. QUANDO uma tela for mostrada (`show()`) O SISTEMA DEVE instalar o
   `InputProcessor` dela, e QUANDO for escondida (`hide()`) O SISTEMA DEVE
   removê-lo. _(verificação: inspeção + manual)_

### R5 — Launcher desktop

**História:** Como jogador, quero abrir o jogo numa janela com um comando
do Gradle.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter no módulo `lwjgl3` um launcher que monta as
   dependências (fábrica, `Random`, `Clock`, repositório de ranking em
   arquivo `ranking.json`) e cria o jogo com elas.
   _(verificação: inspeção)_
2. QUANDO `./gradlew :lwjgl3:run` for executado na raiz da fase com JDK 17
   O SISTEMA DEVE abrir uma janela de 800×600 com o título "Missão Marte
   Unifor" e uma tela inicial provisória com o mesmo título.
   _(verificação: manual)_
3. O SISTEMA DEVE executar o `run` com o diretório de trabalho na raiz da
   fase, para que o `ranking.json` fique em `fases/fase-03-libgdx/`.
   _(verificação: inspeção + manual)_
4. ENQUANTO o sistema operacional for macOS O SISTEMA DEVE passar
   `-XstartOnFirstThread` à JVM do `run`, e NÃO DEVE passá-lo nos outros
   sistemas. _(verificação: inspeção + manual no macOS)_
5. QUANDO a janela for fechada O SISTEMA DEVE encerrar o processo sem
   erro e com os `dispose` registrados no log. _(verificação: manual)_

### R6 — Gate

**Critérios de aceitação:**

1. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar, incluindo
   `fases/fase-03-libgdx: gradlew clean build`, as regras de camadas da
   Fase 3 e a Fase 0 intacta. _(verificação: gate)_
2. O SISTEMA NÃO DEVE ter nenhum teste que abra janela ou use OpenGL.
   _(verificação: inspeção)_
