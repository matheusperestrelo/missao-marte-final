# Tarefas: fase1-application

- [x] 1. Criar `Acao` e `Dificuldade` com parâmetros
  - status: done
  - Em `fases/fase-01-refatoracao/src/missaomarte/application/`, conforme o design; tabela de parâmetros de R2.1.
  - _Requisitos: R1, R2_
  - notas: `Acao` (deTexto pelo primeiro caractere, direcao(), ehMovimento()) e `Dificuldade` (parâmetros da tabela R2.1, deString/toString como na Fase 0) criados. Sem JUnit na Fase 1 (verificação por inspeção): conferi R1.1-R1.4 e R2.1-R2.4 com um programa descartável fora do repositório. Pendências da fase1-domain (missão sem passageiros, embarcar só por `missao.embarcar`) não pertencem a esta tarefa; ficam para as tarefas 2 e 4.

- [x] 2. Criar `MissaoFactory`
  - status: done
  - Um único sorteio de célula livre, na sequência de sorteios da Fase 0; nomes e tipos dos passageiros por índice.
  - _Requisitos: R3_
  - notas: `MissaoFactory(Random)` com `criar(Dificuldade, int)` e um único `sortearCelulaLivre` (x e depois y, rejeita (0,0) e `estaOcupada`), ordem passageiros, asteroides, inimigos como na Fase 0. Sem JUnit na Fase 1: R3.1-R3.5 por inspeção; check.sh passou (compilação e camadas). Não consegui rodar programa descartável de verificação nesta sessão. Tamanho 1 no Médio/Difícil continua em laço infinito (R3.5, comentado no código). Pendência da fase1-domain "missão sem passageiros": toda `Dificuldade` tem >= 4 passageiros, então a fábrica nunca cria missão vazia; `todosEntregues()` com total 0 só seria possível fora da fábrica. O embarque só por `missao.embarcar` fica para a tarefa 4 (GameSession).

- [x] 3. Criar os tipos do resultado do turno
  - status: done
  - `TipoEvento`, `EventoJogo`, `ResultadoTurno`, `ResultadoPartida` e `ResumoPartida`.
  - _Requisitos: R5, R6_
  - notas: Criados `TipoEvento` (9 tipos), `EventoJogo`, `ResultadoTurno`, `ResultadoPartida` e `ResumoPartida` exatamente como na tabela do design (records/enums, sem lógica). Sem JUnit na Fase 1 e sem critério executável nesta tarefa: R5 e R6.2 só se verificam na `GameSession` (tarefa 4), por inspeção; aqui só os tipos. check.sh passou. As pendências da fase1-domain (embarque só por `missao.embarcar`, missão sem passageiros) são da tarefa 4.

- [ ] 4. Criar `GameSession`
  - status: review
  - `iniciar` e `jogar` seguindo o pseudocódigo do design, com `Random` e `Clock` injetados; duração congelada ao terminar.
  - _Requisitos: R4, R5, R6_
  - notas: `GameSession` criada seguindo o pseudocódigo do design (java.time.Clock; duração = (fim − início)/1000 congelada ao terminar, calculada ao vivo enquanto em andamento). Sem JUnit na Fase 1: R4-R6 por inspeção; check.sh passou e o grep de R8.2 na `application` não encontrou nada. Pendências da fase1-domain: o embarque usa só `missao.embarcar(p)` (nunca `nave.embarcar`), mantendo a conta a bordo + entregues + restantes (R6.1); missão sem passageiros não ocorre, pois toda `Dificuldade` tem >= 4 passageiros e a fábrica os cria sempre (sem tratamento extra em `iniciar`). Sugestão da revisão 03-r1 aplicada: `ResultadoTurno` recebe `List.copyOf(eventos)` na `GameSession` (o record não foi alterado). Em `INVALIDA`/`SAIR` a lista é `List.of()`. `jogar` após o fim não é protegido (design: o console nunca chama).

- [ ] 5. Criar a porta do ranking e a regra do Top 5
  - status: todo
  - `RankingEntry`, `RankingRepository`, `RankingException` e `RankingTop5` (listar, registrar, recorde, seriaNovoRecorde, limpar).
  - Rodar a busca do design (`grep` por `System.out`, `Scanner`, `new Random` etc. na `application`) e registrar o resultado nas notas.
  - _Requisitos: R7, R8_
  - notas:
