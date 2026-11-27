# Design: fase5-servidor-partidas-ranking

## Abordagem

Acrescentar ao servidor da spec 13 (`fases/fase-05-servidor-ranking/servidor/`,
pacote `missaomarte.servidor`) a entidade `Partida`, o registro, o
histórico paginado e o ranking global. O ranking é calculado por uma
função pura em `domain` (`RankingGlobal`), testável sem Spring, sobre as
vitórias com pontuação > 0. O registro usa a mesma função para calcular a
`posicaoRanking` da partida recém-salva. Os controllers recebem o
`UsuarioAutenticado` da spec 13 e só traduzem entrada e saída.

## Decisões do alinhamento da Fase 5 (valem para esta spec)

1. Paginação: `pagina` começa em 0 (padrão 0); `tamanho` de 1 a 50 (padrão
   10); fora da faixa ou não inteiro → 400; página além do fim → 200 com
   lista vazia; resposta num `record` próprio
   `{itens, pagina, tamanho, totalItens, totalPaginas}`, nunca o `Page`
   do Spring.
2. Linha do ranking: `{posicao, nome, pontuacao, dificuldade,
   duracaoSegundos, movimentos, dataHora}`; `posicao` calculada no
   servidor, sem `id`. Vazio → `[]`.
3. A resposta 201 do registro traz os campos de um item do histórico e
   mais `posicaoRanking` (1–10 ou `null`). A `dataHora` é do servidor.
4. As decisões da spec 13 continuam valendo: 401 em `ProblemDetail`, erros
   de validação com `erros` `[{campo, mensagem}]`, sem estado.

## Componentes afetados

Caminhos relativos a `fases/fase-05-servidor-ranking/servidor/src/main/java/missaomarte/servidor/`.

| Arquivo | Mudança |
|---|---|
| `domain/Dificuldade.java` | Novo enum `FACIL, MEDIO, DIFICIL` (duplicado de propósito: o servidor não depende do cliente) |
| `domain/Resultado.java` | Novo enum `VITORIA, DERROTA` |
| `domain/Partida.java` | Nova `@Entity` (`partidas`): `id` (`Long`, IDENTITY), `usuario` (`@ManyToOne(fetch = LAZY, optional = false)`), `dificuldade` e `resultado` (`@Enumerated(STRING)`), `pontuacao`, `duracaoSegundos`, `movimentos` (`int`, `nullable = false`), `dataHora` (`Instant`). Índices em `usuario_id, data_hora` e em `resultado, pontuacao`. Construtor de criação; sem setters |
| `domain/PartidaRepository.java` | Novo `JpaRepository<Partida, Long>`: `Page<Partida> findByUsuarioId(Long, Pageable)`; `List<Partida> findByResultadoAndPontuacaoGreaterThan(Resultado, int)` com `@EntityGraph` (ou `join fetch`) do `usuario`, para não fazer uma consulta por linha |
| `domain/RankingGlobal.java` | Nova função pura: `static List<LinhaRanking> calcular(List<Partida> candidatas)`: filtra `VITORIA` e `pontuacao > 0` (defesa, mesmo que a consulta já filtre), ordena pelo comparador abaixo, mantém a primeira partida de cada `usuario.id`, corta em 10 e numera de 1 a N |
| `domain/LinhaRanking.java` | `record (int posicao, Partida partida)`, só interno. A API converte para o DTO sem `id` |
| `domain/OrdemDoRanking.java` (ou constante em `RankingGlobal`) | `Comparator<Partida>`: `pontuacao` ↓, `duracaoSegundos` ↑, `dataHora` ↑, `id` ↑ |
| `application/PartidaService.java` | `registrar(long usuarioId, NovaPartida dados)` → `PartidaRegistrada`: carrega o `Usuario` (`getReferenceById`), cria a `Partida` com `dataHora = Instant.now(clock)`, salva e, se for `VITORIA` com `pontuacao > 0`, calcula o ranking e procura a linha cujo `partida.id` é o salvo; senão `posicaoRanking = null`. `historico(long usuarioId, int pagina, int tamanho)` → `Page<Partida>` com `PageRequest.of(pagina, tamanho, Sort.by(DESC, "dataHora").and(Sort.by(DESC, "id")))` |
| `application/RankingService.java` | `top10()` → `List<LinhaRanking>`: `RankingGlobal.calcular(repository.findByResultadoAndPontuacaoGreaterThan(VITORIA, 0))` |
| `application/NovaPartida.java` | `record (Dificuldade, Resultado, int pontuacao, int duracaoSegundos, int movimentos)` já validado |
| `application/PartidaRegistrada.java` | `record (Partida partida, Integer posicaoRanking)` |
| `api/PartidaController.java` | `POST /api/partidas` (`UsuarioAutenticado`, `@Valid NovaPartidaRequest`) → 201 `PartidaRegistradaResponse` com `Location: /api/partidas/{id}`; `GET /api/partidas/minhas` (`UsuarioAutenticado`, `@RequestParam(defaultValue = "0") @Min(0) Integer pagina`, `@RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer tamanho`) → 200 `PaginaPartidasResponse`. `@Validated` na classe |
| `api/RankingController.java` | `GET /api/ranking` (`UsuarioAutenticado`, só para exigir o usuário existente) → 200 `List<LinhaRankingResponse>` |
| `api/dto/NovaPartidaRequest.java` | `record (@NotNull Dificuldade dificuldade, @NotNull Resultado resultado, @NotNull @Min(-100000) @Max(100000) Integer pontuacao, @NotNull @Min(0) @Max(86400) Integer duracaoSegundos, @NotNull @Min(0) @Max(100000) Integer movimentos)`. Campos a mais são ignorados (padrão do Jackson no Boot) |
| `api/dto/PartidaResponse.java` | `record (long id, Dificuldade dificuldade, Resultado resultado, int pontuacao, int duracaoSegundos, int movimentos, Instant dataHora)` |
| `api/dto/PartidaRegistradaResponse.java` | Os mesmos campos de `PartidaResponse` + `Integer posicaoRanking` (serializado como `null`, não omitido) |
| `api/dto/PaginaPartidasResponse.java` | `record (List<PartidaResponse> itens, int pagina, int tamanho, long totalItens, int totalPaginas)` |
| `api/dto/LinhaRankingResponse.java` | `record (int posicao, String nome, int pontuacao, Dificuldade dificuldade, int duracaoSegundos, int movimentos, Instant dataHora)` |
| `api/TratadorDeErros.java` | Acrescentar: enum inválido / tipo errado no corpo (`HttpMessageNotReadableException`) → 400; parâmetro de consulta inválido (`HandlerMethodValidationException`, `MethodArgumentTypeMismatchException`) → 400 com `erros` citando `pagina` ou `tamanho` |
| `servidor/README.md`, `servidor/requests.http` | Rotas novas, exemplos e a seção "Limitações" (R6) |

