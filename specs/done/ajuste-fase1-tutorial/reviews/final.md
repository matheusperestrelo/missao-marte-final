# Verificação final — ajuste-fase1-tutorial

**Veredito:** APROVADA
**Branch:** chore/fase1-tutorial
**Commits da spec:** 5 (2392f52..2e1c672)

## Cobertura

| Requisito | Critério | Teste(s) | Situação |
|---|---|---|---|
| ajuste | — | testes existentes intactos | coberto |

`git diff --stat main...HEAD` mostra só
`fases/fase-01-refatoracao/docs/fase-01-tutorial.md` (novo), `specs/README.md`
(status da linha 7 para "em andamento") e arquivos da própria spec
(`tasks.md`, `reviews/`). Nenhum arquivo de código, de teste ou o `check.sh`
foi alterado, apagado ou pulado. A tarefa 1 está `[x]` com `status: done`
(aprovada na rodada 2, `reviews/01-r2.md`). As 9 seções obrigatórias da
tarefa estão presentes no tutorial (seções 1 a 9).

## check.sh

passou (Fase 0 imutável, builds e testes das fases, regras de camadas)

## Pendências

- revisão 01-r2, sugestão 1 — `fase-01-tutorial.md:287`, caso 19: a
  observação "(na terceira, ao voltar ao menu, também 'Entrada encerrada.
  Obrigado por jogar!')" vale também para a segunda execução (Ctrl+D antes
  de decolar). Não impede o QA: a mensagem esperada aparece; só fica
  incompleta.
- notas da tarefa 1 — os diagramas Mermaid da seção 6 não foram renderizados
  por ferramenta; conferir a renderização no QA (passo 2 abaixo).
- notas da tarefa 1 — o caso 1 do roteiro usa "digitar 2" em vez de "Enter",
  por causa do ponto *a* (menu só aparece depois da primeira linha). Desvio
  mínimo e justificado.
- `tasks.md` cita a branch `chore/issue-7-fase1-tutorial`; a branch real é
  `chore/fase1-tutorial`. Só registro; sem efeito no conteúdo.

## Roteiro de QA

1. Abrir `fases/fase-01-refatoracao/docs/fase-01-tutorial.md` e conferir que
   tem as 9 seções da tarefa (problemas + passo a passo, tabela
   responsabilidade, SOLID com os 5 princípios, GRASP com 7 padrões, 4
   padrões de projeto com trecho + "o que não é Strategy", diagramas,
   5 limitações, mudanças de comportamento, roteiro) → **esperado:** todas
   presentes e com classes reais citadas.
2. Abrir o arquivo num visualizador com Mermaid (ex.: GitHub, no PR) →
   **esperado:** os 3 diagramas da seção 6 (arquitetura antes, dependências
   entre camadas, classes do domínio) renderizam sem erro.
3. Em `fases/fase-01-refatoracao`, apagar o `ranking.json` local e rodar
   `./build.sh run`; executar os 21 casos da tabela da seção 9, preenchendo a
   coluna "Resultado" → **esperado:** cada caso produz a saída descrita na
   coluna "Esperado" (é o QA manual adiado das specs da Fase 1).
4. No caso 19, segunda execução (Ctrl+D antes de decolar) → **esperado:**
   "Entrada encerrada. Missão não iniciada." seguido de "Entrada encerrada.
   Obrigado por jogar!" (ver pendência 1).
5. Rodar `./build.sh run` na Fase 1 com o fluxo principal (nova missão,
   mover, sair com `q`, opção `4`) → **esperado:** comportamento igual ao de
   antes desta spec; o ajuste não alterou código, e as únicas diferenças em
   relação à Fase 0 são as 4 da seção 8 do tutorial.

## Próximos passos

- APROVADA: QA pelo usuário (roteiro acima, com a coluna "Resultado" do
  tutorial preenchida), merge, e conclusão da spec (active/ → done/).
