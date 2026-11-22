# Requisitos: fase3-partida

**Issue:** #16 · **Trilha:** B · **Fase:** 3 · **Depende de:** 8 (`fase3-estrutura-libgdx`), 9 (`fase3-ranking-service`), 10 (`fase3-telas-menu`)
**Branch sugerida:** `feat/issue-16-fase3-partida` · **PR:** "Closes #16"

## Contexto

A spec 10 deixa o jogador escolher nome e dificuldade, mas a Partida ainda
é uma tela provisória. Esta spec entrega a partida jogável na janela: o
mapa 11×11 desenhado com formas simples, o painel com o estado, a entrada
por teclado convertida em `Acao`, o fim por vitória, derrota ou
abandono, e o registro no Top 10. As regras continuam na `GameSession` e
no `RankingService`. A tela só converte tecla em ação e desenha o estado.

Base: issue #16, `ESCOPO-PROJETO.md`, seção 8, "Entrada", "Mapa" e
"Coordenadas", e o resumo do alinhamento da Fase 3 (fim da partida,
embarque só por `E`).

## Fora de escopo

- Sprites, sons, HUD gráfico, animações e movimento suave: Fase 4. Aqui o
  visual é de retângulos coloridos e texto da fonte padrão.
- Repetição de tecla (segurar a tecla para andar): cortada do projeto.
- Embarque pela tecla `C`: decidido no alinhamento que só `E` embarca na
  janela. `C` é ignorada como qualquer outra tecla. O console da Fase 1/2
  continua com `C`. A troca é registrada no tutorial (spec 12).
- Confirmar antes de abandonar com `Q`/`Esc`: não pedido. Sair volta direto
  ao Menu, como no console.
- Pausa e retomada da partida ao minimizar a janela: o ciclo de vida só é
  registrado no log (spec 8).
- Mudar qualquer regra de jogo ou de ranking.
- README e tutorial: spec 12.

## Requisitos

### R1 — Tecla → ação

**História:** Como jogador, quero jogar com o teclado (WASD ou setas), para
mover a nave, embarcar passageiros e sair.

**Critérios de aceitação:**

1. QUANDO o jogador apertar `W` ou `↑` O SISTEMA DEVE executar um turno de
   movimento para cima. _(verificação: teste do mapeador + manual)_
2. QUANDO o jogador apertar `S` ou `↓` O SISTEMA DEVE executar um turno de
   movimento para baixo. _(verificação: teste)_
3. QUANDO o jogador apertar `A` ou `←` O SISTEMA DEVE executar um turno de
   movimento para a esquerda. _(verificação: teste)_
4. QUANDO o jogador apertar `D` ou `→` O SISTEMA DEVE executar um turno de
   movimento para a direita. _(verificação: teste)_
5. QUANDO o jogador apertar `E` O SISTEMA DEVE executar um turno de
   embarque. _(verificação: teste)_
6. QUANDO o jogador apertar `Q` ou `Esc` O SISTEMA DEVE abandonar a partida.
   _(verificação: teste)_
7. QUANDO o jogador apertar qualquer outra tecla (inclusive `C`, `Enter` e
   números) durante a partida O SISTEMA DEVE ignorá-la **sem gastar
   turno**: pontos, movimentos, posição dos inimigos e mensagens não mudam.
   _(verificação: teste do mapeador e do controle da partida)_
8. O SISTEMA DEVE executar **um** turno por toque de tecla. Segurar a tecla
   não repete o turno. _(verificação: manual)_

### R2 — Mapa 11×11

**História:** Como jogador, quero ver o mapa com a nave, a base, os
passageiros e os perigos, para decidir o próximo movimento.

**Critérios de aceitação:**

1. ENQUANTO a partida estiver aberta O SISTEMA DEVE desenhar a grade de
   11×11 células, cada uma como um retângulo, sem distorcer ao
   redimensionar a janela. _(verificação: manual)_
2. O SISTEMA DEVE desenhar cada elemento do mapa (base, passageiros,
   asteroides, inimigos e nave) como um retângulo colorido na sua célula,
   com uma cor por tipo de elemento e o **símbolo** do elemento escrito em
   cima. O símbolo vem do próprio elemento do domínio (`@`, `P`, `E`, `T`,
   `#`, `X`, `L`). _(verificação: manual + revisão)_
3. O SISTEMA DEVE desenhar a nave **por cima** dos outros elementos quando
   estiverem na mesma célula. _(verificação: manual, nave sobre a base no
   início)_
4. O SISTEMA DEVE mostrar a linha de menor `y` do domínio no **topo** da
   grade e a coluna de menor `x` à **esquerda**, para que "para cima" no
   teclado leve a nave para cima na tela. _(verificação: teste da
   geometria + manual)_
5. QUANDO um turno terminar O SISTEMA DEVE desenhar o mapa com o estado
   novo já no quadro seguinte. _(verificação: manual)_

### R3 — Conversão de coordenadas

**Critérios de aceitação:**

1. O SISTEMA DEVE ter a conversão "célula do domínio → posição em pixels"
   numa classe **sem nenhum import de `com.badlogic`**, que isola a
   inversão do eixo Y (no domínio `y` cresce para baixo; no libGDX, para
   cima). _(verificação: inspeção)_
2. QUANDO a célula for a do canto superior esquerdo (menor `x`, menor `y`)
   O SISTEMA DEVE devolver o ponto mais à esquerda e mais **alto** da
   grade. _(verificação: teste)_
