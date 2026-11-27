# Tarefas: fase4-interface-grafica

- [ ] 1. Desenhar a partida pelo `MundoRenderer`, com base, inimigos e propulsor animados e a nave girando pela direção
  - status: todo
  - Fatia vertical: `RelogioAnimacao` e `DirecaoVisual` com TDD em `gdx/visual/`; `ControlePartida` guarda `ultimaAcao()`; `MundoRenderer` assume o desenho do mapa que a spec 15 deixou na `TelaPartida` (terreno, elementos pelo símbolo, nave por último com propulsor atrás, girados).
  - Conferir na janela: animações em laço, nave começa para cima, gira a cada movimento (também na borda), `E` não gira.
  - _Requisitos: R1, R9_
  - notas:

- [ ] 2. Fazer a nave e os inimigos deslizarem entre as células sem a lógica esperar o desenho
  - status: todo
  - `PosicaoVisual` e `MovimentoSuave` com TDD; `MapaGeometria` com `x(float)`/`y(float)` e testes fracionários; `MundoRenderer` passa a usar a posição desenhada.
  - Conferir na janela: teclas rápidas executam todos os turnos, e o desenho alcança sem passar do alvo.
  - _Requisitos: R2, R9_
  - notas:

- [ ] 3. Responder aos eventos do turno com o efeito sonoro e a explosão
  - status: todo
  - `ReacaoTurno` e a parte de explosão do `EfeitosPartida` com TDD; a `TelaPartida` toca `recursos.som(...)` e o `MundoRenderer` desenha a explosão por cima; nave some ao fim da explosão na destruição.
  - Conferir na janela: sons de mover, embarcar, entregar e colisão; recusa de embarque sem som; explosão com nave visível; derrota por colisão.
  - _Requisitos: R3, R9_
  - notas:

- [ ] 4. Tocar a música em laço, pausar no fim e mostrar a caixa final com o som de vitória ou derrota
  - status: todo
  - `Trilha`, `ControleMusica`, `TrilhaGdx`, `TextoCaixaFinal` e o resto do `EfeitosPartida` (`caixaVisivel`, `consumirSomFinal`) com TDD; `ControlePartida` guarda a causa; `MissaoMarteGame` liga a música ao Menu; `TelaPartida` ignora `Enter` sem caixa; `HudRenderer` (só a caixa, nesta tarefa).
  - Conferir na janela: música desde o Menu sem recomeçar entre telas; vitória (caixa, som, Top 10, `Enter` → Ranking) e derrota por pontuação e por colisão (caixa depois da explosão, `Enter` → Menu, música volta); abandono mantém a música.
  - _Requisitos: R4, R5, R9_
  - notas:

- [ ] 5. Montar o HUD com barra superior, painel lateral e rodapé
  - status: todo
  - `TextoHud` com TDD (`GameSession` real, `Clock` fixo); `HudRenderer` desenha barra (ícones de vida, pontos, tempo), painel (textos, ícones de vaga, mensagens e alerta da Fase 3) e rodapé; remove o painel antigo da `TelaPartida` (o `TextoPainel` da Fase 3 sai se ficar sem uso, com o teste).
  - Conferir na janela: nada sobreposto em 800×600, tempo parado no fim, acentos completos (se faltar glifo, registrar nas notas e avisar).
  - _Requisitos: R6, R9_
  - notas:

- [ ] 6. Desenhar o fundo e tocar "selecionar" no Menu, na Nova partida e no Ranking
  - status: todo
  - `TelaBase.desenharFundo()` e `tocarSelecionar()`; chamadas nas três telas, sem mudar disposição nem textos; sem som ao digitar ou apagar o nome.
  - _Requisitos: R7_
  - notas:

- [ ] 7. Aceitar a semente reproduzível `-Dmissaomarte.semente=N`
  - status: todo
  - `Semente` com TDD (`SementeTest`: reprodução com duas sessões, ausente, inválido com aviso); `Lwjgl3Launcher` usa `Semente.random(...)`; `lwjgl3/build.gradle` repassa `-Pmissaomarte.semente` ao `run`.
  - Conferir: duas aberturas com `./gradlew :lwjgl3:run -Pmissaomarte.semente=42` mostram o mesmo mapa; valor inválido avisa e abre normalmente.
  - _Requisitos: R8, R9_
  - notas:

- [ ] 8. Escrever o README da Fase 4 e o `docs/fase-04-tutorial.md`
  - status: todo
  - README em `fases/fase-04-interface-grafica/README.md` (R10.1) e tutorial em `docs/fase-04-tutorial.md` (R10.2), no padrão de seções do `docs/fase-03-tutorial.md`, citando só arquivos que existem na Fase 4. Só documentação, nenhum código.
  - Commit sugerido: `docs: adiciona readme e tutorial da fase 4`.
  - _Requisitos: R10_
  - notas:
