# Tarefas: fase2-gradle

- [ ] 1. Criar a Fase 2 com o código da Fase 1 rodando pelo Gradle Wrapper 8.7
  - status: todo
  - Fatia vertical: copiar `fases/fase-01-refatoracao/src/` para `fases/fase-02-gradle/src/main/java/` sem alterar nenhum arquivo; criar `src/test/java/.gitkeep`, `settings.gradle`, `build.gradle` (conforme o design), `.gitignore` e o Wrapper 8.7 (`gradle wrapper --gradle-version 8.7 --distribution-type bin`).
  - Conferir com JDK 17: `./gradlew --version`, `./gradlew clean build` e uma partida com `./gradlew run --console=plain`. Rodar o `.workflow/check.sh`.
  - Não alterar nenhum `.java`. Se aparecer bug, registrar aqui e avisar o usuário (vira `fix-` na Fase 1).
  - _Requisitos: R1, R2, R3.1, R3.2, R3.3, R5.2_
  - notas:

- [ ] 2. Criar o `comparar-hash.sh`, o README da fase e executar o roteiro manual
  - status: todo
  - `scripts/comparar-hash.sh` conforme o fluxo do design (executável, `shasum -a 256`, saída 0 só com tudo idêntico). Provar a saída ≠ 0 numa cópia temporária com um arquivo alterado, sem commitar a cópia.
  - `README.md` da fase (R5.1).
  - Roteiro manual: `installDist` e execução do script gerado em `build/install/`; `ranking.json` fora do `git status`; `git diff main --stat` vazio para as Fases 0 e 1. Colar nestas notas a saída do `comparar-hash.sh` e o resultado de cada item (vão para o tutorial da #10).
  - _Requisitos: R1.2, R1.3, R3.4, R3.5, R4, R5.1_
  - notas:
