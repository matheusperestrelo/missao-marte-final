# Tarefas: fase1-domain

- [x] 1. Criar a pasta da Fase 1 com `Direcao`, `Posicao` e `Mapa`
  - status: done
  - Criar `fases/fase-01-refatoracao/` com `.gitignore` (`out/`, `ranking.json`) e `src/missaomarte/domain/` com `Direcao`, `Posicao` (record) e `Mapa`, conforme o design.
  - Conferir que o `.workflow/check.sh` passa a compilar a Fase 1 e continua passando.
  - _Requisitos: R1, R2, R3_
  - notas: Criados `.gitignore`, `Direcao`, `Posicao` (record, `ORIGEM`, `mover`) e `Mapa` (`getTamanho`/`getMin`/`getMax`/`contem`). Critérios são de inspeção (sem JUnit na Fase 1), então não há testes automáticos. `check.sh` agora compila a Fase 1 (javac) e passou, com a Fase 0 intacta.

- [x] 2. Criar `TipoElemento`, `ElementoMapa` e `Base`
  - status: done
  - Símbolos e nomes exatamente como na tabela do design, na ordem da legenda da Fase 0.
  - _Requisitos: R4_
  - notas: Criados `TipoElemento` (enum na ordem da legenda, com `simbolo()`/`nome()`), `ElementoMapa` (`posicao()`, `tipo()`, `simbolo()` default) e `Base` (fixa em `Posicao.ORIGEM`, tipo `BASE`). Critérios de R4 são de inspeção (sem JUnit na Fase 1), então não há testes automáticos. `check.sh` passou antes e depois.

- [x] 3. Criar `Passageiro` abstrata e `Professor`, `Engenheiro`, `Astronauta`
  - status: done
  - Sem campo de tipo em texto; pontuação 10/15/20.
  - _Requisitos: R5, R4_
  - notas: Criados `Passageiro` (abstrata, implementa `ElementoMapa`; `nome`, `posicao`, `pontuacao()` e `tipo()` abstratos, sem campo de tipo em texto) e `Professor`/`Engenheiro`/`Astronauta` (10/15/20, tipo próprio). Construtor `(String nome, Posicao posicao)`; getter `getNome()`. Critérios de inspeção (sem JUnit na Fase 1), sem testes automáticos. `grep` por `instanceof`/`Random`/`System.out` no `domain` sem resultado; `check.sh` passou.

- [x] 4. Criar `Nave`
  - status: done
  - Começa em (0,0), 3 vidas, capacidade do construtor; mover com limite do `Mapa`; embarcar, nave cheia, perder vida e desembarcar todos.
  - _Requisitos: R6_
  - notas: Criada `Nave` (implementa `ElementoMapa`, tipo `NAVE`; começa em `ORIGEM`, 3 vidas; `mover` ignora destino fora do `Mapa`; `embarcar` devolve false se cheia; `perderVida` não passa de 0; `desembarcarTodos` devolve e esvazia; `getABordo` é cópia não modificável). Extras: `getCapacidade()`, `getVidas()`. Critérios de inspeção (sem JUnit na Fase 1), sem testes automáticos. `grep` por `Random`/`System.out`/`instanceof` no `domain` sem resultado; `check.sh` passou.

- [x] 5. Criar `Perigo`, `Asteroide` e `Inimigo`
  - status: done
  - `colideCom` só em `Perigo`; o `Inimigo` move com a `Direcao` recebida, respeitando o `Mapa`.
  - _Requisitos: R7_
  - notas: Criados `Perigo` (abstrata, guarda a posição, `colideCom(Posicao)` só aqui), `Asteroide` (parado, tipo `ASTEROIDE`) e `Inimigo` (`mover(Direcao, Mapa)` anda 1 casa e fica parado se o destino sair do `Mapa`; não checa outros elementos). Critérios de inspeção (sem JUnit na Fase 1), sem testes automáticos. `grep` por `Random`/`System.out`/`instanceof` etc. no `domain` sem resultado; `check.sh` passou.

- [x] 6. Criar `Missao`
  - status: done
  - Ocupação, passageiro na posição da nave e embarque (recusado com a nave cheia), desembarque na base, indicadores (a bordo + entregues + restantes = total), mover inimigos com `Supplier<Direcao>`, colisão e `elementos()` na ordem de prioridade do desenho.
  - Rodar a busca do design (`grep` por `Random`, `System.out`, `instanceof` etc. no `domain`) e registrar o resultado nas notas.
  - _Requisitos: R8, R1_
  - notas: Criada `Missao` com as operações do design (`estaOcupada` exclui a base; `embarcar` também recusa passageiro que não está no mapa; `total` é contado em `adicionarPassageiro`, então a invariante a bordo + entregues + restantes = total vale por construção; `desembarcarNaBase` devolve 0 fora da base ou sem ninguém a bordo; `elementos()` é `List.copyOf` na ordem nave, passageiros, asteroides, inimigos, base). Critérios de inspeção, sem testes automáticos. Busca do design (`grep` por `Random`, `System.out/in`, `Scanner`, `instanceof`, `currentTimeMillis`, `LocalDateTime.now` no `domain`) sem resultado; `check.sh` passou.
