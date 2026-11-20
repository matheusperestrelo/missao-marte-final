# Briefing (design): Fase 5

## Decisões já tomadas

- **Gate:** sem troca prevista. O `.workflow/check.sh` roda `clean build`
  em `cliente/` e `servidor/`. Ele usa JDK 17 em todos os builds Gradle;
  Spring Boot 4.1 e Gradle 9.8.2 rodam no 17. Confirmar na criação da spec
  9. **Se** o servidor exigir JDK maior, o usuário precisa trocar o gate à
  mão.
- **Riscos conhecidos** (ESCOPO, seção 14): `spring-boot-starter-webmvc`,
  Jackson 3 (`tools.jackson`), `@AutoConfigureMockMvc`, `JwtDecoder` com
  `withSecretKey` e algoritmo, liberar `/error`, `Integer` + `@NotNull`,
  tamanho da senha em bytes.

## Perguntas em aberto (para o grill da fase)

1. **Pasta do cliente:** a spec 13 cria `cliente/` copiando a Fase 4, ou a
   spec 9 cria a pasta da fase só com `servidor/`? Recomendo: a spec 9 cria
   `fase-05-servidor-ranking/servidor/`, e a spec 13 cria `cliente/`.
2. **Ordem da fila:** a spec 9 está antes da Fase 4 na fila (ordem 9). Ela
   só deve ser pega quando a trilha C estiver livre, e não depende do
   cliente.
3. **Tempo limite do HTTP no cliente:** recomendo 10 s.
4. **Expiração do token no cliente:** só reagir ao 401, ou checar a
   expiração antes? Recomendo só reagir ao 401, que é mais simples.
5. **Erro de rede ao enviar partida:** a vitória vai para o ranking local
   como plano B? Recomendo que não: mostrar o erro e permitir `R`.