Configuração: nada novo no `application.properties`. Se o Jackson 3 do
Boot 4 aceitar número decimal num `Integer` por padrão, ligar
`spring.jackson.deserialization.accept-float-as-int=false` (conferir no
teste do R2.4). Datas saem em ISO-8601 (padrão do Boot para `Instant`).

## Fluxo

```
POST /api/partidas  (Bearer)
  └─ UsuarioAutenticado (spec 13) → @Valid NovaPartidaRequest (400)
       └─ PartidaService.registrar(usuarioId, dados)
            ├─ save(Partida com dataHora = now(clock))
            ├─ VITORIA e pontuacao > 0?
            │    └─ RankingGlobal.calcular(vitórias > 0)
            │         └─ linha com partida.id == salva? posicao : null
            └─ 201 {id, ..., dataHora, posicaoRanking}

GET /api/partidas/minhas?pagina&tamanho  (Bearer)
  └─ valida faixas (400) → findByUsuarioId(id, page ordenada) → 200 {itens, totais}

GET /api/ranking  (Bearer)
  └─ RankingService.top10 → RankingGlobal.calcular → 200 [linhas sem id]
```

## Decisões e alternativas descartadas

- **Decisão:** ranking calculado em Java por uma função pura sobre as
  vitórias com pontuação > 0.
  **Alternativa descartada:** uma consulta SQL única com agrupamento e
  janela (`ROW_NUMBER`). É mais eficiente, mas difícil de ler e de testar
  e varia por banco. Para o volume do projeto (dezenas a centenas de
  partidas), carregar as vitórias é aceitável. Ver "Riscos".
- **Decisão:** o mesmo comparador escolhe a melhor partida de cada
  jogador e ordena o ranking. Assim, "melhor vitória" e "posição" nunca
  divergem.
