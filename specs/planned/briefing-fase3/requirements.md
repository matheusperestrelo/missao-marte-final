# Briefing: Fase 3 — libGDX (core e lwjgl3), telas e Top 10

> **Briefing, não spec.** Ele guarda o que já foi decidido para a Fase 3 até
> o planejamento desta fase. Na hora de planejá-la: grill das perguntas em
> aberto (`design.md`), criação das specs abaixo em `active/` e remoção
> deste briefing do backlog.

## Contexto

Levar o jogo para uma janela desktop com libGDX 1.12.1, visual simples
(formas geométricas e fonte padrão), reaproveitando `domain` e `application`
sem mudar as regras. Pasta: `fases/fase-03-libgdx/`. A versão de console
fica na Fase 2.

## Specs previstas (fila aprovada)

| Ordem prevista | Spec | Issue | Trilha | Depende de |
|---|---|---|---|---|
| 6 | `fase3-estrutura-libgdx` | a criar | C | 5 (`fase2-testes-junit`) |
| 7 | `fase3-ranking-service` | a criar | A | 6 |
| 8 | `fase3-telas` | a criar | B | 6, 7 |

**Issues a criar:** "[Fase 3] ..." para cada spec, mais uma de documentação
(tutorial), que fica sem spec.

## Base dos requisitos (ESCOPO-PROJETO.md, seção 8, "Fase 3")

- **fase3-estrutura-libgdx:** módulos `core/` (sem plataforma) e `lwjgl3/`
  (só o launcher); versão do libGDX num único lugar; `lwjgl3` depende de
  `core`, `gdx-backend-lwjgl3` e `gdx-platform:natives-desktop`; `run` com
  diretório de trabalho na raiz da fase e `-XstartOnFirstThread` no macOS;
  código e testes da Fase 2 movidos para `core` e passando.
- **fase3-ranking-service:** mover `RankingRepository` e `RankingEntry` para
  `application.ranking`; `RankingService` (listar Top 10, registrar
  informando se entrou, limpar); regras: pontuação ≤ 0 não entra, só
  vitória, ordem decrescente, máximo 10, com a lista cheia só entra quem for
  **maior** que o pior; testes com repositório em memória.
- **fase3-telas:** `Game` criando câmera, `FitViewport` 800×600,
  `SpriteBatch`, `ShapeRenderer` e fonte em `create()`, liberados em
  `dispose()`; dependências pelo construtor, montadas no launcher;
  telas Menu (1 Jogar, 2 Ranking, 3 Limpar, 4 Sair), Nova partida (nome e
  dificuldade), Partida (mapa 11×11 e painel) e Ranking (Top 10 e resumo
  após vitória); classe base de tela que loga o ciclo de vida;
  `InputProcessor` em `show()`/`hide()`; tecla → `Acao` (`W/↑`, `S/↓`,
  `A/←`, `D/→`, `E` embarcar, `Q/Esc` sair; outras teclas ignoradas sem
  consumir turno); símbolo vindo do `ElementoMapa` da Fase 1; painel
  (piloto, dificuldade, vidas, pontos, a bordo/capacidade, entregues/total,
  restantes, movimentos, mensagens); fim com `Enter`; pontuação registrada
  uma única vez; `MapaGeometria` (inversão do eixo Y) sem libGDX e testada.
  Nenhum teste abre janela ou usa OpenGL.

## Decisões já tomadas

- **Top 10:** a Fase 1 manteve o Top 5; o Top 10 é **mudança intencional
  desta fase**, a registrar.
- **Limpar ranking na tela pede confirmação** (decisão 6 do grill): "Limpar
  o ranking? S confirma, N ou Esc cancela", seguido de "Ranking limpo." ou
  "Operação cancelada."; a decisão de limpar fica no `RankingService`.
- **Regras só em `domain`/`application`**: telas não decidem nada.

## Fora de escopo (já decidido)

- Assets, sons, HUD gráfico e movimento suave: Fase 4.
- Repetição de tecla: cortada do projeto.
- Console gráfico e console texto juntos: a versão console fica na Fase 2.
