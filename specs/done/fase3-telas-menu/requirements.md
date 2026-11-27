> **Concluída em 2026-11-26.** Diferenças do planejado: implementada fora do workflow no PR #38, sem `reviews/`; regularizada depois. A tela da Partida provisória (R5) foi substituída pela `TelaPartida` real da spec 11. Itens manuais (R1.1, R1.4, R3.1, R6.5) seguem pendentes de QA na janela.

# Requisitos: fase3-telas-menu

**Issue:** #14 (etapa 2) · **Trilha:** C · **Fase:** 3 · **Depende de:** 8 (`fase3-estrutura-libgdx`), 9 (`fase3-ranking-service`)
**Branch sugerida:** `feat/issue-14-fase3-telas-menu` · **PR:** "Closes #14"

## Contexto

Com a estrutura (spec 8) e o `RankingService` (spec 9) prontos, a janela
precisa da navegação do jogo: o Menu, a confirmação de limpar o ranking, a
tela de Nova partida e a tela de Ranking. Elas substituem o menu de texto
da Fase 2. As telas só mostram dados e repassam comandos para a
`GameSession` e o `RankingService`; nenhuma decide regra de jogo. A tela da
Partida em si é a spec 11. Aqui ela existe só como uma tela provisória.

Base: issue #14, `ESCOPO-PROJETO.md`, seção 8, "Classe `Game` e telas", e
o resumo do alinhamento da Fase 3 (Nova partida com setas, mapa fixo,
confirmação de limpar).

## Fora de escopo

