# Design: fase4-assets-recursos

## Abordagem

1. Copiar a Fase 3 inteira para `fases/fase-04-interface-grafica/` (sem
   `ranking.json`, `build/`, `.gradle/`), como a `fase2-gradle` fez com a
   Fase 1. Nenhum arquivo de `domain`, `application` ou `infrastructure`
   muda depois da cópia.
2. Criar o catálogo `Imagem` e `Som` em `core`, no pacote
   `missaomarte.gdx.catalogo`, **sem `com.badlogic`**. Ele é a fonte única
   de caminhos, quadros e dimensões.
3. Criar o módulo `tools`, que depende de `core` só para ler o catálogo, e
   gera PNG com `javax.imageio` e WAV escrito à mão. Os arquivos gerados
   são commitados em `core/assets/`.
4. Criar `missaomarte.gdx.Recursos` com um `AssetManager`, instanciado pelo
   `MissaoMarteGame` em `create()` e liberado em `dispose()`.
5. Trocar, na `TelaPartida`, os retângulos e símbolos pelas texturas do
   `Recursos`, mantendo o resto da tela da Fase 3.

## Componentes afetados

Caminhos relativos a `fases/fase-04-interface-grafica/`.

| Arquivo / módulo | Mudança |
|---|---|
| (pasta inteira) | Cópia da Fase 3: `build.gradle`, `settings.gradle`, `gradle/`, `gradlew*`, `core/`, `lwjgl3/`, `README.md`, `.gitignore`. O `README.md` copiado só troca o título e a pasta para Fase 4 (o README completo é da spec 16) |
| `settings.gradle` | `include 'core', 'lwjgl3', 'tools'` |
| `tools/build.gradle` | Novo. Plugin `java`; `implementation project(':core')`; JUnit como no `core`; tarefa `gerarAssets` (`JavaExec`, `mainClass = 'missaomarte.tools.GeradorAssets'`, argumento = `rootProject.file('core/assets')`). O `test` recebe a pasta `core/assets` por `systemProperty 'missaomarte.assets'` |
| `tools/src/main/java/missaomarte/tools/GeradorAssets.java` | Novo. `main(String[] args)`: recebe a pasta de saída, chama `GeradorImagens` e `GeradorSons` para cada item do catálogo e escreve `CREDITOS.md` |
| `tools/src/main/java/missaomarte/tools/GeradorImagens.java` | Novo. `BufferedImage desenhar(Imagem)` com `Graphics2D` (formas simples, antialias), um método por item; `void gravar(Imagem, Path pasta)` com `ImageIO.write(..., "png", ...)`. Animações desenham cada quadro deslocado `i × larguraQuadro` na mesma imagem |
| `tools/src/main/java/missaomarte/tools/GeradorSons.java` | Novo. Sintetiza `short[]` (onda quadrada ou senoidal, envelope de ataque e queda, ruído com `Random` de semente fixa para a colisão); `void gravarWav(short[] amostras, Path arquivo)` escreve o cabeçalho RIFF/`fmt `/`data` à mão (PCM 1, mono, 22 050 Hz, 16 bits, little endian). Música: laço de ~8 s com melodia (quadrada) e baixo, pico ≤ 40 % do máximo; efeitos com pico ~80 %. Força a primeira e a última amostra em 0 |
| `tools/src/main/java/missaomarte/tools/Wav.java` | Novo, opcional. Leitura do cabeçalho (formato, canais, taxa, bits, nº de amostras) para os testes |
| `core/src/main/java/missaomarte/gdx/catalogo/Imagem.java` | Novo, **sem `com.badlogic`**. Enum: `FUNDO`, `TERRENO`, `NAVE`, `PROPULSOR`, `BASE`, `INIMIGO`, `ASTEROIDE`, `PROFESSOR`, `ENGENHEIRO`, `ASTRONAUTA`, `GENERICA`, `EXPLOSAO`, `VIDA_CHEIA`, `VIDA_VAZIA`, `VAGA_OCUPADA`, `VAGA_LIVRE`. Campos: `caminho()` (ex.: `imagens/nave.png`), `quadros()`, `larguraQuadro()`, `alturaQuadro()`, `emLaco()`. `static Imagem doSimbolo(char)`: tabela `Map<Character, Imagem>` (`@`→`NAVE`, `P`→`PROFESSOR`, `E`→`ENGENHEIRO`, `T`→`ASTRONAUTA`, `#`→`ASTEROIDE`, `X`→`INIMIGO`, `L`→`BASE`), ausente → `GENERICA` |
| `core/src/main/java/missaomarte/gdx/catalogo/Som.java` | Novo, **sem `com.badlogic`**. Enum: `MOVER`, `SELECIONAR`, `EMBARCAR`, `ENTREGAR`, `COLISAO`, `VITORIA`, `DERROTA`, `MUSICA`. Campos: `caminho()` (ex.: `sons/mover.wav`), `musica()` (verdadeiro só para `MUSICA`) |
| `core/src/main/java/missaomarte/gdx/Recursos.java` | Novo. Não `static`. Construtor cria o `AssetManager`; `carregar()` enfileira todo `Imagem` como `Texture`, todo `Som` não música como `Sound` e `MUSICA` como `Music`, e chama `finishLoading()` (carga síncrona, ~30 arquivos pequenos). `Texture textura(Imagem)`, `TextureRegion quadro(Imagem, int i)` (recorte da faixa por `larguraQuadro`), `Sound som(Som)`, `Music musica()`. `dispose()` chama `assetManager.dispose()`. Registra no log `Gdx.app.log("Recursos", "<n> arquivos carregados")` |
| `core/src/main/java/missaomarte/gdx/MissaoMarteGame.java` | Campo `Recursos recursos`; em `create()`, `recursos = new Recursos(); recursos.carregar();` **antes** do `setScreen`; em `dispose()`, `recursos.dispose()`; `getRecursos()` |
| `core/src/main/java/missaomarte/gdx/telas/TelaPartida.java` | Desenha `TERRENO` em cada célula e, para cada elemento, `recursos.quadro(Imagem.doSimbolo(elemento.simbolo()), 0)` na célula, nave por último. Remove o desenho de retângulos e símbolos dos elementos. Painel e fim iguais |
| `core/src/main/java/missaomarte/gdx/CoresElemento.java` | Removido se ficar sem uso (o teste correspondente, se houver, sai junto) |
| `lwjgl3/build.gradle` | `sourceSets.main.resources.srcDirs += [rootProject.file('core/assets')]`, para o `Gdx.files.internal` achar os arquivos no classpath, com o `run` na raiz da fase como na Fase 3 |
| `core/assets/imagens/*.png`, `core/assets/sons/*.wav`, `core/assets/CREDITOS.md` | Gerados por `./gradlew :tools:gerarAssets` e commitados |
| `.gitignore` | Igual ao da Fase 3 (não ignora `core/assets/`) |

