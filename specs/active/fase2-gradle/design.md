# Design: fase2-gradle

## Abordagem

Copiar `fases/fase-01-refatoracao/src/` inteiro para
`fases/fase-02-gradle/src/main/java/` sem tocar em nenhum byte, e montar ao
redor um projeto Gradle de um módulo só (Groovy, plugin `application`,
Wrapper 8.7). A prova de que nada mudou é um script de hash que compara as
duas árvores.

## Componentes afetados

Todos novos, em `fases/fase-02-gradle/`:

| Arquivo | Conteúdo |
|---|---|
| `src/main/java/missaomarte/**` | Cópia exata de `fases/fase-01-refatoracao/src/missaomarte/**` (inclui `console`, `infrastructure` e `Main` vindos da spec 3) |
| `src/test/java/.gitkeep` | Mantém a pasta de testes versionada até a spec 5 |
| `settings.gradle` | `rootProject.name = 'missao-marte'` |
| `build.gradle` | `plugins { id 'application' }`; `repositories { mavenCentral() }`; `java { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }`; `tasks.withType(JavaCompile).configureEach { options.encoding = 'UTF-8'; options.release = 17 }`; `application { mainClass = 'missaomarte.Main' }`; `run { standardInput = System.in }` |
| `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties` | Wrapper 8.7 (`distributionUrl` com `gradle-8.7-bin.zip`) |
| `.gitignore` | `build/`, `.gradle/`, `out/`, `ranking.json` |
| `scripts/comparar-hash.sh` | Comparação por hash (R4) |
| `README.md` | README mínimo da fase (R5) |

Nada muda fora desta pasta. O gate (`.workflow/check.sh:94-116`) já detecta
a pasta nova, encontra o `gradlew` na raiz dela e roda
`./gradlew clean build` com JDK 17. Não é preciso trocar o gate.

## Fluxo

**Gerar o Wrapper:** com um Gradle qualquer instalado (ex.:
`brew install gradle`), rodar na pasta da fase
`gradle wrapper --gradle-version 8.7 --distribution-type bin` e depois
conferir com `./gradlew --version` usando JDK 17
(`JAVA_HOME=$(/usr/libexec/java_home -v 17)`). O Gradle instalado só serve
para gerar o Wrapper; daí em diante tudo usa `./gradlew`.

**`comparar-hash.sh`:**

```
cd para a pasta do script/.. (raiz da fase); ORIGEM=../fase-01-refatoracao/src; DESTINO=src/main/java
lista A = caminhos relativos dos .java em ORIGEM (ordenados)
lista B = caminhos relativos dos .java em DESTINO (ordenados)
para cada caminho em A ∪ B:
  hash_a = shasum -a 256 (ou "ausente"), hash_b = idem
  imprime "<caminho>  <hash_a>  <hash_b>  OK|DIFERENTE|SÓ NA FASE 1|SÓ NA FASE 2"
resumo: "N arquivos comparados: todos idênticos" → exit 0
        "N arquivos comparados: K com problema"  → exit 1
```

`shasum -a 256` existe no macOS e na maioria das distribuições Linux, então
não é preciso escolher entre `sha256sum` e `shasum`.

## Decisões e alternativas descartadas

- **Decisão:** `options.release = 17` mais `sourceCompatibility`/
  `targetCompatibility`.
  **Alternativa descartada:** `java.toolchain.languageVersion = 17`. Exige
  que o Gradle encontre ou baixe um JDK 17 sozinho; o gate e o README já
  garantem o JDK 17 pelo `JAVA_HOME`, e o download automático precisaria de
  um plugin extra.
- **Decisão:** script `scripts/comparar-hash.sh` na pasta da fase, com a
  saída colada no tutorial (#10). Resposta à pergunta 2 do briefing.
  **Alternativa descartada:** tarefa Gradle de comparação. Colocaria lógica
  de verificação dentro do build e acoplaria a Fase 2 à pasta da Fase 1.
- **Decisão:** README mínimo da fase nesta spec; README da raiz na #10.
  Resposta à pergunta 4 do briefing.
- **Decisão:** `src/test/java/.gitkeep` para versionar a pasta vazia.
  **Alternativa descartada:** deixar a pasta para a spec 5. O escopo pede o
  layout com `src/test/java` já na migração.
- **Decisão:** um único módulo Gradle.
  **Alternativa descartada:** módulos por camada. A separação em módulos
  vem na Fase 3 (`core`/`lwjgl3`); aqui ela mudaria o código de lugar sem
  pedido.

## Riscos e impactos

- **JDK errado:** Gradle 8.7 falha no JDK 25 ("Unsupported class file major
  version"). O gate já procura o JDK 17; para uso manual, o README explica.
- **`ranking.json` no lugar errado:** o `run` do Gradle usa como diretório
  de trabalho a pasta do projeto, então o arquivo cai na raiz da fase,
  coberto pelo `.gitignore`.
- **Bug achado durante a migração:** não corrigir aqui. Registrar nas notas
  da tarefa e avisar o usuário, que abre um `fix-` para a Fase 1. Depois do
  merge, copiar de novo e rodar o hash.
- **Spec 3 ainda não mergeada:** esta spec depende do `Main`, do `console`
  e da `infrastructure` da spec 3. Só começar depois que a spec 3 estiver na
  `main`.

## Estratégia de testes

| Requisito | Como se prova |
|---|---|
| R1.1, R2.1–R2.4, R2.6, R2.7, R5.1 | Inspeção dos arquivos |
| R1.2, R4 | `scripts/comparar-hash.sh` com saída 0; e uma cópia temporária com um arquivo alterado, saída ≠ 0 (não commitada) |
| R1.3 | `git diff main --stat -- fases/fase-00-console fases/fase-01-refatoracao` vazio |
| R2.5, R3.1, R3.3–R3.5 | Roteiro manual: `--version`, uma partida com `run --console=plain`, `installDist` e execução do script gerado, `git status` sem `ranking.json` |
| R3.2, R5.2 | `.workflow/check.sh` |

Não há teste JUnit nesta spec: a infraestrutura de testes chega na spec 5.
