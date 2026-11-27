# Tarefas: fase4-assets-recursos

- [ ] 1. Criar `fases/fase-04-interface-grafica/` copiando a Fase 3
  - status: todo
  - Copiar tudo o que é versionado em `fases/fase-03-libgdx/` (sem `ranking.json`, `build/`, `.gradle/`). No `README.md` copiado, trocar só o título e o nome da pasta para Fase 4. Nenhum `.java` muda.
  - Conferir com `diff -r` que `domain`, `application` e `infrastructure` (main e test) são idênticos aos da Fase 3, registrar a saída nas notas, rodar o `check.sh` e abrir a janela com `./gradlew :lwjgl3:run`.
  - Commit sugerido: `feat: cria a fase 4 a partir da fase 3`.
  - _Requisitos: R1, R8.2_
  - notas:

- [ ] 2. Criar o catálogo `Imagem` com símbolo → imagem e o módulo `tools` gerando e commitando as imagens
  - status: todo
  - TDD do `Imagem` (`ImagemTest`: R5.2, R5.3) em `core/.../gdx/catalogo/`, sem `com.badlogic`.
  - Módulo `tools` (`settings.gradle`, `tools/build.gradle` com `gerarAssets` e `systemProperty 'missaomarte.assets'`), `GeradorAssets` e `GeradorImagens`. Rodar `./gradlew :tools:gerarAssets` e commitar `core/assets/imagens/*.png` e `core/assets/CREDITOS.md` (nesta tarefa, só com as imagens).
  - Testes em `tools`: `ImagensAssetsTest` (R3), `GeradorAssetsTest` só para imagens (R2.2 a R2.4) e `CreditosTest` (R2.5).
  - _Requisitos: R2, R3, R5, R8.1_
  - notas:

- [ ] 3. Carregar os recursos uma vez no jogo e desenhar a partida com as imagens
  - status: todo
  - `Recursos` (`AssetManager`, sem `static`) só com as imagens nesta tarefa; `MissaoMarteGame` cria e carrega em `create()` antes da primeira tela e libera em `dispose()`; `lwjgl3/build.gradle` com `core/assets` nos resources.
  - `TelaPartida` desenha `TERRENO` em cada célula e o quadro 0 de `Imagem.doSimbolo(elemento.simbolo())`, nave por último; remove retângulos, símbolos escritos e `CoresElemento` se ficar sem uso. Painel e fim iguais.
  - Conferir na janela: imagens no lugar dos retângulos, nave sobre a base no início, `W/↑` sobe na tela; renomear um PNG e ver a mensagem de erro com o caminho (R6.4); `grep instanceof` e `grep "new Texture"` na camada `gdx`.
  - _Requisitos: R5.4, R6, R7_
  - notas:

- [ ] 4. Criar o catálogo `Som` e gerar, commitar e carregar os sons e a música
  - status: todo
  - `Som` em `gdx/catalogo/`, sem `com.badlogic`. `GeradorSons` (efeitos sintetizados e música de ~8 s, WAV à mão). Rodar `gerarAssets` de novo e commitar `core/assets/sons/*.wav` e o `CREDITOS.md` atualizado.
  - Testes em `tools`: `SonsAssetsTest` (R4); estender `GeradorAssetsTest` e `CreditosTest` aos sons.
  - `Recursos` passa a carregar os `Sound` e a `Music`, com `som(Som)` e `musica()`. Nada toca ainda (spec 16). Conferir no log que todos os arquivos foram carregados.
  - _Requisitos: R2, R4, R5.1, R6.1, R6.2, R8_
  - notas:
