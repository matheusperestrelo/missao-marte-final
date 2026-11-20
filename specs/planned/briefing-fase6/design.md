# Briefing (design): Fase 6

## Decisões já tomadas

- **Gate:** sem troca. O módulo `android` só entra no `settings.gradle` se
  houver SDK, então o gate continua passando em máquina sem SDK.
- **Verificação manual:** o APK e o toque no aparelho são critérios
  manuais, com evidência (captura, vídeo ou saída do `assembleDebug`).
  Layout e tradução de botões são testes automáticos sem libGDX.

## Perguntas em aberto (para o grill da fase)

1. **Quem gera o APK:** qual integrante tem (ou instala) o Android SDK
   (plataforma 35, build-tools 34.0.0)? Sem SDK, qual evidência substitui?
   Recomendo decidir no início da Sprint 4 e registrar na spec 15.
2. **Botão "Som":** o escopo pede o botão, mas a tecla `M` foi cortada.
   Recomendo cortar também o botão.
3. **Ajustes do servidor na spec 15 ou numa spec própria?** Recomendo na 15
   (são três itens pequenos), para não aumentar a fila.
4. **Aviso de HTTP fora de rede local:** qual regra define "rede local"?
   Recomendo `localhost`, `127.x`, `10.x`, `192.168.x` e `172.16–31.x`.
