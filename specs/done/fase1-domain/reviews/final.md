# Verificação final — fase1-domain

**Veredito:** APROVADA
**Branch:** feat/issue-4-fase1-domain
**Commits da spec:** 15 (1643ffe..c070f2f)

A Fase 1 não tem JUnit (decisão da fila, `design.md` "Estratégia de
testes"). Cada critério é verificado do jeito que a spec define: **gate**
(`check.sh`), **busca** (`grep`) ou **inspeção** do código. Para confirmar a
inspeção, o `domain` também foi compilado fora da árvore e exercitado num
roteiro `jshell` (o mesmo do QA abaixo). Todas as saídas bateram com o
esperado.

## Cobertura

| Requisito | Critério | Verificação | Situação |
|---|---|---|---|
| R1 | 1 | inspeção: 16 fontes em `fases/fase-01-refatoracao/src/missaomarte/domain/`, todos com `package missaomarte.domain;` | coberto |
| R1 | 2 | gate: `check.sh` etapa "javac" (`--release 17 -Xlint:all`) ok | coberto |
| R1 | 3 | gate: `check.sh` etapa "fase 0 imutavel" ok; `git diff --stat main...HEAD` não toca `fases/fase-00-console/` | coberto |
| R1 | 4 | gate: `check.sh` etapa "regras de camadas" ok; `grep` por `application\|console\|infrastructure` no `domain` sem resultado (nem nome qualificado) | coberto |
| R1 | 5 | busca do design + `nanoTime\|Clock\|Instant`: sem resultado | coberto |
| R2 | 1 | inspeção: `Posicao.java:7-9` devolve `new Posicao(...)`; record imutável | coberto |
| R2 | 2 | inspeção: `record Posicao(int x, int y)` (`Posicao.java:3`) | coberto |
| R2 | 3 | inspeção: `Direcao.java:4-7` CIMA(0,-1), BAIXO(0,1), ESQUERDA(-1,0), DIREITA(1,0) = Fase 0 `Nave.java:35-38` | coberto |
| R3 | 1 | inspeção: `Mapa.java:15-21` min = −N, max = +N | coberto |
| R3 | 2 | inspeção: `Mapa.java:23-26` exige x e y no intervalo inclusivo | coberto |
| R4 | 1 | inspeção: `ElementoMapa` (`posicao()`, `simbolo()`); símbolos em `TipoElemento.java:4-10` iguais aos da Fase 0 | coberto |
| R4 | 2 | inspeção: nomes em `TipoElemento.java:4-10` | coberto |
| R4 | 3 | inspeção: `TipoElemento.values()` na ordem da legenda da Fase 0 (`Main.java:386`) | coberto |
| R4 | 4 | busca: sem `instanceof`; os `if` do `domain` (`Nave`, `Inimigo`, `Missao`) não ramificam por tipo de passageiro; sem `switch` | coberto |
| R5 | 1 | inspeção: `Passageiro.java:3` abstrata, campos só `nome` e `posicao` | coberto |
| R5 | 2 | inspeção: `getNome()`, `posicao()`, `pontuacao()` 10/15/20, `tipo()` próprio em cada subclasse | coberto |
| R6 | 1 | inspeção: `Nave.java:8-17` ORIGEM, 3 vidas, lista vazia, capacidade do construtor | coberto |
| R6 | 2 | inspeção: `Nave.java:37-42` | coberto |
| R6 | 3 | inspeção: `Nave.java:39` só move se `mapa.contem(destino)` | coberto |
| R6 | 4 | inspeção: `Nave.java:48-54` devolve `true` ao embarcar | coberto |
| R6 | 5 | inspeção: `Nave.java:44-51` devolve `false` sem alterar a lista | coberto |
| R6 | 6 | inspeção: `Nave.java:56-60` | coberto |
| R6 | 7 | inspeção: `Nave.java:62-66` devolve cópia e limpa | coberto |
| R7 | 1 | inspeção: `Perigo.java:16-18`; usado por `Missao.haColisao` com a posição da nave | coberto |
| R7 | 2 | inspeção: `Inimigo.java:14-19` | coberto |
| R7 | 3 | inspeção: `Inimigo.java:16` só checa `mapa.contem`, não outros elementos | coberto |
| R7 | 4 | inspeção: `colideCom` só em `Perigo.java:16`; `Asteroide` e `Inimigo` não o redefinem | coberto |
| R8 | 1 | inspeção: `Missao.java:44-47` nave, passageiros no mapa, asteroides e inimigos (base excluída) | coberto |
| R8 | 2 | inspeção: `Missao.java:49-56` (ou `null`, como a Fase 0) | coberto |
| R8 | 3 | inspeção: `Missao.java:58-64` | coberto |
| R8 | 4 | inspeção: `Missao.java:59` retorna `false` antes de remover do mapa | coberto |
| R8 | 5 | inspeção: `Missao.java:66-73` | coberto |
| R8 | 6 | inspeção: `Missao.java:67-69` fora da base devolve 0; sem ninguém a bordo, `desembarcarTodos` devolve lista vazia e nada muda | coberto |
| R8 | 7 | inspeção: `Missao.java:86-100`; `total` contado em `adicionarPassageiro`, invariante vale pelas operações da `Missao` | coberto |
| R8 | 8 | inspeção: `Missao.java:102-104` | coberto |
| R8 | 9 | inspeção: `Missao.java:75-79` um `get()` por inimigo, em ordem de inserção | coberto |
| R8 | 10 | inspeção: `Missao.java:81-84` | coberto |
| R8 | 11 | inspeção: `Missao.java:110-118` ordem nave, passageiros, asteroides, inimigos, base = `Main.java:346-379`; `List.copyOf` não modificável | coberto |

## check.sh

passou (fase 0 imutável, javac da Fase 1, regras de camadas)

## Pendências

Nenhuma impede a aprovação. Registradas nas revisões para as próximas specs:

- revisão 01-r1 — `Mapa` aceita `tamanho` negativo (`Mapa.java:7`). Pode ser validado na Fase 2, junto com os testes JUnit de `Mapa`.
- revisão 04-r1 — `Nave.embarcar(null)` é aceito e depois `getABordo()` lança NPE (`Nave.java:48`). Considerar `Objects.requireNonNull`.
- revisão 04-r1 — a tarefa 4 saiu em dois commits (`912fd96` + `90111b7`), contra a regra de um commit por tarefa. Só histórico.
- revisão 05-r1 — `Perigo.posicao` é `protected` (`Perigo.java:5`), então até o `Asteroide` poderia reatribuí-la. Sugestão: campo `private` mais `moverPara` protegido.
- revisão 06-r1 — `estaOcupada` usa a identidade `e != base` (`Missao.java:46`). Funciona, mas fica menos claro.
- revisão 06-r1 — com `total == 0`, `todosEntregues()` devolve `true`. A spec `fase1-application` precisa garantir que a missão tenha passageiros, ou tratar esse caso.
- revisão 06-r1 — a invariante de R8.7 quebra se alguém chamar `missao.getNave().embarcar(p)` direto. A `GameSession` (spec `fase1-application`) deve embarcar só por `missao.embarcar(p)`.
- notas das tarefas 3, 4 e 6 — extras fora do design, sem conflito: `Passageiro.getNome()`, `Nave.getCapacidade()`/`getVidas()`, e `Missao.embarcar` também recusa passageiro que não está no mapa.

## Roteiro de QA

Esta spec não tem jogo executável: o console e o `Main` chegam na spec
`fase1-console-infra`. O QA é feito exercitando o `domain` no `jshell`
(JDK 17+), a partir da raiz do repositório:

0. `bash .workflow/check.sh` → **esperado:** "check.sh: tudo passou" (R1.2, R1.3, R1.4).
1. `grep -rnE "Random|System\.(out|in)|Scanner|instanceof|currentTimeMillis|LocalDateTime\.now" fases/fase-01-refatoracao/src/missaomarte/domain` → **esperado:** nenhuma linha (R1.5, R4.4).
2. `mkdir -p /tmp/qa-f1 && javac --release 17 -d /tmp/qa-f1 fases/fase-01-refatoracao/src/missaomarte/domain/*.java && jshell --class-path /tmp/qa-f1`, depois `import missaomarte.domain.*;`.
3. `var p = new Posicao(1,2); p.mover(Direcao.CIMA); p; p.equals(new Posicao(1,2))` → **esperado:** `Posicao[x=1, y=1]`, `p` continua `Posicao[x=1, y=2]`, `true` (R2.1 a R2.3).
4. `var m = new Mapa(2); m.contem(new Posicao(-2,2)); m.contem(new Posicao(3,0))` → **esperado:** `true`, `false` (R3).
5. `var n = new Nave(1); n.posicao(); n.getVidas(); n.getCapacidade()` → **esperado:** `Posicao[x=0, y=0]`, `3`, `1` (R6.1).
6. Crie a missão: `var ms = new Missao(m, n); var a = new Professor("Ana", new Posicao(1,0)); var b = new Astronauta("Bia", new Posicao(2,0)); ms.adicionarPassageiro(a); ms.adicionarPassageiro(b); ms.adicionarInimigo(new Inimigo(new Posicao(2,2)));`
7. `n.mover(Direcao.DIREITA, m); ms.passageiroNaPosicaoDaNave().getNome(); ms.embarcar(a)` → **esperado:** `"Ana"`, `true` (R6.2, R8.2, R8.3).
8. `n.mover(Direcao.DIREITA, m); ms.embarcar(ms.passageiroNaPosicaoDaNave()); ms.getRestantes()` → **esperado:** `false` (nave cheia), `1` (Bia continua no mapa) (R6.5, R8.4).
9. `n.mover(Direcao.DIREITA, m); n.posicao()` → **esperado:** continua `Posicao[x=2, y=0]` na borda (R6.3).
10. `ms.desembarcarNaBase()` fora da base → **esperado:** `0` (R8.6).
11. `n.mover(Direcao.ESQUERDA, m); n.mover(Direcao.ESQUERDA, m); ms.desembarcarNaBase()`, depois `ms.getABordo(); ms.getEntregues(); ms.getRestantes(); ms.getTotal(); ms.todosEntregues()` → **esperado:** `1`; `0`, `1`, `1`, `2` (0+1+1=2); `false` (R6.7, R8.5, R8.7, R8.8).
12. `ms.moverInimigos(() -> Direcao.BAIXO); ms.elementos().get(2).posicao()` → **esperado:** inimigo continua em `Posicao[x=2, y=2]`, porque y=3 sai do mapa (R7.3, R8.9). Repita com `Direcao.CIMA` → **esperado:** `Posicao[x=2, y=1]` (R7.2).
13. `ms.elementos().forEach(e -> System.out.print(e.simbolo()))` → **esperado:** `@TXL`, na ordem nave, passageiro, inimigo, base (R4.1, R8.11). `ms.elementos().add(n)` → **esperado:** `UnsupportedOperationException` (R8.11).
14. `for (var t : TipoElemento.values()) System.out.print(t.simbolo()+"="+t.nome()+", ")` → **esperado:** `@=Nave, P=Professor, E=Engenheiro, T=Astronauta, #=Asteroide, X=Inimigo, L=Plataforma de Pouso` (R4.2, R4.3).
15. `ms.adicionarAsteroide(new Asteroide(n.posicao())); ms.haColisao()` → **esperado:** `true` (R7.1, R8.10). `ms.estaOcupada(new Posicao(-1,-1))` → **esperado:** `false` (R8.1).
16. `n.perderVida()` quatro vezes, depois `n.getVidas()` → **esperado:** `0`, nunca negativo (R6.6).
17. `a.pontuacao(); b.pontuacao(); new Engenheiro("E", Posicao.ORIGEM).pontuacao()` → **esperado:** `10`, `20`, `15` (R5.2).

## Próximos passos

- APROVADA: QA pelo usuário, merge (PR "Closes #4") e conclusão da spec (active/ → done/).
- Levar para a spec `fase1-application` as pendências da revisão 06-r1: missão sem passageiros e embarque só por `missao.embarcar(p)`.
