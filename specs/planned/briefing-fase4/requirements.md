# Briefing: Fase 4 — Interface gráfica (assets, sons, HUD, eventos, movimento suave)

> **Briefing, não spec.** Ele guarda o que já foi decidido para a Fase 4 até
> o planejamento desta fase. Na hora de planejá-la: grill das perguntas em
> aberto (`design.md`), criação das specs abaixo em `active/` e remoção
> deste briefing do backlog.

## Contexto

Trocar as formas geométricas da Fase 3 por imagens e sons gerados por
programa, com HUD, resposta visual e sonora aos eventos do turno e
movimento suave, sem mudar as regras. Pasta:
`fases/fase-04-interface-grafica/`.

## Specs previstas (fila aprovada)

| Ordem prevista | Spec | Issue | Trilha | Depende de |
|---|---|---|---|---|
| 10 | `fase4-assets-recursos` | a criar | A | 8 (`fase3-telas`) |
| 11 | `fase4-interface-grafica` | a criar | B | 10 |

**Issues a criar:** uma "[Fase 4] ..." por spec, mais a de documentação
(tutorial), que fica sem spec.

## Base dos requisitos (ESCOPO-PROJETO.md, seção 8, "Fase 4"), já com os cortes

- **fase4-assets-recursos:** gerador em `tools/`; assets em `core/assets`:
  fundo 800×600 e terreno da célula; nave; propulsor, base e inimigo (2+
  quadros em laço); asteroide, professor, engenheiro, astronauta e imagem
  genérica; explosão (4+ quadros, uma vez); HUD (vida cheia/vazia, vaga
  ocupada/livre). Sons: mover, selecionar, embarcar, entregar, colisão,
  vitória, derrota e música de fundo em laço. Animações como faixas
  horizontais de quadros iguais; catálogo `Imagem` e `Som` sem libGDX;
  `Recursos` com `AssetManager`, não `static`, carregando uma vez e
  liberando em `dispose()`; imagem escolhida pelo **símbolo**, sem
  `instanceof`, e símbolo desconhecido → imagem genérica; arquivo de
  créditos. Testes de existência e dimensões dos assets e de símbolo →
  imagem.
- **fase4-interface-grafica:** `MundoRenderer` fora da `GameScreen`; nave
  gira pela direção, com propulsor animado; movimento suave (a posição
  visual persegue a lógica a ~12 células/s, sem ultrapassar; a lógica nunca
  espera o desenho), com a lógica do tempo sem libGDX e testada; colisão
  continua regra do domínio, e a tela responde com explosão e som;
  embarque e entrega com som; vitória e derrota com som, música pausada e
  caixa final; música em laço, pausa no fim e retoma no menu; HUD só
  lendo a sessão (barra superior: vidas, pontos, tempo; painel lateral:
  piloto, dificuldade, vagas, progresso de entregas, restantes,
  movimentos, mensagens; rodapé: controles), com textos acentuados;
  semente reproduzível `-Dmissaomarte.semente=N`; duração da partida para
  ao terminar.

## Decisões já tomadas

- **Assets gerados por programa** (decisão 5 do grill): programa Java puro
  em `tools/` (`javax.imageio` para PNG, WAV escrito à mão), executado por
  tarefa Gradle (ex.: `./gradlew :tools:gerarAssets`); **arquivos gerados
  commitados** em `core/assets`; testes conferem existência e dimensões;
  créditos registram a geração própria.
- **Eventos do turno já existem desde a Fase 1:** o item "mudança aditiva no
  `ResultadoTurno`" do escopo já está atendido; a Fase 4 só consome.
  Comando inválido e abortar não geram eventos (Fase 1, R5.8).
- **Duração congelada ao terminar:** já é regra da `GameSession` desde a
  Fase 1.

## Fora de escopo (cortes intencionais)

- Tremor de tela, flash vermelho, nave piscando, texto flutuante
  ("+pontos", "-pontos", "Entregue!") e brilho animado (e o asset de
  brilho).
- Repetição de tecla.
- Tecla `M` (ligar/desligar som) e propriedade `-Dmissaomarte.semaudio`.