- **Decisão:** `posicaoRanking` calculada no registro, pela mesma função.
  **Alternativa descartada:** o cliente chamar `GET /api/ranking` e
  procurar a própria partida. A linha não tem `id`, e a comparação por
  valores falha em partidas iguais.
- **Decisão:** `pagina`/`tamanho` fora da faixa → 400.
  **Alternativa descartada:** ajustar em silêncio. Esconde o erro de quem
  chama.
- **Decisão:** `record` próprio para a página.
  **Alternativa descartada:** devolver o `Page` do Spring. O JSON dele é
  grande, muda entre versões e expõe detalhes de implementação.
- **Decisão:** enums duplicados no servidor.
  **Alternativa descartada:** módulo compartilhado com o cliente. A regra
  da seção 5 do escopo exige projetos independentes.
- **Decisão:** `GET /api/ranking` exige token (decidido no escopo).
- **Decisão:** a ordenação do histórico usa `id` como desempate de
  `dataHora`, para a paginação ser estável.

## Riscos e impactos

- **Ranking em memória:** cresce com o número de vitórias. Fica
  registrado como limitação no README, com a consulta agregada como
  evolução possível. Para o projeto, é suficiente.
- **Duas consultas no registro de vitória:** `save` e a leitura das
  vitórias. Aceitável pelo mesmo motivo.
- **Campo ausente virando 0:** coberto por `Integer` + `@NotNull`
  (`ESCOPO-PROJETO.md`, seção 14).
- **Número decimal aceito como inteiro:** depende da configuração do
  Jackson 3 (ver "Configuração").
- **Validação de `@RequestParam`:** no Spring 7, a falha vira
  `HandlerMethodValidationException`, não `ConstraintViolationException`.
  Tratar a certa no `TratadorDeErros` e conferir pelo teste.
- **`LazyInitializationException`** com `open-in-view=false`: a consulta
  do ranking precisa trazer o `usuario` junto (`@EntityGraph`), e a
  conversão para DTO acontece dentro da transação ou com o usuário já
  carregado.
- **Fase 6** copia este servidor para `fases/fase-06-android/servidor/`.
  Nada aqui deve depender do caminho da pasta da Fase 5.

## Estratégia de testes

JUnit 5. `RankingGlobalTest` é unidade pura, sem Spring. Os testes de API
usam `@SpringBootTest` + `@AutoConfigureMockMvc` com H2 em memória, como
na spec 13, e criam usuários e tokens pelo próprio fluxo de cadastro e
login (ou por um auxiliar de teste que usa o `EmissorDeToken`). O `Clock`
é substituído por um relógio controlável nos testes de `dataHora` e de
ordem. Nomes com o ID do critério (ex.: `r4_4_empateDePontuacaoGanhaMenorDuracao`).
Limpar as tabelas entre testes.

| Requisito | Testes |
|---|---|
| R1 | `RegistroPartidaApiTest`: 201 e formato; `usuarioId`/`nome` no corpo ignorados; `dataHora` com `Clock` fixo; `posicaoRanking` 1 em ranking vazio e 3 ao entrar em 3º; `null` para derrota, pontuação 0, negativa, pior que a própria melhor, 11º e empate com a própria anterior; 401 sem token e com token inválido, sem gravar |
| R2 | `ValidacaoPartidaApiTest`: enums inválidos (`ABORTADA`, `facil`, `""`); cada limite e um valor fora de cada lado; cada campo ausente; tipo errado e decimal; JSON malformado; dois campos inválidos → dois itens em `erros`; contagem no banco sem mudança |
| R3 | `HistoricoApiTest`: formato; só do dono; ordem por `dataHora` e `id`; padrão 0/10; 23 partidas em 3 páginas; além do fim; `pagina=-1`, `tamanho=0`, `tamanho=51`, `tamanho=abc` → 400; `tamanho=1`/`50` aceitos; sem partidas; 401 |
| R4 | `RankingGlobalTest` (unidade): só vitória > 0; uma linha por jogador; cada desempate (pontuação, duração, `dataHora`, `id`); limite de 10 com 12 jogadores; posições 1..N sem repetição; lista vazia. `RankingApiTest`: formato e chaves exatas da linha; nome como cadastrado; `[]`; 401 |
| R5 | Teste lê a partida salva pelo repositório; inspeção dos DTOs; manual: reiniciar o servidor e conferir histórico e ranking |
| R6 | Inspeção do README (tabela, seção "Limitações") e do `requests.http`; QA manual do fluxo completo |
