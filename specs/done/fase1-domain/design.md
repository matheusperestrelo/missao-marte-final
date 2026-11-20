# Design: fase1-domain

## Abordagem

Criar `fases/fase-01-refatoracao/` com o pacote `missaomarte.domain`. As
classes da Fase 0 viram entidades com comportamento. Coordenadas soltas
viram `Posicao`, e os quatro inteiros de limite viram `Mapa`. Os símbolos e
os nomes da legenda saem de um único enum, `TipoElemento`. A aleatoriedade
fica fora do `domain`: o inimigo recebe a direção pronta.

## Componentes afetados

Todos novos, em `fases/fase-01-refatoracao/src/missaomarte/domain/`:

| Arquivo | Responsabilidade |
|---|---|
| `Direcao.java` | enum `CIMA(0,-1)`, `BAIXO(0,1)`, `ESQUERDA(-1,0)`, `DIREITA(1,0)` com `dx`/`dy` |
| `Posicao.java` | `record Posicao(int x, int y)`; `mover(Direcao)` devolve nova instância; constante `ORIGEM` = (0,0). O `record` dá `equals`/`hashCode` por coordenadas |
| `Mapa.java` | `tamanho`, `getMin()`/`getMax()` (= −N/+N), `contem(Posicao)` |
| `TipoElemento.java` | enum na ordem da legenda: `NAVE('@',"Nave")`, `PROFESSOR('P',"Professor")`, `ENGENHEIRO('E',"Engenheiro")`, `ASTRONAUTA('T',"Astronauta")`, `ASTEROIDE('#',"Asteroide")`, `INIMIGO('X',"Inimigo")`, `BASE('L',"Plataforma de Pouso")`; `simbolo()`, `nome()` |
| `ElementoMapa.java` | interface: `Posicao posicao()`, `TipoElemento tipo()`, `default char simbolo()` = `tipo().simbolo()` |
| `Base.java` | elemento fixo em `Posicao.ORIGEM`, tipo `BASE` |
| `Passageiro.java` | abstrata; `nome`, `posicao`; `abstract int pontuacao()`; `abstract TipoElemento tipo()` |
| `Professor.java`, `Engenheiro.java`, `Astronauta.java` | 10/15/20 pontos e o próprio `TipoElemento` |
| `Perigo.java` | abstrata; guarda a posição e implementa `colideCom(Posicao)` uma única vez |
| `Asteroide.java` | `Perigo` parado, tipo `ASTEROIDE` |
| `Inimigo.java` | `Perigo` com `mover(Direcao, Mapa)`: só anda se o destino estiver em `mapa.contem` |
| `Nave.java` | posição (começa em `ORIGEM`), `capacidade`, `vidas` = 3, lista a bordo; `mover(Direcao, Mapa)`, `embarcar(Passageiro)` → boolean, `estaCheia()`, `perderVida()`, `desembarcarTodos()` → `List<Passageiro>`, `getABordo()` (cópia não modificável) |
| `Missao.java` | `Missao(Mapa, Nave)`; guarda a base, os passageiros no mapa, os entregues, os asteroides e os inimigos |

Operações da `Missao`:

- `adicionarPassageiro`, `adicionarAsteroide`, `adicionarInimigo`: usadas
  pela `MissaoFactory`.
- `estaOcupada(Posicao)`: posição ocupada pela nave, por um passageiro no
  mapa, por um asteroide ou por um inimigo.
- `passageiroNaPosicaoDaNave()` → `Passageiro`, ou `null` se não houver,
  como `passagemNaPosicao` da Fase 0 (`Missao.java:51-56`).
- `embarcar(Passageiro)` → `boolean`: `false` se a nave estiver cheia, e
  nesse caso o passageiro fica no mapa.
- `desembarcarNaBase()` → `int` entregues neste pedido.
- `moverInimigos(Supplier<Direcao>)`: chama o `Supplier` uma vez por
  inimigo, na ordem de inserção.
- `haColisao()`.
- `getABordo()`, `getEntregues()`, `getRestantes()`, `getTotal()`,
  `todosEntregues()`.
- `getPassageirosNoMapa()`, para a lista "Passageiros na superfície".
- `elementos()`: `List<ElementoMapa>` não modificável, na ordem nave,
  passageiros, asteroides, inimigos e base.
- `getMapa()`, `getNave()`.

## Fluxo

1. A `MissaoFactory` (spec 2) cria o `Mapa`, a `Nave` e a `Missao` e
   adiciona os elementos.
2. A cada turno, a `GameSession` (spec 2) chama `nave.mover(...)`,
   `missao.desembarcarNaBase()`, `missao.passageiroNaPosicaoDaNave()`,
   `missao.embarcar(p)`,
   `missao.moverInimigos(...)` e `missao.haColisao()`.
3. O console (spec 3) percorre `missao.elementos()` para desenhar e
   `TipoElemento.values()` para a legenda.

## Decisões e alternativas descartadas

- **Decisão:** um enum `TipoElemento` com símbolo e nome da legenda;
  cada classe devolve o seu.
  **Alternativa descartada:** um símbolo `char` em cada classe, sem enum.
  Sem enum, a legenda da Fase 0, que lista **todos** os tipos mesmo os
  ausentes no mapa (ex.: no Fácil não há astronauta), não poderia ser
  montada sem uma lista fixa no console. Isso violaria "sem símbolos fixos
  espalhados". O limite é que um tipo novo exige uma constante nova no
  enum: registrar como limitação no tutorial (#7).
- **Decisão:** a direção do inimigo chega por `Supplier<Direcao>`.
  **Alternativa descartada:** passar `Random` ao `domain`, proibido pela
  issue #4. Também foi descartado passar uma lista de direções, que
  obrigaria quem chama a saber quantos inimigos há.
- **Decisão:** o embarque usa duas operações simples, como a Fase 0:
  `passageiroNaPosicaoDaNave()` e `embarcar(p)`. A `GameSession` distingue
  "sem passageiro" de "nave cheia".
  **Alternativa descartada:** um tipo `ResultadoEmbarque` com enum de
  situação, porque era mais uma classe para o mesmo efeito.
- **Decisão:** a `Posicao` é um `record`. É Java 17, imutável e já traz
  `equals`/`hashCode`.
- **Decisão:** sem pacote `domain.model` e sem subpacotes. São poucas
  classes, e a estrutura fica simples.

## Riscos e impactos

- Nenhum impacto na Fase 0: pasta nova.
- A ordem de `elementos()` define o que aparece quando dois elementos
  dividem a célula (ex.: inimigo sobre passageiro). Ela precisa seguir a
  prioridade da Fase 0, senão o desenho muda.
- A ordem de chamada do `Supplier` em `moverInimigos` define a sequência do
  `Random` (spec 2). Mudar a ordem muda o jogo para a mesma semente.

## Estratégia de testes

A Fase 1 não tem JUnit (decisão da fila; os testes ficam na #9, spec
`fase2-testes-junit`). A verificação de cada critério é:

- **gate** (`.workflow/check.sh`): compilação e imports entre camadas
  (R1.2 a R1.4);
- **busca** (`grep -rnE "Random|System\.(out|in)|Scanner|instanceof|currentTimeMillis|LocalDateTime\.now" fases/fase-01-refatoracao/src/missaomarte/domain`
  sem resultado): R1.5 e R4.4;
- **inspeção** do revisor: os demais critérios, conferindo assinatura e
  corpo dos métodos. O comportamento ponta a ponta é verificado no roteiro
  manual da spec `fase1-console-infra`.

Relacionada: o `briefing-fase2` (em `planned/`) prevê os testes JUnit de
`Posicao`, `Mapa` e `Nave` sobre estas classes.
