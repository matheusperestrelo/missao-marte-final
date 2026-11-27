> **Concluída em 2026-11-27.** Diferenças do planejado: o `CREDITOS.md` é escrito pelo próprio gerador (`GeradorAssets.creditos()`) e o `CreditosTest` também confere que o arquivo commitado é igual ao gerado (desvio aceito na revisão 02-r1); `Recursos` não tem teste automatizado (exige OpenGL) e foi conferido só no QA manual. Ficaram como sugestões abertas para a spec 16 (ver `reviews/final.md`): `Recursos.quadro()` cria um `TextureRegion` por chamada, `desenharGrade()` pinta fundo e linhas cobertos pelo terreno, `Recursos.som(Som.MUSICA)` falha em tempo de execução (usar `musica()`), nomes de `test_R2_2`/`test_R2_3` defasados, README da Fase 4 ainda descreve a Fase 3 e testes de R3.1 e do canto transparente cobrem só parte dos casos.

# Requisitos: fase4-assets-recursos

**Issue:** a criar ("[Fase 4] Assets e recursos") · **Trilha:** C · **Fase:** 4 · **Depende de:** 11 (`fase3-partida`)
**Branch sugerida:** `feat/fase4-assets-recursos` · **PR:** "Closes #<issue>"

## Contexto

A Fase 3 desenha o jogo com retângulos coloridos e não tem som. Esta spec
cria a Fase 4 em `fases/fase-04-interface-grafica/` a partir da Fase 3 e
produz a base gráfica e sonora dela: um gerador próprio de imagens e sons,
os arquivos gerados no projeto, o catálogo desses arquivos, o carregamento
único deles no jogo e a partida já desenhada com imagens no lugar dos
retângulos. Animações, HUD, efeitos, música tocando e movimento suave ficam
para a spec 16 (`fase4-interface-grafica`), que só consome o que esta spec
entrega.

Base: `ESCOPO-PROJETO.md`, seção 8, "Fase 4" (assets e recursos), o
briefing da Fase 4 e o resumo do alinhamento de 2026-11-26 (gerador em
módulo `tools`, sons sintetizados, música de ~8 s, `Recursos` nesta spec).

## Fora de escopo

- Tocar sons e música, animações em laço, explosão, HUD, caixa final,
  movimento suave, nave girando, fundo nas telas e semente: spec 16.
- Asset de brilho (embarque/entrega): cortado do projeto junto com o efeito.
- Arte ou sons de terceiros (Piskel, bancos livres): decidido que tudo é
  gerado por programa próprio, sem licença de terceiros.
- Tecla `M` e `-Dmissaomarte.semaudio`: cortados do projeto. Por isso os
  testes **nunca** instanciam `Recursos` nem `AssetManager`.
- Qualquer mudança de regra de jogo, de ranking, de navegação ou nos
  eventos do turno. `domain` e `application` da Fase 4 são cópias exatas da
  Fase 3.
- Alterar as pastas das Fases 0 a 3 (e da Fase 5).
- README e tutorial da Fase 4: última tarefa da spec 16.

## Requisitos

### R1 — Fase 4 criada a partir da Fase 3

**História:** Como integrante, quero a Fase 4 começando de uma cópia da
Fase 3, para evoluir a interface sem mexer na fase já entregue.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter a pasta `fases/fase-04-interface-grafica/` com o
   mesmo conteúdo versionado de `fases/fase-03-libgdx/` (build, Wrapper,
   módulos `core` e `lwjgl3`, testes e `.gitignore`), sem `ranking.json`,
   `build/` e `.gradle/`. _(verificação: inspeção + `git diff --stat`)_
2. O SISTEMA DEVE ter os pacotes `domain` e `application` (inclusive
   `application.ranking`) e `infrastructure` da Fase 4 idênticos byte a
   byte aos da Fase 3, em `src/main` e em `src/test`.
   _(verificação: `diff -r` entre as pastas)_
3. O SISTEMA NÃO DEVE alterar nada em `fases/fase-00-console/`,
   `fases/fase-01-refatoracao/`, `fases/fase-02-gradle/`,
   `fases/fase-03-libgdx/` nem `fases/fase-05-servidor-ranking/`.
   _(verificação: `git diff` da branch)_
