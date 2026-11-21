# Missão Marte Unifor — Fase 2 (Gradle e JUnit 5)

O mesmo jogo de console da Fase 1, agora num projeto Gradle com Wrapper 8.7
e testes JUnit 5. **O código em `src/main/java` é idêntico ao da Fase 1**
(arquivo por arquivo, conferido por hash); a fase só muda o build e acrescenta
os testes. Detalhes: [docs/fase-02-tutorial.md](../../docs/fase-02-tutorial.md).

## Requisitos

- **JDK 17.** O Gradle 8.7 não roda em JDKs mais novos (no JDK 25 ou 26 falha
  com "Unsupported class file major version"). Se `java -version` não mostrar
  17, aponte o `JAVA_HOME` para um JDK 17:
  - macOS: `brew install openjdk@17` e `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`
  - Windows/Linux: instale o Temurin 17 (adoptium.net) e ajuste o `JAVA_HOME`
- Não é preciso ter o Gradle instalado: o `./gradlew` baixa a versão 8.7 sozinho.

## Comandos

Execute a partir de `fases/fase-02-gradle` (no Windows, `gradlew.bat` ou Git Bash):

```bash
./gradlew --version            # Gradle 8.7 e JVM 17
./gradlew clean build          # compila, roda os testes e empacota
./gradlew test                 # só os testes
./gradlew run --console=plain  # joga no terminal
./gradlew installDist          # gera build/install/missao-marte/bin/missao-marte
```

Relatório dos testes: `build/reports/tests/test/index.html`.

O `ranking.json` é gravado na pasta de onde o jogo é executado (com
`./gradlew run`, na raiz desta fase) e está no `.gitignore`.

## Comparação por hash com a Fase 1

```bash
./scripts/comparar-hash.sh
```

Lista cada `.java` com o SHA-256 nas duas fases e termina com o resumo. Sai
com código 0 só se todos forem idênticos e nenhum faltar ou sobrar.

## Controles

`w`, `a`, `s`, `d` movem a nave, `c` embarca e `q` aborta a missão (os
mesmos da Fase 1).
