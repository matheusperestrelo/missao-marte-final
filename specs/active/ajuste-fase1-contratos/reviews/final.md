# Verificação final — ajuste-fase1-contratos

**Veredito:** APROVADA
**Branch:** chore/fase1-contratos
**Commits da spec:** 3 (5539537..75fecef)

## Cobertura

| Requisito | Critério | Teste(s) | Situação |
|---|---|---|---|
| ajuste | — | testes existentes intactos | coberto |

`git diff --name-status main...HEAD` mostra só quatro arquivos:
`docs/contratos.md` (novo), `specs/README.md` (status da linha 6 para
"em andamento"), `specs/active/ajuste-fase1-contratos/tasks.md` (status e
notas da tarefa 1) e `reviews/01-r1.md` (novo). Nenhum arquivo de código,
de teste, de `fases/` ou o `check.sh` foi alterado, apagado ou pulado.
Não há mudança de comportamento observável.

A única tarefa (1) está `[x]` e `status: done`, aprovada na revisão
`01-r1.md`.

## check.sh

passou (fase 0 imutável, javac e regras de camadas da Fase 1,
`gradlew clean build` com testes e regras de camadas da Fase 2)

## Pendências

- notas da tarefa 1 — divergência do plano registrada na seção 6 do
  documento: `FileRankingRepository.carregar()` ordena, mas não limita a 5;
  o limite vem de `RankingTop5.registrar`. Conferida na revisão 01-r1 e
  procede.
- notas da tarefa 1 / tarefa 1 — a aprovação dos três integrantes e a
  revisão do PR por outro integrante são da equipe; a seção "Aprovação" do
  documento está com os nomes em aberto.
- revisão 01-r1, sugestão 1 — `docs/contratos.md:181-185`: acrescentar que
  `carregar()` devolve lista vazia, sem exceção, quando o arquivo não existe
  ou não pode ser lido, e que um `ranking.json` com mais de 5 entradas
  aparece inteiro em `listar()`. Útil para quem implementar outra trilha.
- revisão 01-r1, sugestão 2 — o cabeçalho do `tasks.md` indica a branch
  `chore/issue-13-fase1-contratos`, mas o trabalho está em
  `chore/fase1-contratos`. O orquestrador decide se renomeia antes do PR.

## Roteiro de QA

1. Abrir `docs/contratos.md` e conferir que existem as seções 1 a 8 e a
   seção "Aprovação" → **esperado:** Domínio, `Acao`, Turno, Sessão e
   cenário, Ranking, Diferenças em relação ao plano, API do servidor
   (Fase 5), Porta `ServidorDoJogo` (Fase 5) e Aprovação, nessa ordem.
2. Conferir os títulos das seções 7 e 8 → **esperado:** ambos marcados
   como "PROPOSTA, a combinar", e a introdução diz que não são decisões.
3. Ler a seção 6 → **esperado:** cita a ausência do record `Placar`,
   `jogar` em vez de `executar`, `ElementoMapa` no papel de
   `ObjetoRenderizavel`, e a divergência do limite de 5 no ranking.
4. Rodar o jogo da Fase 2 (`cd fases/fase-02-gradle && ./gradlew run
   --console=plain`), jogar uma partida curta com `w/a/s/d`, `c` e `q`
   → **esperado:** o jogo se comporta exatamente como antes da branch
   (nada mudou para o jogador), e os controles batem com a seção 2 do
   documento.
5. Conferir uma ou duas tabelas contra o jogo (ex.: escolher Fácil e ver a
   pontuação inicial e a capacidade da nave) → **esperado:** valores iguais
   aos da tabela de `Dificuldade` na seção 4 (Fácil: 30 pontos,
   capacidade 4).
6. Equipe: os três integrantes leem o documento e preenchem a seção
   "Aprovação" (fora do escopo do agente).

## Próximos passos

- APROVADA: QA pelo usuário, merge, e conclusão da spec (active/ → done/).
