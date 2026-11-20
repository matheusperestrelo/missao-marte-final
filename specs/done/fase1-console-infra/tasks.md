# Tarefas: fase1-console-infra

- [x] 1. Fazer o jogo rodar ponta a ponta no console, com ranking em arquivo, `Main` e `build.sh`
  - status: done
  - Fatia vertical: `FileRankingRepository` (formato e parser da Fase 0, `IOException` → `RankingException`); `Main` de composição; `JogoConsole` com boas-vindas, menu (opções 1 e 4 e opção inválida), leitura de nome, dificuldade e tamanho, e laço da partida; `DesenhoMapa` (mapa, legenda e linha de status); `MensagensConsole` com todas as frases de R2.6 (a vitória mostra só o bloco de vitória por enquanto); `build.sh`.
  - Conferir com `./build.sh run` uma partida até o fim.
  - _Requisitos: R1, R2, R3, R5_
  - notas: Implementado FileRankingRepository (parser/formato da Fase 0, IOException -> RankingException), Main, JogoConsole (menu 1/4/invalida, leituras, laco, alerta de retorno), DesenhoMapa, MensagensConsole (R2.6; VITORIA mostra so o bloco) e build.sh. Verificado: gate ok, grep de instanceof/infrastructure no console vazio, `./build.sh` e partida parcial via stdin (invalida, tamanho abc, comando z, colisao, q, Ctrl+D). Vitoria completa nao exercitada nesta sessao (precisa de jogo interativo; fica para o roteiro da tarefa 3). Menu: opcoes 2 e 3 caem em "Opcao invalida" ate a tarefa 2. JogoConsole recebe `ranking` mas ainda nao usa (sera usado na tarefa 2). Sem teste automatico (spec: Fase 1 sem JUnit).

- [x] 2. Completar vitória, estatísticas e ranking no console
  - status: done
  - Estatísticas com o recorde calculado antes de registrar, "Parabéns" do Top 5, opção 2 (exibir) e opção 3 (limpar com confirmação), tratando `RankingException` com as frases da Fase 0.
  - _Requisitos: R4_
  - notas: Arquivos alterados nesta tarefa (unico): fases/fase-01-refatoracao/src/missaomarte/console/JogoConsole.java (tambem tasks.md, so status/notas da tarefa 2). Nada criado. Adicionado: opcoes 2 (exibir ranking, formato Fase 0) e 3 (confirmacao s/sim, 'Ranking resetado', 'Operacao cancelada.', 'Erro ao resetar ranking: msg'); estatisticas na vitoria com recorde/seriaNovoRecorde calculados antes de registrar; 'Parabens' do Top 5; RankingException ao registrar mostra 'Nao foi possivel salvar o ranking: msg' e depois 'Parabens' (como Fase 0). Verificado: check.sh ok; opcoes 2/3 via stdin com ranking.json de teste (exibir, cancelar, limpar apaga arquivo, vazio). Vitoria completa/estatisticas nao exercitadas (exige partida interativa; roteiro da tarefa 3). Sem teste automatico (Fase 1 sem JUnit).

- [x] 3. Escrever o `README.md` da Fase 1 e executar o roteiro manual
  - status: done
  - README com requisitos, comandos, controles e mudanças intencionais. Executar os 17 casos do roteiro do design e registrar o resultado de cada um nestas notas (eles vão para o tutorial da #7).
  - _Requisitos: R5, R6_
  - notas: Arquivos desta tarefa: criado fases/fase-01-refatoracao/README.md (unico); alterado tasks.md (so status/notas da tarefa 3). Nenhum codigo alterado; sem commit (decisao do usuario: commits so no fim da spec). check.sh passou; grep de instanceof/infrastructure no console vazio. Roteiro executado por stdin roteirizado (driver Python no scratchpad, com bot que navega por BFS e le o mapa; cwd temporario para nao tocar no ranking do repo; Fase 0 compilada em pasta temporaria). Nenhum passo exigiu interacao humana. Resultados (OK = como esperado):
    1. OK: opcao 9 -> 'Opcao invalida. Tente novamente.' e menu; 4 -> 'Obrigado por jogar a Missao Marte Unifor!'.
    2. OK: facil 30 pts, 4 pass., cap 0/4; medio 20 pts, 5 pass., cap 0/3; dificil 45 pts, 5 pass., cap 0/2.
    3. OK: 'c' na origem -> 'Nenhum passageiro nesta posicao.'.
    4. OK: 'Passageiro Dr. Silva embarcado com sucesso! +10 pontos!' (Professor +10, Engenheiro +15, Astronauta +20 observados); A bordo sobe e Restantes desce.
    5. OK: dificil, 2 a bordo, 'c' sobre o 3o -> 'Nave cheia! Nao ha espaco para mais passageiros.'.
    6. OK: na base '2 passageiro(s) entregue(s) na base!', A bordo zera (0/2), Entregues sobe (2/5); tambem '1 passageiro(s)...' e '4 passageiro(s)...'.
    7. OK: ao embarcar o ultimo, fora da base, aparece o ALERTA de retorno (repetido a cada turno ate chegar a base, como na Fase 0).
    8. OK: entrega do ultimo -> bloco DECOLAGEM AUTORIZADA, Estatisticas (Passageiros Resgatados = entregues), 'Parabens! ... Top 5'; ranking.json criado. Vistos tambem 'Novo recorde absoluto do sistema!' e ' - Recorde atual a ser batido: 81 pontos (Piloto: Ana)'. Vitoria em facil, medio e dificil.
    9. OK: 'Alerta! Colisao detectada! Vidas restantes: 2' e '...: 1'; na 3a colisao so 'GAME OVER! A nave foi destruida.'. Obs.: inimigos se movem entre turnos (comportamento herdado, nao e defeito).
    10. OK: pontos a 0 -> 'Combustivel/Pontuacao zerada! Missao perdida.'.
    11. OK: sem registros -> ' - Nenhum registro encontrado. Seja o primeiro a jogar!'; com registros -> '1. Ana - 54 pts | Dificuldade: Facil | Coletados: 4 | Tempo: 3s | <data>' entre as linhas '=' do Top 5.
    12. OK: 's' -> 'Ranking resetado com sucesso!' e ranking.json apagado (opcao 2 depois mostra vazio); 'n' -> 'Operacao cancelada.'. Nao exercitado: 'Erro ao resetar ranking' e 'Nao foi possivel salvar o ranking' (exigem falha de E/S).
    13. OK: 'q' -> 'Missao abortada pelo piloto.'.
    14. OK: Ctrl+D no menu 'Entrada encerrada. Obrigado por jogar!'; antes de decolar 'Entrada encerrada. Missao nao iniciada.'; na partida 'Entrada encerrada. Missao abortada.' (seguida da frase do menu, como na Fase 0).
    15. OK: nome vazio -> 'Piloto Anonimo' no cabecalho; mapa 'abc' -> 'Entrada invalida, usando tamanho padrao (5).'; 'z' -> 'Comando invalido.'.
    16. OK nos dois sentidos: ranking.json gravado pela Fase 0 lido e exibido pela Fase 1; o gravado pela Fase 1 (2 entradas, ordenado) lido pela Fase 0.
    17. OK: em 168 linhas de status de todas as partidas (facil/medio/dificil, mapas 4 e 5) a bordo + entregues + restantes = total, 0 violacoes.
    Fora do roteiro/sugestao: nenhuma pendencia. Textos acima sem acentos so por simplicidade; as frases reais tem acentos.
