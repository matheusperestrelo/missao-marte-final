# Design: fase4-interface-grafica

## Abordagem

Seguir o padrão das Fases 3 e 4: **lógica sem libGDX com teste** e
**desenho fino por cima**. A `TelaPartida` passa a ser só a ponte entre
teclado, `ControlePartida` e os renderizadores. Toda a decisão sobre o que
desenhar e tocar fica em classes puras no pacote novo
`missaomarte.gdx.visual`, testadas sem janela:

- **posição desenhada** que persegue a lógica (`MovimentoSuave`);
- **relógio de animação** (quadro atual de laço e de explosão);
- **direção visual** da nave a partir da última ação de movimento;
- **reação ao turno** (eventos → efeito sonoro e explosão);
- **efeitos da partida** (explosão ativa, nave visível, caixa final
  visível, som final pendente);
- **controle de música** atrás de uma porta (`Trilha`);
- **textos do HUD e da caixa final**;
- **semente** a partir da propriedade do sistema.

O libGDX só aparece em `MundoRenderer`, `HudRenderer`, nas telas, no
adaptador da música e no `Recursos` (spec 15). Nada em `domain`,
`application` ou `infrastructure` muda.

## Componentes afetados

Caminhos relativos a `fases/fase-04-interface-grafica/core/src/main/java/missaomarte/`,
salvo indicação.

| Arquivo | Mudança |
|---|---|
| `gdx/visual/PosicaoVisual.java` | Novo, sem libGDX. `float x, y`; `mirar(int x, int y)`; `avancar(float delta)` anda `12 × delta` células na direção do alvo (por eixo, em linha reta) e prende no alvo se passaria; `chegou()` |
| `gdx/visual/MovimentoSuave.java` | Novo, sem libGDX. `IdentityHashMap<ElementoMapa, PosicaoVisual>`. `sincronizar(Iterable<ElementoMapa>)`: elemento novo nasce já na posição lógica; elemento conhecido recebe `mirar(posição lógica)`. `avancar(delta)`, `posicao(ElementoMapa)`, `todosChegaram()`. Não olha tipo nem classe: suaviza qualquer elemento cuja posição lógica mudou (na prática, nave e inimigos) |
| `gdx/visual/RelogioAnimacao.java` | Novo, sem libGDX. `avancar(delta)`; `int quadroEmLaco(int quadros, float segundosPorQuadro)`; constantes `LACO = 0.2f`, `EXPLOSAO = 0.08f` |
| `gdx/visual/DirecaoVisual.java` | Novo, sem libGDX. Começa em `Direcao.CIMA`. `registrar(Optional<Acao>)`: só ações de movimento mudam. `float graus()`: `CIMA` 0, `ESQUERDA` 90, `BAIXO` 180, `DIREITA` 270 (rotação anti-horária do `SpriteBatch.draw`) |
| `gdx/visual/ReacaoTurno.java` | Novo, sem libGDX. `static ReacaoTurno de(ResultadoTurno, Posicao posicaoNave)`: `Optional<Som> efeito()` pela prioridade colisão > entrega > embarque > movimento (recusas e lista vazia → vazio); `Optional<Posicao> explosao()` (posição lógica da nave no turno com `COLISAO`); `boolean naveDestruida()` (`DERROTA_NAVE_DESTRUIDA`); `Optional<Som> somFinal()` (`VITORIA` → `Som.VITORIA`; qualquer `DERROTA_*` → `Som.DERROTA`); `Optional<String> causa()` ("Nave destruída" / "Pontuação zerada") |
| `gdx/visual/EfeitosPartida.java` | Novo, sem libGDX. Recebe cada `ReacaoTurno`; guarda a explosão ativa (posição + tempo); `avancar(delta)`; `Optional<Posicao> explosao()` e `int quadroExplosao()`; `boolean naveVisivel()` (falso depois que a explosão de nave destruída termina); `boolean caixaVisivel(boolean partidaTerminada, boolean movimentoChegou)` (terminada, sem explosão ativa e movimento chegou); `Optional<Som> consumirSomFinal()` devolve o som final **uma vez**, no primeiro quadro em que a caixa fica visível |
| `gdx/visual/Trilha.java` | Novo, sem libGDX. Porta: `void tocar()`, `void pausar()` |
| `gdx/visual/ControleMusica.java` | Novo, sem libGDX. Usa uma `Trilha`. `aoAbrirMenu()`: toca se não estiver tocando (primeira vez ou depois da pausa). `aoTerminarPartida()`: pausa. Abandono não chama nada |
| `gdx/TrilhaGdx.java` | Novo. Implementa `Trilha` com o `Music` do `Recursos`: `setLooping(true)`, `setVolume(0.4f)`, `play()`/`pause()` |
| `gdx/visual/TextoHud.java` | Novo, sem libGDX. Só lê a `GameSession`: `barra()` → "Pontos: n", "Tempo: mm:ss"; `vidasCheias()`, `vidasVazias()` (3 − vidas); `painel()` → "Piloto: …", "Dificuldade: …", "Entregues: e/t", "Restantes: n", "Movimentos: n"; `vagasOcupadas()`, `vagasLivres()`; `RODAPE = "WASD/setas: mover · E: embarcar · Q/Esc: sair"`; `static String tempo(long segundos)` → `mm:ss` |
| `gdx/visual/TextoCaixaFinal.java` | Novo, sem libGDX. `List<String> linhas(ResumoPartida, Optional<String> causa, boolean entrouTop10)`: título ("MISSÃO CUMPRIDA" / "GAME OVER"), causa (só na derrota), "Pontos", "Tempo", "Movimentos", "Entregues: e/t", "Você entrou no Top 10!" (só se entrou), `ControlePartida.CONTINUAR` |
| `gdx/MapaGeometria.java` | Acrescenta `float x(float celulaX)` e `float y(float celulaY)` (geometria fracionária), com a mesma fórmula. Os métodos `int` continuam |
| `gdx/ControlePartida.java` | Guarda o último `ResultadoTurno` e a última `Optional<Acao>` recebida (`ultimoResultado()`, `ultimaAcao()`) e a causa da derrota. `tecla(...)` continua sem executar turno com a partida terminada. Nada de regra novo |
| `gdx/MundoRenderer.java` | Novo (libGDX). `desenhar(SpriteBatch, Missao, MovimentoSuave, RelogioAnimacao, DirecaoVisual, EfeitosPartida)`: terreno em cada célula; elementos pelo `Imagem.doSimbolo(simbolo)` na posição de `MovimentoSuave` convertida pela `MapaGeometria` fracionária, no quadro do relógio quando `emLaco()`; nave por último, com propulsor atrás, girados por `DirecaoVisual.graus()` em torno do centro, se `naveVisivel()`; explosão por cima, se ativa |
| `gdx/HudRenderer.java` | Novo (libGDX). Barra superior (ícones de vida + textos), painel lateral (textos, ícones de vaga, mensagens e alerta da Fase 3 com quebra de linha), rodapé e caixa final (retângulo escuro semitransparente + `TextoCaixaFinal`) |
| `gdx/telas/TelaPartida.java` | `keyDown`: `Enter` só navega se `efeitos.caixaVisivel(...)`; outras teclas → `controle.tecla(...)`, depois `direcao.registrar(acao)`, `ReacaoTurno.de(...)` → toca o efeito (`recursos.som(...).play()`), passa a reação ao `EfeitosPartida` e, se a partida terminou, `game.getMusica().aoTerminarPartida()`. `desenhar(delta)`: `movimento.sincronizar(...)`, `movimento.avancar(delta)`, relógio e efeitos avançam, `MundoRenderer` e `HudRenderer` desenham; ao `consumirSomFinal()` presente, toca o som |
| `gdx/TelaBase.java` | Acrescenta `desenharFundo()` (`Imagem.FUNDO` em 0,0) e `tocarSelecionar()` |
| `gdx/telas/TelaMenu.java`, `TelaNovaPartida.java`, `TelaRanking.java` | Chamam `desenharFundo()` antes dos textos e `tocarSelecionar()` nas ações de R7.2. Disposição e textos iguais |
| `gdx/MissaoMarteGame.java` | Cria `ControleMusica` com `TrilhaGdx` depois de `recursos.carregar()`; chama `musica.aoAbrirMenu()` ao abrir o Menu em `create()` e em `irParaMenu()`; `getMusica()` |
| `gdx/visual/Semente.java` | Novo, sem libGDX. `static Random random(String valor, Consumer<String> aviso)`: `null` → `new Random()`; inteiro (`Long.parseLong`) → `new Random(n)`; inválido → `aviso.accept("Semente inválida: <valor>; usando sorteio")` e `new Random()` |
| `lwjgl3/src/main/java/missaomarte/lwjgl3/Lwjgl3Launcher.java` | `Random random = Semente.random(System.getProperty("missaomarte.semente"), msg -> System.err.println(msg));` (antes de o `Gdx.app` existir) |
| `lwjgl3/build.gradle` | No `run`: `if (project.hasProperty('missaomarte.semente')) systemProperty 'missaomarte.semente', project.property('missaomarte.semente')` |
| `fases/fase-04-interface-grafica/README.md` | Reescrito (R10.1) |
| `docs/fase-04-tutorial.md` (raiz do repositório) | Novo (R10.2), no mesmo lugar do `docs/fase-03-tutorial.md` |

