# Contratos compartilhados

Este documento descreve os contratos que as três trilhas do projeto
compartilham. As seções 1 a 5 descrevem o código **como ele está** em
`fases/fase-01-refatoracao/src/missaomarte/` (fonte de verdade; idêntico ao de
`fases/fase-02-gradle/src/main/java/`). A seção 6 registra onde o código difere
do plano. As seções 7 e 8 (Fase 5) são **propostas a combinar**, não decisões.

## 1. Domínio (`missaomarte.domain`)

### `Posicao`
`record Posicao(int x, int y)`.
- `Posicao.ORIGEM` = `(0, 0)`.
- `Posicao mover(Direcao)` devolve uma **nova** instância (`x + dx`, `y + dy`).

### `Direcao`
Enum com `dx()` e `dy()`:

| Constante | dx | dy |
|---|---|---|
| `CIMA` | 0 | -1 |
| `BAIXO` | 0 | 1 |
| `ESQUERDA` | -1 | 0 |
| `DIREITA` | 1 | 0 |

### `Mapa`
`Mapa(int tamanho)`; os limites vão de `-tamanho` a `+tamanho` nos dois eixos.
- `getTamanho()`, `getMin()` (= `-tamanho`), `getMax()` (= `tamanho`).
- `boolean contem(Posicao)`: verdadeiro se `x` e `y` estão entre `getMin()` e
  `getMax()`, inclusive.

### `ElementoMapa` (interface)
- `Posicao posicao()`
- `TipoElemento tipo()`
- `default char simbolo()`: devolve `tipo().simbolo()`.

### `TipoElemento`

| Constante | `simbolo()` | `nome()` |
|---|---|---|
| `NAVE` | `@` | Nave |
| `PROFESSOR` | `P` | Professor |
| `ENGENHEIRO` | `E` | Engenheiro |
| `ASTRONAUTA` | `T` | Astronauta |
| `ASTEROIDE` | `#` | Asteroide |
| `INIMIGO` | `X` | Inimigo |
| `BASE` | `L` | Plataforma de Pouso |

### `Nave` (implementa `ElementoMapa`)
`Nave(int capacidade)`. Começa em `Posicao.ORIGEM` com 3 vidas.
- `posicao()`, `tipo()` (`NAVE`), `getCapacidade()`, `getVidas()`.
- `void mover(Direcao, Mapa)`: só move se o destino estiver dentro do mapa;
  fora dele, a nave fica parada.
- `boolean estaCheia()`, `boolean embarcar(Passageiro)` (falso se cheia).
- `void perderVida()` (não passa de 0).
- `List<Passageiro> desembarcarTodos()` esvazia a nave e devolve quem estava a bordo.
- `List<Passageiro> getABordo()` (cópia imutável).

### `Passageiro` (abstrata, implementa `ElementoMapa`)
Construtor `(String nome, Posicao posicao)`; `getNome()`, `posicao()`,
`abstract int pontuacao()`, `abstract TipoElemento tipo()`.

| Subclasse | `pontuacao()` | `tipo()` |
|---|---|---|
| `Professor` | 10 | `PROFESSOR` |
| `Engenheiro` | 15 | `ENGENHEIRO` |
| `Astronauta` | 20 | `ASTRONAUTA` |

### `Perigo` (abstrata, implementa `ElementoMapa`)
Construtor `(Posicao)`; `posicao()`; `boolean colideCom(Posicao)` (igualdade de posição).
- `Asteroide(Posicao)`: `tipo()` = `ASTEROIDE`; parado.
- `Inimigo(Posicao)`: `tipo()` = `INIMIGO`; `void mover(Direcao, Mapa)` só move
  se o destino estiver dentro do mapa.

### `Base` (implementa `ElementoMapa`)
Sem argumentos; `posicao()` é sempre `Posicao.ORIGEM`; `tipo()` = `BASE`.

### `Missao`
`Missao(Mapa, Nave)`. Métodos públicos:
- `getMapa()`, `getNave()`.
- `adicionarPassageiro(Passageiro)`, `adicionarAsteroide(Asteroide)`,
  `adicionarInimigo(Inimigo)`.
- `boolean estaOcupada(Posicao)`: há algum elemento (exceto a base) nela.
- `Passageiro passageiroNaPosicaoDaNave()`: nulo se não houver.
- `boolean embarcar(Passageiro)`: falso se o passageiro não está no mapa ou a
  nave está cheia; se der certo, ele sai do mapa.
- `int desembarcarNaBase()`: se a nave está na origem, entrega todos e devolve
  quantos; senão 0.
