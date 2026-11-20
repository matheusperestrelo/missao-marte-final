# Design: fase2-testes-junit

## Abordagem

Acrescentar o JUnit 5 ao `build.gradle` da Fase 2 e escrever os testes em
`src/test/java`, nos mesmos pacotes das classes testadas, para ter acesso
ao que for visível no pacote. Nenhum arquivo de `src/main/java` muda. As
situações de jogo são montadas com dublês escritos à mão: uma fábrica de
teste que devolve uma `Missao` montada no teste, `Random` com semente fixa
e um `Clock` controlável.

## Componentes afetados

Em `fases/fase-02-gradle/`:

| Arquivo | Mudança |
|---|---|
| `build.gradle` | `dependencies { testImplementation platform('org.junit:junit-bom:5.10.3'); testImplementation 'org.junit.jupiter:junit-jupiter'; testRuntimeOnly 'org.junit.platform:junit-platform-launcher' }`; `test { useJUnitPlatform() }` |
| `src/test/java/.gitkeep` | Removido quando entrar o primeiro teste |
| `src/test/java/missaomarte/domain/PosicaoTest.java`, `MapaTest.java`, `NaveTest.java` | R2 |
| `src/test/java/missaomarte/application/AcaoTest.java`, `DificuldadeTest.java` | R3 |
| `src/test/java/missaomarte/application/MissaoFactoryTest.java` | R4 |
| `src/test/java/missaomarte/application/GameSessionTest.java` | R5 |
| `src/test/java/missaomarte/application/RankingTop5Test.java` | R6.1–R6.6 |
| `src/test/java/missaomarte/infrastructure/ranking/FileRankingRepositoryTest.java` | R6.7, R6.8 |
| `src/test/java/missaomarte/testes/` (ou no pacote do teste) | Dublês: `FabricaFixa`, `RelogioControlado`, `RankingEmMemoria`, `RankingQueFalha` |
| `README.md` | Acrescentar `./gradlew test` e onde fica o relatório (`build/reports/tests/test/index.html`) |

**Nome dos testes:** o ID do critério desta spec no nome do método, como
pede a skill de TDD. Ex.: `r5_8_naveDestruidaEPontuacaoZeradaGeramAsDuasDerrotasNaOrdem()`.
Use `@DisplayName` em português se ajudar a leitura do relatório.

## Dublês

| Dublê | Como funciona | Por que funciona sem mudar o código |
|---|---|---|
| `FabricaFixa extends MissaoFactory` | Recebe no construtor uma `Missao` já montada (nave, passageiros, asteroides, inimigos nas posições do teste) e a devolve em `criar(...)` | `MissaoFactory` não é `final` e `criar` é público (`MissaoFactory.java:16,24`) |
| `Random` com semente fixa | `new Random(42)` para a fábrica e a sessão | A sessão sorteia a direção dos inimigos com o `Random` recebido (`GameSession.java:77`) |
| Inimigo previsível | Para colisão por inimigo, sortear a direção certa é frágil. Preferir colisão com **asteroide** (parado) e, quando houver inimigo, um `Random` subclasse que devolve uma sequência fixa em `nextInt(int)` (0 = direita, 1 = esquerda, 2 = baixo, 3 = cima) | `Random.nextInt` não é `final` |
| `RelogioControlado extends Clock` | `millis()` devolve um valor que o teste avança | A sessão só usa `clock.millis()` (`GameSession.java:48,125,153`) |
| `RankingEmMemoria implements RankingRepository` | Lista em memória; conta quantas vezes `salvar` foi chamado | Porta da `application` |
| `RankingQueFalha implements RankingRepository` | `salvar`/`limpar` lançam `RankingException("disco cheio")` | Idem |

## Fluxo

Exemplo do teste de entrega (R5.5):

```
missao = Missao(Mapa(5), Nave(4)); Professor("Dr. Silva") em (1,0); sem perigos
sessao = GameSession(FabricaFixa(missao), new Random(42), RelogioControlado(0))
sessao.iniciar("Ana", FACIL, 5)
jogar(DIREITA)   → nave em (1,0)
jogar(EMBARCAR)  → EMBARQUE("Dr. Silva", 10)
jogar(ESQUERDA)  → eventos = [MOVIMENTO, ENTREGA(1), VITORIA]; entregues = 1
```

Com um único passageiro na missão montada, a mesma sequência prova
também a vitória (R5.10). Para "todos embarcados mas não entregues", usar
dois passageiros e só embarcar.

## Decisões e alternativas descartadas

- **Decisão:** BOM `org.junit:junit-bom:5.10.x` (5.10.3). Resposta à
  pergunta 1 do briefing: uma versão num lugar só.
  **Alternativa descartada:** versão em cada dependência. Espalha versões.
- **Decisão:** sem teste do console. Resposta à pergunta 3 do briefing.
- **Decisão:** dublês escritos à mão em `src/test/java`.
  **Alternativa descartada:** Mockito. Mais uma dependência para três
  interfaces pequenas, e o escopo pede o mínimo.
- **Decisão:** situações montadas pela `FabricaFixa`, com `Random` de
  semente fixa para o restante.
  **Alternativa descartada:** procurar uma semente que gere cada situação
  (ex.: colisão no 3º turno). O teste quebraria sem motivo claro e não
  documenta a situação que prova.
- **Decisão:** testes nos mesmos pacotes das classes.
  **Alternativa descartada:** pacote `missaomarte.testes` para tudo. Tira o
  acesso ao que é visível no pacote.

## Riscos e impactos

- **Teste revela bug:** não mudar `src/main/java` (quebra o hash). Marcar o
  teste com `@Disabled("bug: <descrição>")` só com aprovação do usuário,
  registrar nas notas da tarefa e avisar o usuário, que abre um `fix-` para
  a Fase 1. Depois do merge do fix, copiar de novo para a Fase 2, rodar o
  hash e reativar o teste.
- **Ponto *d*:** tamanho 1 no Médio/Difícil trava a fábrica. Nenhum teste
  usa tamanho < 2 com a fábrica real.
- **`Missao` montada à mão:** depende dos construtores do `domain` (`Missao`,
  `Nave`, `Professor`, `Asteroide`, `Inimigo`). Se a montagem exigir algo não
  público, usar o pacote certo; nunca mudar o código principal.

## Estratégia de testes

Esta spec é a própria estratégia de testes da Fase 1. Cada critério de R2 a
R6 vira pelo menos um método de teste com o ID no nome. R1 é provado pelo
gate (`./gradlew clean build` com os testes), pelo `comparar-hash.sh` e por
inspeção do `build.gradle` e dos testes (sem `System.in`, sem `new Random()`
sem semente, sem `Clock.systemDefaultZone()`, arquivos só em `@TempDir`).
