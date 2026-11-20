# Tarefas: fase1-console-infra

- [ ] 1. Fazer o jogo rodar ponta a ponta no console, com ranking em arquivo, `Main` e `build.sh`
  - status: todo
  - Fatia vertical: `FileRankingRepository` (formato e parser da Fase 0, `IOException` → `RankingException`); `Main` de composição; `JogoConsole` com boas-vindas, menu (opções 1 e 4 e opção inválida), leitura de nome, dificuldade e tamanho, e laço da partida; `DesenhoMapa` (mapa, legenda e linha de status); `MensagensConsole` com todas as frases de R2.6 (a vitória mostra só o bloco de vitória por enquanto); `build.sh`.
  - Conferir com `./build.sh run` uma partida até o fim.
  - _Requisitos: R1, R2, R3, R5_
  - notas:

- [ ] 2. Completar vitória, estatísticas e ranking no console
  - status: todo
  - Estatísticas com o recorde calculado antes de registrar, "Parabéns" do Top 5, opção 2 (exibir) e opção 3 (limpar com confirmação), tratando `RankingException` com as frases da Fase 0.
  - _Requisitos: R4_
  - notas:

- [ ] 3. Escrever o `README.md` da Fase 1 e executar o roteiro manual
  - status: todo
  - README com requisitos, comandos, controles e mudanças intencionais. Executar os 17 casos do roteiro do design e registrar o resultado de cada um nestas notas (eles vão para o tutorial da #7).
  - _Requisitos: R5, R6_
  - notas:
