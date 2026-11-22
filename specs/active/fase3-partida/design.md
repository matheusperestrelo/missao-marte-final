# Design: fase3-partida

## Abordagem

Seguir o padrão da spec 10: um controle sem libGDX com teste e uma tela
fina por cima. Quatro peças novas:

- **`MapeadorTeclas`:** keycode → `Optional<Acao>`. Usa só as constantes
  inteiras de `Input.Keys`, então roda em teste sem janela.
- **`MapaGeometria`:** célula → pixel, com a inversão do eixo Y. Não tem
  nenhum import de `com.badlogic`.
- **`MensagensPainel`:** eventos do turno → frases.
- **`ControlePartida`:** guarda a sessão, as mensagens do último turno, o
  registro único no ranking e o destino do `Enter`.

A `TelaPartida` provisória da spec 10 é reescrita. Ela desenha a grade e os
elementos com o `ShapeRenderer`, os símbolos e o painel com a fonte, e
encaminha o `keyDown` ao controle.

## Componentes afetados

| Arquivo (em `fases/fase-03-libgdx/core/src/main/java/missaomarte/`) | Mudança |
|---|---|
| `gdx/MapeadorTeclas.java` | Novo. `Optional<Acao> acao(int keycode)`: `W`/`UP` → `CIMA`, `S`/`DOWN` → `BAIXO`, `A`/`LEFT` → `ESQUERDA`, `D`/`RIGHT` → `DIREITA`, `E` → `EMBARCAR`, `Q`/`ESCAPE` → `SAIR`; o resto → `Optional.empty()`. **Nunca** devolve `Acao.INVALIDA`: tecla desconhecida não chega à sessão |
| `gdx/MapaGeometria.java` | Novo, **sem `com.badlogic`**. Construtor `(int min, int max, float origemX, float origemY, float lado)` (ou a partir do `Mapa`). `float x(int celulaX) = origemX + (celulaX - min) * lado`; `float y(int celulaY) = origemY + (max - celulaY) * lado` (canto inferior esquerdo da célula, em coordenadas libGDX); `float lado()`; largura e altura da grade |
| `gdx/MensagensPainel.java` | Novo, sem libGDX. `List<String> traduzir(ResultadoTurno)` com as frases de R4.2 (as mesmas de `fases/fase-02-gradle/.../console/MensagensConsole.java`, sem `✨`/`🚀` e sem `SEPARADOR`/`\n`). `Optional<String> alerta(Missao, boolean encerrada)` para R4.4 (mesma condição do `JogoConsole`: não encerrada, restantes = 0, a bordo > 0) |
| `gdx/TextoPainel.java` | Novo, sem libGDX. `List<String> estado(GameSession)` com as 8 linhas de R4.1 (`getPiloto`, `getDificuldade`, `getVidas`, `getPontos`, `missao.getABordo()`/`getNave().getCapacidade()`, `getEntregues()`/`getTotal()`, `getRestantes()`, `getMovimentos()`) |
| `gdx/ControlePartida.java` | Novo, sem libGDX. Construtor `(GameSession sessao, RankingService ranking)`. `void tecla(Optional<Acao> acao)`: se a partida acabou ou a ação está vazia, não faz nada; senão `sessao.jogar(acao)`, guarda as mensagens do turno e, se o resultado virou `VITORIA` e ainda não registrou, chama `ranking.registrar(sessao.resumo())` uma vez e guarda `entrou` (ou a mensagem de erro, com `RankingException`, e `entrou = false`). `DestinoFim enter()`: `VITORIA` → `RANKING`, `DERROTA` → `MENU`, senão `NENHUM`. `boolean abandonada()` (`ABORTADA`). Getters: `mensagens()`, `entrouNoRanking()`, `resumo()`, `terminada()` |
| `gdx/DestinoFim.java` | Enum `NENHUM`, `RANKING`, `MENU` |
| `gdx/telas/TelaPartida.java` | Reescrita. `keyDown(keycode)`: se `ENTER`, aplica `controle.enter()` (`RANKING` → `game.irParaRanking(resumo, entrou)`, `MENU` → `game.irParaMenu()`); senão `controle.tecla(mapeador.acao(keycode))` e, se `abandonada()`, `game.irParaMenu()`. Desenho: grade (linhas do `ShapeRenderer`), elementos (`Filled`) com a cor por `TipoElemento`, primeiro todos menos a nave e por último a nave; símbolos com `font.draw` centralizados na célula usando `elemento.tipo().simbolo()`; painel com `TextoPainel` + mensagens + alerta + linha de fim ("Pressione Enter para continuar"), usando `font.draw(batch, texto, x, y, larguraPainel, Align.left, true)` para quebrar linha |
| `gdx/CoresElemento.java` (ou um `switch` na tela) | `TipoElemento` → `Color`. Apresentação, fica no pacote `gdx` |
| `gdx/MissaoMarteGame.java` | `iniciarPartida` cria o `ControlePartida` com a sessão nova e o `RankingService` e abre a `TelaPartida` com ele |

Layout sugerido (área de 800×600): célula de 48 px, grade 528×528 com
origem em `(16, 36)`, painel de `x = 560` a `790`. Ajuste fino é livre,
desde que a grade inteira e o painel caibam sem sobreposição.

Testes em `core/src/test/java/missaomarte/gdx/`: `MapeadorTeclasTest`,
`MapaGeometriaTest`, `MensagensPainelTest`, `TextoPainelTest`,
`ControlePartidaTest`. O `ControlePartidaTest` usa uma `GameSession` real
com uma fábrica e um `Random` controlados (um mapa pequeno montado à mão,
como os dublês de `application/Dubles.java`, mas definidos no pacote de
teste `gdx`, porque os de lá são package-private) e um `RankingService`
com repositório em memória.

## Fluxo

```
TelaPartida.keyDown(k)
  ├─ k == ENTER → controle.enter(): VITORIA → irParaRanking(resumo, entrou) | DERROTA → irParaMenu | senão nada
  └─ senão → controle.tecla(mapeador.acao(k))
               ├─ vazio ou terminada → nada (sem turno)
               ├─ sessao.jogar(acao) → mensagens = MensagensPainel.traduzir(turno)
               ├─ VITORIA e !registrou → entrou = ranking.registrar(sessao.resumo()); registrou = true
               └─ ABORTADA → tela chama irParaMenu()
render → grade + elementos (nave por último) + símbolos + painel (estado, mensagens, alerta, fim)
```

## Decisões e alternativas descartadas

- **Decisão:** só `E` embarca. **Alternativa descartada:** aceitar `C`
  também, como no console. O escopo define `E`, e a troca é registrada.
- **Decisão:** tecla desconhecida vira `Optional.empty()` e não chega à
  sessão. **Alternativa descartada:** mandar `Acao.INVALIDA`. A sessão já
  não gasta turno com ela, mas o "ignorar" ficaria espalhado entre a tela
  e a sessão.
- **Decisão:** o registro no ranking fica no `ControlePartida`, guardado
  por um `boolean`, no turno da vitória. **Alternativa descartada:**
  registrar ao abrir a tela Ranking. Reabrir a tela ou redimensionar a
  janela poderia registrar duas vezes.
- **Decisão:** fim com `Enter`. Vitória vai ao Ranking com resumo, derrota
  vai ao Menu, abandono vai direto ao Menu. Decisão do alinhamento.
- **Decisão:** o símbolo vem de `ElementoMapa.tipo().simbolo()`. No
  contrato (`docs/contratos.md`), `ElementoMapa` faz o papel de
  `ObjetoRenderizavel`. Não criar interface nova.
- **Decisão:** as mensagens reaproveitam as frases do console da Fase 2,
  sem emoji (a fonte padrão não tem esses glifos).
- **Decisão:** um turno por `keyDown`, sem repetição. A repetição de tecla
  foi cortada do projeto.

## Riscos e impactos

- **Inversão do eixo Y:** é o erro mais provável ("para cima" andando para
  baixo na tela). O `MapaGeometriaTest` e o QA manual pegam.
- **`Input.Keys` em teste:** são constantes `static final int`. Ler essas
  constantes não inicializa o `Gdx`. Se algum teste exigir o `Gdx`, a
  classe está errada.
- **Duração da partida:** a sessão congela a duração no fim. Ficar parado
  na tela de fim não muda o tempo do resumo.
- **Fonte padrão:** acentos ("posição", "Não", "órbita") podem não ter
  glifo. Conferir no QA e registrar nas `notas:` se faltar, sem trocar o
  texto nem a fonte sem combinar.
- **Painel estreito:** mensagens longas quebram em várias linhas. Se
  passarem da altura, cortar as mais antigas do turno é aceitável, desde
  que fique registrado nas `notas:`.

## Estratégia de testes

| Requisito | Teste |
|---|---|
| R1.1 a R1.7 | `MapeadorTeclasTest` (cada tecla e `C`/`ENTER`/números → vazio) + `ControlePartidaTest` (vazio não muda pontos nem movimentos) |
| R3.1 a R3.5 | `MapaGeometriaTest` (cantos, origem, passos em `x` e `y`) + inspeção dos imports |
| R4.1 | `TextoPainelTest` com sessão de cenário fixo |
| R4.2 a R4.4 | `MensagensPainelTest` (cada `TipoEvento`, colisão com 0 vidas sem frase, alerta) + `ControlePartidaTest` (mensagens trocam a cada turno) |
| R5.1 a R5.7 | `ControlePartidaTest`: vitória registra uma vez (teclas depois do fim não registram de novo), derrota não registra, `enter()` por resultado, abandono, repositório que falha |
| R1.8, R2, R4.5, R5.8 | Roteiro manual em `:lwjgl3:run` |
| R6 | Revisão + gate |