3. QUANDO a célula for a do canto inferior direito (maior `x`, maior `y`)
   O SISTEMA DEVE devolver o ponto mais à direita e mais **baixo** da
   grade. _(verificação: teste)_
4. QUANDO a célula for a origem `(0, 0)` O SISTEMA DEVE devolver o centro
   da grade (a 6ª linha e a 6ª coluna). _(verificação: teste)_
5. QUANDO `y` aumentar em 1 O SISTEMA DEVE devolver um ponto uma célula
   **abaixo**. Quando `x` aumentar em 1, uma célula à **direita**.
   _(verificação: teste)_

### R4 — Painel

**História:** Como jogador, quero ver meu estado e o que aconteceu no
turno, para entender a partida.

**Critérios de aceitação:**

1. ENQUANTO a partida estiver aberta O SISTEMA DEVE mostrar ao lado do mapa
   as linhas "Piloto: <nome>", "Dificuldade: <dificuldade>", "Vidas:
   <vidas>", "Pontos: <pontos>", "A bordo: <a bordo>/<capacidade>",
   "Entregues: <entregues>/<total>", "Restantes: <restantes>" e
   "Movimentos: <movimentos>", com os valores lidos da sessão de jogo.
   _(verificação: teste do texto do painel + manual)_
2. QUANDO um turno gerar eventos O SISTEMA DEVE mostrar no painel as
   mensagens desse turno, com as mesmas frases do console da Fase 2, sem
   emoji e sem as linhas de separador:
   - "Passageiro <nome> embarcado com sucesso! +<pontos> pontos!"
   - "Nenhum passageiro nesta posição."
   - "Nave cheia! Não há espaço para mais passageiros."
   - "<n> passageiro(s) entregue(s) na base!"
   - "Alerta! Colisão detectada! Vidas restantes: <n>" (só com vidas > 0)
   - "GAME OVER! A nave foi destruída."
   - "Combustível/Pontuação zerada! Missão perdida."
   - "DECOLAGEM AUTORIZADA! Nave acoplada à plataforma em (0,0)." e
     "Retornando à órbita marciana com todos os passageiros. Missão
     cumprida!"

   Movimento sem outro evento não gera mensagem. _(verificação: teste)_
3. QUANDO o turno seguinte for executado O SISTEMA DEVE trocar as mensagens
   pelas desse turno. As do turno anterior somem. _(verificação: teste)_
4. QUANDO não houver mais passageiros no mapa, houver passageiros a bordo e
   a partida continuar O SISTEMA DEVE mostrar também "ALERTA: Todos os
   passageiros resgatados! Retorne para a Plataforma de Pouso 'L' em (0,0)
   para completar a missão.". _(verificação: teste)_
5. O SISTEMA DEVE quebrar as linhas longas do painel dentro da largura
   dele, sem escrever por cima do mapa. _(verificação: manual)_

### R5 — Fim da partida

**História:** Como jogador, quero ver como a partida terminou e seguir para
o ranking ou para o menu.

**Critérios de aceitação:**

1. QUANDO a partida terminar em vitória O SISTEMA DEVE registrar a partida
   no serviço de ranking **uma única vez**, no turno da vitória, e mostrar
   no painel "Pressione Enter para continuar". _(verificação: teste do
   controle da partida com serviço em memória)_
2. QUANDO a partida terminar em derrota O SISTEMA DEVE mostrar no painel o
   motivo (pelas mensagens de R4.2) e "Pressione Enter para continuar", e
   NÃO DEVE registrar nada no ranking. _(verificação: teste)_
3. ENQUANTO a partida estiver terminada (vitória ou derrota) O SISTEMA DEVE
   ignorar todas as teclas exceto `Enter`. Nenhum turno é executado e nada
   é registrado de novo. _(verificação: teste)_
4. QUANDO o jogador apertar `Enter` depois de uma vitória O SISTEMA DEVE
   abrir a tela Ranking com o resumo da partida (pontuação final,
   movimentos, tempo, passageiros entregues) e a informação de se ela
   entrou no Top 10. _(verificação: teste do destino + manual)_
5. QUANDO o jogador apertar `Enter` depois de uma derrota O SISTEMA DEVE
   voltar ao Menu. _(verificação: teste + manual)_
6. QUANDO o jogador abandonar a partida (`Q` ou `Esc`) O SISTEMA DEVE
   voltar direto ao Menu, sem tela de fim e sem registro no ranking.
   _(verificação: teste + manual)_
7. SE o serviço de ranking falhar ao registrar a vitória ENTÃO O SISTEMA
   DEVE mostrar no painel "Não foi possível salvar o ranking: <mensagem do
   erro>", sem fechar o jogo, e a tela Ranking deve informar que a
   pontuação não entrou no Top 10. _(verificação: teste com repositório
   que falha)_
8. QUANDO o jogador voltar ao Menu e começar outra partida O SISTEMA DEVE
   começar do zero (sessão nova, mensagens vazias, registro permitido de
   novo para a nova partida). _(verificação: manual)_

### R6 — Separação entre tela e regra

**Critérios de aceitação:**

1. O SISTEMA NÃO DEVE ter regra de jogo na tela da Partida: movimento,
   pontos, colisão, vitória, derrota e entrada no ranking vêm da sessão de
   jogo e do serviço de ranking. _(verificação: revisão)_
2. O SISTEMA DEVE ter os testes de R1, R3, R4 e R5 rodando sem janela e
   sem OpenGL. _(verificação: inspeção + gate)_
3. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar, com todos os
   testes anteriores. _(verificação: gate)_
