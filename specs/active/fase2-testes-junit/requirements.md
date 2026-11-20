# Requisitos: fase2-testes-junit

**Issue:** #9 · **Trilha:** C · **Fase:** 2 · **Depende de:** 4 (`fase2-gradle`)
**Branch sugerida:** `feat/issue-9-fase2-testes-junit` · **PR:** "Closes #9"

## Contexto

As regras do jogo foram separadas em `domain` e `application` na Fase 1,
mas só foram verificadas por inspeção e por roteiro manual. Esta spec
acrescenta JUnit 5 ao build da Fase 2 (`fases/fase-02-gradle/`) e cobre com
testes automáticos as regras e as decisões da Fase 1. Assim, as Fases 3 a 7,
que reaproveitam essas camadas, têm uma rede de segurança.

Base: issue #9, `ESCOPO-PROJETO.md` seção 7 ("Testes mínimos") e os
critérios de `specs/done/fase1-domain` e `specs/done/fase1-application`.
Cada critério abaixo aponta o requisito da Fase 1 que ele prova.

## Fora de escopo

- Alterar qualquer `.java` em `src/main/java`: quebra a comparação por hash
  da spec 4. Se um teste revelar bug, o teste **não** é ajustado ao bug
  nem o código é corrigido aqui: a correção vai para a Fase 1 (`fix-` na
  fila), e a Fase 2 copia de novo. Depois do merge da #8, o bug fica
  registrado como limitação no tutorial (#10).
- Testes do console (`missaomarte.console`) e do `Main`: o console não tem
  regra, e a #9 não pede.
- Testes que leem teclado, abrem terminal ou gravam fora de pasta
  temporária.
- Biblioteca de mocks (Mockito etc.) e de asserções (AssertJ): dublês
  escritos à mão bastam.
- Medição de cobertura (JaCoCo).
- Mudar regras de jogo.
- Tutorial e resultado dos testes na documentação: issue #10.

## Requisitos

### R1 — JUnit 5 no build

**História:** Como integrante, quero rodar os testes com o Gradle, para
saber em segundos se uma mudança quebrou uma regra.

**Critérios de aceitação:**

1. O SISTEMA DEVE declarar o JUnit 5 pelo BOM `org.junit:junit-bom` 5.10.x
   em `testImplementation`, com `org.junit.jupiter:junit-jupiter` sem
   versão própria, `org.junit.platform:junit-platform-launcher` em
   `testRuntimeOnly` e `useJUnitPlatform()` na tarefa `test`.
   _(verificação: inspeção)_
2. QUANDO `./gradlew test` ou `./gradlew clean build` for executado O
   SISTEMA DEVE rodar todos os testes e passar. _(verificação: gate)_
3. O SISTEMA DEVE continuar sem dependências de execução: o JUnit só
   aparece em configurações de teste. _(verificação: inspeção)_
4. O SISTEMA DEVE manter `src/main/java` idêntico à Fase 1:
   `scripts/comparar-hash.sh` sai com código 0. _(verificação: script)_
5. O SISTEMA DEVE ter testes que não leem `System.in`, não dependem de
   relógio real nem de `Random` sem semente, e não gravam fora de pasta
   temporária. _(verificação: inspeção)_

### R2 — Testes do domínio: posição, mapa e nave

**Critérios de aceitação:**

1. QUANDO uma `Posicao` for movida O SISTEMA DEVE devolver uma nova
   instância com a coordenada alterada e manter a original igual.
   _(verificação: teste)_
2. O SISTEMA DEVE considerar duas `Posicao` iguais, com o mesmo `hashCode`,
   quando têm as mesmas coordenadas, e diferentes quando não têm.
   _(verificação: teste)_
3. O SISTEMA DEVE ter testes de `Mapa` que provam os limites: as posições
   da borda estão contidas e as de fora não estão. _(verificação: teste)_
4. QUANDO a nave tentar sair do mapa O SISTEMA DEVE mantê-la parada na
   borda. _(verificação: teste)_
5. O SISTEMA DEVE ter testes de embarque na `Nave`: embarcar até a
   capacidade aceita, e embarcar com a nave cheia recusa sem alterar os
   passageiros a bordo. _(verificação: teste)_
6. QUANDO a nave desembarcar todos O SISTEMA DEVE devolver os passageiros
   e liberar a capacidade. _(verificação: teste)_

### R3 — Testes de ação e dificuldade

**Critérios de aceitação:**

1. O SISTEMA DEVE ter testes de `Acao.deTexto` para `w`, `s`, `a`, `d`,
   `c` e `q`, em maiúscula e minúscula; para texto que só considera o
   primeiro caractere (`sair` vira BAIXO, ponto *c*); e para vazio, nulo e
   outro caractere, que viram INVALIDA. _(prova: fase1-application R1)_
   _(verificação: teste)_
2. O SISTEMA DEVE ter testes dos parâmetros de cada dificuldade: pontuação
   inicial 30/20/45, passageiros 4/5/5, asteroides 1/2/3, inimigos 1/2/3 e
   capacidade 4/3/2 (FACIL/MEDIO/DIFICIL). _(prova: fase1-application R2.1)_
   _(verificação: teste)_
3. O SISTEMA DEVE ter testes de `Dificuldade.deString` (com e sem acento,
   maiúsculas e espaços; texto desconhecido e nulo viram MEDIO) e dos nomes
   de exibição "Fácil", "Médio" e "Difícil".
   _(prova: fase1-application R2.3, R2.4)_ _(verificação: teste)_

### R4 — Testes da criação do cenário

**Critérios de aceitação:**

1. QUANDO a missão for criada com `Random` de semente fixa, para cada
   dificuldade, O SISTEMA DEVE ter a quantidade de passageiros, asteroides
   e inimigos e a capacidade da nave da dificuldade.
   _(prova: fase1-application R3.1)_ _(verificação: teste)_
2. QUANDO a missão for criada, para várias sementes e tamanhos de mapa ≥ 2,
   O SISTEMA DEVE ter todos os objetos em células diferentes e nenhum na
   base (0,0). _(prova: fase1-application R3.2)_ _(verificação: teste)_
3. O SISTEMA DEVE criar os passageiros na sequência "Dr. Silva", "Eng.
   Rosa", "Dr. Lima", "Eng. Carlos", "Ast. Maria", com os tipos da Fase 0.
   _(prova: fase1-application R3.4)_ _(verificação: teste)_
4. QUANDO duas fábricas receberem `Random` com a mesma semente O SISTEMA
   DEVE criar o mesmo cenário. _(prova: fase1-application R3.3)_
   _(verificação: teste)_
5. O SISTEMA NÃO DEVE testar tamanho 1 no Médio/Difícil: a criação não
   termina (ponto *d*, limitação conhecida). _(verificação: inspeção)_

### R5 — Testes do turno da partida

Cada teste monta uma situação conhecida (posição da nave, passageiros,
perigos) e confere o `ResultadoTurno` e o estado da sessão.

**Critérios de aceitação:**

1. QUANDO a partida for iniciada O SISTEMA DEVE ter a pontuação inicial da
   dificuldade, 0 movimentos, as vidas iniciais e, com nome vazio ou só
   espaços, o piloto "Piloto Anônimo"; com tamanho < 1, o mapa de tamanho 5.
   _(prova: fase1-application R4.1, R4.2)_ _(verificação: teste)_
2. QUANDO houver movimento O SISTEMA DEVE tirar 1 ponto, contar 1
   movimento e gerar MOVIMENTO, inclusive ao bater na borda (ponto *g*).
   _(prova: R4.3, R5.1)_ _(verificação: teste)_
3. QUANDO a nave embarcar um passageiro O SISTEMA DEVE somar a pontuação
   dele, não contar movimento e gerar EMBARQUE com o nome e os pontos.
   _(prova: R4.5, R5.2)_ _(verificação: teste)_
4. QUANDO o embarque for recusado O SISTEMA DEVE gerar
   EMBARQUE_RECUSADO_SEM_PASSAGEIRO sem passageiro na célula, e
   EMBARQUE_RECUSADO_NAVE_CHEIA com a nave cheia ("nave cheia" alcançável
   com a capacidade da dificuldade). _(prova: R5.3)_ _(verificação: teste)_
5. QUANDO a nave chegar à base com passageiros a bordo O SISTEMA DEVE
   desembarcar todos automaticamente, sem pontos extras, e gerar ENTREGA
   com a quantidade. _(prova: R4.4, R5.4)_ _(verificação: teste)_
6. ENQUANTO a partida existir O SISTEMA DEVE manter a bordo + entregues +
   restantes = total, conferido antes e depois de embarque e entrega.
   _(prova: R6.1)_ _(verificação: teste)_
7. QUANDO houver colisão O SISTEMA DEVE tirar 1 vida e gerar COLISAO com as
   vidas restantes; com 0 vidas, terminar em derrota e gerar
   DERROTA_NAVE_DESTRUIDA. _(prova: R4.7, R5.5, R5.6)_
   _(verificação: teste)_
8. QUANDO a nave for destruída e a pontuação também zerar no mesmo turno O
   SISTEMA DEVE gerar DERROTA_NAVE_DESTRUIDA seguida de
   DERROTA_PONTUACAO_ZERADA. _(prova: R5.6, R5.8)_ _(verificação: teste)_
9. QUANDO a pontuação chegar a 0 ou menos O SISTEMA DEVE terminar em
   derrota com DERROTA_PONTUACAO_ZERADA, mesmo que o último passageiro
   tenha acabado de ser entregue (ponto *h*). _(prova: R4.8)_
   _(verificação: teste)_
10. QUANDO todos os passageiros forem entregues O SISTEMA DEVE terminar em
    vitória e gerar VITORIA; com todos embarcados mas não entregues, a
    partida continua. _(prova: R4.9, R5.7)_ _(verificação: teste)_
11. O SISTEMA DEVE devolver os eventos de um turno na ordem ação, entrega,
    colisão, derrota e vitória. _(prova: R5.8)_ _(verificação: teste)_
12. QUANDO a ação for INVALIDA O SISTEMA DEVE devolver comando inválido sem
    eventos e sem mudar pontos, movimentos nem as posições da nave e dos
    inimigos. QUANDO for SAIR, a partida termina como abortada, sem
    eventos e sem mover inimigos. _(prova: R4.10, R4.11, R5.8)_
    _(verificação: teste)_
13. QUANDO a partida terminar O SISTEMA DEVE congelar a duração: com um
    `Clock` controlado pelo teste, avançar o relógio depois do fim não muda
    a duração, e ela é (fim − início) / 1000 em segundos inteiros.
    _(prova: R4.12)_ _(verificação: teste)_
14. QUANDO a partida terminar O SISTEMA DEVE oferecer o resumo com piloto,
    dificuldade, pontos, movimentos, entregues, duração e resultado.
    _(prova: R6.2)_ _(verificação: teste)_

### R6 — Testes do ranking

**Critérios de aceitação:**

1. O SISTEMA DEVE ter testes do `RankingTop5` com um repositório em memória
   (dublê escrito à mão em `src/test/java`) e `Clock` fixo.
   _(verificação: inspeção)_
2. QUANDO a partida não for vitória ou tiver pontuação ≤ 0 O SISTEMA DEVE
   recusar o registro sem salvar. _(prova: fase1-application R7.2)_
   _(verificação: teste)_
3. QUANDO o ranking tiver menos de 5 entradas O SISTEMA DEVE aceitar a
   vitória; com 5, aceitar só pontuação **maior** que a da última (igual é
   recusada). _(prova: R7.3)_ _(verificação: teste)_
4. QUANDO a vitória for aceita O SISTEMA DEVE salvar em ordem decrescente,
   com a entrada nova depois das já existentes no empate, e no máximo 5
   entradas; a data e hora vêm do `Clock` no formato `yyyy-MM-dd HH:mm:ss`.
   _(prova: R7.1, R7.4)_ _(verificação: teste)_
5. O SISTEMA DEVE ter testes do recorde (melhor entrada, ou nenhuma) e de
   "seria novo recorde" (maior que o recorde, com recorde > 0).
   _(prova: R7.6)_ _(verificação: teste)_
6. SE o repositório falhar ao salvar ou limpar ENTÃO O SISTEMA DEVE
   propagar `RankingException` com a mensagem original. _(prova: R7.8)_
   _(verificação: teste)_
7. O SISTEMA DEVE ter testes do `FileRankingRepository` com `@TempDir`:
   salvar e ler de volta as mesmas entradas, em ordem decrescente; arquivo
   inexistente lê como lista vazia; limpar apaga o arquivo.
   _(prova: fase1-console-infra R3.2–R3.5)_ _(verificação: teste)_
8. QUANDO o `FileRankingRepository` ler um arquivo no formato da Fase 0
   (chaves `name`, `score`, `dificuldade`, `passageirosColetados`,
   `dataHora`, `tempoJogo`) O SISTEMA DEVE devolver as entradas
   correspondentes. _(prova: fase1-console-infra R3.2)_
   _(verificação: teste)_
