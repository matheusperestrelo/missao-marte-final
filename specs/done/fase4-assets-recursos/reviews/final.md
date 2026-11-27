# Verificação final — fase4-assets-recursos

**Veredito:** APROVADA
**Branch:** feat/fase4-assets-recursos
**Commits da spec:** 9 (8b6a0e7..6cfbb3e)

Os caminhos abaixo são relativos a `fases/fase-04-interface-grafica/`, salvo
quando indicado. A verificação foi feita em modo subagente, sem acesso à janela.
Por isso os critérios de verificação manual (R1.4, R6.4, R7.1 e a parte manual
de R6.1, R7.2 e R7.3) dependem do roteiro de QA abaixo.

## Cobertura

| Requisito | Critério | Teste(s) / verificação | Situação |
|---|---|---|---|
| R1 | 1 | inspeção: `diff -rq fases/fase-03-libgdx fases/fase-04-interface-grafica -x build -x .gradle -x ranking.json` mostra só README, `settings.gradle`, `lwjgl3/build.gradle`, `MissaoMarteGame`, `TelaPartida`, `CoresElemento` removido e os itens novos (`tools/`, `core/assets/`, `catalogo/`, `Recursos`). `.gitignore` igual ao da Fase 3 | coberto (inspeção) |
| R1 | 2 | `diff -r` de `domain`, `application` (com `ranking`) e `infrastructure`, em `src/main` e `src/test`: as 6 comparações sem diferença | coberto (diff) |
| R1 | 3 | `git diff --name-only main...HEAD`: nenhum arquivo fora de `fases/fase-04-interface-grafica/` e `specs/` | coberto (git diff) |
| R1 | 4 | manual | **QA** (passos 1 e 2) |
| R2 | 1 | inspeção: `settings.gradle` inclui `tools`; `lwjgl3/build.gradle` depende só de `:core` | coberto (inspeção) |
| R2 | 2 | `GeradorAssetsTest.test_R2_2_gera_todas_as_imagens_do_catalogo_criando_as_pastas` (imagens e sons, em pasta que ainda não existe) | coberto (+ QA passo 9) |
| R2 | 3 | `GeradorAssetsTest.test_R2_3_duas_execucoes_produzem_os_mesmos_pixels` (pixels nos PNG, bytes nos WAV) | coberto |
| R2 | 4 | `GeradorAssetsTest.test_R2_4_assets_commitados_sao_iguais_ao_que_o_gerador_produz` | coberto |
| R2 | 5 | `CreditosTest.test_R2_5_creditos_listam_cada_arquivo_e_o_comando_de_geracao`, `CreditosTest.test_R2_5_creditos_commitados_sao_os_que_o_gerador_escreve` | coberto |
| R3 | 1 | `ImagensAssetsTest.test_R3_1_cada_imagem_existe_com_as_dimensoes_do_catalogo`, `test_R3_1_dimensoes_da_tabela_da_spec`, `test_R3_1_todo_png_da_pasta_pertence_ao_catalogo` | coberto |
| R3 | 2 | `ImagensAssetsTest.test_R3_2_animacao_e_uma_faixa_horizontal_de_quadros_iguais_em_tamanho` | coberto |
| R3 | 3 | `ImagensAssetsTest.test_R3_3_elementos_tem_canto_transparente_e_fundo_e_terreno_sao_opacos` | coberto (+ QA passo 4) |
| R3 | 4 | `ImagensAssetsTest.test_R3_4_quadros_de_cada_animacao_sao_diferentes_entre_si` | coberto |
| R4 | 1 | `SonsAssetsTest.test_R4_1_cada_som_e_wav_pcm_mono_22050_hz_16_bits`, `test_R4_1_todo_wav_da_pasta_pertence_ao_catalogo`, `test_R4_1_wav_escrito_pode_ser_lido_de_volta` | coberto |
| R4 | 2 | `SonsAssetsTest.test_R4_2_efeitos_duram_de_0_05_a_2_s_e_a_musica_de_7_a_9_s` | coberto |
| R4 | 3 | `SonsAssetsTest.test_R4_3_os_sete_efeitos_sao_diferentes_entre_si` | coberto |
| R4 | 4 | `SonsAssetsTest.test_R4_4_cada_som_comeca_e_termina_em_silencio` | coberto |
| R4 | 5 | `SonsAssetsTest.test_R4_5_musica_tem_pico_menor_que_o_de_todos_os_efeitos` | coberto (+ QA passo 8, escuta) |
| R5 | 1 | inspeção: `grep badlogic core/src/main/java/missaomarte/gdx/catalogo` vazio; `Imagem` e `Som` com caminho, quadros e dimensões; gerador, testes e `Recursos` usam o mesmo enum | coberto (inspeção + gate) |
| R5 | 2 | `ImagemTest.test_R5_2_*` (7 casos, um por símbolo) | coberto |
| R5 | 3 | `ImagemTest.test_R5_3_simbolo_desconhecido_devolve_generica` | coberto |
| R5 | 4 | `grep -rn instanceof core/src/main/java/missaomarte/gdx` vazio; `TelaPartida.java:143` usa só `Imagem.doSimbolo(elemento.simbolo())`; nenhum `switch` sobre a classe do elemento | coberto (revisão + grep) |
| R6 | 1 | revisão: `MissaoMarteGame.create()` faz `new Recursos()` e `carregar()` antes do `setScreen`; `Recursos` sem `static`; log `"<n> arquivos carregados"` | coberto (revisão) + **QA** (passo 1, log de 24) |
| R6 | 2 | revisão: `MissaoMarteGame.dispose()` chama `recursos.dispose()`, que chama `assetManager.dispose()` | coberto (revisão) |
| R6 | 3 | `grep -rnE "new Texture\(\|new Sound\|newSound\|newMusic" core/src/main/java/missaomarte/gdx` vazio | coberto (grep) |
| R6 | 4 | manual | **QA** (passo 6) |
| R7 | 1 | manual (revisão de `TelaPartida.java:130-147` confere a troca) | **QA** (passos 3 e 4) |
| R7 | 2 | `MapaGeometriaTest` (copiado da Fase 3) + revisão da ordem base → demais → nave (`TelaPartida.java:96-107`) | coberto + **QA** (passo 5) |
| R7 | 3 | testes copiados da Fase 3 (`ControlePartidaTest`, `MensagensPainelTest`, `TextoPainelTest`), `core/src/test` idêntico ao da Fase 3 fora de `catalogo/` | coberto + **QA** (passo 5) |
| R8 | 1 | inspeção: nenhum teste de `core/src/test` ou `tools/src/test` cita `Recursos` ou `AssetManager`; os testes só leem arquivos com `ImageIO` e `Wav` | coberto (inspeção + gate) |
| R8 | 2 | `.workflow/check.sh` | coberto |

