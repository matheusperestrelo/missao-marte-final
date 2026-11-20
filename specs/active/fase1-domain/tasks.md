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

- [ ] 3. Criar `Passageiro` abstrata e `Professor`, `Engenheiro`, `Astronauta`
  - status: todo
  - Sem campo de tipo em texto; pontuação 10/15/20.
  - _Requisitos: R5, R4_
  - notas:

- [ ] 4. Criar `Nave`
  - status: todo
  - Começa em (0,0), 3 vidas, capacidade do construtor; mover com limite do `Mapa`; embarcar, nave cheia, perder vida e desembarcar todos.
  - _Requisitos: R6_
  - notas:

- [ ] 5. Criar `Perigo`, `Asteroide` e `Inimigo`
  - status: todo
  - `colideCom` só em `Perigo`; o `Inimigo` move com a `Direcao` recebida, respeitando o `Mapa`.
  - _Requisitos: R7_
  - notas:

- [ ] 6. Criar `Missao`
  - status: todo
  - Ocupação, passageiro na posição da nave e embarque (recusado com a nave cheia), desembarque na base, indicadores (a bordo + entregues + restantes = total), mover inimigos com `Supplier<Direcao>`, colisão e `elementos()` na ordem de prioridade do desenho.
  - Rodar a busca do design (`grep` por `Random`, `System.out`, `instanceof` etc. no `domain`) e registrar o resultado nas notas.
  - _Requisitos: R8, R1_
  - notas:
