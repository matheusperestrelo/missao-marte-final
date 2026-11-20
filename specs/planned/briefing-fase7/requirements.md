# Briefing: Fase 7 — Melhoria: escudo

> **Briefing, não spec.** Ele guarda o que já foi decidido para a Fase 7 até
> o planejamento desta fase. Na hora de planejá-la: grill das perguntas em
> aberto (`design.md`), criação da spec abaixo em `active/` e remoção deste
> briefing do backlog.

## Contexto

Uma melhoria pequena e testada: um **escudo que absorve a próxima
colisão**. Pasta: `fases/fase-07-evolucao/` (`cliente/` e `servidor/`,
copiados da Fase 6).

## Spec prevista (fila aprovada)

| Ordem prevista | Spec | Issue | Trilha | Depende de |
|---|---|---|---|---|
| 18 | `fase7-escudo` | a criar | A | 16 (`fase6-toque`), 17 (`fase6-config-servidor`) |

**Issues a criar:** "[Fase 7] Implementar o escudo" e a de documentação, que
fica sem spec.

## Base dos requisitos (ESCOPO-PROJETO.md, seção 10, "Fase 7")

- Regra nova em `domain`/`application`, nunca na tela.
- Tempo como parâmetro e aleatoriedade pelo `Random` injetado.
- Reaproveitar `Acao`, `EventoJogo` e pontos de extensão existentes.
- Novos assets gerados por programa em `tools/`, sem alterar os existentes.
- Comportamento anterior preservado, ou mudança explícita e justificada.
- Servidor só muda se necessário (o escudo não exige mudança).
- Testes: regra nova (normais e bordas), integração com a sessão
  (eventos, pontuação, estado), **compatibilidade** (mesma semente gera o
  mesmo cenário antigo) e lógica de apresentação sem libGDX.
- Documentar o que foi feito, qual decisão de arquitetura facilitou e as
  limitações.

## Fora de escopo (já decidido)

- Modo tempo real, tiro, inimigo perseguidor e layout responsivo: já feitos
  na referência do professor; evitar para não parecer cópia.