4. QUANDO o jogador rodar `./gradlew :lwjgl3:run` na pasta da Fase 4 O
   SISTEMA DEVE abrir a janela 800×600 com o mesmo fluxo da Fase 3 (Menu,
   Nova partida, Partida, Ranking). _(verificação: manual)_

### R2 — Gerador de assets

**História:** Como integrante, quero gerar todas as imagens e sons com um
comando, para não depender de arte de terceiros nem de licença.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter um módulo Gradle `tools` na Fase 4, separado de
   `core` e `lwjgl3`, que não entra no jogo empacotado (o `lwjgl3` não
   depende dele). _(verificação: inspeção do `settings.gradle` e dos
   `build.gradle`)_
2. QUANDO o integrante rodar `./gradlew :tools:gerarAssets` O SISTEMA DEVE
   (re)escrever em `core/assets/` todos os arquivos de imagem e de som do
   catálogo (R3), criando as pastas que faltarem. _(verificação: teste do
   gerador em pasta temporária + manual)_
3. QUANDO o gerador rodar duas vezes O SISTEMA DEVE produzir o mesmo
   resultado: imagens com os mesmos pixels e sons com os mesmos bytes. O
   gerador não usa nada aleatório sem semente fixa. _(verificação: teste)_
4. O SISTEMA DEVE ter os arquivos gerados **commitados** em `core/assets/`
   e iguais ao que o gerador produz hoje (mesmos pixels nas imagens, mesmos
   bytes nos sons), para que o jogo rode sem executar o gerador.
   _(verificação: teste que gera em pasta temporária e compara com
   `core/assets/`)_
5. O SISTEMA DEVE ter em `core/assets/` um arquivo de créditos que diga
   que todas as imagens e sons foram gerados por programa próprio (módulo
   `tools`), com o comando para gerá-los de novo, e que liste cada arquivo.
   _(verificação: inspeção + teste de que cada arquivo do catálogo aparece
   nos créditos)_

### R3 — Imagens

**História:** Como jogador, quero ver a nave, a base, os passageiros e os
perigos como imagens, para reconhecer cada elemento sem ler símbolos.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter uma imagem PNG para cada item abaixo, com as
   dimensões indicadas (célula = 48 px, ícone do HUD = 24 px):

   | Item | Quadros | Dimensões do arquivo |
   |---|---|---|
   | Fundo | 1 | 800×600 |
   | Terreno da célula | 1 | 48×48 |
   | Nave | 1 | 48×48 |
   | Propulsor | 2, em laço | 96×48 |
   | Base | 2, em laço | 96×48 |
   | Inimigo | 2, em laço | 96×48 |
   | Asteroide | 1 | 48×48 |
   | Professor | 1 | 48×48 |
   | Engenheiro | 1 | 48×48 |
   | Astronauta | 1 | 48×48 |
   | Imagem genérica | 1 | 48×48 |
   | Explosão | 4, uma vez | 192×48 |
   | Vida cheia, vida vazia | 1 cada | 24×24 |
   | Vaga ocupada, vaga livre | 1 cada | 24×24 |

   _(verificação: teste que lê cada PNG de `core/assets/` e confere
   existência e dimensões)_
2. O SISTEMA DEVE guardar cada animação como **uma faixa horizontal** de
   quadros de mesmo tamanho: largura do arquivo = quadros × largura do
   quadro, altura = altura do quadro. _(verificação: teste)_
3. O SISTEMA DEVE desenhar os elementos com fundo transparente (canal
   alfa), para que o terreno apareça em volta deles. O fundo e o terreno
   são opacos. _(verificação: teste de pixel de canto + manual)_
4. O SISTEMA DEVE ter os quadros de cada animação diferentes entre si,
   para que a animação seja visível. _(verificação: teste)_

### R4 — Sons e música

**História:** Como jogador, quero sons distintos para cada ação e uma
música de fundo, para perceber o que aconteceu sem olhar o painel.

**Critérios de aceitação:**

1. O SISTEMA DEVE ter um arquivo WAV (PCM, mono, 22 050 Hz, 16 bits) para
   cada som: mover, selecionar, embarcar, entregar, colisão, vitória,
   derrota e música. _(verificação: teste que lê o cabeçalho de cada WAV)_
