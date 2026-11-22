# Tarefas: fase3-estrutura-libgdx

- [ ] 1. Criar a Fase 3 multi-módulo com as regras da Fase 2 em `core` e a janela abrindo pelo `lwjgl3`
  - status: todo
  - Fatia vertical: `settings.gradle`, `build.gradle` raiz (`gdxVersion = '1.12.1'`), `core/build.gradle`, `lwjgl3/build.gradle`, Wrapper 8.7 copiado de `fases/fase-02-gradle/` e `.gitignore`, conforme o design.
  - Copiar `fases/fase-02-gradle/src/main/java/missaomarte/{domain,application,infrastructure}` para `core/src/main/java/missaomarte/` e `fases/fase-02-gradle/src/test/java/` para `core/src/test/java/`, **sem alterar nenhum byte**. Não copiar `console/` nem `Main.java`.
  - Criar `MissaoMarteGame` (create/dispose com câmera, `FitViewport` 800×600, `SpriteBatch`, `ShapeRenderer`, `BitmapFont`), `TelaBase` (log do ciclo de vida, `resize` → viewport, `InputProcessor` em `show`/`hide`), `TelaInicial` provisória com o título e o `Lwjgl3Launcher` montando as dependências.
  - `run` com `workingDir = rootProject.projectDir` e `-XstartOnFirstThread` só no macOS.
  - Conferir com JDK 17: `./gradlew clean build`, contagem de testes igual à da Fase 2, `./gradlew :lwjgl3:run` abrindo a janela, redimensionar, fechar e ler o log. Rodar o `.workflow/check.sh`.
  - Não alterar nada em `fases/fase-02-gradle/`. Se aparecer bug no código copiado, registrar aqui e avisar o usuário (vira `fix-` na Fase 2).
  - _Requisitos: R1, R2, R3, R4, R5, R6_
  - notas:
