# Requisitos: fase4-interface-grafica

**Issue:** a criar ("[Fase 4] Interface gráfica") · **Trilha:** C · **Fase:** 4 · **Depende de:** 15 (`fase4-assets-recursos`)
**Branch sugerida:** `feat/fase4-interface-grafica` · **PR:** "Closes #<issue>"

## Contexto

A spec 15 deixa a Fase 4 com imagens e sons gerados, carregados uma vez
pelo `Recursos`, e a partida desenhada com o primeiro quadro de cada
imagem. Esta spec dá vida à interface: animações, nave girando, movimento
suave, resposta visual e sonora aos eventos do turno, música de fundo,
caixa final, HUD, fundo nas outras telas e semente reproduzível. Também
entrega o README e o tutorial da Fase 4. As regras continuam na
`GameSession`: a tela só lê a sessão e reage aos eventos que ela já gera
desde a Fase 1.

Base: `ESCOPO-PROJETO.md`, seção 8, "Fase 4" (visual e jogabilidade), o
briefing da Fase 4 e o resumo do alinhamento de 2026-11-26.

## Fora de escopo

- Tremor de tela, flash vermelho, nave piscando, texto flutuante
  ("+pontos", "-pontos", "Entregue!") e brilho animado: cortados do
  projeto (briefing da Fase 4).
- Repetição de tecla (segurar para andar): cortada. Continua um turno por
  toque, como na Fase 3.
- Tecla `M` (liga/desliga som), indicador de som na barra superior e
  `-Dmissaomarte.semaudio`: cortados.
- Outras teclas na caixa final (ex.: `R` para jogar de novo): o destino do
  `Enter` continua o da Fase 3.
- Mudar regra de jogo, de ranking, de navegação ou os eventos do turno.
  `domain`, `application` e `infrastructure` continuam idênticos aos da
  Fase 3.
- Novos assets ou mudança nos existentes: são da spec 15. Se faltar algum,
  volta como ajuste na spec 15 ou spec nova.
- Volume configurável, tela de opções e tela de carregamento.

## Requisitos

### R1 — Mundo desenhado fora da tela, com animações

**História:** Como jogador, quero ver a nave, a base e os inimigos
animados, para o jogo parecer vivo.

**Critérios de aceitação:**

1. O SISTEMA DEVE desenhar o mapa (terreno, elementos, nave, efeitos) num
   componente próprio, fora da tela da Partida. A tela só repassa o tempo
   do quadro e chama o desenho. _(verificação: revisão)_
2. ENQUANTO a partida estiver aberta O SISTEMA DEVE animar a base, os
   inimigos e o propulsor da nave em laço, trocando de quadro a cada
   0,2 s. _(verificação: teste do relógio de animação + manual)_
3. QUANDO a nave fizer um movimento O SISTEMA DEVE girar a imagem da nave
   (e do propulsor, atrás dela) para a direção desse movimento: `CIMA` 0°,
   `DIREITA`, `BAIXO` e `ESQUERDA`, mesmo que a nave não saia do lugar por
   estar na borda. _(verificação: teste da direção visual + manual)_
4. QUANDO a partida começar O SISTEMA DEVE mostrar a nave virada para
   cima. Embarque e teclas ignoradas não mudam a direção.
   _(verificação: teste)_
5. O SISTEMA DEVE continuar escolhendo a imagem de cada elemento pelo
   símbolo (spec 15, R5), sem `instanceof`. _(verificação: revisão)_

### R2 — Movimento suave

**História:** Como jogador, quero ver a nave e os inimigos deslizando de
uma célula para outra, sem saltos.

**Critérios de aceitação:**

1. QUANDO a posição lógica de um elemento móvel (nave ou inimigo) mudar O
   SISTEMA DEVE mover a posição desenhada em direção à nova posição a
   12 células por segundo. _(verificação: teste)_
2. O SISTEMA NÃO DEVE ultrapassar a posição lógica: ao chegar, a posição
   desenhada fica exatamente igual a ela. _(verificação: teste com passo de
   tempo maior que o necessário)_
3. QUANDO o jogador apertar outra tecla antes de o movimento anterior
   terminar O SISTEMA DEVE executar o turno na hora (a lógica nunca espera
   o desenho), e a posição desenhada segue do ponto onde está para o novo
   alvo. _(verificação: teste + manual)_
4. O SISTEMA DEVE converter posição fracionária de célula em pixels com a
   mesma orientação da Fase 3 (menor `y` no topo). _(verificação: teste da
   geometria fracionária)_
5. O SISTEMA DEVE ter a lógica da posição desenhada sem nenhum import de
   `com.badlogic`. _(verificação: inspeção + gate)_

### R3 — Resposta aos eventos do turno

**História:** Como jogador, quero ouvir e ver o que aconteceu no turno,
para perceber colisões, embarques e entregas na hora.

**Critérios de aceitação:**

1. QUANDO um turno terminar O SISTEMA DEVE tocar **um** efeito sonoro,
   escolhido pelo evento mais importante do turno, nesta ordem: colisão,
   entrega, embarque, movimento. Os sons de vitória e de derrota não tocam
   aqui: tocam quando a caixa final aparece (R5.7). Assim, no turno da
   derrota por colisão, toca a colisão com a explosão e, depois, a derrota.
   _(verificação: teste)_
2. QUANDO o embarque for recusado (sem passageiro ou nave cheia) e não
   houver outro evento de R3.1 no turno O SISTEMA NÃO DEVE tocar efeito.
   _(verificação: teste)_
3. QUANDO o turno tiver colisão O SISTEMA DEVE tocar a explosão (4 quadros,
   0,08 s cada, uma vez) **por cima** da célula da colisão, com a nave
   continuando visível. _(verificação: teste do efeito + manual)_
4. QUANDO a colisão destruir a nave (derrota por nave destruída) O SISTEMA
   DEVE deixar de desenhar a nave ao fim da explosão.
   _(verificação: teste + manual)_
5. ENQUANTO a explosão estiver tocando O SISTEMA DEVE continuar aceitando
   teclas normalmente (se a partida não acabou), e a explosão fica na
   célula da colisão mesmo que a nave saia dela. _(verificação: manual)_
6. QUANDO o turno não tiver eventos (tecla ignorada) O SISTEMA NÃO DEVE
   tocar som nem efeito. _(verificação: teste)_
7. O SISTEMA DEVE ter a escolha "eventos do turno → som e efeitos" sem
   nenhum import de `com.badlogic`. _(verificação: inspeção + gate)_

### R4 — Música de fundo

**Critérios de aceitação:**

1. QUANDO o Menu abrir pela primeira vez O SISTEMA DEVE começar a tocar a
   música em laço, com volume menor que o dos efeitos.
   _(verificação: manual)_
2. ENQUANTO o jogador navegar entre Menu, Nova partida, Ranking e Partida
   em andamento O SISTEMA DEVE manter a música tocando, sem recomeçar ao
   trocar de tela. _(verificação: manual)_
3. QUANDO a partida terminar em vitória ou derrota O SISTEMA DEVE pausar a
   música. _(verificação: teste do controle de música + manual)_
4. QUANDO o Menu abrir com a música pausada O SISTEMA DEVE retomar a
   música. _(verificação: teste + manual)_
5. QUANDO o jogador abandonar a partida (`Q`/`Esc`) O SISTEMA DEVE manter
   a música tocando. _(verificação: teste)_

### R5 — Caixa final

**História:** Como jogador, quero uma tela clara de fim, para saber se
venci, por que perdi e como foi a partida.

**Critérios de aceitação:**

1. QUANDO a partida terminar em vitória O SISTEMA DEVE mostrar, centralizada
   sobre o mapa, uma caixa com "MISSÃO CUMPRIDA", o resumo e "Pressione
   Enter para continuar". _(verificação: teste do texto + manual)_
2. QUANDO a partida terminar em derrota O SISTEMA DEVE mostrar a caixa com
   "GAME OVER", a causa ("Nave destruída" ou "Pontuação zerada"), o resumo
   e "Pressione Enter para continuar". _(verificação: teste)_
3. O SISTEMA DEVE mostrar no resumo "Pontos: <n>", "Tempo: <mm:ss>",
   "Movimentos: <n>" e "Entregues: <entregues>/<total>", com os valores da
   sessão. _(verificação: teste)_
4. QUANDO a vitória entrar no Top 10 O SISTEMA DEVE acrescentar "Você
   entrou no Top 10!" à caixa. SE não entrar ou o ranking falhar ENTÃO a
   linha não aparece (a mensagem de falha da Fase 3 continua no painel).
   _(verificação: teste)_
5. QUANDO a partida terminar O SISTEMA DEVE mostrar a caixa só depois que a
   posição desenhada da nave chegar à lógica e a explosão do último turno
   (se houver) terminar. _(verificação: teste do controle de fim)_
6. ENQUANTO a caixa não estiver visível O SISTEMA DEVE ignorar o `Enter`.
   Com a caixa visível, o `Enter` leva ao Ranking na vitória e ao Menu na
   derrota, como na Fase 3. _(verificação: teste + manual)_
7. QUANDO a caixa aparecer O SISTEMA DEVE tocar o som de vitória ou de
   derrota uma única vez, com a música já pausada (R4.3).
   _(verificação: teste + manual)_

### R6 — HUD

**História:** Como jogador, quero ver meu estado organizado em barra,
painel e rodapé, para ler tudo sem procurar.

**Critérios de aceitação:**

1. ENQUANTO a partida estiver aberta O SISTEMA DEVE mostrar uma barra no
   topo com as vidas (um ícone de vida cheia por vida restante e um de vida
   vazia por vida perdida, 3 no total), "Pontos: <n>" e "Tempo: <mm:ss>".
   _(verificação: teste do texto e da contagem de ícones + manual)_
