# Tarefas: ajuste-fase1-tutorial

**Issue:** #7 · **Fase:** 1 · **Depende de:** 6 (`ajuste-fase1-contratos`) · **Branch:** `chore/issue-7-fase1-tutorial`

Ajuste só de documentação: cria `fases/fase-01-refatoracao/docs/fase-01-tutorial.md`
(caminho da issue #7 e do `ESCOPO-PROJETO.md`, seção "Fase 1" e linha
"Cada pasta de fase deve ter ... `docs/fase-XX-tutorial.md`"). Pontos de
partida: `docs/analise-inicial.md`, `docs/fase-00-arquitetura.md`, o
`README.md` da Fase 1, `docs/contratos.md` (Ordem 6) e as specs em
`specs/done/fase1-*`.

- [ ] 1. Escrever `fases/fase-01-refatoracao/docs/fase-01-tutorial.md`
  - status: review
  - Não alterar nenhum arquivo fora do tutorial: nada em
    `fases/fase-01-refatoracao/src/`, `fases/fase-00-console/`,
    `fases/fase-02-gradle/` ou testes. O `check.sh` deve continuar passando.
  - Toda afirmação sobre o código cita a classe real (e, nos padrões, um
    trecho curto do código). Ser preciso: o `switch` de `Acao.deTexto` ou a
    tabela da `Dificuldade` **não** são Strategy.
  - Seções obrigatórias (critérios da issue #7 e do `ESCOPO-PROJETO.md`):
    1. **Problemas do código original** (Fase 0, `Main` único) e **passo a
       passo da refatoração** (domain → application → console/infra →
       Main de composição).
    2. **Tabela "responsabilidade → onde estava → onde ficou"**, a partir do
       mapeamento de `docs/fase-00-arquitetura.md`.
    3. **SOLID:** os 5 princípios, cada um com classes reais, como foi
       aplicado e o limite.
    4. **GRASP:** no mínimo 5 padrões com justificativa (ex.: Controller
       `GameSession`, Information Expert `Missao`/`Nave`, Creator
       `MissaoFactory`, Low Coupling `RankingRepository`, Polymorphism
       `Passageiro`/`ElementoMapa`, Pure Fabrication `FileRankingRepository`).
    5. **Padrões de projeto:** no mínimo 2, com trecho de código (ex.:
       Factory, Repository, Value Object `Posicao`, Injeção de Dependência no
       `Main`).
    6. **Diagramas** (Mermaid ou texto): arquitetura antes, dependências entre
       camadas depois e classes do domínio.
    7. **Limitações reais e como resolver (no mínimo 3)**, registradas nas
       specs concluídas: tamanho 1 no Médio/Difícil trava a criação do
       cenário (ponto *d*); tipo novo de elemento exige constante nova em
       `TipoElemento`; `GameSession` sem guarda antes de `iniciar` e depois do
       fim; nome com vírgula some do ranking (ponto *b*); menu só aparece
       depois da primeira linha lida (ponto *a*).
    8. **Mudanças de comportamento em relação ao original:** desembarque
       automático na base, vitória só com todos entregues, linha de status
       com "Entregues" (a bordo + entregues + restantes = total) e capacidade
       por dificuldade (4/3/2), conforme o `README.md` da Fase 1.
    9. **Roteiro de testes manuais:** menu (válido e inválido), as 3
       dificuldades, embarcar com e sem passageiro, entregar na base e
       vencer, colidir, gravar e consultar ranking, limpar ranking, sair com
       `Q` e fim da entrada (Ctrl+D). Usar como base o roteiro de
       `specs/done/fase1-console-infra/reviews/final.md`. Montar como tabela
       com coluna "Resultado" **em branco**: o agente não marca como
       executado o que não executou. O resultado é preenchido pelo usuário
       no QA desta spec (o QA manual das specs da Fase 1 foi adiado para
       este ponto).
  - _Requisitos: nenhum (ajuste)_
  - notas: Criado só `fases/fase-01-refatoracao/docs/fase-01-tutorial.md` com as 9 seções pedidas (7 GRASP, 4 padrões de projeto mais a seção "o que não é Strategy", 5 limitações, 21 casos no roteiro). Coluna "Resultado" do roteiro em branco, para o usuário preencher no QA. Nenhum código, teste ou spec alterado; sem decisões fora da spec. Ajuste de escopo mínimo: o caso 1 do roteiro usa "digitar 2" em vez de "Enter", porque o menu só aparece depois da primeira linha (ponto a). Sugestão fora de escopo: os diagramas Mermaid não foram renderizados por ferramenta, só escritos conforme `docs/fase-00-arquitetura.md`; vale conferir a renderização no QA. check.sh passou.
