# Briefing (design): Fase 5 — cliente

## Decisões já tomadas

- **Servidor planejado** nas specs 13 e 14 (`specs/active/`). As perguntas
  do servidor que estavam aqui (pasta da fase, ordem na fila, JDK do gate)
  foram resolvidas no alinhamento de 2026-11-24: a spec 13 cria só
  `servidor/`; o `cliente/` nasce na `fase5-cliente-rede`; o gate roda
  `clean build` em `cliente/` e `servidor/` com JDK 17.
- **Riscos conhecidos do cliente** (ESCOPO, seção 14): a rede do libGDX não
  bloqueia (não ler o resultado logo após `sendHttpRequest`; usar callback
  + `postRunnable`); Android 9+ bloqueia HTTP (Fase 6).

## Perguntas em aberto (para o grill do cliente)

1. **Tempo limite do HTTP no cliente:** recomendo 10 s.
2. **Expiração do token no cliente:** só reagir ao 401, ou checar a
   expiração antes? Recomendo só reagir ao 401, que é mais simples (o
   servidor devolve `expiraEmSegundos`, mas o cliente não precisa usar).
3. **Erro de rede ao enviar partida:** a vitória vai para o ranking local
   como plano B? Recomendo que não: mostrar o erro e permitir `R`.
4. **Criar conta e entrar:** se o cadastro der 201 e o login seguinte
   falhar por rede, o que a tela mostra? Recomendo "Conta criada. Não foi
   possível entrar agora." e manter o nome preenchido para tentar entrar.
