> **Concluída em 2026-11-20.** Diferenças do planejado: QA manual adiado pelo usuário para o fim da Fase 1 (roteiro em `reviews/final.md`). Ficam para `fase1-console-infra`: não chamar `jogar` depois do fim nem consultar a sessão antes de `iniciar` (sem guarda na `GameSession`), e o `FileRankingRepository` deve devolver a lista ordenada com no máximo 5 entradas (contrato suposto por `RankingTop5`). Para o tutorial (#7): tamanho 1 no Médio/Difícil trava a criação do cenário e a capacidade da nave muda por dificuldade.

# Requisitos: fase1-application

**Issue:** #5 · **Trilha:** A (núcleo do jogo) · **Fase:** 1 · **Depende de:** 1 (`fase1-domain`)
**Branch sugerida:** `feat/issue-5-fase1-application` · **PR:** "Closes #5"

## Contexto

Na Fase 0 as regras do turno, o estado da partida, os parâmetros por
dificuldade, a criação do cenário e o Top 5 estão no `Main.java` (linhas
81-302 e 434-439), misturados com `System.out` e `Scanner`. Esta spec cria
a camada `missaomarte.application`. A `GameSession` recebe uma `Acao` e
devolve o resultado do turno, com uma lista de eventos, sem entrada nem
saída, para que o console (spec 3) e as telas gráficas (Fases 3 e 4) só
traduzam esse resultado.

## Fora de escopo

- Console, mensagens em texto, desenho do mapa, `Main` e `build.sh`: spec
  `fase1-console-infra`.
- Implementação em arquivo do ranking (`FileRankingRepository`): spec
  `fase1-console-infra`.
- Top 10 e `RankingService`: Fase 3. A Fase 1 mantém o Top 5.
- Ouvinte (listener) de eventos entre sessão e console: descartado na
  `fase-00-arquitetura.md`; os eventos vêm no retorno do turno.
- Configuração de dificuldade em arquivo: descartada na
  `fase-00-arquitetura.md`.
- Pontos por entrega na base: descartado. Os pontos continuam vindo do
  embarque, como na Fase 0.
- Comando novo para desembarcar: descartado. O desembarque é automático.
- Corrigir os pontos de atenção *a*, *b*, *c*, *d*, *f*, *g* e *h* da
  `docs/analise-inicial.md`: eles são preservados (o *c* está em R1, o *d* em
  R3, o *f* em R2, o *g* e o *h* em R4).
- Gradle e JUnit: Fase 2.

## Requisitos

### R1 — Ação do jogador

**História:** Como sessão, quero receber um tipo próprio em vez de texto,
para que a regra não dependa de como o comando foi digitado.

**Critérios de aceitação:**

1. QUANDO o texto começar com `w`, `s`, `a` ou `d` (maiúscula ou
   minúscula) O SISTEMA DEVE convertê-lo em CIMA, BAIXO, ESQUERDA ou
   DIREITA, respectivamente. _(verificação: inspeção)_
2. QUANDO o texto começar com `c` O SISTEMA DEVE convertê-lo em EMBARCAR, e
   com `q`, em SAIR. _(verificação: inspeção)_
3. O SISTEMA DEVE considerar só o primeiro caractere, como na Fase 0
   (ponto *c*: `sair` vira BAIXO). _(verificação: inspeção)_
4. SE o texto for vazio, nulo ou começar com outro caractere ENTÃO O
   SISTEMA DEVE convertê-lo em INVALIDA. _(verificação: inspeção)_

### R2 — Dificuldade com parâmetros

**História:** Como fábrica e sessão, quero ler os parâmetros da própria
dificuldade, para que uma dificuldade nova não exija editar `switch`
espalhados.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter três dificuldades com estes parâmetros:

   | Dificuldade | Pontuação inicial | Passageiros | Asteroides | Inimigos | Capacidade da nave |
   |---|---|---|---|---|---|
   | FACIL | 30 | 4 | 1 | 1 | 4 |
   | MEDIO | 20 | 5 | 2 | 2 | 3 |
   | DIFICIL | 45 | 5 | 3 | 3 | 2 |

   _(verificação: inspeção)_
2. A capacidade por dificuldade é **mudança intencional** em relação à Fase
   0, que usava capacidade 5 em todas (`Main.java:253`). O objetivo é que o
   desembarque e a "nave cheia" tenham efeito. _(verificação: inspeção)_
3. QUANDO o texto da dificuldade for `facil`/`fácil` ou `dificil`/`difícil`,
   ignorando espaços nas pontas e maiúsculas, O SISTEMA DEVE escolher FACIL
   ou DIFICIL. Qualquer outro texto, ou nulo, escolhe MEDIO.
   _(verificação: inspeção)_
4. O SISTEMA DEVE exibir as dificuldades como "Fácil", "Médio" e
   "Difícil". _(verificação: inspeção)_

### R3 — Criação do cenário

**História:** Como sessão, quero que uma fábrica monte a missão a partir da
dificuldade, para que a criação do cenário fique num lugar só.

**Critérios de aceitação:**

1. QUANDO a missão for criada O SISTEMA DEVE usar as quantidades de
   passageiros, asteroides e inimigos e a capacidade da nave definidas na
   dificuldade. _(verificação: inspeção)_
2. O SISTEMA DEVE criar cada objeto numa célula livre: nenhum objeto na
   mesma célula de outro, nem na base (0,0). _(verificação: inspeção)_
3. O SISTEMA DEVE usar o `Random` recebido pelo construtor, e nenhum outro.
   _(verificação: inspeção)_
4. O SISTEMA DEVE criar os passageiros na sequência da Fase 0
   (`Main.java:304-312`): "Dr. Silva" (Professor), "Eng. Rosa" (Engenheiro),
   "Dr. Lima" (Professor), "Eng. Carlos" (Engenheiro) e "Ast. Maria"
   (Astronauta). _(verificação: inspeção)_
5. SE não houver células livres suficientes (ex.: tamanho 1 no Médio ou no
   Difícil) ENTÃO O SISTEMA mantém o comportamento da Fase 0 (ponto *d*: a
   criação não termina). O ponto fica registrado como limitação conhecida.
   _(verificação: inspeção)_

### R4 — Turno da partida

**História:** Como console (e, depois, como tela), quero mandar uma ação e
receber o que aconteceu, para não precisar conhecer as regras.

**Critérios de aceitação:**

1. QUANDO a partida for iniciada com piloto, dificuldade e tamanho O
   SISTEMA DEVE criar a missão pela fábrica, iniciar a pontuação com a
   pontuação inicial da dificuldade, zerar os movimentos e marcar o início
   pelo `Clock` injetado. _(verificação: inspeção)_
2. SE o nome do piloto for vazio ou só espaços ENTÃO O SISTEMA DEVE usar
   "Piloto Anônimo". SE o tamanho for menor que 1 ENTÃO O SISTEMA DEVE usar 5.
   _(verificação: inspeção)_
3. QUANDO a ação for de movimento O SISTEMA DEVE mover a nave (parada se
   sair do mapa), tirar 1 ponto e contar 1 movimento, inclusive ao bater na
   borda (ponto *g*). _(verificação: inspeção)_
4. QUANDO, depois de um movimento, a nave estiver na base com passageiros a
   bordo O SISTEMA DEVE desembarcar todos automaticamente, sem pontos
   extras. **Mudança intencional.** _(verificação: inspeção)_
5. QUANDO a ação for EMBARCAR O SISTEMA DEVE tentar embarcar o passageiro
   da posição da nave. Se embarcar, soma a pontuação do passageiro. EMBARCAR
   não custa ponto nem conta movimento. _(verificação: inspeção)_
6. QUANDO a ação for de movimento ou EMBARCAR O SISTEMA DEVE, depois dela,
   mover cada inimigo numa direção sorteada pelo `Random` injetado, como na
   Fase 0 (`Inimigo.java:25-38`: 0 = direita, 1 = esquerda, 2 = baixo,
   3 = cima). Depois verifica a colisão, a pontuação zerada e a vitória,
   nessa ordem. _(verificação: inspeção)_
7. QUANDO houver colisão O SISTEMA DEVE tirar 1 vida. SE as vidas chegarem
   a 0 ENTÃO a partida termina em derrota (nave destruída).
   _(verificação: inspeção)_
8. SE, depois da colisão, a pontuação for menor ou igual a 0 ENTÃO a
   partida termina em derrota (pontuação zerada), mesmo que a nave tenha
   acabado de entregar o último passageiro (ponto *h*).
   _(verificação: inspeção)_
9. QUANDO a partida não tiver terminado e todos os passageiros tiverem
   sido entregues O SISTEMA DEVE terminar a partida em vitória. **Mudança
   intencional:** na Fase 0, bastava todos embarcados e a nave em (0,0).
   _(verificação: inspeção)_
10. QUANDO a ação for SAIR O SISTEMA DEVE encerrar a partida como abortada,
    sem mover inimigos e sem vitória nem derrota. _(verificação: inspeção)_
11. QUANDO a ação for INVALIDA O SISTEMA DEVE devolver o resultado marcado
    como comando inválido, sem consumir turno: nada se move e nada muda.
    _(verificação: inspeção)_
12. QUANDO a partida terminar (vitória, derrota ou abortada) O SISTEMA DEVE
    congelar a duração em segundos inteiros, calculada pelo `Clock`: (fim −
    início) / 1000. _(verificação: inspeção)_
13. O SISTEMA DEVE oferecer, a qualquer momento: piloto, dificuldade,
    pontos, vidas, movimentos, duração, a missão (para desenho e
    indicadores), e se a partida terminou e como. _(verificação: inspeção)_

### R5 — Eventos do turno

**História:** Como console (e, depois, como interface gráfica da Fase 4),
quero a lista do que aconteceu no turno, na ordem, para mostrar mensagens e
efeitos sem consultar regras.

**Critérios de aceitação:**

1. QUANDO houver movimento O SISTEMA DEVE gerar MOVIMENTO, inclusive quando
   a nave bate na borda. _(verificação: inspeção)_
2. QUANDO houver embarque O SISTEMA DEVE gerar EMBARQUE, com o nome do
   passageiro e os pontos ganhos. _(verificação: inspeção)_
3. QUANDO o embarque for recusado O SISTEMA DEVE gerar
   EMBARQUE_RECUSADO_SEM_PASSAGEIRO ou EMBARQUE_RECUSADO_NAVE_CHEIA.
   _(verificação: inspeção)_
4. QUANDO houver desembarque na base O SISTEMA DEVE gerar ENTREGA, com a
   quantidade entregue. _(verificação: inspeção)_
5. QUANDO houver colisão O SISTEMA DEVE gerar COLISAO, com as vidas
   restantes (inclusive 0). _(verificação: inspeção)_
6. QUANDO a partida terminar em derrota O SISTEMA DEVE gerar
   DERROTA_NAVE_DESTRUIDA ou DERROTA_PONTUACAO_ZERADA. SE as duas condições
   acontecerem no mesmo turno ENTÃO gera as duas, nessa ordem, como as duas
   mensagens da Fase 0 (`Main.java:159-173`). _(verificação: inspeção)_
7. QUANDO a partida terminar em vitória O SISTEMA DEVE gerar VITORIA.
   _(verificação: inspeção)_
8. O SISTEMA DEVE devolver os eventos na ordem em que aconteceram: ação,
   entrega, colisão, derrota e vitória. SAIR e INVALIDA não geram eventos.
   _(verificação: inspeção)_

### R6 — Indicadores e resumo da partida

**História:** Como jogador, quero ver quantos passageiros estão a bordo,
quantos foram entregues e quantos faltam, com a conta sempre fechando.

**Critérios de aceitação:**

1. ENQUANTO a partida existir O SISTEMA DEVE manter a bordo + entregues +
   restantes no mapa = total de passageiros da dificuldade.
   _(verificação: inspeção)_
2. QUANDO a partida terminar O SISTEMA DEVE oferecer um resumo com piloto,
   dificuldade, pontuação final, movimentos, passageiros entregues,
   duração em segundos e resultado (vitória ou não). Esse resumo é usado
   pelo ranking e pelas estatísticas. _(verificação: inspeção)_

### R7 — Ranking: porta e regra do Top 5

**História:** Como jogador, quero que só as melhores vitórias fiquem no
ranking, para comparar meu desempenho, sem que a regra dependa de arquivo.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter, em `application`, a interface `RankingRepository`
   (carregar, salvar e limpar) e o registro `RankingEntry` com nome,
   pontuação, dificuldade, passageiros coletados (= entregues), data e hora
   (`yyyy-MM-dd HH:mm:ss`) e tempo de jogo em segundos.
   _(verificação: inspeção)_
2. QUANDO uma partida for registrada O SISTEMA DEVE recusar se não for
   vitória ou se a pontuação for menor ou igual a 0. _(verificação: inspeção)_
3. QUANDO o ranking tiver menos de 5 entradas O SISTEMA DEVE aceitar a
   vitória. Com 5 entradas, aceita só se a pontuação for **maior** que a da
   última. _(verificação: inspeção)_
4. QUANDO a vitória for aceita O SISTEMA DEVE ordenar o ranking por
   pontuação decrescente, mantendo a entrada nova depois das já existentes
   em caso de empate, guardar no máximo 5 entradas, salvar pelo repositório
   e informar que entrou. _(verificação: inspeção)_
5. O SISTEMA DEVE listar o ranking lendo o repositório a cada consulta. A
   Fase 0 recarregava o arquivo depois de cada partida e ao cancelar a
   limpeza. _(verificação: inspeção)_
6. O SISTEMA DEVE informar o recorde atual (a melhor entrada, se houver) e
   se uma pontuação seria novo recorde: maior que o recorde, com recorde
   maior que 0 (`Main.java:219-224`). _(verificação: inspeção)_
7. O SISTEMA DEVE limpar o ranking pelo repositório.
   _(verificação: inspeção)_
8. SE o repositório falhar ao salvar ou limpar ENTÃO O SISTEMA DEVE
   propagar a falha como exceção da `application`, com a mensagem original,
   para o console mostrar o texto da Fase 0. _(verificação: inspeção)_

### R8 — Isolamento da camada

**Critérios de aceitação:**

1. O SISTEMA DEVE ter uma `application` que não importa
   `missaomarte.console` nem `missaomarte.infrastructure`.
   _(verificação: gate)_
2. O SISTEMA NÃO DEVE usar `System.out`, `System.in`, `Scanner`,
   `new Random()` nem `System.currentTimeMillis()`/`LocalDateTime.now()` sem
   `Clock` na `application`. `Random` e `Clock` chegam pelo construtor.
   _(verificação: busca por `grep`)_
3. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE compilar sem erro.
   _(verificação: gate)_
