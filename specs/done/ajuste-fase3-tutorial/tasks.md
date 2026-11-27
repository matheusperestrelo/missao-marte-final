> **Concluída em 2026-11-26.** Diferenças do planejado: entregue no mesmo PR #38 das specs 8 a 11, sem `reviews/`. Entregou `fases/fase-03-libgdx/README.md` e `docs/fase-03-tutorial.md` (9 limitações, mais que as 3 mínimas); nenhum arquivo de código foi alterado por este ajuste.

# Tarefas: ajuste-fase3-tutorial

**Issue:** #35 · **Fase:** 3 · **Depende de:** 10 (`fase3-telas-menu`), 11 (`fase3-partida`) · **Branch:** `chore/issue-35-fase3-tutorial`

Ajuste só de documentação. Ele cria o README da Fase 3 e o tutorial
`docs/fase-03-tutorial.md`, no mesmo lugar do tutorial da Fase 2
(`docs/fase-02-tutorial.md`, issue #10). Base: `ESCOPO-PROJETO.md`,
seção 8, e o enunciado (`specs/referencia/enunciado-professor.md`, Sprint 2:
"Explique o ciclo de vida da aplicação e o papel das telas e dos módulos
de plataforma"). Pontos de partida: as specs `specs/done/fase3-*`
(requisitos, design, `notas:` das tarefas e `reviews/final.md`),
`docs/contratos.md` e o tutorial da Fase 2 como modelo de estilo.

- [x] 1. Escrever `fases/fase-03-libgdx/README.md` e `docs/fase-03-tutorial.md`
  - status: done
  - Não alterar nenhum arquivo fora desses dois: nada em `fases/*/core`, `fases/*/lwjgl3`, `src/`, testes ou build. O `check.sh` deve continuar passando.
  - Toda afirmação sobre o código cita a classe real (`MissaoMarteGame`, `TelaBase`, `Lwjgl3Launcher`, `RankingService`, `MapaGeometria`, `MapeadorTeclas`, `ControlePartida` etc.), com trecho curto quando ajudar.
  - **README da fase:** requisito (JDK 17), `./gradlew :lwjgl3:run` (e o porquê do `-XstartOnFirstThread` no macOS), `./gradlew test`, onde fica o `ranking.json` e as teclas do jogo.
  - **Tutorial**, seções obrigatórias:
    1. **Módulos `core` e `lwjgl3`:** o que fica em cada um, por que `core` não depende de plataforma (preparação para o Android na Fase 6) e onde fica a versão do libGDX.
    2. **Ciclo de vida do libGDX:** `create`, `render`, `resize`, `pause`, `resume`, `dispose` no `Game` e `show`/`hide` nas telas. Usar o log da `TelaBase` como evidência, com um trecho real do log de uma execução.
    3. **Papel das telas:** cada tela, a navegação entre elas (diagrama Mermaid ou texto) e a divisão controle sem libGDX / tela fina. Mostrar por que "nenhuma tela contém regra de jogo".
    4. **Composição de dependências:** o launcher como raiz de composição e o que chega pelo construtor do `Game`.
    5. **Entrada e coordenadas:** tecla → `Acao` e a inversão do eixo Y na `MapaGeometria`.
    6. **Mudanças em relação às fases anteriores:** Top 5 → Top 10 (com a remoção do `RankingTop5` e a migração dos testes para o `RankingServiceTest`), mapa fixo em 11×11 (a tela não pergunta o tamanho), embarque por `E` em vez de `C`, Nova partida com setas e `Enter`, limpar com confirmação, console e `Main` removidos da Fase 3 (a versão de terminal continua na Fase 2).
    7. **Testes:** o que tem teste automático (sem janela nem OpenGL) e o que é verificado à mão.
    8. **Limitações reais** da fase (no mínimo 3), tiradas das `notas:` e dos `reviews/final.md` das specs 8 a 11. Exemplos a confirmar: acentos na fonte padrão, painel estreito para mensagens longas, sem pausa ao minimizar, visual só com formas.
  - _Requisitos: nenhum (ajuste)_
  - notas: (regularização) implementada fora do workflow no PR #38 (commit 943322f, junto com as outras specs da Fase 3), sem `review` por tarefa e sem `reviews/`. Conferida em 2026-11-26 com o gate: `.workflow/check.sh` passou (`./gradlew clean build` da Fase 3: 157 testes, 0 falhas). Código de `domain`/`application`/`infrastructure` comparado com a Fase 2: só mudaram os `import` e o ranking migrou para `application.ranking`. Pendente para o QA: abrir a janela (`./gradlew :lwjgl3:run`) e conferir à mão os itens marcados como manuais.