Nenhuma lacuna. Todo critério com verificação por teste tem teste com o ID e
passando. Os demais estão cobertos pela verificação que a própria spec define
(inspeção, diff, grep, revisão), e os manuais ficam para o QA.

## check.sh

passou (`check.sh: tudo passou`, incluindo `fases/fase-04-interface-grafica: gradlew clean build`, com `:core:test` e `:tools:test`, e as regras de camadas de todas as fases)

## Pendências

- notas das tarefas 1, 3 e 4: nada foi conferido na janela (modo subagente). R1.4, R6.4, R7.1, o log de 24 arquivos e a escuta dos sons ficam para o QA abaixo.
- notas das tarefas 2 e 4: a arte e os sons são simples, feitos "a olho" e "a ouvido". Vale julgar no QA se são aceitáveis para a spec 16.
- notas da tarefa 2 (desvio do design, aceito na revisão 02-r1): `CREDITOS.md` é escrito pelo próprio gerador (`GeradorAssets.creditos()`), e `CreditosTest` também confere que o arquivo commitado é igual ao gerado.
- revisão 03-r1, sugestão 1: `Recursos.quadro()` (`core/src/main/java/missaomarte/gdx/Recursos.java:43-46`) cria um `TextureRegion` novo a cada chamada, mais de 121 por quadro na `TelaPartida`. Guardar os recortes em `carregar()` ajuda a spec 16, que vai animar os quadros.
- revisão 03-r1, sugestão 2, e notas da tarefa 3: `TelaPartida.desenharGrade()` ainda pinta o fundo e as linhas, que ficam cobertos pelo terreno opaco. Pode ser limpo na spec 16.
- revisão 04-r1, sugestão 2: `Recursos.som(Som.MUSICA)` falha em tempo de execução, porque a música foi carregada como `Music`. A spec 16 deve usar `musica()`, ou o método deve rejeitar `MUSICA` com uma mensagem clara.
- revisão 04-r1, sugestão 1: os nomes `test_R2_2_gera_todas_as_imagens_...` e `test_R2_3_..._mesmos_pixels` já cobrem os sons também. Podem ser renomeados numa próxima mexida.
- revisão 01-r1, sugestão 2: `README.md` da Fase 4 ainda descreve a Fase 3 (texto e link para `docs/fase-03-tutorial.md`). Isso está previsto para a última tarefa da spec 16.
- revisão 02-r1, sugestões 1 e 2: `test_R3_1_dimensoes_da_tabela_da_spec` fixa só parte da tabela de R3.1, e o canto transparente é conferido só no quadro 0. Reforçar os testes é opcional.

## Roteiro de QA

Abra um Terminal no macOS e rode tudo a partir da pasta da fase. Os passos 1 a
7 usam a janela; os passos 8 e 9 rodam só no terminal.

```bash
cd <raiz do repositório>/fases/fase-04-interface-grafica
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
# (alternativa: export JAVA_HOME=$(/usr/libexec/java_home -v 17))
java -version   # deve mostrar 17.x
git status --short   # deve sair vazio antes de começar
```

1. Rode `./gradlew :lwjgl3:run 2>&1 | tee /tmp/fase4-run.log` → **esperado:** a janela "Missão Marte Unifor" abre em 800×600 no Menu. Depois de fechar a janela, `grep "arquivos carregados" /tmp/fase4-run.log` mostra `Recursos: 24 arquivos carregados` (16 imagens + 8 sons), uma única vez e antes do log da `TelaMenu` (R1.4, R6.1).
2. No Menu, tecle `2` → Ranking abre (vazio ou com as entradas do `ranking.json` desta pasta). Volte ao Menu (Esc ou a tecla indicada na tela). Tecle `1` → Nova partida: digite um nome, escolha a dificuldade com ↑/↓ e confirme com Enter → a Partida abre. Esse é o mesmo fluxo da Fase 3 (R1.4).
3. Na Partida, olhe a grade → **esperado:** cada célula mostra a imagem do terreno, e não há retângulos coloridos nem letras (`@`, `P`, `E`, `T`, `#`, `X`, `L`) desenhados na grade. A nave, a base, os passageiros (professor, engenheiro, astronauta), os asteroides e os inimigos aparecem como imagens distintas (R7.1, R3.1).
4. Ainda na grade → **esperado:** em volta de cada elemento aparece o terreno, sem nenhum quadrado preto ou branco de fundo, porque os elementos são transparentes. A base e o inimigo aparecem parados no primeiro quadro (a animação é da spec 16) (R3.3, R7.1).
5. No início da partida, a nave aparece por cima da base, na mesma célula. Tecle `W` ou `↑` → a nave sobe na tela (o menor `y` fica no topo). `S`/`↓`, `A`/`←` e `D`/`→` movem para baixo, para a esquerda e para a direita. Embarque um passageiro com `E`, entregue-o na base e depois perca ou vença a partida → **esperado:** o painel, as mensagens e o fim da partida estão iguais aos da Fase 3. Tecle `Q`/Esc para sair da partida e `4` no Menu para fechar o jogo, que deve fechar sem erro no terminal (R7.2, R7.3, R6.2).
6. Arquivo faltando (R6.4):
   ```bash
   mv core/assets/imagens/nave.png core/assets/imagens/nave.png.bak
   ./gradlew clean :lwjgl3:run
   ```
   → **esperado:** o jogo não chega a uma tela preta parada. A abertura falha com uma exceção do libGDX no terminal (`GdxRuntimeException`, algo como `File not found: imagens/nave.png (Internal)` ou `Couldn't load dependencies of asset: imagens/nave.png`), e a mensagem traz o caminho `imagens/nave.png`. O `clean` garante que a cópia antiga em `lwjgl3/build/resources` não seja usada. Depois restaure o arquivo:
   ```bash
   mv core/assets/imagens/nave.png.bak core/assets/imagens/nave.png
   ./gradlew clean :lwjgl3:run   # volta a abrir normalmente
   git status --short            # deve sair vazio
   ```
7. (Opcional) Repita o passo 6 com `core/assets/sons/mover.wav` → **esperado:** a mesma falha, com o caminho `sons/mover.wav` (R6.4).
8. Escute os sons. Nada toca dentro do jogo nesta spec (a reprodução é da spec 16), então use o `afplay` do macOS:
   ```bash
   for s in mover selecionar embarcar entregar colisao vitoria derrota; do echo "$s"; afplay core/assets/sons/$s.wav; sleep 0.5; done
   afplay core/assets/sons/musica.wav
   ```
   → **esperado:** os sete efeitos são audíveis e diferentes entre si, curtos (até 2 s) e sem estalo no começo ou no fim. A colisão soa como ruído. A música dura cerca de 8 s e soa mais baixa que os efeitos. Para ouvir a emenda do laço: `for i in 1 2 3; do afplay core/assets/sons/musica.wav; done` → sem clique na volta, salvo a pequena pausa que o próprio `afplay` faz entre execuções (R4.2 a R4.5).
9. Gerador (R2.2, R2.4, R2.5):
   ```bash
   ./gradlew :tools:gerarAssets
   git status --short core/assets   # deve sair vazio: o gerado é igual ao commitado
   cat core/assets/CREDITOS.md       # diz "programa próprio", traz o comando e lista os 24 arquivos
   open core/assets/imagens          # (opcional) confira as imagens no Finder/Pré-visualização
   ```
   Se o `git status` mostrar diferença só em PNG, rode `./gradlew :tools:test`. Se esse comando passar, a diferença é só de compressão e os pixels são iguais (ver design). Nesse caso, descarte a diferença com `git checkout -- core/assets`.

## Próximos passos

- APROVADA: QA pelo usuário com o roteiro acima, merge e conclusão da spec (active/ → done/).
- As pendências acima são sugestões. Elas não exigem tarefa nova nesta spec, e as que tocam `Recursos` e `TelaPartida` cabem na spec 16 (`fase4-interface-grafica`).
