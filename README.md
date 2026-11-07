# Missão Marte Unifor

Projeto Integrador da disciplina de Projeto de Arquitetura de Sistemas (Unifor, 2026.2).

O jogo **Missão Marte** começa como uma aplicação Java de console e evolui por fases até um jogo gráfico multiplataforma (desktop e Android, com libGDX), com servidor de ranking (Spring Boot) e uma melhoria própria da equipe.

O escopo completo, com o que implementar em cada sprint, está em [ESCOPO-PROJETO.md](ESCOPO-PROJETO.md).

## Estrutura

```text
fases/
  fase-00-console/    jogo original em modo texto (ponto de partida, não alterar)
docs/                 análises e decisões da equipe
ESCOPO-PROJETO.md     guia de escopo do projeto
```

Cada fase nova é criada numa pasta própria em `fases/`, sem alterar as anteriores.

## Requisitos

- JDK 17 (`java -version` deve mostrar 17)

## Como executar

### Fase 0 (console)

```bash
cd fases/fase-00-console
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Controles: `w`, `a`, `s`, `d` movem a nave, `c` embarca o passageiro e `q` aborta a missão.

O arquivo `ranking.json` é criado na pasta de onde o jogo é executado e não é versionado.

## Equipe

| Integrante | github |
|---|---|
| Matheus Correia |  |
| André Felipe |  |
| João Gabriel Rocha |  |

