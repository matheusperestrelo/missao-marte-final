# Requisitos: fase1-domain

**Issue:** #4 · **Trilha:** A (núcleo do jogo) · **Fase:** 1 · **Depende de:** — (primeira spec da fila)
**Branch sugerida:** `feat/issue-4-fase1-domain` · **PR:** "Closes #4"

## Contexto

A Fase 0 (`fases/fase-00-console/src`) concentra regras, desenho e ranking no
`Main.java`, e as entidades são só dados (`docs/fase-00-arquitetura.md`,
seções 1 e 2). Esta spec cria a pasta da Fase 1 e a camada `domain`: as
entidades e as regras que pertencem a elas, sem aleatoriedade, sem entrada e
sem saída. É a base das specs `fase1-application` (ordem 2) e
`fase1-console-infra` (ordem 3).

## Fora de escopo

- `Acao`, `Dificuldade`, `GameSession`, `MissaoFactory`, resultado do turno,
  eventos e ranking: são da spec `fase1-application`.
- Console, desenho do mapa, `Main` e `build.sh` executável do jogo: são da
  spec `fase1-console-infra`. Nesta spec o código só precisa compilar.
- Gradle e JUnit: chegam na Fase 2 (issues #8 e #9). Nesta fase não há
  testes automáticos.
- Sorteio de posições: fica na `MissaoFactory` (application), nunca no
  `domain`.
- `Mapa` controlando ocupação de células ou sorteio: descartado na
  `fase-00-arquitetura.md` (decisão 3 e "Fora de escopo"). A ocupação fica
  na `Missao`.
- Documentação da refatoração (tutorial, SOLID, GRASP): issue #7, sem spec.
- Qualquer alteração em `fases/fase-00-console/`.

## Requisitos

### R1 — Pasta da Fase 1 e isolamento da camada

**História:** Como equipe, quero a Fase 1 numa pasta nova com a camada
`domain` isolada, para evoluir o jogo sem tocar na Fase 0 e sem acoplar as
regras à interface.

**Critérios de aceitação:**

1. QUANDO a spec estiver concluída O SISTEMA DEVE ter o código em
   `fases/fase-01-refatoracao/src/missaomarte/domain/`, no pacote
   `missaomarte.domain`. _(verificação: inspeção)_
2. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE compilar todos os
   fontes da Fase 1 com `javac --release 17` sem erro. _(verificação: gate)_
3. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE confirmar que
   `fases/fase-00-console/src` não mudou. _(verificação: gate)_
4. O SISTEMA DEVE ter um `domain` que não importa `missaomarte.application`,
   `missaomarte.console` nem `missaomarte.infrastructure`.
   _(verificação: gate)_
5. O SISTEMA DEVE ter um `domain` sem `java.util.Random`, `System.out`,
   `System.in`, `Scanner` e relógio do sistema. _(verificação: busca por
   `grep` nos fontes do `domain`)_

### R2 — Posição e direção

**História:** Como jogo, quero representar coordenadas e direções como
valores, para comparar posições sem repetir `x == x && y == y` pelo código.

**Critérios de aceitação:**

1. QUANDO uma posição for movida numa direção O SISTEMA DEVE devolver uma
   **nova** posição, sem alterar a original. _(verificação: inspeção)_
2. O SISTEMA DEVE considerar duas posições iguais (`equals` e `hashCode`)
   se e somente se tiverem as mesmas coordenadas. _(verificação: inspeção)_
3. O SISTEMA DEVE aplicar as direções como na Fase 0
   (`Nave.moverComLimites`, linhas 35-38): CIMA = y−1, BAIXO = y+1,
   ESQUERDA = x−1, DIREITA = x+1. _(verificação: inspeção)_

### R3 — Mapa

**História:** Como jogo, quero um mapa com tamanho e limites, para saber se
uma posição está dentro dele sem passar quatro inteiros de método em método.

**Critérios de aceitação:**

1. QUANDO o mapa for criado com tamanho N O SISTEMA DEVE aceitar as
   coordenadas x e y de −N a +N, inclusive. _(verificação: inspeção)_
2. QUANDO for perguntado se uma posição está dentro do mapa O SISTEMA DEVE
   responder sim só se as duas coordenadas estiverem nesse intervalo.
   _(verificação: inspeção)_

### R4 — Elementos do mapa informam posição e símbolo

**História:** Como console (e, depois, as telas gráficas), quero que cada
elemento do mapa diga a própria posição, o próprio símbolo e o nome da
legenda, para desenhar sem `instanceof` e sem símbolos fixos espalhados.

**Critérios de aceitação:**

1. O SISTEMA DEVE fazer cada elemento do mapa informar a posição atual e o
   símbolo, com os mesmos símbolos da Fase 0: nave `@`, Professor `P`,
   Engenheiro `E`, Astronauta `T`, asteroide `#`, inimigo `X` e base `L`.
   _(verificação: inspeção)_
2. O SISTEMA DEVE fazer cada tipo de elemento informar o nome usado na
   legenda e na lista de passageiros: "Nave", "Professor", "Engenheiro",
   "Astronauta", "Asteroide", "Inimigo" e "Plataforma de Pouso".
   _(verificação: inspeção)_
3. O SISTEMA DEVE permitir listar todos os tipos de elemento, na ordem da
   legenda da Fase 0 (`Main.java:386`), para a legenda ser montada a partir
   deles. _(verificação: inspeção)_
4. O SISTEMA NÃO DEVE usar `instanceof` nem `if`/`switch` por tipo de
   passageiro no `domain`. _(verificação: busca por `grep "instanceof"`)_

### R5 — Passageiros

**História:** Como jogo, quero tratar todos os passageiros do mesmo jeito,
para que um tipo novo não exija editar o resto do código.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `Passageiro` como classe abstrata, sem campo de tipo
   em texto. _(verificação: inspeção)_
2. O SISTEMA DEVE dar a cada passageiro nome, posição, pontuação e tipo de
   elemento. A pontuação é: Professor 10, Engenheiro 15 e Astronauta 20,
   como na Fase 0. _(verificação: inspeção)_

### R6 — Nave

**História:** Como piloto, quero que a nave se mova dentro do mapa, embarque
passageiros até a capacidade e os entregue na base.

**Critérios de aceitação:**

1. QUANDO a nave for criada O SISTEMA DEVE posicioná-la em (0,0), com 3
   vidas, nenhum passageiro a bordo e a capacidade recebida no construtor.
   _(verificação: inspeção)_
2. QUANDO a nave for movida para uma posição dentro do mapa O SISTEMA DEVE
   mudar a posição dela. _(verificação: inspeção)_
3. SE o destino estiver fora do mapa ENTÃO O SISTEMA DEVE manter a nave
   parada, como na Fase 0. _(verificação: inspeção)_
4. QUANDO houver vaga O SISTEMA DEVE embarcar o passageiro e informar
   sucesso. _(verificação: inspeção)_
5. SE a nave estiver cheia (a bordo = capacidade) ENTÃO O SISTEMA DEVE
   recusar o embarque sem alterar os passageiros a bordo.
   _(verificação: inspeção)_
6. QUANDO a nave perder uma vida O SISTEMA DEVE reduzir as vidas em 1, sem
   ficar abaixo de 0. _(verificação: inspeção)_
7. QUANDO a nave desembarcar os passageiros O SISTEMA DEVE devolver todos
   os que estavam a bordo e esvaziar a nave, liberando a capacidade.
   _(verificação: inspeção)_

### R7 — Perigos

**História:** Como jogo, quero asteroides parados e inimigos que andam, para
o piloto ter o que evitar.

**Critérios de aceitação:**

1. O SISTEMA DEVE considerar colisão quando o perigo está na mesma posição
   que a nave. _(verificação: inspeção)_
2. QUANDO um inimigo for movido numa direção recebida de fora O SISTEMA
   DEVE andar 1 casa nessa direção. _(verificação: inspeção)_
3. SE o destino do inimigo estiver fora do mapa ENTÃO O SISTEMA DEVE
   deixá-lo parado. O inimigo pode entrar em células com passageiros, base
   ou outros perigos, como na Fase 0. _(verificação: inspeção)_
4. O SISTEMA NÃO DEVE ter a lógica de colisão duplicada entre asteroide e
   inimigo (hoje está repetida em `Asteroide.java:13` e `Inimigo.java:20`).
   _(verificação: inspeção)_

### R8 — Missão: ocupação, embarque, desembarque e indicadores

**História:** Como jogo, quero que a missão guarde os elementos e ofereça
operações (não listas), para que quem a usa não decida a regra lendo
coordenadas.

**Critérios de aceitação:**

1. QUANDO for perguntado se uma posição está ocupada O SISTEMA DEVE
   considerar a nave, os passageiros que ainda estão no mapa, os asteroides
   e os inimigos. _(verificação: inspeção)_
2. O SISTEMA DEVE informar qual passageiro está na posição da nave, ou que
   não há nenhum. _(verificação: inspeção)_
3. QUANDO um passageiro do mapa for embarcado e houver vaga O SISTEMA DEVE
   tirá-lo do mapa, colocá-lo a bordo e informar sucesso.
   _(verificação: inspeção)_
4. SE a nave estiver cheia ENTÃO O SISTEMA DEVE recusar o embarque e
   manter o passageiro no mapa. _(verificação: inspeção)_
5. QUANDO o desembarque for pedido com a nave na base e passageiros a
   bordo O SISTEMA DEVE desembarcar todos, contá-los como entregues e
   informar quantos foram entregues. **Mudança intencional em relação à
   Fase 0**, que não tinha desembarque. _(verificação: inspeção)_
6. SE o desembarque for pedido com a nave fora da base, ou sem ninguém a
   bordo, ENTÃO O SISTEMA DEVE informar zero entregues sem alterar nada.
   _(verificação: inspeção)_
7. O SISTEMA DEVE informar a qualquer momento: a bordo, entregues,
   restantes no mapa e total. Vale sempre a bordo + entregues + restantes =
   total. _(verificação: inspeção)_
8. QUANDO todos os passageiros tiverem sido entregues O SISTEMA DEVE
   responder sim a "todos entregues". _(verificação: inspeção)_
9. QUANDO os inimigos forem movidos O SISTEMA DEVE pedir uma direção de
   fora para cada inimigo, na ordem em que foram adicionados.
   _(verificação: inspeção)_
10. O SISTEMA DEVE informar se algum perigo colide com a nave.
    _(verificação: inspeção)_
11. O SISTEMA DEVE oferecer os elementos para desenho, só para leitura, na
    ordem de prioridade do desenho da Fase 0 (`Main.java:346-379`): nave,
    passageiros no mapa, asteroides, inimigos e base. Alterar a lista
    devolvida não altera a missão. _(verificação: inspeção)_
