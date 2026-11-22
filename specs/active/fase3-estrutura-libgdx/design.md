# Design: fase3-estrutura-libgdx

## Abordagem

Criar `fases/fase-03-libgdx/` como um build Gradle multi-módulo em Groovy,
com o Wrapper 8.7 copiado da Fase 2. O módulo `core` recebe uma cópia
exata de `domain`, `application` e `infrastructure` da Fase 2, mais os
testes, e ganha o pacote `missaomarte.gdx` com a classe do jogo e a tela
base. O módulo `lwjgl3` só tem o launcher, que é a raiz de composição.
A primeira tela é uma tela provisória com o título. A spec 10 a substitui
pelo Menu.

## Componentes afetados

Todos novos, em `fases/fase-03-libgdx/`:

| Arquivo | Conteúdo |
|---|---|
| `settings.gradle` | `rootProject.name = 'missao-marte'`; `include 'core', 'lwjgl3'` |
| `build.gradle` (raiz) | `ext { gdxVersion = '1.12.1' }`; em `subprojects`: `repositories { mavenCentral() }`, plugin `java`, Java 17 (`sourceCompatibility`/`targetCompatibility`), `options.encoding = 'UTF-8'` e `options.release = 17` em toda `JavaCompile` |
| `core/build.gradle` | plugin `java-library`; `api "com.badlogicgames.gdx:gdx:$gdxVersion"`; JUnit igual à Fase 2 (`platform('org.junit:junit-bom:5.10.3')`, `junit-jupiter`, `junit-platform-launcher` em `testRuntimeOnly`); `test { useJUnitPlatform() }` |
| `lwjgl3/build.gradle` | plugin `application`; `implementation project(':core')`, `"com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion"`, `"com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop"`; `application { mainClass = 'missaomarte.lwjgl3.Lwjgl3Launcher' }`; `run { workingDir = rootProject.projectDir; if (System.getProperty('os.name').toLowerCase().contains('mac')) jvmArgs '-XstartOnFirstThread' }` |
| `gradlew`, `gradlew.bat`, `gradle/wrapper/*` | Cópia do Wrapper 8.7 da Fase 2 |
| `.gitignore` | `build/`, `.gradle/`, `out/`, `ranking.json` |
| `core/src/main/java/missaomarte/{domain,application,infrastructure}/**` | Cópia exata da Fase 2 (sem `console/` e sem `Main.java`) |
| `core/src/test/java/missaomarte/**` | Cópia exata dos testes da Fase 2 |
| `core/src/main/java/missaomarte/gdx/MissaoMarteGame.java` | `extends Game`. Construtor `(MissaoFactory fabrica, Random random, Clock clock, RankingRepository ranking)`. Em `create()`: `OrthographicCamera`, `FitViewport(800, 600, camera)`, `SpriteBatch`, `ShapeRenderer`, `BitmapFont()` (fonte padrão); `setScreen(new TelaInicial(this))`. Em `dispose()`: `getScreen().dispose()` e liberação dos quatro recursos. Getters para as telas. Constantes `LARGURA = 800`, `ALTURA = 600` |
| `core/src/main/java/missaomarte/gdx/TelaBase.java` | `abstract class TelaBase extends ScreenAdapter implements InputProcessor` (ou com um `InputAdapter` interno). Guarda o `MissaoMarteGame`. `show()`: log + `Gdx.input.setInputProcessor(this)`. `hide()`: log + `Gdx.input.setInputProcessor(null)`. `resize(w, h)`: log + `viewport.update(w, h, true)`. `pause/resume/dispose`: log. `render(delta)`: limpa a tela (`ScreenUtils.clear`), aplica o viewport, ajusta as matrizes de projeção do batch e do shape renderer e chama `desenhar(delta)`, que é abstrato. Log: `Gdx.app.log("Tela", getClass().getSimpleName() + " " + evento)` |
| `core/src/main/java/missaomarte/gdx/TelaInicial.java` | Tela provisória: escreve "Missão Marte Unifor" centralizado. A spec 10 a remove e põe a `TelaMenu` no lugar |
| `lwjgl3/src/main/java/missaomarte/lwjgl3/Lwjgl3Launcher.java` | `main`: cria `new MissaoFactory(random)`, `new Random()`, `Clock.systemDefaultZone()`, `new FileRankingRepository(Paths.get("ranking.json"))`; `Lwjgl3ApplicationConfiguration` com título "Missão Marte Unifor", `setWindowedMode(800, 600)`, `useVsync(true)`, `setForegroundFPS(60)`; `new Lwjgl3Application(new MissaoMarteGame(...), config)` |

