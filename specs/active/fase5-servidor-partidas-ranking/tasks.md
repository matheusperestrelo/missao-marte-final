# Tarefas: fase5-servidor-partidas-ranking

- [x] 1. Registrar partidas em `POST /api/partidas` com o usuário do token e validação completa
  - status: done
  - Fatia vertical: `Dificuldade`, `Resultado`, `Partida`, `PartidaRepository`, `NovaPartida`, `PartidaRegistrada`, `PartidaService.registrar` (por enquanto com `posicaoRanking = null` sempre), `PartidaController` (`POST`), `NovaPartidaRequest`, `PartidaRegistradaResponse`, e os casos novos no `TratadorDeErros` (enum e tipo inválidos no corpo).
  - Testes: `RegistroPartidaApiTest` (R1.1, R1.2, R1.3, R1.7 e teste de leitura da partida salva, R5.1) e `ValidacaoPartidaApiTest` (R2 inteiro). Se o decimal passar como inteiro, ligar `spring.jackson.deserialization.accept-float-as-int=false` e anotar aqui.
  - Acrescentar `POST /api/partidas` à tabela do `servidor/README.md` e exemplos ao `requests.http` (vitória, derrota, inválido).
  - Rodar o `.workflow/check.sh`. Não alterar nada fora de `fases/fase-05-servidor-ranking/`.
  - _Requisitos: R1, R2, R5, R6_
  - notas: Entidade Partida, repositório, serviço (posicaoRanking sempre null por enquanto), controller e DTOs criados; testes RegistroPartidaApiTest e ValidacaoPartidaApiTest com auxiliares RelogioDeTeste/ConfiguracaoRelogioDeTeste (Clock @Primary controlável). O Jackson aceitava decimal como inteiro (1.5 virava 1): ligado `spring.jackson.deserialization.accept-float-as-int=false` em main e em src/test/resources/application.properties (o de teste substitui o principal). Não foi preciso novo handler no TratadorDeErros: enum inválido, tipo errado e JSON malformado já saem 400 em ProblemDetail pelo ResponseEntityExceptionHandler (sem lista `erros`, que só existe para campos de bean validation); os handlers de parâmetro de consulta ficam para a tarefa 2. README e requests.http atualizados (rota POST, faixas, 3 exemplos).

- [ ] 2. Expor o histórico paginado `GET /api/partidas/minhas` só com as partidas do dono
  - status: todo
  - `PartidaService.historico` com a ordenação `dataHora` ↓ e `id` ↓, `PaginaPartidasResponse`, `PartidaResponse`, `GET /api/partidas/minhas` com `pagina` (padrão 0, ≥ 0) e `tamanho` (padrão 10, de 1 a 50) e o tratamento de `HandlerMethodValidationException`/`MethodArgumentTypeMismatchException` no `TratadorDeErros`.
  - Testes `HistoricoApiTest` com todos os casos de R3.
  - Acrescentar a rota ao README e ao `requests.http` (padrão, página 1, `tamanho=51`).
  - _Requisitos: R3, R5, R6_
  - notas:

- [ ] 3. Calcular o ranking global Top 10 e expor em `GET /api/ranking`
  - status: todo
  - `RankingGlobal` (função pura com o comparador pontuação ↓, duração ↑, `dataHora` ↑, `id` ↑; uma linha por jogador; corte em 10; posições 1..N), `LinhaRanking`, consulta `findByResultadoAndPontuacaoGreaterThan` trazendo o `usuario` junto, `RankingService`, `RankingController`, `LinhaRankingResponse` (sem `id`).
  - Testes `RankingGlobalTest` (unidade, sem Spring) e `RankingApiTest` com os casos de R4.
  - Acrescentar a rota ao README e ao `requests.http`.
  - _Requisitos: R4, R5, R6_
  - notas:

- [ ] 4. Devolver a `posicaoRanking` no registro e documentar a limitação da pontuação
  - status: todo
  - Em `PartidaService.registrar`, depois do `save`: se `VITORIA` e `pontuacao > 0`, calcular o ranking com `RankingGlobal` e devolver a posição da linha cuja partida é a recém-salva; senão `null`.
  - Testes em `RegistroPartidaApiTest` para R1.4, R1.5 (derrota, pontuação 0, negativa, pior que a própria melhor, 11º) e R1.6 (empate com a própria anterior → `null`).
  - `servidor/README.md`: seção "Limitações" (R6.3; incluir também o ranking calculado em memória) e revisão final da tabela da API. Conferir o fluxo completo cadastro → login → partidas → histórico → ranking pelo `requests.http` com `bootRun`.
  - _Requisitos: R1, R6_
  - notas:
