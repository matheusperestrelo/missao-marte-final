# Briefing (design): Fase 4

## Decisões já tomadas

- Ver `requirements.md`: assets gerados e commitados, eventos desde a Fase 1,
  cortes de escopo.
- **Testes sem áudio:** sem `semaudio`, os testes nunca instanciam
  `Recursos` nem `AssetManager`; testam só o catálogo, os arquivos no disco e
  a lógica sem libGDX.

## Perguntas em aberto (para o grill da fase)

1. **Módulo `tools`:** entra no `settings.gradle` como módulo Gradle ou é só
   uma tarefa `JavaExec` no `core`? Recomendo módulo `tools` próprio,
   porque a Fase 6 também gera o ícone do Android por programa.
2. **Sons:** tons sintetizados simples (onda quadrada/senoidal com
   envelope)? Recomendo que sim, porque não há arte de terceiros nem
   licença.
3. **Música:** um laço curto gerado também? Recomendo um laço de ~8 s.
4. **Explosão:** a nave continua visível durante a explosão? Recomendo
   que a explosão seja desenhada por cima, sem esconder a nave.
5. **Caixa final:** qual texto e qual tecla? Recomendo "MISSÃO CUMPRIDA" ou
   "GAME OVER", com o resumo e "Enter para continuar", seguindo o fluxo de
   fim decidido na Fase 3.
6. **Divisão entre A e B:** quem faz o `Recursos`? Recomendo A (spec 10),
   porque a spec 11 só consome.
