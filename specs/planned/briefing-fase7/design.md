# Briefing (design): Fase 7

## Pontos de extensão já preparados nas fases anteriores

- `ElementoMapa` + `TipoElemento` (Fase 1): um item novo no mapa precisa de
  uma constante nova (ex.: `ESCUDO('S', "Escudo")`), e o console e a legenda
  o mostram sem outra mudança.
- `EventoJogo` com `TipoEvento` (Fase 1): eventos novos entram como
  constantes novas.
- `MissaoFactory` com um único sorteio de célula livre (Fase 1).
- Imagem escolhida pelo símbolo, com genérica para desconhecido (Fase 4).

## Pergunta em aberto (ficou pendente no grill; retomar aqui)

**Pergunta 7 — Como o jogador ganha o escudo?**

**Recomendação registrada:**
- um item de escudo por partida, em todas as dificuldades, símbolo `S`,
  sorteado pela `MissaoFactory` numa célula livre;
- coletado automaticamente quando a nave termina um movimento na célula
  dele, sem comando novo e sem pontos;
- a nave guarda no máximo um escudo; ele absorve a próxima colisão (não
  perde vida) e é gasto;
- eventos novos: escudo coletado e colisão absorvida;
- console e HUD mostram "Escudo: ativo" ou "Escudo: —".

**Por quê:** reaproveita o sorteio, o símbolo, os eventos e o HUD; a regra
fica toda testável sem libGDX; é diferente do que a referência fez.

**Compatibilidade:** sortear o escudo **depois** de todos os outros objetos,
para que a mesma semente gere as mesmas posições de passageiros e perigos.
O sorteio extra muda a sequência do `Random` para os inimigos; decidir no
grill se isso é aceitável (registrar como mudança) ou se o escudo usa outro
`Random`.

**Alternativa:** o escudo começa ativo, sem item no mapa. É mais simples,
mas a melhoria fica pouco visível.

## Outras perguntas em aberto

1. Colisão absorvida ainda tira pontos? (Hoje colisão não tira pontos, só
   vida.) Recomendo que não.
2. A nave pode pegar outro escudo depois de gastar? Só se houver mais de um
   item; com um por partida, não.
3. Botão por toque ou tecla nova? Não é necessário se a coleta for
   automática.