Layout (800×600): barra superior de `y = 568` a `600`; rodapé de `0` a
`24`; mapa 528×528 com origem em `(16, 32)`; painel de `x = 560` a `790`;
caixa final centralizada sobre o mapa (~360×240). Ajuste fino é livre,
desde que nada se sobreponha (R6.7).

## Fluxo

```
keyDown(tecla)
  └─ Enter? ── caixaVisivel? ── sim → navegar(controle.enter())  ; não → ignora
  └─ senão: acao = mapeador.acao(tecla)
            controle.tecla(acao)                       (turno na hora; lógica não espera)
            direcao.registrar(acao)
            r = ReacaoTurno.de(controle.ultimoResultado(), nave.posicao())
            r.efeito() → recursos.som(...).play()
            efeitos.receber(r)
            sessao terminou? → musica.aoTerminarPartida()
            abandonada? → game.irParaMenu()            (música segue tocando)

desenhar(delta)
  movimento.sincronizar(elementos); movimento.avancar(delta)
  relogio.avancar(delta); efeitos.avancar(delta)
  MundoRenderer.desenhar(...); HudRenderer.desenhar(...)
  efeitos.caixaVisivel(terminada, movimento.todosChegaram()) → caixa
  efeitos.consumirSomFinal() → toca vitória/derrota uma vez
```

A `ReacaoTurno` só é criada quando o turno executou (o último resultado
mudou). Tecla ignorada não gera reação (R3.6).

## Decisões e alternativas descartadas

- **Decisão:** um efeito sonoro por turno, por prioridade (colisão >
  entrega > embarque > movimento); vitória e derrota tocam com a caixa.
  **Alternativa descartada:** tocar um som por evento. Movimento + entrega
  + colisão no mesmo turno virariam ruído, e o som de derrota tocaria antes
  de a explosão acabar.
- **Decisão:** explosão por cima, nave visível; na destruição, a nave some
  ao fim da explosão (alinhamento, pergunta 5).
  **Alternativas descartadas:** esconder a nave durante toda explosão
  (pareceria destruição na colisão comum) e nunca esconder (a destruição
  ficaria sem diferença visual).
- **Decisão:** caixa só depois que o movimento chega e a explosão termina,
  e `Enter` ignorado até lá.
  **Alternativa descartada:** caixa e `Enter` imediatos, como na Fase 3.
  A caixa cobriria a explosão e o jogador poderia sair sem ver o fim.
- **Decisão:** direção da nave guardada na camada `gdx`
  (`DirecaoVisual`), a partir da última ação de movimento.
  **Alternativa descartada:** campo de direção na `Nave`. Mudaria o
  `domain`, que deve ficar idêntico ao da Fase 3 (R9.1), só por causa do
  desenho.
- **Decisão:** `MovimentoSuave` por identidade do elemento, sem olhar tipo.
  **Alternativa descartada:** suavizar só a nave. Os inimigos saltariam
  enquanto a nave desliza.
- **Decisão:** música atrás da porta `Trilha`, controlada pelo
  `ControleMusica`.
  **Alternativa descartada:** chamar `Music` direto nas telas. Não daria
  para testar pausa e retomada sem áudio.
- **Decisão:** semente lida no launcher, com `-P` repassado pelo `run`.
  **Alternativa descartada:** ler a propriedade dentro do `core`. A raiz
  de composição é o launcher desde a Fase 3.
- **Decisão:** fonte padrão do libGDX (`BitmapFont()`), como na Fase 3.
  **Alternativa descartada:** `gdx-freetype` com TTF. Dependência nativa
  nova, só para acentos que a fonte padrão já deve cobrir (ver riscos).
- **Decisão:** README e tutorial como última tarefa desta spec
  (alinhamento), não como ajuste separado.

## Riscos e impactos

- **Acentos na fonte padrão:** se faltar algum glifo (R6.5), registrar nas
  notas e propor ao usuário a troca de fonte como ajuste; não trocar por
  conta própria.
- **`delta` grande (janela arrastada, pausa do SO):** `PosicaoVisual`
  prende no alvo, e o relógio de explosão termina; nada ultrapassa (R2.2).
- **Muitos inimigos:** `IdentityHashMap` por quadro é barato no 11×11.
- **Elemento que some do mapa (passageiro embarcado):** `sincronizar`
  remove do mapa as entradas que não estão mais na missão.
- **Fase 5 (cliente):** o `briefing-fase5` copia a Fase 4. Os pacotes
  `gdx.visual` e os renderizadores vão junto.

## Estratégia de testes

Testes em `core/src/test/java/missaomarte/gdx/` (e `gdx/visual/`), sem
janela, sem OpenGL, sem áudio:

| Requisito | Teste |
|---|---|
| R1.2 | `RelogioAnimacaoTest`: quadro 0 em t < 0,2 s, 1 em 0,2 ≤ t < 0,4 s, volta a 0 |
| R1.3, R1.4 | `DirecaoVisualTest`: começa `CIMA`/0°; cada movimento → graus; `EMBARCAR`, vazio e `SAIR` não mudam |
| R2.1–R2.3 | `PosicaoVisualTest` e `MovimentoSuaveTest`: 1 célula em 1/12 s; `delta` grande prende no alvo; novo alvo no meio do caminho segue do ponto atual; elemento novo nasce no lugar |
| R2.4 | `MapaGeometriaTest`: casos fracionários (ex.: `x(0.5f)`) coerentes com os inteiros |
| R3.1–R3.4, R3.6 | `ReacaoTurnoTest` (com `ResultadoTurno` montado à mão ou `GameSession` real com `Random` fixo) e `EfeitosPartidaTest` (explosão 4 × 0,08 s, nave some só na destruição) |
| R4.3–R4.5 | `ControleMusicaTest` com `Trilha` falsa que conta chamadas |
| R5.1–R5.7 | `TextoCaixaFinalTest` e `EfeitosPartidaTest` (caixa espera movimento e explosão; som final uma vez); `Enter` ignorado sem caixa pela lógica de `caixaVisivel` |
| R6.1–R6.4, R6.6 | `TextoHudTest` com `GameSession` real e `Clock` fixo (tempo `mm:ss`, parado no fim, contagens de ícones) |
| R8.1–R8.3 | `SementeTest`: mesma semente → duas `GameSession` com o mesmo mapa e mesmas posições de inimigos após a mesma sequência; `null` e inválido; aviso recebido |
| R1.1, R1.5, R4.1, R4.2, R6.5, R6.7, R7, R8.4, R10 | Revisão + manual no roteiro de QA |
| R9 | `diff -r` com a Fase 3 + gate |