Nada muda fora desta pasta. O gate (`.workflow/check.sh`, laço de fases)
encontra o `gradlew` na raiz da fase e roda `clean build`. A regra
`camadas` varre os dois módulos.

Conferir os nomes exatos dos construtores de `MissaoFactory` e
`FileRankingRepository` no código copiado (ver
`fases/fase-02-gradle/src/main/java/missaomarte/Main.java`, que é a
referência de composição da Fase 2).

## Fluxo

```
./gradlew :lwjgl3:run  (workingDir = fases/fase-03-libgdx, -XstartOnFirstThread no macOS)
  └─ Lwjgl3Launcher.main
       ├─ monta fábrica, Random, Clock, FileRankingRepository("ranking.json")
       └─ new Lwjgl3Application(new MissaoMarteGame(...), config 800x600)
            └─ MissaoMarteGame.create()
                 ├─ câmera, FitViewport 800x600, SpriteBatch, ShapeRenderer, BitmapFont
                 └─ setScreen(TelaInicial)  → log "TelaInicial show" / "resize"
fechar a janela → "TelaInicial hide" → "TelaInicial dispose" → recursos liberados
```

## Decisões e alternativas descartadas

- **Decisão:** console e `Main` do terminal fora da Fase 3.
  **Alternativa descartada:** manter os dois em `core`. Ficaria código sem
  uso e um segundo `main`, o que confunde o `run` e o tutorial. A versão de
  terminal continua na Fase 2.
- **Decisão:** pacotes `missaomarte.gdx` (jogo, telas e auxiliares) e
  `missaomarte.lwjgl3` (launcher). O gate já proíbe `gdx` e `com.badlogic`
  em `domain`, `application` e `infrastructure`.
  **Alternativa descartada:** `missaomarte.ui` ou `missaomarte.telas`. O
  gate não protegeria esses nomes.
- **Decisão:** versão do libGDX em `ext.gdxVersion` no `build.gradle` raiz.
  **Alternativa descartada:** `gradle.properties`. Também funciona, mas o
  escopo pede "no `build.gradle` raiz".
- **Decisão:** `RankingTop5` e `RankingTop5Test` copiados sem mudança. A
  spec 9 os substitui. Assim esta spec prova que **todos** os testes da
  Fase 2 passam em `core`.
- **Decisão:** a classe do jogo recebe o `RankingRepository`. A spec 9
  troca esse parâmetro pelo `RankingService`.
- **Decisão:** fonte padrão (`new BitmapFont()`), sem arquivo de fonte. O
  escopo pede visual simples. Fonte própria fica para a Fase 4.

## Riscos e impactos

- **Primeira execução baixa as dependências do libGDX e as nativas.** O
  `check.sh` precisa de rede na primeira vez.
- **`-XstartOnFirstThread`:** sem ele o LWJGL3 trava no macOS. Com ele
  fora do macOS a JVM pode recusar a opção. Por isso a condição pelo
  `os.name`.
- **Diretório de trabalho:** sem `workingDir` o `ranking.json` iria para
  `lwjgl3/` e não seria o mesmo arquivo entre execuções feitas de lugares
  diferentes.
- **Gate e `build`:** `clean build` compila o `lwjgl3`, mas não o executa.
  A janela só é provada no QA manual.
- **Acentos na fonte padrão:** a fonte embutida pode não ter todos os
  glifos do português. Conferir no QA o título "Missão". Se faltar glifo,
  registrar nas `notas:` (vira limitação no tutorial). Não trocar o texto
  nem a fonte nesta spec sem combinar.

## Estratégia de testes

| Requisito | Como se prova |
|---|---|
| R1 | Inspeção dos arquivos de build; `grep` da versão; `git diff main --stat` |
| R2 | `./gradlew test` com todos os testes da Fase 2 (contagem igual à da Fase 2); hash dos arquivos copiados igual ao da Fase 2 |
| R3, R4, R5 | Inspeção e roteiro manual (`:lwjgl3:run`, redimensionar, fechar, ler o log). A classe do jogo e a tela base dependem de OpenGL e **não** têm teste automático |
| R6 | `check.sh` |
