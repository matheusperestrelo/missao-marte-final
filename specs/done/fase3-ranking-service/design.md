# Design: fase3-ranking-service

## Abordagem

Mover `RankingRepository`, `RankingEntry` e `RankingException` de
`missaomarte.application` para `missaomarte.application.ranking`. Criar o
`RankingService` com a mesma regra do `RankingTop5`, mas com
`MAXIMO = 10`. Depois remover o `RankingTop5` e levar os testes dele para
o `RankingServiceTest`. A classe do jogo e o launcher passam a usar o
serviço. Todo o trabalho fica em `fases/fase-03-libgdx/core` e `lwjgl3`.

## Componentes afetados

| Arquivo (em `fases/fase-03-libgdx/`) | Mudança |
|---|---|
| `core/src/main/java/missaomarte/application/ranking/RankingRepository.java` | Movido de `application/` (só `package` muda). Métodos `carregar()`, `salvar(List)`, `limpar()` |
| `core/src/main/java/missaomarte/application/ranking/RankingEntry.java` | Movido (só `package` muda) |
| `core/src/main/java/missaomarte/application/ranking/RankingException.java` | Movido (só `package` muda). Vai junto porque é a exceção da porta |
| `core/src/main/java/missaomarte/application/ranking/RankingService.java` | Novo. Construtor `(RankingRepository repositorio, Clock clock)`. `List<RankingEntry> top10()`, `boolean registrar(ResumoPartida resumo)`, `Optional<RankingEntry> recorde()`, `void limpar()`. Constante `MAXIMO = 10` |
| `core/src/main/java/missaomarte/application/RankingTop5.java` | Removido |
| `core/src/main/java/missaomarte/infrastructure/ranking/FileRankingRepository.java` | Só os `import` dos tipos movidos |
| `core/src/main/java/missaomarte/gdx/MissaoMarteGame.java` | Construtor troca `RankingRepository` por `RankingService`; getter `getRanking()` |
| `lwjgl3/src/main/java/missaomarte/lwjgl3/Lwjgl3Launcher.java` | Cria `new RankingService(new FileRankingRepository(Paths.get("ranking.json")), clock)` |
| `core/src/test/java/missaomarte/application/RankingTop5Test.java` | Removido |
| `core/src/test/java/missaomarte/application/ranking/RankingServiceTest.java` | Novo (R2 a R5) |
| `core/src/test/java/missaomarte/application/ranking/RankingEmMemoria.java`, `RankingQueFalha.java` | Dublês movidos de `application/Dubles.java`, que é package-private e não é visível no subpacote. Tirar de `Dubles` só esses dois. Se outro teste ainda usar algum deles, mantê-lo também onde estava |
| `core/src/test/java/missaomarte/infrastructure/ranking/FileRankingRepositoryTest.java` | Só os `import` |

`ResumoPartida` continua em `missaomarte.application`. O serviço depende
dele, e não o contrário. Conferir no código copiado quem mais importa os
tipos movidos (`grep -rn "RankingEntry\|RankingRepository\|RankingException"`)
e ajustar só os `import`.

## Fluxo

```
registrar(resumo)
  ├─ !resumo.vitoria() || resumo.pontos() <= 0  → false (não salva)
  ├─ lista = carregar()
  ├─ lista.size() >= 10 && pontos <= lista.get(9).pontuacao() → false
  ├─ lista.add(new RankingEntry(piloto, pontos, dificuldade, entregues,
  │                             now(clock) "yyyy-MM-dd HH:mm:ss", duracaoSegundos))
  ├─ sort estável por pontuação decrescente (empate: antigas na frente)
  ├─ salvar(primeiras 10)
  └─ true
top10() = carregar() ordenado (estável, decrescente), cortado em 10
```

## Decisões e alternativas descartadas

- **Decisão:** remover o `RankingTop5` e o `RankingTop5Test` e levar os
  casos para o `RankingServiceTest` com limite 10. O "testes da Fase 2
  continuam passando" do escopo fica atendido assim: as regras continuam
  testadas e só o limite muda, o que é registrado.
  **Alternativa descartada:** manter o `RankingTop5` sem uso só para o
  teste antigo passar. Seria código morto e duas regras de ranking no
  mesmo módulo.
- **Decisão:** `top10()` corta e ordena mesmo que o repositório devolva
  mais de 10 (por exemplo, arquivo editado à mão).
  **Alternativa descartada:** confiar no repositório. O
  `FileRankingRepository` ordena, mas não corta.
- **Decisão:** mesmo desempate do Top 5 (sort estável, nova entrada depois
  das antigas com a mesma pontuação). Mantém o comportamento conhecido e o
  caso do teste `r6_4`.
- **Decisão:** `registrar` recebe `ResumoPartida`, como o `RankingTop5`. É
  o que a `GameSession.resumo()` já entrega.

## Riscos e impactos

- **Arquivo existente:** um `ranking.json` gravado pela Fase 2 (até 5
  entradas) é lido normalmente. O formato não muda.
- **Imports:** esquecer um `import` quebra a compilação. O gate pega.
- **Dublês:** mover `RankingEmMemoria`/`RankingQueFalha` sem quebrar os
  outros testes que usam `Dubles` (`FabricaFixa`, `RelogioControlado`,
  `RandomSequencia` ficam onde estão).

## Estratégia de testes

Testes de unidade em `RankingServiceTest`, com `RankingEmMemoria` e
`Clock.fixed`. O nome de cada teste leva o ID do critério (ex.:
`r3_4_listaCheiaSoAceitaMaiorQueODecimo`).

| Requisito | Teste |
|---|---|
| R1.1, R1.3 | Inspeção |
| R1.2 | `FileRankingRepositoryTest` existente |
| R2.1 a R2.3 | `top10` ordenado e cortado (repositório com 12 entradas), vazio, recorde |
| R3.1 a R3.6 | Derrota, pontuação 0 e negativa, menos de 10, lista cheia (igual, menor, maior), empate, campos e data |
| R4.1 | Limpar e `top10()` vazio |
| R5.1 | `RankingQueFalha` ao registrar e ao limpar |
| R6 | Inspeção + gate |