## Fluxo

```
./gradlew :tools:gerarAssets
  GeradorAssets(core/assets) ── para cada Imagem → GeradorImagens → imagens/<nome>.png
                             ── para cada Som    → GeradorSons    → sons/<nome>.wav
                             └─ CREDITOS.md (gerado, lista os arquivos)

./gradlew :lwjgl3:run
  Lwjgl3Launcher → MissaoMarteGame.create()
    → recursos.carregar()   (AssetManager, uma vez)
    → TelaMenu ... TelaPartida desenha recursos.quadro(Imagem.doSimbolo(simbolo), 0)
  fechar → MissaoMarteGame.dispose() → recursos.dispose()
```

## Decisões e alternativas descartadas

- **Decisão:** gerador num módulo Gradle `tools`.
  **Alternativa descartada:** tarefa `JavaExec` dentro do `core`. O
  gerador iria no jar do jogo e misturaria ferramenta de build com código
  do jogo. O `tools` também será reaproveitado no ícone do Android
  (Fase 6).
- **Decisão:** catálogo em `core` (`gdx.catalogo`), sem libGDX, e `tools`
  dependendo de `core`.
  **Alternativa descartada:** catálogo duplicado no `tools`. Dois lugares
  com os mesmos caminhos e dimensões divergiriam. A dependência de `core`
  traz o jar do `gdx` para o classpath do `tools`, mas nenhuma classe do
  libGDX é usada nele.