2. ENQUANTO a partida estiver aberta O SISTEMA DEVE mostrar no painel
   lateral "Piloto: <nome>", "Dificuldade: <dificuldade>", as vagas (um
   ícone de vaga ocupada por passageiro a bordo e um de vaga livre por
   lugar vazio, até a capacidade), "Entregues: <entregues>/<total>",
   "Restantes: <n>", "Movimentos: <n>", as mensagens do turno e o alerta de
   passageiros resgatados da Fase 3. _(verificação: teste + manual)_
3. ENQUANTO a partida estiver aberta O SISTEMA DEVE mostrar no rodapé os
   controles: "WASD/setas: mover · E: embarcar · Q/Esc: sair".
   _(verificação: teste + manual)_
4. O SISTEMA DEVE mostrar o tempo correndo durante a partida e parado no
   valor final depois que ela termina. _(verificação: teste com relógio
   controlado)_
5. O SISTEMA DEVE mostrar os textos com acentos (ex.: "Missão", "Não",
   "Pontuação") sem caracteres faltando. _(verificação: manual)_
6. O SISTEMA DEVE montar os textos e as contagens do HUD só lendo a sessão,
   sem nenhum import de `com.badlogic`, e sem chamar nada que mude a
   sessão. _(verificação: inspeção + teste)_
7. O SISTEMA DEVE caber em 800×600 sem sobrepor barra, mapa, painel e
   rodapé. _(verificação: manual)_

### R7 — Arte e som nas outras telas

**Critérios de aceitação:**

1. ENQUANTO o Menu, a Nova partida ou o Ranking estiverem abertos O SISTEMA
   DEVE desenhar a imagem de fundo atrás dos textos, mantendo a disposição
   e os textos da Fase 3. _(verificação: manual)_
2. QUANDO o jogador escolher uma opção do Menu, trocar a dificuldade na
   Nova partida, confirmar a Nova partida ou sair do Ranking O SISTEMA DEVE
   tocar o som "selecionar". _(verificação: manual)_
3. QUANDO o jogador digitar ou apagar letras do nome O SISTEMA NÃO DEVE
   tocar som. _(verificação: manual)_

### R8 — Semente reproduzível

**Critérios de aceitação:**

1. QUANDO o jogo for aberto com `-Dmissaomarte.semente=N` (N inteiro) O
   SISTEMA DEVE usar N como semente do sorteio, de modo que a mesma semente
   e a mesma sequência de teclas produzam o mesmo mapa e os mesmos
   movimentos dos inimigos. _(verificação: teste com duas sessões + manual)_
2. QUANDO a propriedade não existir O SISTEMA DEVE sortear como na Fase 3.
   _(verificação: teste)_
3. SE o valor não for um inteiro válido ENTÃO O SISTEMA DEVE registrar um
   aviso no log com o valor recebido e sortear como na Fase 3, sem fechar o
   jogo. _(verificação: teste)_
4. QUANDO o jogo for aberto com `./gradlew :lwjgl3:run
   -Pmissaomarte.semente=N` O SISTEMA DEVE repassar N à JVM do jogo como
   `-Dmissaomarte.semente=N`, com o comando documentado no README.
   _(verificação: manual, duas aberturas com a mesma semente mostram o
   mesmo mapa)_

### R9 — Separação e gate

**Critérios de aceitação:**

1. O SISTEMA DEVE manter `domain`, `application` e `infrastructure` (main e
   test) idênticos byte a byte aos da Fase 3. _(verificação: `diff -r`)_
2. O SISTEMA DEVE ter os testes de R1 a R6 e R8 rodando sem janela, sem
   OpenGL e sem áudio (nenhum teste instancia `Recursos` nem
   `AssetManager`). _(verificação: inspeção + gate)_
3. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar.
   _(verificação: gate)_

### R10 — README e tutorial da Fase 4

**Critérios de aceitação:**

1. O SISTEMA DEVE ter `fases/fase-04-interface-grafica/README.md` com: como
   rodar o jogo, como gerar os assets (`./gradlew :tools:gerarAssets`),
   como rodar os testes, como usar a semente e os controles.
   _(verificação: inspeção)_
2. O SISTEMA DEVE ter `docs/fase-04-tutorial.md` (no mesmo lugar do da
   Fase 3) com, no mínimo, as seções: módulos (`core`, `lwjgl3`, `tools`);
   assets gerados por programa e commitados; catálogo sem libGDX e
   `Recursos` com `AssetManager` (carga única e `dispose()`); símbolo →
   imagem sem `instanceof`; desenho fora da tela e HUD só lendo a sessão;
   movimento suave com a lógica do tempo sem libGDX; eventos do turno →
   som e efeitos; música; semente; mudanças em relação à Fase 3; testes;
   cortes de escopo e limitações reais (pelo menos 3).
   _(verificação: inspeção)_
3. O SISTEMA DEVE citar arquivos e classes que existem de fato na Fase 4.
   _(verificação: revisão)_