2. O SISTEMA DEVE ter cada efeito com duração entre 0,05 s e 2 s, e a
   música com duração entre 7 s e 9 s. _(verificação: teste)_
3. O SISTEMA DEVE ter os sete efeitos diferentes entre si (nenhum par com
   os mesmos bytes de áudio). _(verificação: teste)_
4. O SISTEMA DEVE começar e terminar cada som e a música em silêncio
   (primeira e última amostra iguais a zero), para não estalar e para que
   o laço da música emende sem clique. _(verificação: teste)_
5. O SISTEMA DEVE gerar a música com amplitude máxima menor que a dos
   efeitos, para que ela não cubra os efeitos. _(verificação: teste do
   pico de amplitude)_

### R5 — Catálogo e símbolo → imagem

**Critérios de aceitação:**

1. O SISTEMA DEVE ter um catálogo de imagens e um de sons **sem nenhum
   import de `com.badlogic`**, cada item com o caminho do arquivo em
   `core/assets/` e, nas imagens, o número de quadros e as dimensões do
   quadro. O gerador e os testes usam esse mesmo catálogo.
   _(verificação: inspeção + gate)_
2. QUANDO o símbolo for `@`, `P`, `E`, `T`, `#`, `X` ou `L` O SISTEMA DEVE
   devolver, respectivamente, a imagem da nave, do professor, do
   engenheiro, do astronauta, do asteroide, do inimigo e da base.
   _(verificação: teste, um caso por símbolo)_
3. SE o símbolo não for nenhum dos de R5.2 ENTÃO O SISTEMA DEVE devolver a
   imagem genérica, sem lançar exceção. _(verificação: teste)_
4. O SISTEMA DEVE escolher a imagem só pelo símbolo do elemento, sem
   `instanceof` nem `switch` sobre a classe do elemento.
   _(verificação: revisão + `grep instanceof` na camada `gdx`)_

### R6 — Recursos carregados uma vez

**Critérios de aceitação:**

1. QUANDO o jogo abrir O SISTEMA DEVE carregar todas as imagens e sons do
   catálogo uma única vez, antes da primeira tela, por um objeto de
   recursos criado pelo jogo (sem campo ou método `static` que guarde
   recursos). _(verificação: revisão + log de carga + manual)_
2. QUANDO o jogo fechar O SISTEMA DEVE liberar todos os recursos
   carregados. _(verificação: revisão do `dispose()`)_
3. O SISTEMA NÃO DEVE criar nem liberar texturas ou sons dentro das telas:
   as telas só pedem ao objeto de recursos. _(verificação: revisão +
   `grep "new Texture\|new Sound\|newSound\|newMusic"` fora do objeto de
   recursos)_
4. SE um arquivo do catálogo faltar em `core/assets/` ENTÃO O SISTEMA DEVE
   falhar na abertura com uma mensagem que diga o caminho do arquivo
   (erro do carregamento, sem tela preta silenciosa). _(verificação:
   manual, renomeando um arquivo)_

### R7 — Partida desenhada com imagens

**História:** Como jogador, quero ver a partida com as imagens novas já
nesta entrega, para conferir os assets dentro do jogo.

**Critérios de aceitação:**

1. ENQUANTO a partida estiver aberta O SISTEMA DEVE desenhar cada célula
   da grade com a imagem do terreno e cada elemento com a imagem escolhida
   pelo seu símbolo (R5), no primeiro quadro quando for animação, no lugar
   dos retângulos coloridos e dos símbolos escritos da Fase 3.
   _(verificação: manual)_
2. O SISTEMA DEVE continuar desenhando a nave por cima dos outros
   elementos na mesma célula e mantendo a orientação da grade da Fase 3
   (menor `y` no topo). _(verificação: manual + testes da geometria
   copiados da Fase 3)_
3. O SISTEMA DEVE manter o painel, as mensagens e o fim da partida da
   Fase 3 sem mudança. _(verificação: testes copiados da Fase 3 + manual)_

### R8 — Testes e gate

**Critérios de aceitação:**

1. O SISTEMA DEVE ter os testes de R2 a R5 rodando sem janela, sem OpenGL
   e sem áudio. _(verificação: inspeção + gate)_
2. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar, incluindo o
   build da Fase 4 com todos os testes copiados da Fase 3.
   _(verificação: gate)_
