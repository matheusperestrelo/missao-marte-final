# Verificação final — fase1-console-infra

**Veredito:** APROVADA
**Branch:** feat/issue-6-fase1-console-infra
**Commits da spec:** 1 (`8248268`, só o `docs:` de início). Por decisão do usuário, nesta spec nenhum commit é feito durante as tarefas: as tarefas 1, 2 e 3 estão na working tree sobre `8248268`, sem commit, e todos os commits ficam para o fim da spec. Isso é esperado e não é pendência. Este `final.md` também não foi commitado.

## Cobertura

A Fase 1 não tem JUnit ("Fora de escopo" do `requirements.md` e "Estratégia de testes" do `design.md`). Cada critério é provado pelo tipo de verificação que a própria spec indica (manual, inspeção ou gate). A prova está nas revisões por tarefa e no roteiro de 17 casos registrado nas `notas:` da tarefa 3. Nesta verificação, rodei de novo o `check.sh`, o grep de camadas do console (vazio) e uma partida curta por stdin (menu, opção inválida, opção 2 sem registros, tamanho `abc`, `c` sem passageiro, `z`, `q`, opção 3 com `n` e opção 4), com saída de acordo com a spec.

| Requisito | Critério | Verificação | Situação |
|---|---|---|---|
| R1 | 1 | manual (01-r1; roteiro 1) | coberto |
| R1 | 2 | manual (01-r1; roteiro 1) | coberto |
| R1 | 3 | manual (01-r1; roteiro 15) | coberto |
| R1 | 4 | manual (01-r1; roteiro 1) | coberto |
| R1 | 5 | manual (01-r1; roteiro 14) | coberto |
| R1 | 6 | inspeção `JogoConsole.java` (01-r1) | coberto |
| R2 | 1 | manual (01-r1) | coberto |
| R2 | 2 | inspeção `DesenhoMapa.java` + grep vazio (01-r1; refeito aqui) | coberto |
| R2 | 3 | manual + inspeção (01-r1) | coberto |
| R2 | 4 | manual (01-r1; roteiro 17) | coberto |
| R2 | 5 | inspeção `JogoConsole.java` (01-r1) | coberto |
| R2 | 6 | manual + inspeção `MensagensConsole.java` (01-r1; roteiro 3-10) | coberto |
| R2 | 7 | manual (01-r1; roteiro 13, 15) | coberto |
| R2 | 8 | manual (01-r1 harness; roteiro 7) | coberto |
| R3 | 1 | inspeção `FileRankingRepository.java` (01-r1) | coberto |
| R3 | 2 | manual, nos dois sentidos (01-r1; roteiro 16) | coberto |
| R3 | 3 | inspeção + manual (01-r1) | coberto |
| R3 | 4 | manual, ordenação e ponto *b* (01-r1) | coberto |
| R3 | 5 | manual (01-r1; roteiro 12) | coberto |
| R3 | 6 | manual com falha provocada (01-r1) | coberto |
| R4 | 1 | manual (02-r1 harness; roteiro 8) | coberto |
| R4 | 2 | manual, inclusive falha de gravação (02-r1; roteiro 8) | coberto |
| R4 | 3 | manual (02-r1; roteiro 11) | coberto |
| R4 | 4 | manual, inclusive falha ao limpar (02-r1; roteiro 12) | coberto |
| R5 | 1 | inspeção `Main.java` (01-r1; conferido aqui) | coberto |
| R5 | 2 | inspeção + grep (01-r1, 03-r1; refeito aqui) | coberto |
| R5 | 3 | manual `./build.sh` e `./build.sh run` (01-r1) | coberto |
| R5 | 4 | inspeção `fases/fase-01-refatoracao/README.md` (03-r1; conferido aqui) | coberto |
| R5 | 5 | gate `.workflow/check.sh` (refeito aqui) | coberto |
| R6 | 1 | roteiro manual, 17 casos OK (notas da tarefa 3; 03-r1 reexecutou parte) | coberto |
| R6 | 2 | roteiro 5, 6, 8 e 17 (03-r1) | coberto |

