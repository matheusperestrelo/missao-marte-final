# Design: fase1-application

## Abordagem

Pacote `missaomarte.application` em `fases/fase-01-refatoracao/src/missaomarte/application/`.

- A `GameSession` é criada **uma vez** pelo `Main` e reutilizada: cada
  partida começa com `iniciar(...)`, que pede a missão à `MissaoFactory`.
  Assim o `Main` "cria a sessão", como pede a issue #6.
- `jogar(Acao)` executa o turno na ordem da Fase 0, com o desembarque
  inserido, e devolve um `ResultadoTurno` com a lista de `EventoJogo`.
- O ranking é uma porta (`RankingRepository`) com a regra do Top 5 numa
  classe própria, `RankingTop5`.

## Componentes afetados

Todos novos, em `src/missaomarte/application/`:

| Arquivo | Responsabilidade |
|---|---|
| `Acao.java` | enum `CIMA, BAIXO, ESQUERDA, DIREITA, EMBARCAR, SAIR, INVALIDA`; `static Acao deTexto(String)` (primeiro caractere, minúsculo); `Direcao direcao()` (nulo para as não-movimento); `boolean ehMovimento()` |
| `Dificuldade.java` | enum com `pontuacaoInicial`, `passageiros`, `asteroides`, `inimigos`, `capacidadeNave`; `deString` e `toString` iguais aos da Fase 0 (`Dificuldade.java` da Fase 0) |
| `MissaoFactory.java` | `MissaoFactory(Random)`; `Missao criar(Dificuldade, int tamanho)` |
| `TipoEvento.java` | enum `MOVIMENTO, EMBARQUE, EMBARQUE_RECUSADO_SEM_PASSAGEIRO, EMBARQUE_RECUSADO_NAVE_CHEIA, ENTREGA, COLISAO, VITORIA, DERROTA_NAVE_DESTRUIDA, DERROTA_PONTUACAO_ZERADA` |
| `EventoJogo.java` | `record EventoJogo(TipoEvento tipo, String passageiro, int valor)`: `passageiro` só no EMBARQUE; `valor` = pontos (EMBARQUE), quantidade (ENTREGA) ou vidas restantes (COLISAO), e 0 nos demais |
| `ResultadoTurno.java` | `record ResultadoTurno(List<EventoJogo> eventos, boolean comandoInvalido)`; se a partida terminou, quem chama consulta a sessão (`isEncerrada()`, `getResultado()`) |
| `ResultadoPartida.java` | enum `EM_ANDAMENTO, VITORIA, DERROTA, ABORTADA` |
| `ResumoPartida.java` | `record ResumoPartida(String piloto, Dificuldade dificuldade, int pontos, int movimentos, int entregues, long duracaoSegundos, boolean vitoria)` |
| `GameSession.java` | `GameSession(MissaoFactory, Random, Clock)`; `iniciar(String piloto, Dificuldade, int tamanho)`; `ResultadoTurno jogar(Acao)` (só é chamado com a partida em andamento; o console nunca chama depois do fim); getters `getMissao()`, `getPiloto()`, `getDificuldade()`, `getPontos()`, `getVidas()`, `getMovimentos()`, `getDuracaoSegundos()`, `getResultado()`, `isEncerrada()`, `resumo()` |
| `RankingEntry.java` | `record RankingEntry(String nome, int pontuacao, Dificuldade dificuldade, int passageirosColetados, String dataHora, long tempoJogo)` |
| `RankingRepository.java` | interface: `List<RankingEntry> carregar()`, `void salvar(List<RankingEntry>)`, `void limpar()` |
| `RankingException.java` | `RuntimeException` lançada pela implementação quando salvar ou limpar falha |
| `RankingTop5.java` | `RankingTop5(RankingRepository, Clock)`; `List<RankingEntry> listar()`, `boolean registrar(ResumoPartida)`, `Optional<RankingEntry> recorde()`, `boolean seriaNovoRecorde(int pontos)`, `void limpar()` |

## Fluxo

**Criação do cenário** (`MissaoFactory.criar`), na mesma sequência de
sorteios da Fase 0 (`Main.java:270-299`):

1. `mapa = new Mapa(tamanho)`, `nave = new Nave(dificuldade.capacidadeNave())`
   e `missao = new Missao(mapa, nave)`.
2. Para cada passageiro, depois cada asteroide, depois cada inimigo:
   repetir `x = random.nextInt(2N+1) − N` e depois `y = random.nextInt(2N+1) − N`
   até a posição não ser (0,0) e `!missao.estaOcupada(p)`.
3. Passageiro de índice `i`: `i % 5` → 0 Professor "Dr. Silva",
   1 Engenheiro "Eng. Rosa", 2 Professor "Dr. Lima", 3 Engenheiro
   "Eng. Carlos", 4 Astronauta "Ast. Maria".

Um único método privado `sortearCelulaLivre(missao, mapa)` substitui os três
laços duplicados (problema 7 da `fase-00-arquitetura.md`).

**Turno** (`GameSession.jogar`):

```
INVALIDA → ResultadoTurno([], comandoInvalido=true)
SAIR     → resultado = ABORTADA; congela duração; ResultadoTurno([], false)
movimento:
  nave.mover(direcao, mapa); pontos--; movimentos++; + MOVIMENTO
  n = missao.desembarcarNaBase(); se n > 0 → + ENTREGA(n)
EMBARCAR:
  p = missao.passageiroNaPosicaoDaNave()
  p == null            → + EMBARQUE_RECUSADO_SEM_PASSAGEIRO
  !missao.embarcar(p)  → + EMBARQUE_RECUSADO_NAVE_CHEIA
  senão                → pontos += p.pontuacao(); + EMBARQUE(nome, pontos do passageiro)
missao.moverInimigos(() -> direcaoSorteada(random.nextInt(4)))   // 0 DIREITA, 1 ESQUERDA, 2 BAIXO, 3 CIMA
se missao.haColisao(): nave.perderVida(); + COLISAO(vidas)
   se vidas == 0: + DERROTA_NAVE_DESTRUIDA; resultado = DERROTA
se pontos <= 0: + DERROTA_PONTUACAO_ZERADA; resultado = DERROTA
se resultado == EM_ANDAMENTO e missao.todosEntregues(): + VITORIA; resultado = VITORIA
se terminou → congela duração
```

**Ranking** (`RankingTop5.registrar`):

```
se !resumo.vitoria() ou resumo.pontos() <= 0 → false
lista = repo.carregar()  (ordenada por pontuação decrescente)
se lista.size() >= 5 e resumo.pontos() <= lista.get(4).pontuacao() → false
lista + nova entrada (dataHora = LocalDateTime.now(clock) formatado "yyyy-MM-dd HH:mm:ss")
ordenar por pontuação decrescente (sort estável) → limitar a 5 → repo.salvar(lista) → true
```

## Decisões e alternativas descartadas

- **Decisão:** uma `GameSession` reutilizável, com `iniciar(...)`.
  **Alternativa descartada:** criar uma sessão nova por partida no console.
  O console teria de conhecer `Random`, `Clock` e a fábrica, e o `Main`
  deixaria de "criar a sessão" (issue #6).
- **Decisão:** um `EventoJogo` único (`record` com `TipoEvento` e dois
  campos) em vez de uma classe por evento. O motivo da recusa e da derrota
  fica no próprio nome do tipo.
  **Alternativas descartadas:** hierarquia de eventos, porque em Java 17,
  sem `switch` com padrões, o console precisaria de `instanceof` em cadeia;
  e um enum separado de motivo, com a direção do movimento no evento, por ser
  mais campos do que a Fase 1 usa. Se a Fase 4 precisar da direção para
  girar a nave, ela pode ser acrescentada lá, de forma aditiva.
- **Decisão:** eventos já na Fase 1, decisão da equipe. Na Fase 4, o
  `ResultadoTurno` não precisa mudar: o item "eventos do turno (mudança
  aditiva)" do escopo já fica atendido.
- **Decisão:** a data do ranking com `DateTimeFormatter`
  (`yyyy-MM-dd HH:mm:ss`). A saída é igual à da Fase 0
  (`toString().substring(0,19)`), sem o erro que a Fase 0 teria quando
  segundos e nanos fossem zero, caso fácil de acontecer com um `Clock` fixo
  em teste.
- **Decisão:** a classe se chama `RankingTop5`.
  **Alternativa descartada:** chamá-la já de `RankingService`. Na Fase 3, o
  escopo cria o `RankingService` com Top 10 em `application.ranking`, e os
  nomes diferentes deixam a evolução visível.
- **Decisão:** o nome vazio vira "Piloto Anônimo" e o tamanho menor que 1
  vira 5 dentro de `iniciar`. A regra fica na `application`, para as telas
  gráficas reaproveitarem.
- **Decisão:** a direção sorteada do inimigo é traduzida na `GameSession`
  (`random.nextInt(4)` → `Direcao`), mantendo o mapeamento da Fase 0.

## Riscos e impactos

- A ordem das chamadas ao `Random` (sorteio do cenário e direção dos
  inimigos) precisa seguir a da Fase 0. Os testes da Fase 2, com semente
  fixa, dependem dela.
- O ponto *d* (tamanho 1 no Médio ou no Difícil trava a criação) continua
  existindo e precisa estar registrado como limitação no tutorial (#7).
- A capacidade menor muda a estratégia do jogador. Registrar como mudança
  intencional no tutorial (#7).

## Estratégia de testes

Sem JUnit na Fase 1. A verificação é:

- **gate:** compilação e camadas (R8.1, R8.3);
- **busca:** `grep -rnE "System\.(out|in)|Scanner|new Random|currentTimeMillis|LocalDateTime\.now\(\)" fases/fase-01-refatoracao/src/missaomarte/application`
  sem resultado (R8.2);
- **inspeção** do revisor: os demais critérios, conferindo o fluxo de
  `jogar` e `registrar` contra o pseudocódigo acima;
- **ponta a ponta:** o roteiro manual da spec `fase1-console-infra`.

Relacionada: o `briefing-fase2` (em `planned/`) prevê os testes JUnit de
`Acao`, `Dificuldade`, `MissaoFactory`, `GameSession` e do Top 5 sobre
estas classes. Por isso `Random` e `Clock` precisam ser injetáveis.