- `void moverInimigos(Supplier<Direcao>)`: pede uma direção por inimigo.
- `boolean haColisao()`: nave sobre algum asteroide ou inimigo.
- `getABordo()`, `getEntregues()`, `getRestantes()`, `getTotal()` (contadores).
- `boolean todosEntregues()`.
- `List<Passageiro> getPassageirosNoMapa()` (cópia imutável).
- `List<ElementoMapa> elementos()`: nave, passageiros no mapa, asteroides,
  inimigos e base, nessa ordem (cópia imutável).

## 2. `Acao` (`missaomarte.application`)

Constantes: `CIMA`, `BAIXO`, `ESQUERDA`, `DIREITA`, `EMBARCAR`, `SAIR`, `INVALIDA`.

- `static Acao deTexto(String)`: lê **só o primeiro caractere**, sem diferenciar
  maiúsculas de minúsculas. Texto nulo, vazio ou desconhecido dá `INVALIDA`.
- `Direcao direcao()`: a direção dos quatro movimentos; `null` nas demais.
- `boolean ehMovimento()`: verdadeiro se `direcao() != null`.

Mapeamento do console:

| Tecla | Ação |
|---|---|
| `w` | `CIMA` |
| `s` | `BAIXO` |
| `a` | `ESQUERDA` |
| `d` | `DIREITA` |
| `c` | `EMBARCAR` |
| `q` | `SAIR` |
| outra | `INVALIDA` |

## 3. Turno

- `ResultadoPartida`: `EM_ANDAMENTO`, `VITORIA`, `DERROTA`, `ABORTADA`.
- `TipoEvento` (9 valores): `MOVIMENTO`, `EMBARQUE`,
  `EMBARQUE_RECUSADO_SEM_PASSAGEIRO`, `EMBARQUE_RECUSADO_NAVE_CHEIA`, `ENTREGA`,
  `COLISAO`, `VITORIA`, `DERROTA_NAVE_DESTRUIDA`, `DERROTA_PONTUACAO_ZERADA`.
- `record EventoJogo(TipoEvento tipo, String passageiro, int valor)`:
  `passageiro` só é preenchido no `EMBARQUE` (nulo nos demais); `valor` é pontos
  (`EMBARQUE`), quantidade entregue (`ENTREGA`) ou vidas restantes (`COLISAO`);
  0 nos demais.
- `record ResultadoTurno(List<EventoJogo> eventos, boolean comandoInvalido)`:
  eventos na ordem em que aconteceram.
- **`INVALIDA` e `SAIR` não geram eventos.** `INVALIDA` devolve
  `ResultadoTurno(lista vazia, true)` e não altera o estado. `SAIR` devolve
  `ResultadoTurno(lista vazia, false)`, muda o resultado para `ABORTADA` e
  congela a duração.

## 4. Sessão e cenário

### `GameSession`
`GameSession(MissaoFactory, Random, Clock)`.
- `void iniciar(String piloto, Dificuldade, int tamanho)`: piloto nulo ou em
  branco vira "Piloto Anônimo"; `tamanho < 1` vira 5. Zera movimentos e duração,
  define os pontos como `dificuldade.pontuacaoInicial()` e o resultado como
  `EM_ANDAMENTO`.
- `ResultadoTurno jogar(Acao)`: aplica a ação, move os inimigos (sorteio
  `random.nextInt(4)`: 0 direita, 1 esquerda, 2 baixo, 3 cima), trata colisão e
  fim de partida. Cada movimento custa 1 ponto.
- Getters de estado: `getMissao()`, `getPiloto()`, `getDificuldade()`,
  `getPontos()`, `getVidas()`, `getMovimentos()`, `long getDuracaoSegundos()`
  (congelada após o fim), `getResultado()`, `isEncerrada()`.
- `ResumoPartida resumo()`.
- `record ResumoPartida(String piloto, Dificuldade dificuldade, int pontos,
  int movimentos, int entregues, long duracaoSegundos, boolean vitoria)`.

**Limitação registrada:** a sessão não tem guarda contra `jogar` depois do fim
da partida, nem contra consulta (`getVidas`, `resumo` etc.) antes de `iniciar`
(a missão ainda é nula). Quem usa a sessão deve respeitar a ordem.

### `MissaoFactory`
`MissaoFactory(Random)`; `Missao criar(Dificuldade, int tamanho)`. Cria a nave
com a capacidade da dificuldade e sorteia posições livres (nunca a origem) para
passageiros, asteroides e inimigos. Com tamanho 1 no Médio/Difícil não há
células livres suficientes e o sorteio não termina (limitação conhecida).

### `Dificuldade`

| Constante | Rótulo | Pontuação inicial | Passageiros | Asteroides | Inimigos | Capacidade da nave |
|---|---|---|---|---|---|---|
| `FACIL` | Fácil | 30 | 4 | 1 | 1 | 4 |
| `MEDIO` | Médio | 20 | 5 | 2 | 2 | 3 |
| `DIFICIL` | Difícil | 45 | 5 | 3 | 3 | 2 |

Acessores: `pontuacaoInicial()`, `passageiros()`, `asteroides()`, `inimigos()`,
`capacidadeNave()`; `toString()` devolve o rótulo.
`static Dificuldade deString(String)`: ignora espaços e maiúsculas, aceita
"facil"/"fácil" e "dificil"/"difícil"; qualquer outro valor (inclusive nulo) dá `MEDIO`.

## 5. Ranking

- `record RankingEntry(String nome, int pontuacao, Dificuldade dificuldade,
  int passageirosColetados, String dataHora, long tempoJogo)`; `dataHora` no
  formato `yyyy-MM-dd HH:mm:ss`.
- `interface RankingRepository`: `List<RankingEntry> carregar()`,
  `void salvar(List<RankingEntry>)`, `void limpar()`. A implementação
  (`FileRankingRepository`) devolve a lista **ordenada por pontuação
  decrescente**; o limite de 5 é aplicado por `RankingTop5` antes de salvar
  (ver seção 6).
- `RankingException` (`RuntimeException`): falha ao salvar ou limpar; construtores
  `(String)` e `(String, Throwable)`.
- `RankingTop5(RankingRepository, Clock)`:
  - `List<RankingEntry> listar()`.
  - `boolean registrar(ResumoPartida)`: só vitórias com pontuação positiva
    entram; com 5 entradas, a nova precisa superar a quinta. Devolve se entrou.
  - `Optional<RankingEntry> recorde()`: a primeira entrada.
  - `boolean seriaNovoRecorde(int pontos)`: há recorde positivo e `pontos` o supera.
  - `void limpar()`.
- `infrastructure.ranking.FileRankingRepository(Path)`: JSON escrito e lido à
  mão, no formato da Fase 0 (chaves `name`, `score`, `dificuldade`,
  `passageirosColetados`, `dataHora`, `tempoJogo`).

**Nota:** na Fase 3 a regra virou `RankingService` com Top 10 em
`application.ranking` (issue #15, concluída; o `RankingTop5` foi removido da Fase 3).

## 6. Diferenças em relação ao plano

- Não existe o record `Placar`: a sessão expõe o estado por getters.
- O método da sessão é `jogar`, não `executar`.
- `ElementoMapa` faz o papel de `ObjetoRenderizavel`.
- `FileRankingRepository.carregar()` ordena a lista, mas **não** limita a 5; o
  limite vem de `RankingTop5.registrar`, que só salva as 5 melhores.

## 7. API do servidor (Fase 5) — PROPOSTA, a combinar

Nada abaixo está decidido; precisa da aprovação dos três integrantes.

| Método e caminho | Corpo / parâmetros | Resposta |
|---|---|---|
| `POST /api/usuarios` | `{nome, senha}` | cadastra o usuário |
| `POST /api/auth/login` | `{nome, senha}` | `{token, tipo, expiraEmSegundos}` |
| `POST /api/partidas` | `{dificuldade, resultado, pontuacao, duracaoSegundos, movimentos}` | registra a partida (autenticado) |
| `GET /api/partidas/minhas?pagina&tamanho` | paginação | partidas do usuário (autenticado) |
| `GET /api/ranking` | — | ranking |
| `GET /api/saude` | — | público, sem autenticação |

- Enums: `dificuldade` = `FACIL`/`MEDIO`/`DIFICIL`; `resultado` = `VITORIA`/`DERROTA`.
- Erros no formato `ProblemDetail`.

## 8. Porta `ServidorDoJogo` (Fase 5) — PROPOSTA, a combinar

Porta usada pelo cliente para falar com o servidor; assinatura a combinar:

- `cadastrar(nome, senha, Callback<...>)`
- `entrar(nome, senha, Callback<...>)`
- `enviarPartida(partida, Callback<...>)`
- `carregarRanking(Callback<...>)`
- `Callback<T>` recebe uma `Resposta<T>`.
- `record Resposta<T>(T valor, String erro, boolean naoAutorizado)`.

## Aprovação

Aprovação dos três integrantes (pendente):

- [ ] ______________________
- [ ] ______________________
- [ ] ______________________
