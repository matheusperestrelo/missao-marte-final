# Fase 5 — Servidor e ranking global

Hoje esta pasta contém só o `servidor/`: um projeto Spring Boot com
cadastro de usuário, login por token JWT e verificação de saúde. Partidas,
histórico e ranking chegam na spec seguinte. A pasta `cliente/` (o jogo
falando com o servidor) só nasce depois da Fase 4, copiando-a.

## Dois builds independentes

Cliente e servidor são dois projetos Gradle separados, cada um com o seu
Wrapper:

- o cliente herda o Gradle 8.7 das fases anteriores;
- o servidor usa Spring Boot 4, que exige o Gradle 9.8.2.

O servidor não depende do código do jogo: se precisar de algo, duplica.
Assim cada lado evolui e é testado sozinho.

Para rodar o servidor, veja [`servidor/README.md`](servidor/README.md).
