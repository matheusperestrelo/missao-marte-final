# Design: fase3-telas-menu

## Abordagem

Cada tela fica em duas partes:

- **Controle** em `missaomarte.gdx`, sem nenhum import de `com.badlogic`.
  Ele guarda o estado da navegação ou do formulário e responde a
  comandos simples (caractere digitado, `Enter`, `Esc`, setas).
- **Tela** em `missaomarte.gdx.telas`, que estende a `TelaBase`.
  Ela converte o evento do libGDX em comando para o controle e desenha o
  estado dele.

Os controles têm teste de unidade, e as telas são verificadas à mão. A
classe do jogo ganha métodos de navegação e cria uma `GameSession` nova a
cada partida. A `TelaInicial` provisória da spec 8 é removida, e o jogo
abre no Menu.

## Componentes afetados

| Arquivo (em `fases/fase-03-libgdx/core/src/main/java/missaomarte/`) | Mudança |
|---|---|
| `gdx/MissaoMarteGame.java` | `create()` abre a `TelaMenu`. Constante `TAMANHO_MAPA = 5` (11×11). Navegação: `irParaMenu()`, `irParaNovaPartida()`, `irParaRanking()` (sem resumo), `irParaRanking(ResumoPartida, boolean entrou)`, `iniciarPartida(String piloto, Dificuldade d)` (cria `new GameSession(fabrica, random, clock)`, chama `iniciar(piloto, d, TAMANHO_MAPA)` e abre a `TelaPartida`), `sair()` (`Gdx.app.exit()`). Um auxiliar `trocarTela(Screen nova)` faz `setScreen(nova)` e depois `dispose()` da anterior |
| `gdx/TelaInicial.java` | Removida |
| `gdx/ControleMenu.java` | Novo, sem libGDX. Construtor `(RankingService ranking)`. `ComandoMenu caractere(char c)`, `ComandoMenu esc()`, `boolean confirmando()`, `Optional<String> mensagem()`. Estados `NORMAL` e `CONFIRMANDO_LIMPEZA`. `'1'`→`NOVA_PARTIDA`, `'2'`→`RANKING`, `'4'`→`SAIR`, `'3'`→ entra em confirmação. Na confirmação: `s`/`S` chama `ranking.limpar()` e põe a mensagem "Ranking limpo." (ou a de erro, com `RankingException`); `n`/`N`/`esc()` põe "Operação cancelada.". Qualquer tecla com mensagem visível apaga a mensagem e depois é tratada normalmente |
| `gdx/ComandoMenu.java` | Enum `NADA`, `NOVA_PARTIDA`, `RANKING`, `SAIR` |
| `gdx/FormularioNovaPartida.java` | Novo, sem libGDX. `MAX_NOME = 20`. Etapas `NOME` e `DIFICULDADE`. `caractere(char c)` (aceita se `!Character.isISOControl(c)`, na etapa NOME e com menos de 20), `apagar()`, `cima()`, `baixo()`, `ResultadoFormulario enter()`, `ResultadoFormulario esc()`, `etapa()`, `nome()`, `opcoes()` (= `List.of(Dificuldade.values())`, na ordem FACIL, MEDIO, DIFICIL), `selecionada()` (começa em `FACIL`). `enter()` no NOME → etapa DIFICULDADE (`CONTINUA`); na DIFICULDADE → `INICIAR`. `esc()` na DIFICULDADE → etapa NOME (`CONTINUA`); no NOME → `VOLTAR_MENU` |
| `gdx/ResultadoFormulario.java` | Enum `CONTINUA`, `INICIAR`, `VOLTAR_MENU` |
| `gdx/TextosRanking.java` | Novo, sem libGDX. `List<String> linhas(List<RankingEntry>)` (linha por entrada, no formato de R6.1, ou a frase de ranking vazio) e `List<String> resumo(ResumoPartida, boolean entrou)` (R6.3) |
| `gdx/telas/TelaMenu.java` | `keyTyped` → `controle.caractere(c)`; `keyDown(ESCAPE)` → `controle.esc()`; executa o `ComandoMenu` pela navegação do jogo; desenha título, opções e a mensagem ou a pergunta de confirmação |
| `gdx/telas/TelaNovaPartida.java` | `keyTyped` → `caractere` (filtra `\b`, `\r`, `\n`); `keyDown`: `BACKSPACE` → `apagar`, `UP` → `cima`, `DOWN` → `baixo`, `ENTER` → `enter`, `ESCAPE` → `esc`. Em `INICIAR` chama `game.iniciarPartida(nome, selecionada)`; em `VOLTAR_MENU` chama `game.irParaMenu()`. Desenha o nome com cursor, a dica e a lista com `>` na selecionada. A etapa em foco aparece destacada |
| `gdx/telas/TelaPartida.java` | Provisória (R5): texto "Partida iniciada: <piloto> - <dificuldade>" lido da `GameSession`; `Esc` → Menu. A spec 11 reescreve |
| `gdx/telas/TelaRanking.java` | Recebe `Optional` do resumo e o `entrou`. Lê `ranking.top10()` ao ser mostrada. Desenha título, resumo (se houver) e linhas do `TextosRanking`. `ENTER`/`ESCAPE` → Menu |

Os testes ficam em `core/src/test/java/missaomarte/gdx/`:
`ControleMenuTest`, `FormularioNovaPartidaTest` e `TextosRankingTest`.
Para o `ControleMenu`, usar um `RankingService` real com um
`RankingRepository` em memória e outro que falha, definidos no próprio
pacote de teste. Os dublês da spec 9 ficam em outro pacote e são
package-private.

## Fluxo

```
create() → TelaMenu
  1 → TelaNovaPartida ─ nome ─Enter→ dificuldade ─Enter→ game.iniciarPartida → TelaPartida (provisória) ─Esc→ Menu
                         │  Esc → Menu        │ Esc → nome
  2 → TelaRanking(sem resumo) ─Enter/Esc→ Menu
  3 → "Limpar o ranking? S confirma, N ou Esc cancela"
        S → ranking.limpar() → "Ranking limpo."  (falha → "Não foi possível limpar o ranking: <msg>")
        N/Esc → "Operação cancelada."
  4 → Gdx.app.exit()
```

## Decisões e alternativas descartadas

- **Decisão:** dificuldade por setas e `Enter`, começando em Fácil e sem
  dar a volta nas pontas. É mais previsível e mais simples de testar.
  **Alternativa descartada:** teclas `1`/`2`/`3`.
- **Decisão:** mapa fixo em 11×11 (`TAMANHO_MAPA = 5`, uma constante na
  classe do jogo). **Alternativa descartada:** perguntar o tamanho. O
  escopo pede "nome + dificuldade" e "mapa 11×11".
- **Decisão:** caracteres por `keyTyped` (letras, acentos, números do
  teclado e do teclado numérico) e teclas especiais por `keyDown`.
  **Alternativa descartada:** converter os `Input.Keys` em letra, o que
  perde acentos e o layout do teclado.
- **Decisão:** separar controle (sem libGDX) e tela. Assim a navegação tem
  teste sem janela, e o "nenhuma tela contém regra" fica visível.
  **Alternativa descartada:** estado dentro das telas. Não seria testável
  sem OpenGL.
- **Decisão:** o piloto padrão continua sendo da `GameSession`. A tela não
  troca nome vazio por "Piloto Anônimo".
- **Decisão:** a tela anterior é liberada a cada troca de tela. As telas
  são leves e criadas de novo a cada navegação. Os recursos pesados são da
  classe do jogo.

## Riscos e impactos

- **`keyTyped` também recebe `\b`, `\r` e `\t`.** O formulário filtra
  caracteres de controle (`Character.isISOControl`).
- **Fonte padrão e acentos.** "Médio", "Difícil" e "Anônimo" podem
  aparecer sem glifo. Conferir no QA. Se faltar glifo, registrar nas
  `notas:` como limitação, sem trocar o texto nem a fonte sem combinar.
- **Sair com recursos abertos:** `Gdx.app.exit()` dispara o `dispose()` do
  jogo. Conferir no log.
- **Tela da Partida provisória:** a spec 11 a substitui. Não colocar nela
  lógica que precise ser desfeita.

## Estratégia de testes

| Requisito | Teste |
|---|---|
| R1.2, R1.3, R1.5 | `ControleMenuTest`: `'1'`, `'2'`, outras teclas → `NADA` |
| R2.1 a R2.6 | `ControleMenuTest`: confirmação, `S` limpa (repositório vazio depois), `N`/`Esc` não limpam, teclas ignoradas na confirmação, falha → mensagem com o erro, tecla depois da mensagem |
| R3.2 a R3.5, R4.1 a R4.5, R4.7 | `FormularioNovaPartidaTest`: limite de 20, backspace, `Enter` com nome vazio, `Esc` nas duas etapas, seleção inicial Fácil, setas com bordas, letras ignoradas na dificuldade, `INICIAR` com nome e dificuldade |
| R6.1 a R6.4 | `TextosRankingTest`: formato da linha, ranking vazio, resumo com entrou e sem entrou |
| R1.1, R1.4, R3.1, R4.6, R5, R6.5 | Roteiro manual em `:lwjgl3:run` |
| R7 | Revisão + gate |
