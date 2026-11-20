# Briefing (design): Fase 3

## Decisões já tomadas

- **`ObjetoRenderizavel` do escopo = `ElementoMapa` da Fase 1** (posição +
  símbolo via `TipoElemento`). Não criar interface nova.
- **Eventos do turno já existem desde a Fase 1** (`ResultadoTurno.eventos`);
  as mensagens do painel saem deles.
- **Gate:** sem troca. A fase tem `gradlew` na raiz e o gate roda
  `clean build`; as regras de camadas também valem dentro de `core`.

## Perguntas em aberto (para o grill da fase)

1. **Nova partida:** como escolher a dificuldade (teclas 1/2/3, setas)?
   Nome com limite de caracteres? Recomendo: setas ou 1/2/3; nome de até
   20 caracteres; vazio vira "Piloto Anônimo", regra que já está na
   `GameSession`.
2. **Tamanho do mapa:** fixo em 5 (11×11), sem pergunta na tela?
   Recomendo fixo, porque o escopo pede o 11×11.
3. **Fim da partida:** `Enter` leva ao Ranking com o resumo (vitória) ou ao
   Menu (derrota)? Recomendo Ranking em vitória e Menu em derrota.
4. **Embarque por `E`:** a tecla `C` também embarca, como no console?
   Recomendo só `E`, como no escopo.
5. **Pacote do adaptador libGDX:** `gdx` em `core` (ex.: `missaomarte.gdx`)?
   Recomendo `missaomarte.gdx`, que o gate já proíbe em
   `domain`/`application`/`infrastructure`.
