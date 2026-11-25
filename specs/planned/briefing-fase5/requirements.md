# Briefing: Fase 5 — Cliente integrado ao servidor

> **Briefing, não spec.** Ele guarda o que já foi decidido para o
> **cliente** da Fase 5 até o planejamento dele. Na hora de planejá-lo:
> grill das perguntas em aberto (`design.md`), criação das specs abaixo em
> `active/` e remoção deste briefing do backlog.
>
> **O servidor já foi planejado** (2026-11-24) e está na fila:
> `specs/active/fase5-servidor-usuarios/` (Ordem 13) e
> `specs/active/fase5-servidor-partidas-ranking/` (Ordem 14). A API, os
> formatos de resposta e as regras do servidor estão nessas specs, que são
> a fonte de verdade. Não repetir aqui.

## Contexto

Ranking global com cadastro e login no jogo. Estrutura:
`fases/fase-05-servidor-ranking/cliente/` (Fase 4 evoluída, Wrapper 8.7) e
`fases/fase-05-servidor-ranking/servidor/` (criado pela spec 13). **Dois
builds independentes.** O `cliente/` é criado pela `fase5-cliente-rede`,
copiando a Fase 4.

## Specs previstas

| Spec | Issue | Trilha | Depende de |
|---|---|---|---|
| `fase5-cliente-rede` | a criar | A | `fase4-interface-grafica`, 14 (`fase5-servidor-partidas-ranking`) |
| `fase5-cliente-telas-online` | a criar | B | `fase5-cliente-rede` |

As ordens serão as próximas livres quando a Fase 4 entrar na fila.

**Issues a criar:** uma "[Fase 5] ..." por spec do cliente, mais a de
documentação (tutorial), que vira um ajuste depois do cliente.

## O que o servidor já decidiu e afeta o cliente

- Rotas, corpos e códigos: ver os `requirements.md` das specs 13 e 14.
- **Todo 401 vem como `ProblemDetail`** com `detail` genérico, inclusive
  token ausente, inválido ou expirado. Qualquer 401 fora do login descarta
  a sessão.
- **Login pelo nome normalizado** (não diferencia maiúsculas). O servidor
  devolve o nome como foi cadastrado.
- **`POST /api/partidas` devolve `posicaoRanking`** (1–10 ou `null`). O
  cliente usa esse campo para "indicar se entrou no Top 10", sem chamar o
  ranking de novo.
- **Linha do ranking:** `posicao`, `nome`, `pontuacao`, `dificuldade`,
  `duracaoSegundos`, `movimentos`, `dataHora`.
- **Erros de validação** trazem `erros` `[{campo, mensagem}]`.
- O cadastro devolve só `{id, nome}`, sem token: "criar conta já entra"
  significa que o cliente chama o login logo depois.

## Base dos requisitos (ESCOPO-PROJETO.md, seção 9, "Cliente")

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
- **Limitação obrigatória a documentar:** o servidor não executa a lógica
  do jogo e não prova que a pontuação é legítima (já registrada no README
  do servidor pela spec 14; o tutorial repete).
- **Restrições gerais:** nada de senha, token ou segredo versionado ou em
  log; cliente e servidor independentes; testes do cliente sem janela,
  OpenGL ou rede.

## Fora de escopo (cortes intencionais)

- Tela "Meu histórico" no cliente.
- Swagger/springdoc.
- Docker e `DEPLOY.md`.
- Banco ou autenticação externos (Supabase): descartado no alinhamento do
  servidor.
