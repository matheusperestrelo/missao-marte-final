# Escopo do Projeto Integrador — Missão Marte Unifor

Guia da equipe para saber **o que precisa ser implementado**, **em que ordem** e **como saber que está pronto**, partindo do código-base do professor.

> Repositório-base (referência): `github.com/marcelobezerra-dotcom/T200-ProjArqSist/tree/main/missaoMarteUnifor/projetoExtensaoFinal`
>
> ⚠️ **Regra principal:** o repositório do professor já tem **todas as fases prontas** (Fase 0 a 7). Ele serve **só para estudo e consulta**. Não copiem o código: cada fase deve ser implementada pela equipe, com histórico real de commits, issues e PRs.

---

## Sumário

1. [Visão geral](#1-visão-geral)
2. [O que existe no código-base](#2-o-que-existe-no-código-base)
3. [Ambiente e versões](#3-ambiente-e-versões)
4. [Estrutura do nosso repositório](#4-estrutura-do-nosso-repositório)
5. [Regras que valem para todas as fases](#5-regras-que-valem-para-todas-as-fases)
6. [Organização no GitHub (Parte 1)](#6-organização-no-github-parte-1)
7. [Sprint 1 — Fases 0, 1 e 2: console, refatoração e Gradle](#7-sprint-1--fases-0-1-e-2-console-refatoração-e-gradle-0311-a-0811)
8. [Sprint 2 — Fases 3 e 4: libGDX e interface gráfica](#8-sprint-2--fases-3-e-4-libgdx-e-interface-gráfica-0911-a-1511)
9. [Sprint 3 — Fase 5: servidor e ranking global](#9-sprint-3--fase-5-servidor-e-ranking-global-1611-a-2211)
10. [Sprint 4 — Fases 6 e 7: Android e evolução livre](#10-sprint-4--fases-6-e-7-android-e-evolução-livre-2311-a-2911)
11. [Sprint 5 — Estabilização e apresentação](#11-sprint-5--estabilização-e-apresentação-3011-a-0312)
12. [Entregáveis finais](#12-entregáveis-finais)
13. [Critérios de avaliação e o que tira nota](#13-critérios-de-avaliação-e-o-que-tira-nota)
14. [Riscos e armadilhas conhecidas](#14-riscos-e-armadilhas-conhecidas)
15. [Perguntas da apresentação](#15-perguntas-da-apresentação)

---

## 1. Visão geral

Vamos evoluir o jogo **Missão Marte Unifor** em etapas, **sem reescrever do zero**:

```text
Console (Java puro)  →  Camadas + SOLID  →  Gradle + JUnit 5  →  libGDX (janela)
   →  Gráficos/sons/HUD  →  Servidor Spring Boot + ranking global  →  Android + toque  →  Melhoria própria
```

O jogo: o jogador pilota uma nave num mapa em grade, resgata passageiros (Professor, Engenheiro, Astronauta), desvia de asteroides e inimigos e leva os passageiros até a base em (0,0). Cada movimento custa pontos, colisões tiram vida e pontos.

| Componente | Tecnologia |
|---|---|
| Cliente (jogo) | Java 17, Gradle, libGDX 1.12.1 (desktop LWJGL3 + Android) |
| Servidor | Spring Boot 4.1.2, API REST, JPA + H2, JWT, BCrypt |
| Testes | JUnit 5 (cliente), MockMvc + H2 em memória (servidor) |
| Gestão | Scrum + GitHub Projects (Kanban), issues e milestones |

---

## 2. O que existe no código-base

O repositório do professor tem uma pasta por fase em `exercicio10/fases/`. Cada fase tem `README.md` e `docs/` com **enunciado**, **planejamento** e **tutorial**. Os **enunciados são a especificação** do que precisamos entregar — leiam o da fase antes de começar.

| Fase | Pasta de referência | O que tem |
|---|---|---|
| 0 | `fase-0-oopuro` | Jogo de console original: tudo num `Main.java` de ~557 linhas, métodos estáticos, ranking Top 5 em `ranking.json` |
| 1 | `fase-01-refatoracao` | Pacotes `domain`, `application`, `console`, `infrastructure.ranking`; `build.ps1` |
| 2 | `fase-02-gradle` | Mesmo código no layout Gradle + 28 testes JUnit 5 |
| 3 | `fase-03-libgdx` | Multi-módulo `core` + `lwjgl3`, telas com formas geométricas, 39 testes |
| 4 | `fase-04-interface-grafica` | Sprites, animações, sons, HUD, efeitos, 66 testes |
| 5 | `fase-05-servidor-ranking` | `cliente/` + `servidor/` independentes, 97 testes no cliente e 34 no servidor |
| 6 | `fase-06-android` | Módulo `android`, toque, configuração do servidor, `Dockerfile`, `DEPLOY.md` |
| 7 | `fase-07-evolucao-livre` | Exemplo: modo tempo real, tiro, inimigo perseguidor, layout responsivo (208 testes no cliente) |

Outros arquivos úteis na raiz da referência:

- `ENUNCIADO_AVA.md` — o enunciado geral (o mesmo que recebemos).
- `Issues_GitHub_Missao_Marte.md` — backlog sugerido (MM-01 a MM-15) com critérios de aceite. **Usem como base para criar nossas issues.**
- `tutorial-api-gradle-libgdx.md` — teoria de Gradle e libGDX (Fases 2 a 4).
- `Plano_de_Aulas_Missao_Marte.md` — cronograma e entregáveis por fase.

> As contagens de testes acima são da referência. Não precisamos bater o número, mas elas mostram o **nível de cobertura esperado**.

---

## 3. Ambiente e versões

| Item | Versão | Observação |
|---|---|---|
| JDK | **17** | Em `JAVA_HOME` **e** primeiro no `PATH` (`java -version` precisa mostrar 17) |
| Gradle Wrapper (cliente) | **8.7** | Fases 2 a 7 |
| Gradle Wrapper (servidor) | **9.8.2** | Spring Boot 4.1 exige Gradle 8.14+ ou 9.x |
| libGDX | **1.12.1** | Fases 3 a 7 |
| Spring Boot | **4.1.2** | Fases 5 a 7 |
| Android Gradle Plugin | **8.6.x** | Compatível com Gradle 8.7 |
| Android SDK | Plataforma **35**, build-tools **34.0.0** | `minSdk 26`, `compileSdk/targetSdk 35` |
| Banco | H2 em arquivo | Nada a instalar |

**macOS:** os comandos da referência estão em PowerShell. No Mac usem `./gradlew` em vez de `.\gradlew.bat`, e a tarefa `run` do `lwjgl3` precisa do argumento `-XstartOnFirstThread` (configurar condicionalmente no `build.gradle`).

**Antes da Sprint 4:** quem vai gerar o APK deve instalar o Android SDK com antecedência (é grande e demora).

---

## 4. Estrutura do nosso repositório

A atividade exige **preservar o histórico das fases**: cada fase é uma pasta nova, a anterior não é alterada. Sugestão:

```text
missao-marte/                      (repositório público da equipe)
  README.md                        visão geral, requisitos, como rodar cada fase
  docs/
    arquitetura.md                 decisões arquiteturais (ADRs) e diagramas
    relatorio-tecnico.md           relatório final (2 a 4 páginas)
    retrospectiva.md               retrospectiva da equipe
  fases/
    fase-00-console/               jogo original (preservado, só para rodar e comparar)
    fase-01-refatoracao/
    fase-02-gradle/
    fase-03-libgdx/
    fase-04-interface-grafica/
    fase-05-servidor-ranking/
      cliente/
      servidor/
    fase-06-android/
      cliente/  (core, lwjgl3, android, tools)
      servidor/ (+ Dockerfile, DEPLOY.md)
    fase-07-evolucao/
      cliente/
      servidor/
```

Cada pasta de fase deve ter: `README.md` (como rodar), `docs/fase-XX-tutorial.md` (o que foi feito e por quê) e `.gitignore` (`build/`, `.gradle/`, `out/`, `ranking.json`, `local.properties`, `dados/`).

Sugestão extra: criar uma **tag Git por fase** concluída (`fase-01`, `fase-02`...).

---

## 5. Regras que valem para todas as fases

### Arquitetura

- [ ] Regras do jogo ficam em `domain` / `application`. Telas e controllers **só traduzem entrada e desenham/devolvem resultado**.
- [ ] `domain`, `application` e `infrastructure` **nunca** importam `com.badlogic` (libGDX) nem Android. Conferir com busca: `grep -r "com.badlogic" core/src/main/java/**/domain`.
- [ ] `application` não importa `infrastructure` (depende de interfaces).
- [ ] Cliente e servidor são **projetos independentes**: o servidor não depende do código do cliente (duplicar o que precisar, ex.: enum de dificuldade).
- [ ] API nunca devolve entidade JPA: sempre DTO (`record`).
- [ ] Fase nova **não altera** a pasta da fase anterior.
- [ ] Mudança de comportamento do jogo só se for **intencional e documentada** (com teste).

### Segurança

- [ ] Nada de senha, token, chave ou segredo no código, no Git, em log ou em captura de tela.
- [ ] Segredo do JWT vem de variável de ambiente (`MISSAOMARTE_JWT_SEGREDO`).
- [ ] Usar só dados de teste na demonstração.

### Processo

- [ ] Commits pequenos, frequentes e descritivos (sugestão: Conventional Commits — `feat:`, `fix:`, `refactor:`, `test:`, `docs:`).
- [ ] Todo trabalho entra por **issue → branch → PR → revisão de outro integrante → merge**.
- [ ] Rodar `./gradlew clean build` (e testes) **antes** de mover a issue para "Em revisão".
- [ ] Atualizar o README sempre que mudar comando, requisito ou configuração.
- [ ] Se algo não puder ser verificado (ex.: sem celular, sem SDK), **registrar o motivo e mostrar evidência alternativa**.

---

## 6. Organização no GitHub (Parte 1)

Fazer **antes** de começar o código (já atrasado se ainda não foi feito — o projeto começou em 03/11).

- [ ] Criar o repositório **público** da equipe; todos com permissão de escrita.
- [ ] Criar o **GitHub Project** ligado ao repositório, com as colunas:

| Coluna | Significado |
|---|---|
| Backlog | Identificado, não iniciado |
| Em andamento | Sendo executado |
| Em revisão | Pronto pelo responsável, aguardando revisão |
| Concluído | Revisado, validado e aceito |

- [ ] Criar os **milestones** por sprint (ou por fase) e o campo **Iteration "Sprint"** com os períodos:

| Sprint | Período | Fases |
|---|---|---|
| 🚀 Sprint 1 | 03/11 – 08/11 | 0, 1, 2 |
| 🎮 Sprint 2 | 09/11 – 15/11 | 3, 4 |
| 🌐 Sprint 3 | 16/11 – 22/11 | 5 |
| 📱 Sprint 4 | 23/11 – 29/11 | 6, 7 |
| 🏁 Sprint 5 | 30/11 – 03/12 | Estabilização e apresentação |

- [ ] Cadastrar as issues (abaixo, MM-01 a MM-15) com **responsável, sprint e critérios de aceite**. Quebrar em issues menores quando passar de 1–2 dias de trabalho.
- [ ] Mover os cartões **conforme o trabalho avança** — o professor avalia o histórico do quadro, não só o estado final.

---

## 7. Sprint 1 — Fases 0, 1 e 2: console, refatoração e Gradle (03/11 a 08/11)

**Entrega da sprint:** jogo de console executável por Gradle, código em camadas, testes passando e registro da arquitetura.

### Fase 0 — Entender o jogo original · Issue MM-01

- [ ] Rodar o jogo original e anotar como ele funciona (menu, controles `w/a/s/d`, `c` embarcar, `q` sair, dificuldades, ranking Top 5).
- [ ] Listar as classes e o que cada uma faz; mapear o fluxo de uma partida.
- [ ] Mapear as responsabilidades concentradas no `Main` (menu, entrada, regras, criação do cenário, desenho, ranking, JSON manual) e os problemas de acoplamento.
- [ ] Diagrama da arquitetura **antes** (Mermaid).
- [ ] Listar os **comportamentos que devem ser preservados**.

### Fase 1 — Refatoração arquitetural · Issue MM-02

Pasta nova; não alterar a Fase 0.

**Camadas (mínimo 4 pacotes):**

| Pacote | Responsabilidade |
|---|---|
| `domain` | Nave, passageiros, perigos, missão, posição, base |
| `application` | Sessão da partida (turno), fábrica do cenário, dificuldade |
| `console` | Menu, leitura de comandos, desenho do mapa |
| `infrastructure` | Persistência do ranking em arquivo |

**Requisitos de projeto:**

- [ ] `Main` só monta as dependências e inicia o jogo.
- [ ] Regras de turno (movimento, embarque, colisão, pontuação, vitória, derrota) fora do `console`.
- [ ] Comandos digitados viram um tipo próprio (ex.: `enum Acao`) antes de chegar na regra.
- [ ] Criação do cenário por dificuldade isolada numa classe (ex.: `MissaoFactory`).
- [ ] Ranking acessado por **interface** (ex.: `RankingRepository`), com implementação em arquivo substituível.
- [ ] Cada elemento do mapa informa a **própria posição e símbolo**; a legenda é montada a partir das entidades (sem símbolos fixos espalhados, sem `instanceof`).
- [ ] Passageiros tratados de forma uniforme (polimorfismo, sem `if` por tipo).
- [ ] Script de build único (`build.sh`/`build.ps1`, com opção de executar).

**Regras de jogo que precisam ficar coerentes:**

- [ ] Nenhum objeto começa na mesma célula que outro.
- [ ] Ao chegar na base com passageiros, eles **desembarcam** e liberam capacidade.
- [ ] Vitória só quando **todos os passageiros forem entregues na base**.
- [ ] Indicadores fecham: `a bordo + entregues + restantes = total`.
- [ ] Capacidade da nave coerente com a dificuldade (ou regra explicada).
- [ ] Toda mudança em relação ao original (pontuação dos passageiros, tamanho do mapa, Top 5 → Top 10 etc.) registrada.

**Documentação `docs/fase-01-tutorial.md`:**

- [ ] Problemas do código original e passo a passo da refatoração.
- [ ] Tabela "responsabilidade → onde estava → onde ficou".
- [ ] **SOLID:** os 5 princípios com classes reais, como foi aplicado e o limite de cada um.
- [ ] **GRASP:** no mínimo 5 padrões (Controller, Information Expert, Creator, Low Coupling, High Cohesion, Polymorphism, Pure Fabrication...) com justificativa.
- [ ] **Padrões de projeto:** no mínimo 2 (Factory, Repository, Value Object, Injeção de Dependência...). Ser preciso: um `switch` de configuração **não é** Strategy.
- [ ] Diagramas: arquitetura antes, dependências entre camadas depois, classes do domínio.
- [ ] Pelo menos 3 limitações reais do nosso código e como resolver.
- [ ] Roteiro de testes manuais executado: menu (válido e inválido), as 3 dificuldades, embarcar com e sem passageiro, entregar na base e vencer, colidir, gravar e consultar ranking, limpar ranking, sair com `Q` e fim da entrada (Ctrl+D).

### Fase 2 — Migração para Gradle · Issue MM-03

Pasta nova; o código `.java` deve ser **apenas movido** (idêntico à Fase 1, conferir por hash).

- [ ] Layout `src/main/java` e `src/test/java`; `settings.gradle` e `build.gradle` (DSL Groovy).
- [ ] Plugin `application`, `mainClass` definida, Java 17, UTF-8 em todas as `JavaCompile`, só `mavenCentral()`.
- [ ] `run { standardInput = System.in }` para o teclado funcionar.
- [ ] JUnit 5 (`testImplementation`, `testRuntimeOnly 'org.junit.platform:junit-platform-launcher'`, `useJUnitPlatform()`).
- [ ] Jogo sem dependências de execução.
- [ ] Wrapper **8.7** gerado e versionado (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`).
- [ ] `.gitignore` com `build/`, `.gradle/`, `out/`, `ranking.json`.

**Testes mínimos:**

| Classe | O que verificar |
|---|---|
| `Posicao` | Movimento retorna nova instância; `equals`/`hashCode` por coordenadas |
| `Nave` | Embarque, capacidade, nave cheia |
| `Acao` | Texto → ação, incluindo entrada inválida |
| `MissaoFactory` | Passageiros por dificuldade; nenhum objeto na mesma célula |
| `GameSession` | Embarque, desembarque na base, colisão, vitória |
| `FileRankingRepository` | Gravar e ler com `@TempDir` |

Testes não dependem de teclado nem gravam fora de pasta temporária.

**Comandos que devem funcionar:** `./gradlew --version` (8.7 / JVM 17), `./gradlew clean build`, `./gradlew test`, `./gradlew run --console=plain`, `./gradlew installDist`.

**Documentação:** tasks e DAG, plugin `application`, escopos de dependência, wrapper vs Gradle instalado, ciclo de vida do build, problemas encontrados, resultado dos testes, comparação por hash.

---

## 8. Sprint 2 — Fases 3 e 4: libGDX e interface gráfica (09/11 a 15/11)

**Entrega da sprint:** jogo gráfico desktop jogável, recursos liberados corretamente, testes automatizados.

### Fase 3 — Introdução ao libGDX · Issues MM-04 e MM-05

Visual simples (formas geométricas + fonte padrão). A versão console fica na Fase 2.

**Estrutura multi-módulo:**

- [ ] `core/` (jogo, sem plataforma) e `lwjgl3/` (só o launcher desktop).
- [ ] Versão do libGDX definida em **um único lugar** no `build.gradle` raiz.
- [ ] `lwjgl3` depende de `core`, `gdx-backend-lwjgl3` e `gdx-platform:natives-desktop`.
- [ ] `run` do `lwjgl3` com diretório de trabalho na raiz da fase; `-XstartOnFirstThread` no macOS.

**Ranking vai para a aplicação:**

- [ ] Mover `RankingRepository` e `RankingEntry` para `application.ranking` (implementação em arquivo continua em `infrastructure`).
- [ ] Criar `RankingService`: listar Top 10, registrar (informa se entrou), limpar.
- [ ] Regras: pontuação ≤ 0 não entra; ordem decrescente; máximo 10; com lista cheia só entra quem for **maior** que o pior.

**Classe `Game` e telas:**

- [ ] `Game` cria câmera, `FitViewport` 800×600, `SpriteBatch`, `ShapeRenderer` e fonte em `create()`; libera em `dispose()`.
- [ ] Dependências (fábrica, ranking, `Random`) chegam **pelo construtor**; quem cria é o launcher (composition root).
- [ ] Telas mínimas: **Menu** (1 Jogar, 2 Ranking, 3 Limpar, 4 Sair), **Nova partida** (nome + dificuldade), **Partida** (mapa 11×11 + painel), **Ranking** (Top 10 + resumo após vitória).
- [ ] Classe base de tela que loga `show/resize/pause/resume/hide/dispose` (sem repetir em cada tela).
- [ ] `resize()` atualiza o viewport; `InputProcessor` instalado em `show()` e removido em `hide()`.
- [ ] **Nenhuma tela contém regra de jogo.**

**Entrada (por turnos):** classe que converte tecla → `Acao`: `W/↑`, `S/↓`, `A/←`, `D/→`, `E` embarcar, `Q/Esc` sair; outras teclas ignoradas **sem consumir turno**.

**Mapa:** retângulos coloridos com o símbolo vindo de `ObjetoRenderizavel`; nave por cima; painel com piloto, dificuldade, vidas, pontos, a bordo/capacidade, entregues/total, restantes, movimentos, mensagens; fim com vitória/game over e `Enter`; pontuação registrada **uma única vez**.

**Coordenadas:** isolar a inversão do eixo Y numa classe **sem libGDX** (ex.: `MapaGeometria`) e testar.

**Testes novos:** tecla → ação; conversão de coordenadas; `RankingService` com repositório em memória. Os testes da Fase 2 continuam passando. Nenhum teste abre janela ou usa OpenGL.

### Fase 4 — Interface gráfica · Issue MM-06

**Assets em `core/assets`** (criar no Piskel, gerar por código em `tools/`, ou usar arte livre registrando autor/origem/licença num arquivo de créditos):

| Elemento | Quadros |
|---|---|
| Fundo 800×600 e terreno da célula | 1 |
| Nave | 1 |
| Propulsor, base, inimigo | 2+ em laço |
| Asteroide; professor, engenheiro, astronauta; imagem genérica | 1 cada |
| Explosão | 4+, uma vez |
| Brilho (embarque/entrega) | 3+, uma vez |
| HUD: vida cheia/vazia, vaga ocupada/livre | 1 cada |

Sons: mover, selecionar, embarcar, entregar, colisão, vitória, derrota + **música de fundo** em laço.

**Recursos:**

- [ ] Animações como faixas horizontais de quadros iguais.
- [ ] Catálogo (enums `Imagem` e `Som`) **sem libGDX**.
- [ ] Classe `Recursos` com `AssetManager` (**não `static`**): carrega uma vez, libera em `dispose()`. Telas não criam nem liberam texturas.
- [ ] Imagem escolhida na camada `gdx` a partir do **símbolo** do objeto (sem `instanceof`); símbolo desconhecido → imagem genérica.

**Eventos do turno (mudança aditiva):**

- [ ] `ResultadoTurno` passa a trazer uma lista de eventos: Movimento, Embarque, Entrega, Colisão, Vitória, Derrota.
- [ ] Construtor antigo continua válido e testes antigos passam **sem alteração**.
- [ ] Comando inválido e abortar não geram eventos.
- [ ] Duração da partida **para** ao terminar.

**Visual e jogabilidade:**

- [ ] `MundoRenderer` desenha o mapa (fora da `GameScreen`); nave gira pela direção e tem propulsor animado.
- [ ] **Movimento suave:** posição visual persegue a lógica (~12 células/s), sem ultrapassar; a lógica **nunca espera** o desenho.
- [ ] **Repetição de tecla:** atraso 0,28 s, depois a cada 0,14 s; cada repetição é um turno; lógica do tempo sem libGDX.
- [ ] **Colisão continua regra do domínio** (mesma célula). A tela só responde: explosão, tremor que decai, flash vermelho, nave piscando, texto "-pontos", som.
- [ ] Efeitos: brilho + som + "+pontos" no embarque; brilho + "Entregue!" na entrega; som + música pausada + caixa final em vitória/derrota.
- [ ] Texto de variação de pontos só aparece se |variação| > 1.
- [ ] Música em laço, pausa no fim e retoma no menu; tecla `M` liga/desliga som (exceto na tela de digitar nome); propriedade para desativar áudio (`-Dmissaomarte.semaudio=true`).
- [ ] **HUD** (só lê a sessão): barra superior (vidas, pontos, tempo, som), painel lateral (piloto, dificuldade, vagas, progresso de entregas, restantes, movimentos, mensagens), rodapé (controles). Textos acentuados.
- [ ] Semente reproduzível: `-Dmissaomarte.semente=N`.

**Testes novos:** eventos do turno; existência e dimensões dos assets; símbolo → imagem; posição visual; efeitos e textos flutuantes; tremor; repetição de tecla; geometria fracionária. Sem janela, OpenGL ou áudio.

---

## 9. Sprint 3 — Fase 5: servidor e ranking global (16/11 a 22/11)

**Entrega da sprint:** cliente e servidor executáveis, API documentada, fluxo cadastro → login → partida → ranking demonstrável, testes dos dois lados.

Estrutura: `fase-05-servidor-ranking/cliente/` (Fase 4 evoluída, wrapper 8.7) e `servidor/` (Spring Boot novo, wrapper 9.8.2). **Dois builds independentes** (explicar o porquê). `domain` do cliente idêntico ao da Fase 4.

### Servidor · Issues MM-07 e MM-08

**Modelo (JPA + H2 em arquivo):**

| Entidade | Campos |
|---|---|
| `Usuario` | id, nome, nome normalizado (minúsculas, **único no banco**), hash da senha, data de criação |
| `Partida` | id, usuário, dificuldade, resultado (`VITORIA`/`DERROTA`), pontuação, duração (s), movimentos, data/hora |

- [ ] Pasta do banco configurável por variável (`MISSAOMARTE_DADOS`); `ddl-auto=update` (explicar por que não serve para produção).

**API:**

| Método e rota | Token? | Resposta |
|---|---|---|
| `POST /api/usuarios` | não | 201 (`id`, `nome`); 409 nome existe; 400 inválido |
| `POST /api/auth/login` | não | 200 (token, `Bearer`, expiração); 401 credenciais erradas |
| `POST /api/partidas` | sim | 201 com a partida |
| `GET /api/partidas/minhas` | sim | Histórico do dono, mais recente primeiro, paginado (`pagina`, `tamanho` ≤ 50) |
| `GET /api/ranking` | sim | Top 10 |

- [ ] **Tudo protegido por padrão**, liberando só cadastro, login (e `/error`).
- [ ] Usuário da partida vem **do token**, nunca do corpo.
- [ ] Abortar não é enviado; resultado só `VITORIA`/`DERROTA`.
- [ ] Erros no formato `ProblemDetail` (RFC 9457), com campos inválidos na validação.
- [ ] Nenhuma resposta traz senha ou hash.

**Ranking global:** uma linha **por jogador** (sua melhor vitória), só pontuação > 0, ordem por pontuação ↓, empate por menor duração, depois partida mais antiga; máximo 10.

**Validação:**

| Campo | Regra |
|---|---|
| Nome | 3–20 caracteres: letras, números, `_`, `-` |
| Senha | ≥ 8 caracteres e **≤ 72 bytes** (limite do BCrypt) |
| Dificuldade / resultado | Valores do enum, senão 400 |
| Pontuação | -100000 a 100000 |
| Duração | 0 a 86400 s |
| Movimentos | 0 a 100000 |
| Campo ausente | 400 (usar `Integer` + `@NotNull`, não `int`) |

**Segurança:**

- [ ] BCrypt custo ≥ 10 (mesma senha → hashes diferentes).
- [ ] JWT assinado, `sub` = id do usuário, expira em 2 h, sem senha no token.
- [ ] Segredo do JWT por variável de ambiente, ≥ 32 bytes, **fora do repositório** (sem a variável: gerar aleatório e avisar no log).
- [ ] API sem estado (sem sessão/cookie); explicar por que CSRF pode ficar desligado.
- [ ] Mesma resposta (e tempo parecido, usando hash falso) para usuário inexistente e senha errada.
- [ ] DTOs de login/cadastro não imprimem a senha no `toString`.
- [ ] Escuta só em `127.0.0.1` por padrão.
- [ ] Arquivo `requests.http` com chamadas de exemplo. Swagger (springdoc) opcional, liberando só a leitura da documentação.

**Testes do servidor (MockMvc + H2 em memória + propriedades de teste):** cadastro (201 sem senha, hash BCrypt, sal diferente, nome repetido com outra caixa = 409, inválidos = 400, limite 72 bytes); login (sucesso, mesma resposta nos dois erros); proteção (sem token, inválido, **expirado** = 401; válido = 200); partidas (registro, usuário do corpo ignorado, enum/faixa inválidos, histórico só do dono, ordem, paginação); ranking (vazio, uma linha por jogador, derrotas fora, desempates, limite 10, sem dados sensíveis).

### Cliente · Issue MM-09

**Porta e adaptador:**

- [ ] Porta `ServidorDoJogo` em `application` (cadastrar, entrar, enviar partida, carregar ranking, carregar histórico), **assíncrona com callback**.
- [ ] Tipos de apoio sem libGDX: resposta (valor ou erro, com marca de "não autorizado"), callback, sessão (nome + token, `toString` sem token, só em memória), partida concluída, partida registrada.
- [ ] Adaptador HTTP em `gdx` com `Gdx.net.sendHttpRequest`; **todo** resultado repassado com `Gdx.app.postRunnable` (não bloquear a thread da interface).
- [ ] `Content-Type: application/json`, `Authorization: Bearer ...`, tempo limite.
- [ ] Classe só para JSON (`JsonWriter`/`JsonReader`), **nunca concatenação de texto** (senha `a"b\c` não pode quebrar).
- [ ] Mensagem de erro ao jogador vem do `ProblemDetail` ou de texto padrão por código HTTP; nunca o corpo bruto.
- [ ] Endereço configurável: `-Dmissaomarte.servidor=http://localhost:8080`.

**Modos e telas:**

| Tela | Convidado | Logado |
|---|---|---|
| Menu | 1 Jogar, 2 Ranking local, 3 Limpar local, 4 Entrar/criar conta, 5 Sair | 1 Jogar, 2 Ranking global, 3 Meu histórico, 4 Sair da conta, 5 Sair |
| Nova partida | Digita o nome | Usa o nome da conta |
| Fim | Grava no ranking local (só vitória) | Envia vitória **e** derrota |
| Ranking | Local, síncrono | Global, assíncrono |

- [ ] Tela **Entrar/criar conta**: nome e senha mascarada, `Tab`/setas trocam campo, `F2` alterna modo, `Enter` confirma, `Esc` volta; criar conta já entra. Lógica do formulário em classe sem libGDX.
- [ ] Tela **Meu histórico**: últimas 10 partidas.
- [ ] Estados visíveis: carregando, pronto, erro (nunca tela em branco).
- [ ] Durante envio, `Enter` ignorado; falhou → mostra motivo e `R` tenta de novo.
- [ ] 401 = sessão expirada → descarta sessão e volta a convidado.
- [ ] **Servidor desligado não derruba o jogo**: "não foi possível conectar" e convidado continua.
- [ ] Após vitória logada, indicar se entrou no Top 10.

**Testes do cliente:** JSON (escape, números negativos, token, ranking, histórico, lista vazia); mensagens de erro; formulário; dados do servidor; formatação de data. Sem janela, OpenGL ou rede.

**Limitação obrigatória a documentar:** o servidor **não executa a lógica do jogo** e não consegue provar que a pontuação enviada é legítima (as validações só barram valores absurdos).

---

## 10. Sprint 4 — Fases 6 e 7: Android e evolução livre (23/11 a 29/11)

**Entrega da sprint:** desktop preservado, APK de debug (ou evidência da limitação), melhoria implementada, testada e documentada, versão candidata à apresentação.

### Fase 6 — Android · Issues MM-10, MM-11 e MM-12

**Módulo Android:**

- [ ] Módulo `android` com `AndroidLauncher` montando as mesmas dependências do desktop.
- [ ] Mesmos assets de `core/assets` (sem duplicar); nativas para `armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`.
- [ ] Manifesto: só `INTERNET`, paisagem, ícone gerado por programa em `tools/`.
- [ ] Ranking local do convidado na pasta privada do app.
- [ ] Módulo `android` **só entra no build se houver SDK** — quem não tem continua compilando o desktop.
- [ ] Trocar APIs que não existem na API 26 (`Files.readString/writeString`, `List.of`, `String.isBlank`, `String.repeat`...) e explicar no tutorial.

**Toque e tela:**

- [ ] Botões virtuais na partida (4 setas, Embarcar, Som, Abortar) geram a **mesma `Acao`** das teclas.
- [ ] Setas repetem enquanto o dedo está apoiado (por id do ponteiro).
- [ ] Menus por toque chamam o mesmo código das teclas; botão **Voltar** do Android tratado.
- [ ] Modo toque no desktop: `-Dmissaomarte.toque=true` (mouse = dedo). Sem ele, desktop igual à Fase 5.
- [ ] Modo toque com `ExtendViewport` e controles na área extra; botões sem sobreposição (testar); registrar o comportamento em 4:3.
- [ ] Layout dos botões e tradução botão → ação em classes **sem libGDX**, testadas.
- [ ] Campos de texto abrem a **caixa nativa** do sistema; resposta devolvida à thread do jogo; operação "definir texto" aplica as mesmas regras de digitação.

**Configurar o servidor no jogo:**

- [ ] Objeto de valor imutável (endereço, porta, HTTPS) que valida na criação (IPv4/hostname, porta 1–65535; recusar `@`, espaço, barra).
- [ ] Gravado nas Preferences atrás de interface — **nunca senha nem token**.
- [ ] Precedência: salvo > padrão do launcher (`-Dmissaomarte.servidor` / `-PservidorPadrao`) > `http://localhost:8080`.
- [ ] Tela de configuração com **Testar conexão** (`GET /api/saude`), Salvar, Restaurar padrão; trocar de servidor encerra a sessão; aviso para HTTP fora de rede local.
- [ ] HTTP sem criptografia liberado **só no build de debug**; release só HTTPS.
- [ ] Controle Bluetooth: **opcional**.

**Servidor pronto para publicação (sem mudar rotas):**

- [ ] Endereço e porta por variável de ambiente (padrão `127.0.0.1:8080`).
- [ ] `GET /api/saude` público, com teste.
- [ ] Nome fixo do JAR, encerramento gracioso.
- [ ] `Dockerfile` em duas etapas, usuário sem privilégio, dados em volume.
- [ ] `DEPLOY.md` com caminho (A) Wi-Fi local e (B) contêiner na nuvem, com os avisos (HTTP só em rede confiável, disco efêmero em planos grátis, partida a frio, segredo do token, Swagger, sem limite de tentativas de login). Publicação real só se o professor exigir.

**Comandos:** `./gradlew clean build`, `./gradlew :lwjgl3:run`, `./gradlew :lwjgl3:run -Dmissaomarte.toque=true`, `./gradlew :android:assembleDebug`.

**Testes novos:** configuração do servidor, precedência, controle de toque, formulários, teclas/Voltar, `/api/saude`. Sem janela, OpenGL, rede ou SDK.

### Fase 7 — Evolução livre · Issues MM-13 e MM-14

**Decidir a melhoria logo no início da Sprint 4** (ou antes) e combinar com o professor se for fora da lista. Uma melhoria menor bem feita vale mais que uma grande pela metade.

| Melhoria | Nível |
|---|---|
| Personalização da nave (cor/modelo nas Preferences) | Básico |
| Nova arma (tiro em área, que atravessa, escudo, bomba) | Básico a médio |
| Novo inimigo (patrulha, fuga, teleporte) como nova estratégia de movimento | Médio |
| Dificuldade crescente ao longo da partida | Médio |
| Campanha de fases com pontuação acumulada | Médio a avançado |
| Chefão (várias vidas + padrão de ataque, exige arma) | Avançado |
| Tela responsiva (retrato) em mais telas | Médio |

> A referência do professor já fez: modo tempo real, tiro, inimigo perseguidor e layout responsivo. **Escolham algo diferente** para não parecer cópia.

**Regras da melhoria:**

- [ ] Regra nova em `domain`/`application`, nunca na tela.
- [ ] Tempo é **parâmetro** (nada de relógio do sistema ou `Thread.sleep` nas regras); aleatoriedade recebe `Random`.
- [ ] Reaproveitar `Acao`, `EventoJogo` e pontos de extensão existentes.
- [ ] Novos assets gerados por programa em `tools/`, sem alterar os existentes.
- [ ] Modo/comportamento anterior preservado, **ou** mudança explícita e justificada.
- [ ] Servidor só muda se necessário (ex.: nova dificuldade exige mudar o enum validado) — com testes e doc da API.

**Testes:** regra nova (casos normais e bordas), integração com a sessão (eventos, pontuação, estado), **compatibilidade** (comportamento antigo igual, ex.: mesma semente gera o mesmo cenário), lógica de apresentação sem libGDX.

**Documentar:** o que foi feito, qual decisão de arquitetura facilitou, limitações.

---

## 11. Sprint 5 — Estabilização e apresentação (30/11 a 03/12)

**Congelar funcionalidades.** Só corrigir o que impede rodar ou demonstrar. Issue MM-15.

- [ ] Rodar todos os testes e builds de todas as fases a partir de um clone limpo, seguindo **só o README**.
- [ ] Validar em **outra máquina** (de outro integrante).
- [ ] Atualizar README, tutoriais e quadro Kanban.
- [ ] Escrever o relatório técnico e a retrospectiva.
- [ ] Ensaiar a demonstração (10–15 min) e ter **plano B** (vídeo/capturas) para o que não puder rodar ao vivo (ex.: celular).
- [ ] Entregar no AVA até **03/12**: link do repositório, link do Project e relatório.
- [ ] Fechar o Kanban; documentar o que ficou pendente ou não verificado.

---

## 12. Entregáveis finais

**Repositório GitHub:**

- [ ] Código organizado por fase.
- [ ] Instruções para rodar cliente e servidor; requisitos de ambiente; comandos de build/teste.
- [ ] Documentação dos endpoints e configurações (variáveis de ambiente, porta, saúde, persistência).
- [ ] Tutoriais/registros técnicos de cada fase.

**GitHub Project:** backlog, issues, responsáveis e progresso das sprints.

**Relatório técnico (PDF ou Markdown, 2–4 páginas):**

- [ ] Arquitetura e decisões técnicas, com trechos do código.
- [ ] Padrões e princípios aplicados: onde e por quê.
- [ ] Testes realizados e resultados.
- [ ] Dificuldades, limitações e melhorias futuras.
- [ ] Participação de cada integrante.

**Demonstração ao vivo (10–15 min):** jogo, integração com servidor e a melhoria da equipe.

---

## 13. Critérios de avaliação e o que tira nota

| Critério | Pontos |
|---|---|
| Evolução funcional do jogo (console → gráfico, comportamento preservado) | 20 |
| Arquitetura e qualidade do código (camadas, SOLID/GRASP, sem acoplamento domínio ↔ interface) | 20 |
| Servidor e integração (REST, persistência, validação, autenticação, integração demonstrável) | 15 |
| Android e experiência de uso (reuso, toque, adaptação, evidência de build) | 10 |
| Testes e confiabilidade (testes relevantes, build reproduzível, falhas documentadas) | 15 |
| Evolução livre (melhoria integrada e testada) | 10 |
| Gestão, documentação e apresentação (Kanban real, README, relatório, demo) | 10 |
| **Total** | **100** |

**Não conformidades que reduzem a nota:**

- Sistema não roda conforme o README.
- Regra de negócio em tela ou controller.
- Senha, token ou segredo versionado ou em log.
- Entidade do banco devolvida direto na API.
- Kanban sem histórico de acompanhamento.
- Contribuição dos integrantes não identificável no histórico.
- Melhoria que altera comportamento anterior sem teste ou explicação.

---

## 14. Riscos e armadilhas conhecidas

| Risco | Como evitar |
|---|---|
| Sprints 2 e 4 têm duas fases cada | Começar cedo; dividir o trabalho em paralelo (ex.: um faz assets, outro faz eventos/HUD) |
| JDK errado no `PATH` ("class file version") | Conferir `java -version` antes de tudo |
| Gradle 8.7 não roda em JDK muito novo (ex.: 25) | Usar JDK 17 |
| Teclado não chega ao jogo no `gradlew run` | `standardInput = System.in` |
| Janela não abre no Mac | `-XstartOnFirstThread` na tarefa `run` |
| Spring Boot 4 mudou starters | Usar `spring-boot-starter-webmvc` (não `-web`); Jackson 3 (`tools.jackson`); `@AutoConfigureMockMvc` nos testes |
| JWT HS256 rejeitado | Declarar `JwtDecoder` com `withSecretKey` e o algoritmo |
| Erro vira 401 | Liberar `/error` |
| Campo ausente vira 0 | `Integer` + `@NotNull` nos `record` |
| BCrypt corta senha > 72 bytes em silêncio | Validar o tamanho em bytes |
| Rede no libGDX não bloqueia | Não ler o resultado logo após `sendHttpRequest`; usar callback + `postRunnable` |
| Dois servidores no mesmo arquivo H2 | Parar um antes de subir outro |
| Android SDK ausente/demorado | Instalar antes da Sprint 4; módulo `android` opcional no build |
| Android 9+ bloqueia HTTP | Liberar só no build de debug |
| Kanban atualizado só no final | Mover cartões diariamente |

---

## 15. Perguntas da apresentação

Preparar respostas com exemplos do **nosso** código:

1. Como as responsabilidades mudaram entre a versão console e a versão gráfica?
2. Onde ficam as regras de negócio? Como os testes provam que elas não dependem da interface?
3. Qual a diferença entre o cliente libGDX e o servidor Spring Boot neste projeto?
4. Como o cliente fala com a API e o que acontece se o servidor estiver fora do ar?
5. Por que senhas são guardadas como hash? Onde o token é validado?
6. O que é um DTO e por que usamos na API?
7. Como o jogo reaproveita código entre desktop e Android?
8. Qual decisão de arquitetura facilitou a nossa melhoria?
9. O que faríamos diferente com mais tempo?

---

## Apêndice — Backlog sugerido (MM-01 a MM-15)

Baseado em `Issues_GitHub_Missao_Marte.md` da referência. Copiar os critérios de aceite de lá ao criar cada issue e quebrar em sub-issues quando necessário.

| Issue | Título | Sprint | Fase |
|---|---|---|---|
| MM-01 | Diagnosticar e registrar a arquitetura do jogo de console | 1 | 0/1 |
| MM-02 | Refatorar a arquitetura sem alterar as regras | 1 | 1 |
| MM-03 | Migrar compilação e execução para Gradle | 1 | 2 |
| MM-04 | Criar a estrutura inicial libGDX | 2 | 3 |
| MM-05 | Integrar partida e entrada do jogador à tela do jogo | 2 | 3 |
| MM-06 | Entregar a interface gráfica jogável | 2 | 4 |
| MM-07 | Implementar API e persistência de usuários | 3 | 5 |
| MM-08 | Registrar partidas e calcular o ranking global | 3 | 5 |
| MM-09 | Integrar cliente libGDX à API | 3 | 5 |
| MM-10 | Configurar o módulo Android | 4 | 6 |
| MM-11 | Adaptar controles, layout e ciclo de vida para toque | 4 | 6 |
| MM-12 | Validar distribuição e registrar evidências Android | 4 | 6 |
| MM-13 | Escolher uma melhoria e fechar o escopo | 4 | 7 |
| MM-14 | Implementar a melhoria selecionada | 4 | 7 |
| MM-15 | Documentar, demonstrar e revisar a evolução | 5 | — |
