# Tarefas: fase1-domain

- [ ] 1. Criar a pasta da Fase 1 com `Direcao`, `Posicao` e `Mapa`
  - status: todo
  - Criar `fases/fase-01-refatoracao/` com `.gitignore` (`out/`, `ranking.json`) e `src/missaomarte/domain/` com `Direcao`, `Posicao` (record) e `Mapa`, conforme o design.
  - Conferir que o `.workflow/check.sh` passa a compilar a Fase 1 e continua passando.
  - _Requisitos: R1, R2, R3_
  - notas:

- [ ] 2. Criar `TipoElemento`, `ElementoMapa` e `Base`
  - status: todo
  - Símbolos e nomes exatamente como na tabela do design, na ordem da legenda da Fase 0.
  - _Requisitos: R4_
  - notas:

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
