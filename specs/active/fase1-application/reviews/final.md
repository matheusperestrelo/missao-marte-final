# Verificação final — fase1-application

**Veredito:** APROVADA
**Branch:** feat/issue-5-fase1-application
**Commits da spec:** 11 (d2777ba..16096ac)

A Fase 1 não tem JUnit (`design.md`, "Estratégia de testes"). Todos os
critérios da spec são marcados como `inspeção`, `gate` ou `busca por grep`.
Por isso a coluna "Teste(s)" aponta a evidência usada: a inspeção do código,
os programas descartáveis que os revisores rodaram fora do repositório
(registrados nas revisões `01-r1` a `05-r1`), o `check.sh` e o `grep` do
design. Nesta verificação final, o revisor também releu `GameSession`,
`RankingTop5` e `Acao` inteiros contra o pseudocódigo do design. Ele rodou
ainda um programa descartável que exercita a `application` montada
(resultado na seção "Roteiro de QA").

## Cobertura

| Requisito | Critério | Teste(s) | Situação |
|---|---|---|---|
| R1 | 1 | inspeção `Acao.java:25-29` (01-r1) | coberto |
| R1 | 2 | inspeção `Acao.java:30-31` (01-r1) | coberto |
| R1 | 3 | inspeção `Acao.java:25`; execução final: `"sair"` → BAIXO | coberto |
| R1 | 4 | inspeção `Acao.java:22-24,32`; execução final: `""` e `null` → INVALIDA | coberto |
| R2 | 1 | inspeção `Dificuldade.java:4-6` contra a tabela (01-r1) | coberto |
| R2 | 2 | inspeção: capacidade 4/3/2 (01-r1) | coberto |
| R2 | 3 | inspeção `Dificuldade.java:31-43`; execução final: `"  DIFÍCIL "` → Difícil, `"xyz"`/`null` → Médio | coberto |
| R2 | 4 | inspeção `Dificuldade.java:45-48` (01-r1) | coberto |
| R3 | 1 | inspeção `MissaoFactory.java:26-37` + comparação com a Fase 0, 3000 casos (02-r1) | coberto |
| R3 | 2 | inspeção `MissaoFactory.java:43-53` + comparação (02-r1) | coberto |
| R3 | 3 | inspeção (único `Random` injetado) + `grep` de R8.2 (02-r1) | coberto |
| R3 | 4 | inspeção `MissaoFactory.java:56-64` + comparação (02-r1) | coberto |
| R3 | 5 | inspeção: `while (true)` sem limite, comentado (02-r1) | coberto |
| R4 | 1 | inspeção `GameSession.java:38-49` + H2 (04-r1) | coberto |
| R4 | 2 | inspeção `GameSession.java:39,41` + H2; execução final: `"  "`/0 → "Piloto Anônimo"/5 | coberto |
| R4 | 3 | inspeção `GameSession.java:64-67` + H0/H1/H2 (04-r1) | coberto |
| R4 | 4 | inspeção `GameSession.java:69-72` + H2/H3 (04-r1) | coberto |
| R4 | 5 | inspeção `GameSession.java:102-111` + H2/H3 (04-r1) | coberto |
| R4 | 6 | inspeção `GameSession.java:77,115-122` + H0/H1, 216 partidas iguais às da Fase 0 (04-r1) | coberto |
| R4 | 7 | inspeção `GameSession.java:79-86` + H3 (04-r1) | coberto |
| R4 | 8 | inspeção `GameSession.java:87-90` + H3 (04-r1) | coberto |
| R4 | 9 | inspeção `GameSession.java:91-94` + H2 (04-r1) | coberto |
| R4 | 10 | inspeção `GameSession.java:55-59` + H2 (04-r1) | coberto |
| R4 | 11 | inspeção `GameSession.java:52-54` + H2 (04-r1) | coberto |
| R4 | 12 | inspeção `GameSession.java:57,95-97,124-126,152-154` + H2 (04-r1) | coberto |
| R4 | 13 | inspeção `GameSession.java:128-167` (04-r1) | coberto |
| R5 | 1 | inspeção `GameSession.java:68` + H0/H1/H2 (04-r1) | coberto |
| R5 | 2 | inspeção `GameSession.java:110` + H3 (04-r1) | coberto |
| R5 | 3 | inspeção `GameSession.java:104-107` + H3 (04-r1) | coberto |
| R5 | 4 | inspeção `GameSession.java:70-72` + H3 (04-r1) | coberto |
| R5 | 5 | inspeção `GameSession.java:81` + H3, `COLISAO(0)` (04-r1) | coberto |
| R5 | 6 | inspeção `GameSession.java:83,88` + H3, as duas na ordem (04-r1) | coberto |
| R5 | 7 | inspeção `GameSession.java:92` + H2 (04-r1) | coberto |
| R5 | 8 | inspeção (ordem do código) + H2 (04-r1) | coberto |
| R6 | 1 | inspeção (embarque só por `missao.embarcar`) + H1/H2, invariante em todos os turnos (04-r1) | coberto |
| R6 | 2 | inspeção `ResumoPartida.java` (03-r1) e `GameSession.java:164-167` + H2 (04-r1) | coberto |
| R7 | 1 | inspeção `RankingRepository.java`, `RankingEntry.java`, `RankingTop5.java:14-15` + H (05-r1) | coberto |
| R7 | 2 | inspeção `RankingTop5.java:31` + H (05-r1) | coberto |
| R7 | 3 | inspeção `RankingTop5.java:33-35` + H (05-r1) | coberto |
| R7 | 4 | inspeção `RankingTop5.java:36-42` + H, empate e limite (05-r1) | coberto |
| R7 | 5 | inspeção `RankingTop5.java:25-27` + H (05-r1) | coberto |
| R7 | 6 | inspeção `RankingTop5.java:45-53` + H (05-r1) | coberto |
| R7 | 7 | inspeção `RankingTop5.java:55-57` + H (05-r1) | coberto |
| R7 | 8 | inspeção `RankingException.java` (nada é capturado) + H (05-r1); execução final: mensagem original propagada | coberto |
| R8 | 1 | gate: "regras de camadas" do `check.sh` | coberto |
| R8 | 2 | `grep -rnE "System\.(out\|in)\|Scanner\|new Random\|currentTimeMillis\|LocalDateTime\.now\(\)"` na `application`: nenhum resultado (rodado de novo nesta verificação) | coberto |
| R8 | 3 | gate: javac do `check.sh` (`-Xlint:all`) | coberto |

47 critérios, 47 cobertos, nenhuma lacuna.

## check.sh

passou (fase 0 imutável, `fases/fase-01-refatoracao: javac`, regras de
camadas), na branch, com o conjunto das 5 tarefas.

## Pendências

Nenhuma bloqueia a aprovação. Ficam registradas para a Fase 2, para a spec
`fase1-console-infra` e para o tutorial (#7):

- Revisão 04-r1, S1: `jogar` depois do fim continua alterando o estado (os
  pontos podem ficar negativos). O design aceita isso ("o console nunca
  chama depois do fim"). A `fase1-console-infra` precisa respeitar esse
  contrato. Na Fase 2, vale considerar um guarda ou uma
  `IllegalStateException`.
- Revisão 04-r1, S2: antes do primeiro `iniciar`, `getVidas()` e `resumo()`
  lançam `NullPointerException`, e `getDuracaoSegundos()` mede desde a
  época. O `Main` da spec 3 precisa chamar `iniciar` antes de consultar a
  sessão.
- Revisão 05-r1, S1: `RankingTop5.registrar` supõe que
  `RankingRepository.carregar()` devolve a lista já ordenada e com no
  máximo 5 entradas. O `FileRankingRepository` (spec 3) precisa garantir
  isso, ou o contrato precisa ser documentado.
- Notas da tarefa 2 e `design.md` "Riscos": o ponto *d* (tamanho 1 no Médio
  ou no Difícil trava a criação) continua existindo. A capacidade por
  dificuldade é mudança intencional. As duas coisas precisam entrar no
  tutorial (#7).
- Revisão 03-r1, S1: aplicada na tarefa 4 (`List.copyOf` na `GameSession`).
  Revisão 01-r1, S1 (nome do parâmetro `s` em `Dificuldade.deString`): é só
  cosmética e não foi aplicada.
- Desvios do `design.md`: nenhum registrado nas notas, e nenhum encontrado.
  `RankingException` ganhou um `serialVersionUID` (notas da tarefa 5) para
  não gerar aviso do `-Xlint:all`. Isso é aditivo.

## Roteiro de QA

Esta spec não tem `Main` nem console (são da `fase1-console-infra`). Por
isso o QA usa um programa pequeno que chama a `application` direto. Esse
programa fica **fora** do repositório.

Preparação, na raiz do repositório:

```
mkdir -p /tmp/qa-app && javac --release 17 -encoding UTF-8 -d /tmp/qa-app/out $(find fases/fase-01-refatoracao/src -name '*.java')
```

Crie `/tmp/qa-app/QaApplication.java` com o programa abaixo e rode
`java -cp /tmp/qa-app/out /tmp/qa-app/QaApplication.java`:

```java
import missaomarte.application.*;
import java.time.*;
import java.util.*;

public class QaApplication {
    public static void main(String[] a) {
        System.out.println(Acao.deTexto("sair") + " " + Acao.deTexto("Q") + " " + Acao.deTexto("") + " " + Acao.deTexto(null));
        System.out.println(Dificuldade.deString("  DIFÍCIL ") + " " + Dificuldade.deString("xyz") + " " + Dificuldade.deString(null));
        GameSession s = new GameSession(new MissaoFactory(new Random(42)), new Random(42), Clock.systemUTC());
        s.iniciar("  ", Dificuldade.FACIL, 0);
        System.out.println(s.getPiloto() + " pontos=" + s.getPontos() + " vidas=" + s.getVidas());
        System.out.println(s.jogar(Acao.deTexto("x")) + " pontos=" + s.getPontos());
        System.out.println(s.jogar(Acao.EMBARCAR) + " pontos=" + s.getPontos() + " mov=" + s.getMovimentos());
        System.out.println(s.jogar(Acao.DIREITA) + " pontos=" + s.getPontos() + " mov=" + s.getMovimentos());
        System.out.println(s.jogar(Acao.SAIR) + " " + s.getResultado());
        System.out.println(s.resumo());
        List<RankingEntry> mem = new ArrayList<>();
        RankingRepository repo = new RankingRepository() {
            public List<RankingEntry> carregar() { return new ArrayList<>(mem); }
            public void salvar(List<RankingEntry> l) { mem.clear(); mem.addAll(l); }
            public void limpar() { throw new RankingException("falha simulada"); }
        };
        RankingTop5 r = new RankingTop5(repo, Clock.fixed(Instant.parse("2026-01-02T03:04:00Z"), ZoneOffset.UTC));
        System.out.println(r.registrar(new ResumoPartida("A", Dificuldade.FACIL, 10, 5, 4, 7, true)) + " "
                + r.registrar(new ResumoPartida("B", Dificuldade.FACIL, 10, 5, 4, 7, false)) + " " + r.listar());
        System.out.println(r.recorde() + " " + r.seriaNovoRecorde(11) + " " + r.seriaNovoRecorde(10));
        try { r.limpar(); } catch (RankingException e) { System.out.println("exceção: " + e.getMessage()); }
    }
}
```

Conferir a saída, linha a linha (o revisor rodou o programa e obteve
exatamente isto):

1. `BAIXO SAIR INVALIDA INVALIDA` → **esperado:** só o primeiro caractere
   conta, e vazio ou nulo vira INVALIDA (R1.2, R1.3, R1.4).
2. `Difícil Médio Médio` → **esperado:** espaços, maiúsculas e acento
   aceitos. Texto desconhecido ou nulo vira Médio. Os rótulos são exibidos
   com acento (R2.3, R2.4).
3. `Piloto Anônimo pontos=30 vidas=3` → **esperado:** nome em branco vira o
   padrão, e o Fácil começa com 30 (R4.1, R4.2, R2.1).
4. `ResultadoTurno[eventos=[], comandoInvalido=true] pontos=30` →
   **esperado:** comando inválido não consome turno (R4.11, R5.8).
5. Linha com `EMBARQUE_RECUSADO_SEM_PASSAGEIRO ... pontos=30 mov=0` →
   **esperado:** EMBARCAR sem passageiro não custa ponto nem movimento
   (R4.5, R5.3).
6. Linha com `MOVIMENTO ... pontos=29 mov=1` → **esperado:** o movimento
   custa 1 ponto e conta 1 movimento (R4.3, R5.1).
7. `ResultadoTurno[eventos=[], comandoInvalido=false] ABORTADA` →
   **esperado:** SAIR encerra como abortada e não gera eventos (R4.10).
8. `ResumoPartida[piloto=Piloto Anônimo, dificuldade=Fácil, pontos=29, movimentos=1, entregues=0, duracaoSegundos=0, vitoria=false]`
   → **esperado:** o resumo tem todos os campos de R6.2 e a duração foi
   congelada (R4.12).
9. `true false [RankingEntry[nome=A, ..., dataHora=2026-01-02 03:04:00, tempoJogo=7]]`
   → **esperado:** a vitória entra e a derrota é recusada. A data sai no
   formato `yyyy-MM-dd HH:mm:ss`, pelo `Clock` (R7.1, R7.2, R7.3).
10. `Optional[RankingEntry[nome=A, ...]] true false` → **esperado:** o
    recorde é a melhor entrada. 11 seria novo recorde; 10 (igual) não seria
    (R7.6).
11. `exceção: falha simulada` → **esperado:** a falha do repositório
    propaga com a mensagem original (R7.8).
12. Opcional: troque `Dificuldade.FACIL` por `MEDIO` e `DIFICIL` no
    `iniciar` → **esperado:** pontuação inicial 20 e 45 (R2.1).

O jogo ponta a ponta (vitória entregando todos na base, nave cheia,
colisão e derrota) só pode ser testado com o console. Esse teste fica no
roteiro manual da spec `fase1-console-infra`, como prevê o `design.md`.

## Próximos passos

- APROVADA: QA pelo usuário, merge e conclusão da spec (active/ → done/).
- Levar as pendências acima para a `fase1-console-infra` (contrato de
  `jogar`/`iniciar` e ordenação do repositório), para o tutorial (#7) e
  para o `briefing-fase2`.
