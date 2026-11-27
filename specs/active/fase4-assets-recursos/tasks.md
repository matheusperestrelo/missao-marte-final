# Tarefas: fase4-assets-recursos

- [x] 1. Criar `fases/fase-04-interface-grafica/` copiando a Fase 3
  - status: done
  - Copiar tudo o que é versionado em `fases/fase-03-libgdx/` (sem `ranking.json`, `build/`, `.gradle/`). No `README.md` copiado, trocar só o título e o nome da pasta para Fase 4. Nenhum `.java` muda.
  - Conferir com `diff -r` que `domain`, `application` e `infrastructure` (main e test) são idênticos aos da Fase 3, registrar a saída nas notas, rodar o `check.sh` e abrir a janela com `./gradlew :lwjgl3:run`.
  - Commit sugerido: `feat: cria a fase 4 a partir da fase 3`.
  - _Requisitos: R1, R8.2_
  - notas: Pasta copiada de `git ls-files fases/fase-03-libgdx` (79 arquivos, sem ranking.json/build/.gradle); README: título e 2 caminhos `fase-03-libgdx` trocados por Fase 4 / `fase-04-interface-grafica`. `diff -r` de main e test em domain, application e infrastructure: sem saída (idênticos). `diff -rq` da pasta toda: só README.md difere. check.sh passou (inclui build/testes da fase 4). Não foi possível abrir a janela (`./gradlew :lwjgl3:run`) em modo subagente: conferência manual fica para o QA.

- [x] 2. Criar o catálogo `Imagem` com símbolo → imagem e o módulo `tools` gerando e commitando as imagens
  - status: done
  - TDD do `Imagem` (`ImagemTest`: R5.2, R5.3) em `core/.../gdx/catalogo/`, sem `com.badlogic`.
  - Módulo `tools` (`settings.gradle`, `tools/build.gradle` com `gerarAssets` e `systemProperty 'missaomarte.assets'`), `GeradorAssets` e `GeradorImagens`. Rodar `./gradlew :tools:gerarAssets` e commitar `core/assets/imagens/*.png` e `core/assets/CREDITOS.md` (nesta tarefa, só com as imagens).
  - Testes em `tools`: `ImagensAssetsTest` (R3), `GeradorAssetsTest` só para imagens (R2.2 a R2.4) e `CreditosTest` (R2.5).
  - _Requisitos: R2, R3, R5, R8.1_
  - notas: Catálogo `Imagem` (enum, sem com.badlogic, `doSimbolo` por tabela) e módulo `tools` (`GeradorAssets`, `GeradorImagens` com Java2D, semente fixa; `gerarAssets` grava em `core/assets`). 16 PNG e `CREDITOS.md` (só imagens) gerados e commitados. Testes: `ImagemTest` (core), `ImagensAssetsTest`, `GeradorAssetsTest` (R2.2 a R2.4, só imagens), `CreditosTest` (tools). Decisões fora da spec: `CREDITOS.md` é escrito pelo gerador (`GeradorAssets.creditos()`), e `CreditosTest` também confere que o commitado é igual ao gerado; `GeradorImagens.desenhar` é público. Arte simples a olho, vale conferir no QA. A tarefa 4 deve estender `creditos()` com a seção de sons e regerar. Alfa de canto: teste usa pixel (0,0).

- [x] 3. Carregar os recursos uma vez no jogo e desenhar a partida com as imagens
  - status: done
  - `Recursos` (`AssetManager`, sem `static`) só com as imagens nesta tarefa; `MissaoMarteGame` cria e carrega em `create()` antes da primeira tela e libera em `dispose()`; `lwjgl3/build.gradle` com `core/assets` nos resources.
  - `TelaPartida` desenha `TERRENO` em cada célula e o quadro 0 de `Imagem.doSimbolo(elemento.simbolo())`, nave por último; remove retângulos, símbolos escritos e `CoresElemento` se ficar sem uso. Painel e fim iguais.
  - Conferir na janela: imagens no lugar dos retângulos, nave sobre a base no início, `W/↑` sobe na tela; renomear um PNG e ver a mensagem de erro com o caminho (R6.4); `grep instanceof` e `grep "new Texture"` na camada `gdx`.
  - _Requisitos: R5.4, R6, R7_
  - notas: `Recursos` (AssetManager, sem static; `carregar()` síncrono só com as imagens, log "n arquivos carregados"; `textura`, `quadro(Imagem,i)`, `dispose`) criado em `MissaoMarteGame.create()` antes da primeira tela e liberado em `dispose()`; `getRecursos()`. `TelaPartida` desenha `TERRENO` em cada célula e o quadro 0 de `Imagem.doSimbolo(simbolo)` (ordem base, demais, nave mantida); retângulos, símbolos escritos e `CoresElemento` removidos (sem teste próprio). `lwjgl3/build.gradle` com `core/assets` em resources. `grep instanceof` na camada gdx: vazio; `new Texture`: vazio (só `new TextureRegion` em Recursos). Sem teste novo: `Recursos` exige OpenGL (design). Não verificado em janela (modo subagente): imagens no lugar dos retângulos, nave sobre a base, `W/↑` e erro de arquivo renomeado (R6.4, a mensagem vem do AssetManager) ficam para o QA. `desenharGrade` ainda desenha fundo e linhas sob o terreno (ficam cobertos; limpar é sugestão fora de escopo).

- [ ] 4. Criar o catálogo `Som` e gerar, commitar e carregar os sons e a música
  - status: review
  - `Som` em `gdx/catalogo/`, sem `com.badlogic`. `GeradorSons` (efeitos sintetizados e música de ~8 s, WAV à mão). Rodar `gerarAssets` de novo e commitar `core/assets/sons/*.wav` e o `CREDITOS.md` atualizado.
  - Testes em `tools`: `SonsAssetsTest` (R4); estender `GeradorAssetsTest` e `CreditosTest` aos sons.
  - `Recursos` passa a carregar os `Sound` e a `Music`, com `som(Som)` e `musica()`. Nada toca ainda (spec 16). Conferir no log que todos os arquivos foram carregados.
  - _Requisitos: R2, R4, R5.1, R6.1, R6.2, R8_
  - notas: `Som` (enum, sem com.badlogic), `GeradorSons` (tons senoidais/quadrados com envelope, ruído com semente fixa na colisão, música de 8 s com melodia e baixo, pico 35% contra 80% dos efeitos, 1ª e última amostra zeradas, WAV escrito à mão) e `Wav` (leitura para testes) em `tools`; `GeradorAssets` gera os sons e `creditos()` ganhou a seção Sons. `gerarAssets` rodado: 8 WAV (musica.wav ~345 KB) e `CREDITOS.md` commitados. Testes: `SonsAssetsTest` (R4.1 a R4.5) e extensões a `GeradorAssetsTest` (R2.2 a R2.4) e `CreditosTest` (R2.5) só com laços novos sobre `Som`, sem mexer nos asserts existentes. `Recursos` carrega `Sound` (efeitos) e `Music` (música) e expõe `som(Som)` e `musica()`; nada toca. Sem teste de `Recursos` (OpenGL). Não verificado em janela: o log "n arquivos carregados" deve mostrar 24 (16 imagens + 8 sons) no QA. Sons soam simples, vale ouvir no QA. Nenhum `new Texture`/`newSound`/`newMusic`/`instanceof` na camada gdx.
