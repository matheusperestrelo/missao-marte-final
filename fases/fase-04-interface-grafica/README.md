# Missão Marte Unifor — Fase 4 (Interface gráfica)

O jogo ganha uma janela desktop com libGDX. As regras são as mesmas da Fase 2
(`domain` e `application` reaproveitados); o que muda é a apresentação (telas)
e o ranking, que passa a ser **Top 10**. A versão de terminal continua na Fase 2.
Detalhes e decisões: [docs/fase-03-tutorial.md](../../docs/fase-03-tutorial.md).

## Estrutura

| Módulo | O que tem |
|---|---|
| `core/` | O jogo: `domain`, `application`, `infrastructure` e o pacote `gdx` (`MissaoMarteGame`, telas e controles). Só depende do núcleo do libGDX, sem backend de plataforma. |
| `lwjgl3/` | Só o `Lwjgl3Launcher`: cria as dependências e abre a janela no desktop. |

A versão do libGDX está em um único lugar: `ext { gdxVersion }` no `build.gradle` desta pasta.

## Requisitos

- **JDK 17.** O Gradle 8.7 não roda em JDKs mais novos. Se `java -version` não
  mostrar 17, aponte o `JAVA_HOME` para um JDK 17:
  - macOS: `brew install openjdk@17` e `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`
  - Windows/Linux: instale o Temurin 17 (adoptium.net) e ajuste o `JAVA_HOME`
- Não é preciso ter o Gradle instalado: o `./gradlew` baixa o 8.7 sozinho.
- Placa de vídeo/driver com OpenGL (o libGDX desenha com OpenGL).

## Comandos

Execute a partir de `fases/fase-04-interface-grafica` (no Windows, `gradlew.bat` ou Git Bash):

```bash
./gradlew :lwjgl3:run        # abre a janela do jogo
./gradlew test               # roda os testes (nenhum abre janela ou usa OpenGL)
./gradlew clean build        # compila tudo e roda os testes
```

No **macOS**, o `run` já passa `-XstartOnFirstThread` à JVM (está no
`lwjgl3/build.gradle`). Isso é necessário porque o GLFW, usado pelo LWJGL3, exige
que as chamadas de janela rodem na primeira thread do processo; sem a opção a
janela não abre. Nos outros sistemas a opção não é usada.

Relatório dos testes: `core/build/reports/tests/test/index.html`.

## Ranking

O ranking fica em **`ranking.json` na raiz desta fase** (`fases/fase-04-interface-grafica/`),
porque o `run` do `lwjgl3` usa essa pasta como diretório de trabalho. Guarda as
10 melhores vitórias e está no `.gitignore`. O formato é o mesmo das Fases 0 a 2.

## Controles

| Tela | Teclas |
|---|---|
| **Menu** | `1` Jogar · `2` Ranking · `3` Limpar ranking (depois `S` confirma, `N` ou `Esc` cancela) · `4` Sair |
| **Nova partida** | Digite o nome (até 20 caracteres) e `Enter`; nome vazio vira "Piloto Anônimo". Setas cima/baixo escolhem a dificuldade, `Enter` inicia, `Esc` volta (ao nome, ou ao Menu) |
| **Partida** | `W`/`A`/`S`/`D` ou setas movem a nave · `E` embarca · `Q` ou `Esc` abandona · Depois da vitória ou da derrota, `Enter` continua |
| **Ranking** | `Enter` ou `Esc` volta ao Menu |

Cada toque de tecla na Partida é um turno. Teclas que não fazem nada (por exemplo
`C` ou números) não gastam turno. O mapa é fixo em 11×11 células.