- Mapa, painel, entrada tecla → `Acao` e fim da partida: spec 11
  (`fase3-partida`, #16).
- Registrar a pontuação no ranking: spec 11. Aqui a tela de Ranking só
  sabe **mostrar** um resumo que receber.
- Escolher o tamanho do mapa: decidido no alinhamento que o mapa é fixo em
  11×11 e a tela não pergunta (`ESCOPO-PROJETO.md:299` pede "nome +
  dificuldade"). É mudança em relação ao original, registrada no tutorial
  (spec 12).
- Escolher a dificuldade por teclas `1`/`2`/`3`: descartado no alinhamento
  em favor de setas e `Enter`.
- Lista de dificuldade que dá a volta (de Difícil para Fácil): descartado.
- Mouse, toque, botões gráficos, assets e sons: Fases 4 e 6.
- Repetição de tecla: cortada do projeto.

## Requisitos

### R1 — Menu principal

**História:** Como jogador, quero um menu na janela para jogar, ver o
ranking, limpar o ranking ou sair.

**Critérios de aceitação:**

1. QUANDO o jogo abrir O SISTEMA DEVE mostrar o Menu com o título "Missão
   Marte Unifor" e as opções "1 - Jogar", "2 - Ranking", "3 - Limpar
   ranking" e "4 - Sair". _(verificação: manual)_
2. QUANDO o jogador apertar `1` no Menu O SISTEMA DEVE abrir a tela Nova
   partida. _(verificação: teste do controle do menu + manual)_
3. QUANDO o jogador apertar `2` no Menu O SISTEMA DEVE abrir a tela
   Ranking sem resumo de partida. _(verificação: teste + manual)_
4. QUANDO o jogador apertar `4` no Menu O SISTEMA DEVE fechar o jogo,
   liberando os recursos. _(verificação: manual)_
5. QUANDO o jogador apertar qualquer outra tecla no Menu O SISTEMA DEVE
   ignorá-la, sem mudar de tela. _(verificação: teste)_

### R2 — Limpar o ranking com confirmação

**História:** Como jogador, quero confirmar antes de apagar o ranking, para
não perder os recordes por engano.

**Critérios de aceitação:**

1. QUANDO o jogador apertar `3` no Menu O SISTEMA DEVE mostrar "Limpar o
   ranking? S confirma, N ou Esc cancela" e esperar a resposta, sem limpar
   ainda. _(verificação: teste + manual)_
2. QUANDO o jogador apertar `S` na confirmação O SISTEMA DEVE pedir ao
   serviço de ranking para limpar e mostrar "Ranking limpo." no Menu.
   _(verificação: teste com serviço em memória)_
3. QUANDO o jogador apertar `N` ou `Esc` na confirmação O SISTEMA DEVE
   mostrar "Operação cancelada." e não limpar nada.
   _(verificação: teste)_
4. ENQUANTO a confirmação estiver aberta O SISTEMA DEVE ignorar as outras
   teclas, inclusive `1`, `2` e `4`. _(verificação: teste)_
5. SE o serviço falhar ao limpar ENTÃO O SISTEMA DEVE mostrar "Não foi
   possível limpar o ranking: <mensagem do erro>" no Menu, sem fechar o
   jogo. _(verificação: teste com repositório que falha)_
6. QUANDO o jogador apertar qualquer tecla depois de uma dessas mensagens
   O SISTEMA DEVE apagar a mensagem e tratar a tecla como uma opção normal
   do Menu. _(verificação: teste)_

### R3 — Nova partida: nome do piloto

**História:** Como jogador, quero digitar meu nome antes de jogar, para
aparecer no ranking.

**Critérios de aceitação:**

1. QUANDO a tela Nova partida abrir O SISTEMA DEVE mostrar o campo do nome
   vazio e em foco, com a dica "Enter confirma · vazio = Piloto Anônimo ·
   Esc volta ao Menu". _(verificação: manual)_
2. QUANDO o jogador digitar um caractere imprimível O SISTEMA DEVE
   acrescentá-lo ao nome, até **20** caracteres. A partir do 20º, os
   caracteres digitados são ignorados. _(verificação: teste)_
3. QUANDO o jogador apertar `Backspace` O SISTEMA DEVE apagar o último
   caractere do nome, se houver. _(verificação: teste)_
4. QUANDO o jogador apertar `Enter` no nome O SISTEMA DEVE passar o foco
   para a escolha da dificuldade, mesmo com o nome vazio.
   _(verificação: teste)_
5. QUANDO o jogador apertar `Esc` no nome O SISTEMA DEVE voltar ao Menu sem
   iniciar partida. _(verificação: teste + manual)_

### R4 — Nova partida: dificuldade

**Critérios de aceitação:**

1. QUANDO o foco passar para a dificuldade O SISTEMA DEVE mostrar a lista
   "Fácil", "Médio" e "Difícil", nessa ordem, com **Fácil** selecionado e
   a seleção destacada. _(verificação: teste + manual)_
2. QUANDO o jogador apertar `↓` O SISTEMA DEVE selecionar a próxima opção.
   Na última (Difícil), a seleção não muda. _(verificação: teste)_
3. QUANDO o jogador apertar `↑` O SISTEMA DEVE selecionar a opção
   anterior. Na primeira (Fácil), a seleção não muda.
   _(verificação: teste)_
4. QUANDO o jogador apertar `Esc` na dificuldade O SISTEMA DEVE voltar o
   foco ao nome, mantendo o que foi digitado. _(verificação: teste)_
5. QUANDO o jogador apertar `Enter` na dificuldade O SISTEMA DEVE iniciar
   uma partida nova com o nome digitado, a dificuldade selecionada e o mapa
   de **11×11**, e abrir a tela da Partida. _(verificação: teste do
   formulário + manual)_
6. SE o nome estiver vazio ou só com espaços ENTÃO O SISTEMA DEVE iniciar a
   partida com o piloto "Piloto Anônimo", pela regra que já existe na
   sessão de jogo. _(verificação: manual; a regra já tem teste na Fase 2)_
7. ENQUANTO o foco estiver na dificuldade O SISTEMA DEVE ignorar letras e
   números, sem mudar o nome. _(verificação: teste)_

### R5 — Tela da Partida provisória

**Critérios de aceitação:**

1. QUANDO a partida for iniciada O SISTEMA DEVE mostrar uma tela com
   "Partida iniciada: <piloto> - <dificuldade>" e "Esc volta ao Menu". A
   spec 11 substitui essa tela pela Partida de verdade.
   _(verificação: manual)_
2. QUANDO o jogador apertar `Esc` nessa tela O SISTEMA DEVE voltar ao Menu.
   _(verificação: manual)_

### R6 — Tela de Ranking

**História:** Como jogador, quero ver o Top 10 e, depois de uma vitória, o
resumo da minha partida.

**Critérios de aceitação:**

1. QUANDO a tela Ranking abrir O SISTEMA DEVE mostrar o título "Ranking
   Top 10" e, para cada entrada do Top 10, uma linha
   "<posição>. <nome> - <pontos> pts | <dificuldade> | Entregues:
   <passageiros> | Tempo: <segundos>s | <data e hora>".
   _(verificação: teste da formatação + manual)_
2. QUANDO o ranking estiver vazio O SISTEMA DEVE mostrar "Nenhum registro
   encontrado. Seja o primeiro a jogar!". _(verificação: teste + manual)_
3. QUANDO a tela Ranking for aberta **com** o resumo de uma partida O
   SISTEMA DEVE mostrar, acima do Top 10, a pontuação final, os
   movimentos, o tempo de jogo, os passageiros entregues e "Parabéns! Você
   entrou para o Top 10!" ou "Sua pontuação não entrou no Top 10.",
   conforme o que for informado. _(verificação: teste da formatação)_
4. QUANDO a tela Ranking for aberta **sem** resumo (pelo Menu) O SISTEMA
   NÃO DEVE mostrar o bloco do resumo. _(verificação: teste)_
5. QUANDO o jogador apertar `Enter` ou `Esc` na tela Ranking O SISTEMA DEVE
   voltar ao Menu. Outras teclas são ignoradas. _(verificação: manual)_

### R7 — Separação entre tela e regra

**Critérios de aceitação:**

1. O SISTEMA NÃO DEVE ter regra de jogo nem de ranking nas telas: limpar,
   iniciar a partida, o piloto padrão e o conteúdo do Top 10 vêm da sessão
   de jogo e do serviço de ranking. _(verificação: revisão)_
2. O SISTEMA DEVE ter a lógica de navegação e de formulário (menu,
   confirmação, nome e lista de dificuldade, linhas do ranking) em classes
   **sem** import de `com.badlogic`, testadas sem janela nem OpenGL.
   _(verificação: inspeção + gate)_
3. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar.
   _(verificação: gate)_