## check.sh

passou (Fase 0 imutável, javac da Fase 1 e regras de camadas).

## Pendências

Nenhuma impede a aprovação.

- Fluxo de versionamento: os arquivos das tarefas 1 a 3, as revisões e este `final.md` ainda não têm commit (decisão do usuário: commits no fim da spec). Antes do PR, eles precisam ser commitados.
- Revisão 03-r1, sugestão 1: `fases/fase-01-refatoracao/README.md:53-54` cita só os pontos de atenção *a* e *b* como preservados. Também foram preservados *c* (só o 1º caractere do comando conta) e *d* (mapa de tamanho 1 no Médio/Difícil trava). Vale citar o *d*, que trava o jogo e pode ser reportado como defeito novo.
- Revisão 02-r1, sugestão 1: `console/JogoConsole.java:146` repete no console a guarda `recorde > 0` da Fase 0. Hoje o resultado é o mesmo, mas é um pedaço da regra do recorde fora da `application` (R5.2). Uma candidata para depois é um `RankingTop5.recordeASerBatido()`.
- Revisão 02-r1, sugestão 2: `console/JogoConsole.java:155-157` (`entrou = true` no `catch`) supõe que `registrar` só lança a exceção depois de decidir que a entrada entra no Top 5. Falta um comentário explicando essa suposição.
- Revisão 01-r1, sugestão 1: `console/MensagensConsole.java:51-52` tem um `default: break;`, então um `TipoEvento` novo passaria sem frase e sem aviso.
- Revisão 01-r1, sugestão 3: o separador `====` está repetido em `JogoConsole.java:56-60` (cosmético).
- Notas da tarefa 3, item 12: os caminhos de erro de E/S ("Erro ao resetar ranking" e "Não foi possível salvar o ranking") aparecem como "não exercitados" no roteiro. Eles foram exercitados com falha provocada em `reviews/02-r1.md` (R4.2 e R4.4). Isso não é lacuna, mas vale registrar no tutorial da #7.
- Observação: uma linha vazia como primeira entrada do menu gera "Opção inválida." (é o ponto *a* da Fase 0, preservado de propósito).

Desvios do `design.md` registrados nas notas: nenhum.

## Roteiro de QA

Execute em `fases/fase-01-refatoracao`. De preferência, use uma cópia ou apague o `ranking.json` local antes, para começar sem registros.

