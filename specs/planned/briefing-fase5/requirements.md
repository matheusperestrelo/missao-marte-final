# Briefing: Fase 5 — Servidor Spring Boot e cliente integrado

> **Briefing, não spec.** Ele guarda o que já foi decidido para a Fase 5 até
> o planejamento desta fase. Na hora de planejá-la: grill das perguntas em
> aberto (`design.md`), criação das specs abaixo em `active/` e remoção
> deste briefing do backlog. As specs de servidor (9 e 12) não dependem do
> cliente e podem ser planejadas antes das Fases 3 e 4 terminarem.

## Contexto

Ranking global com cadastro e login. Estrutura:
`fases/fase-05-servidor-ranking/cliente/` (Fase 4 evoluída, Wrapper 8.7) e
`fases/fase-05-servidor-ranking/servidor/` (Spring Boot 4.1.2, Wrapper
9.8.2), **dois builds independentes**.

## Specs previstas (fila aprovada)

| Ordem prevista | Spec | Issue | Trilha | Depende de |
|---|---|---|---|---|
| 9 | `fase5-servidor-usuarios` | a criar | C | — |
| 12 | `fase5-servidor-partidas-ranking` | a criar | C | 9 |
| 13 | `fase5-cliente-rede` | a criar | A | 11 (`fase4-interface-grafica`) |
| 14 | `fase5-cliente-telas-online` | a criar | B | 12, 13 |

**Issues a criar:** uma "[Fase 5] ..." por spec, mais a de documentação, que
fica sem spec.

## Base dos requisitos (ESCOPO-PROJETO.md, seção 9)

- **API (decidida):** `POST /api/usuarios` (201 `id`,`nome`; 409 nome
  existe; 400), `POST /api/auth/login` (200 token, `Bearer`, expiração;
  401), `POST /api/partidas` (token; 201), `GET /api/partidas/minhas?pagina&tamanho`
  (token; dono; mais recente primeiro; `tamanho` ≤ 50), `GET /api/ranking`
  (token; Top 10), `GET /api/saude` (público). Enums `FACIL/MEDIO/DIFICIL`
  e `VITORIA/DERROTA`.
- **fase5-servidor-usuarios:** `Usuario` (id, nome, nome normalizado único,
  hash, data de criação); JPA + H2 em arquivo com pasta por
  `MISSAOMARTE_DADOS`; validação (nome 3–20 de `[A-Za-z0-9_-]`; senha ≥ 8
  e ≤ 72 bytes); BCrypt custo ≥ 10; JWT (`sub` = id, 2 h, segredo por
  `MISSAOMARTE_JWT_SEGREDO` ≥ 32 bytes; sem a variável, segredo aleatório e
  aviso no log); mesma resposta para usuário inexistente e senha errada (hash
  falso); tudo protegido por padrão, liberando cadastro, login, saúde e
  `/error`; sem estado; escuta em `127.0.0.1`; `ProblemDetail` com campos
  inválidos; DTOs `record` sem senha no `toString`; `requests.http`.
- **fase5-servidor-partidas-ranking:** `Partida` (usuário, dificuldade,
  resultado, pontuação, duração, movimentos, data/hora); usuário vem do
  token, nunca do corpo; validações de faixa (pontuação −100000..100000,
  duração 0..86400, movimentos 0..100000; `Integer` + `@NotNull`); histórico
  paginado do dono; ranking global com uma linha por jogador (melhor
  vitória), pontuação > 0, desempate por menor duração e depois partida mais
  antiga, máximo 10.
- **fase5-cliente-rede:** porta `ServidorDoJogo` em `application`,
  assíncrona com callback (cadastrar, entrar, enviar partida, carregar
  ranking); tipos sem libGDX (resposta com valor ou erro e marca de "não
  autorizado", callback, sessão em memória com `toString` sem token,
  partida concluída, partida registrada); adaptador HTTP em `gdx` com
  `Gdx.net.sendHttpRequest` e `Gdx.app.postRunnable`; JSON por
  `JsonWriter`/`JsonReader`, nunca concatenação; mensagem de erro do
  `ProblemDetail` ou texto padrão por código; `-Dmissaomarte.servidor`.
- **fase5-cliente-telas-online:** modos convidado e logado; tela Entrar/criar
  conta (senha mascarada, `Tab`/setas, `F2`, `Enter`, `Esc`; criar já entra;
  lógica do formulário sem libGDX); estados carregando/pronto/erro; envio no
  fim (`Enter` ignorado durante envio; `R` tenta de novo); 401 descarta a
  sessão; servidor desligado não derruba o jogo; indicar se entrou no Top 10.

## Decisões já tomadas

- **Histórico** (decisão 4 do grill): o servidor implementa e testa
  `GET /api/partidas/minhas`; a porta `ServidorDoJogo` **não** tem a operação
  de histórico.
- **Menus:** convidado = 1 Jogar, 2 Ranking local, 3 Limpar local, 4
  Entrar/criar conta, 5 Sair. Logado = 1 Jogar, 2 Ranking global, 3 Sair
  da conta, 4 Sair.
- **Limpar local pede confirmação**, como na Fase 3.
- **Fim de partida:** convidado grava no ranking local (só vitória); logado
  envia vitória e derrota; abortar não é enviado.
- **`/api/saude` já na Fase 5**, porque está na lista de rotas decidida.
- **Limitação obrigatória a documentar:** o servidor não executa a lógica
  do jogo e não prova que a pontuação é legítima.
- **Restrições gerais:** API devolve só DTO `record`; nada de senha, token ou
  segredo versionado ou em log; cliente e servidor independentes (o servidor
  duplica o enum de dificuldade); testes sem rede (MockMvc + H2 em memória no
  servidor).

## Fora de escopo (cortes intencionais)

- Tela "Meu histórico" no cliente.
- Swagger/springdoc.
- Docker e `DEPLOY.md`.