- **Decisão:** sons sintetizados (quadrada/senoidal com envelope; ruído
  com semente fixa na colisão), WAV mono 22 050 Hz 16 bits.
  **Alternativa descartada:** sons de bancos livres. Exigiriam créditos
  por arquivo e licença, e o gerador deixaria de cobrir os sons.
- **Decisão:** música de ~8 s gerada, com pico menor que os efeitos.
  **Alternativa descartada:** laço de 30 s (≈1,3 MB no git, mais trabalho
  de composição) e ficar sem música (o escopo pede).
- **Decisão:** comparação "commitado = gerado" por **pixels** nas imagens
  e por **bytes** nos sons.
  **Alternativa descartada:** comparar bytes dos PNG. A compressão do
  `ImageIO` pode variar entre JDKs sem mudar a imagem.
- **Decisão:** `Recursos` nesta spec; a spec 16 só consome.
  **Alternativa descartada:** `Recursos` na spec 16. Esta spec não teria
  como mostrar os assets no jogo.
- **Decisão:** carga síncrona com `finishLoading()` antes da primeira tela.
  **Alternativa descartada:** tela de carregamento com progresso. São
  poucos arquivos pequenos, e não foi pedida.
- **Decisão:** símbolo → imagem por tabela no enum `Imagem`.
  **Alternativa descartada:** `instanceof` ou `switch` por classe do
  elemento (o escopo proíbe) e `switch` por `TipoElemento` (amarraria a
  camada `gdx` ao enum do domínio em vez do símbolo, que é o contrato
  pedido).
- **Decisão:** assets no classpath do `lwjgl3` por `resources.srcDirs`.
  **Alternativa descartada:** mudar o `workingDir` do `run` para
  `core/assets`. O `ranking.json` da raiz da fase mudaria de lugar.

## Riscos e impactos

- **Cópia grande num commit só:** a tarefa 1 deve ser só a cópia (e o
  título do README), para o diff ser revisável por `diff -r`.
- **`AssetManager` exige contexto OpenGL:** qualquer teste que instancie
  `Recursos` quebra no gate. Os testes usam só o catálogo, os arquivos no
  disco e o gerador.
- **Tamanho no git:** ~350 KB da música + < 100 KB do resto. Aceitável.
- **Fonte padrão e acentos:** sem impacto aqui (os textos não mudam).
- **Fase 5 (cliente):** o `briefing-fase5` prevê copiar a Fase 4. O
  catálogo, os assets e o `Recursos` irão junto.

## Estratégia de testes

| Requisito | Teste |
|---|---|
| R1.2 | `diff -r` das pastas `domain`, `application`, `infrastructure` (main e test) entre Fase 3 e Fase 4, registrado nas notas da tarefa 1 |
| R2.2, R2.3 | `tools/src/test/.../GeradorAssetsTest`: gera duas vezes em `@TempDir` e compara (pixels / bytes); confere que todos os arquivos do catálogo foram criados |
| R2.4 | `GeradorAssetsTest`: gera em `@TempDir` e compara com a pasta `core/assets` (via `System.getProperty("missaomarte.assets")`) |
| R2.5 | `CreditosTest`: cada `caminho()` do catálogo aparece no `CREDITOS.md` |
| R3 | `ImagensAssetsTest` (em `tools`): para cada `Imagem`, lê o PNG com `ImageIO`, confere largura = quadros × larguraQuadro e altura; alfa 0 num canto dos elementos; alfa 255 no fundo e no terreno; quadros de animação com pixels diferentes |
| R4 | `SonsAssetsTest` (em `tools`): cabeçalho (PCM, 1 canal, 22 050 Hz, 16 bits), duração pelo nº de amostras, primeira e última amostra 0, efeitos distintos, pico da música < menor pico dos efeitos |
| R5.2, R5.3 | `core/src/test/.../gdx/catalogo/ImagemTest`: um caso por símbolo e símbolos desconhecidos (`'?'`, `' '`, `'Z'`) → `GENERICA` |
| R5.1, R5.4, R6, R7 | Revisão + `grep` + manual (janela) |
| R8 | Gate |