1. `./build.sh` → **esperado:** termina sem erro e cria `out/`. Introduza um erro de sintaxe temporário num `.java` e rode de novo → **esperado:** código de saída diferente de zero e o jogo não executa. Desfaça o erro depois. (R5.3)
2. `./build.sh run`, pressione Enter → **esperado:** a tela de boas-vindas "MISSÃO MARTE UNIFOR - VERSÃO 10.0" e o menu de 4 opções, iguais aos da Fase 0. (R1.1, R1.6)
3. Digite `9` → **esperado:** "Opção inválida. Tente novamente." e o menu de novo. (R1.2)
4. Opção `2` sem `ranking.json` → **esperado:** " - Nenhum registro encontrado. Seja o primeiro a jogar!" entre as linhas do Top 5. (R4.3)
5. Opção `1`, nome vazio, `facil`, tamanho `abc`, Enter → **esperado:** "Entrada inválida, usando tamanho padrão (5).", "Iniciando missão na dificuldade Fácil...", "Pressione Enter para decolar!", e o mapa com "Piloto Anônimo", a legenda exata da Fase 0 e a linha `Nave em (0,0) | Pontos: 30 | Vidas: 3 | A bordo: 0/4 | Entregues: 0/4 | Restantes: 4`. (R1.3, R2.1, R2.3, R2.4)
6. Na partida, digite `c` na origem, depois `z` e depois uma linha vazia → **esperado:** "Nenhum passageiro nesta posição.", "Comando inválido." e, para a linha vazia, só um novo pedido de comando, sem mensagem. (R2.5, R2.6, R2.7)
7. Vá até um passageiro e digite `c` → **esperado:** "Passageiro <nome> embarcado com sucesso! +<10/15/20> pontos!", "A bordo" sobe e "Restantes" desce. (R2.6)
8. Volte à base `(0,0)` → **esperado:** "1 passageiro(s) entregue(s) na base!", "A bordo" zera e "Entregues" sobe. Confira que a bordo + entregues + restantes = total em toda linha de status. (R2.6, R6.2)
9. Embarque todos os passageiros restantes e fique fora da base → **esperado:** "✨ ALERTA: Todos os passageiros resgatados! Retorne para a Plataforma de Pouso 'L' em (0,0)..." a cada turno. (R2.8)
10. Entregue o último na base → **esperado:** o bloco de vitória da Fase 0, as estatísticas ("Passageiros Resgatados" = entregues), "Parabéns! Você entrou para o Top 5 de pilotos!" e `ranking.json` criado. A vitória só acontece depois da entrega, não ao embarcar. (R4.1, R4.2, R6.2)
11. Jogue de novo e vença com menos pontos → **esperado:** "Recorde atual a ser batido: N pontos (Piloto: X)". Vença com mais pontos → **esperado:** "🏆 Novo recorde absoluto do sistema!". (R4.1)
12. Opção `2` → **esperado:** as entradas ordenadas por pontuação decrescente, no formato `1. Nome - P pts | Dificuldade: Fácil | Coletados: N | Tempo: Ts | <data>`. (R4.3, R3.4)
13. Partida no `dificil`: embarque 2 e tente o 3º → **esperado:** "Nave cheia! Não há espaço para mais passageiros." e capacidade `0/2` na linha de status. Confira também `0/3` no `medio`. (R6.2)
14. Colida com `#` ou `X` → **esperado:** "Alerta! Colisão detectada! Vidas restantes: 2". Na 3ª colisão, só "GAME OVER! A nave foi destruída.". (R2.6)
15. Ande sem objetivo até zerar os pontos → **esperado:** "Combustível/Pontuação zerada! Missão perdida.". (R2.6)
16. `q` numa partida → **esperado:** "Missão abortada pelo piloto." e a volta ao menu. (R2.7)
17. Opção `3` com `n` → **esperado:** "Operação cancelada.". Opção `3` com `s` → **esperado:** "Ranking resetado com sucesso!" e `ranking.json` apagado. (R4.4, R3.5)
18. Ctrl+D no menu, depois de digitar o tamanho (antes de decolar) e durante a partida → **esperado:** "Entrada encerrada. Obrigado por jogar!", "Entrada encerrada. Missão não iniciada." e "Entrada encerrada. Missão abortada.". (R1.5)
19. Opção `4` → **esperado:** "Obrigado por jogar a Missão Marte Unifor!" e o programa encerra. (R1.4)
20. Compatibilidade: rode a Fase 0 (`fases/fase-00-console`) na mesma pasta de trabalho, vença uma partida e abra a opção 2 na Fase 1. Depois faça o caminho inverso → **esperado:** cada fase lê o ranking gravado pela outra. (R3.2)
21. Falha de E/S (opcional): troque `ranking.json` por um diretório não vazio e use a opção 3 → **esperado:** "Erro ao resetar ranking: ranking.json". (R4.4, R3.6)

## Próximos passos

- APROVADA: commitar o trabalho das tarefas 1 a 3, as revisões e este relatório (fluxo desta spec: commits no fim). Depois, QA pelo usuário, merge e conclusão da spec (active/ → done/).
- Opcional: decidir se as sugestões acima (README com *c*/*d*, `default` do `switch`, guarda do recorde) entram antes do merge como ajuste ou ficam para depois.
