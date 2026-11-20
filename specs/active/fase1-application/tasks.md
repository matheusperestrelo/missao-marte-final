# Tarefas: fase1-application

- [ ] 1. Criar `Acao` e `Dificuldade` com parâmetros
  - status: todo
  - Em `fases/fase-01-refatoracao/src/missaomarte/application/`, conforme o design; tabela de parâmetros de R2.1.
  - _Requisitos: R1, R2_
  - notas:

- [ ] 2. Criar `MissaoFactory`
  - status: todo
  - Um único sorteio de célula livre, na sequência de sorteios da Fase 0; nomes e tipos dos passageiros por índice.
  - _Requisitos: R3_
  - notas:

- [ ] 3. Criar os tipos do resultado do turno
  - status: todo
  - `TipoEvento`, `EventoJogo`, `ResultadoTurno`, `ResultadoPartida` e `ResumoPartida`.
  - _Requisitos: R5, R6_
  - notas:

- [ ] 4. Criar `GameSession`
  - status: todo
  - `iniciar` e `jogar` seguindo o pseudocódigo do design, com `Random` e `Clock` injetados; duração congelada ao terminar.
  - _Requisitos: R4, R5, R6_
  - notas:

- [ ] 5. Criar a porta do ranking e a regra do Top 5
  - status: todo
  - `RankingEntry`, `RankingRepository`, `RankingException` e `RankingTop5` (listar, registrar, recorde, seriaNovoRecorde, limpar).
  - Rodar a busca do design (`grep` por `System.out`, `Scanner`, `new Random` etc. na `application`) e registrar o resultado nas notas.
  - _Requisitos: R7, R8_
  - notas:
