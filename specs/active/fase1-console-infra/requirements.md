# Requisitos: fase1-console-infra

**Issue:** #6 · **Trilha:** B (console e interfaces) · **Fase:** 1 · **Depende de:** 2 (`fase1-application`)
**Branch sugerida:** `feat/issue-6-fase1-console-infra` · **PR:** "Closes #6"

## Contexto

Com o `domain` (spec 1) e a `application` (spec 2) prontos, falta ligar as
camadas e devolver ao jogador um jogo de console executável. Esse jogo deve
se comportar como a Fase 0, exceto nas mudanças intencionais da Fase 1. Esta
spec cria a camada `console` (menu, leitura, desenho e tradução dos
eventos em frases), a `infrastructure` (ranking em arquivo JSON), o `Main`
de composição e o `build.sh`.

Comportamento de referência: `docs/analise-inicial.md`, seções 3 a 6, e o
código da Fase 0 em `fases/fase-00-console/src/Main.java`.

## Fora de escopo

- Qualquer regra de jogo no console: elas estão na `application` (spec 2).
- Biblioteca de JSON: o JSON continua escrito e lido à mão, como na Fase 0
  (`fase-00-arquitetura.md`, decisão 7).
- Corrigir os pontos de atenção *a*, *b*, *c* e *d* da análise: são
  preservados (*a*: o menu só aparece depois da primeira linha; *b*: nome
  com vírgula some do ranking).
- `build.ps1` para Windows: só `build.sh` (equipe em macOS/Linux).
- Testes automáticos: Fase 2 (#9). Aqui a verificação é o gate e o
  roteiro manual.
- Tutorial da Fase 1 (`docs/fase-01-tutorial.md`): issue #7, sem spec.
  O resultado do roteiro manual desta spec alimenta o tutorial.

## Requisitos

### R1 — Menu e fluxo do programa iguais aos da Fase 0

**História:** Como jogador da versão original, quero o mesmo menu e o mesmo
fluxo, para não estranhar a versão refatorada.

**Critérios de aceitação:**

1. QUANDO o programa iniciar O SISTEMA DEVE exibir a mesma tela de
   boas-vindas e o mesmo menu de 4 opções da Fase 0 (`Main.java:64-79`),
   com os mesmos textos. _(verificação: manual)_
2. SE a opção for desconhecida ENTÃO O SISTEMA DEVE exibir "Opção
   inválida. Tente novamente." e voltar ao menu. _(verificação: manual)_
3. QUANDO a opção for 1 O SISTEMA DEVE pedir nome, dificuldade e tamanho
   com os mesmos textos da Fase 0 e mostrar "Iniciando missão na
   dificuldade …" e "Pressione Enter para decolar!". Tamanho não numérico
   mostra "Entrada inválida, usando tamanho padrão (5)."
   _(verificação: manual)_
4. QUANDO a opção for 4 O SISTEMA DEVE exibir "Obrigado por jogar a Missão
   Marte Unifor!" e encerrar. _(verificação: manual)_
5. QUANDO a entrada terminar (Ctrl+D) O SISTEMA DEVE se comportar como a
   Fase 0: no menu, "Entrada encerrada. Obrigado por jogar!"; antes de
   decolar, "Entrada encerrada. Missão não iniciada."; durante a partida,
   "Entrada encerrada. Missão abortada.". _(verificação: manual)_
6. O SISTEMA DEVE preservar o ponto *a*: o menu só aparece depois que há
   uma linha para ler. _(verificação: manual)_

### R2 — Partida no console

**História:** Como jogador, quero ver o mapa, os indicadores e as mensagens
de cada turno, para decidir a próxima jogada.

**Critérios de aceitação:**

1. QUANDO um turno começar O SISTEMA DEVE desenhar o mapa no formato da
   Fase 0 (`Main.java:328-392`): cabeçalho com pontos e piloto,
   coordenadas, linha `__`, linhas com o número de y e os símbolos, a
   legenda, a linha de comandos e a lista "Passageiros na superfície
   marciana:". _(verificação: manual)_
2. O SISTEMA DEVE escolher o símbolo de cada célula a partir dos elementos
   da missão, na ordem de prioridade dada pela `Missao` (nave, passageiro,
   asteroide, inimigo, base). Células sem elemento mostram `.`.
   _(verificação: inspeção, sem `instanceof` e sem `char` fixo de elemento no
   console)_
3. O SISTEMA DEVE montar a legenda a partir dos tipos de elemento,
   produzindo exatamente a linha da Fase 0: `Legenda: @=Nave, P=Professor,
   E=Engenheiro, T=Astronauta, #=Asteroide, X=Inimigo, L=Plataforma de
   Pouso, .=Vazio`. _(verificação: manual + inspeção)_
4. O SISTEMA DEVE exibir a linha de status com o campo novo "Entregues":
   `Nave em (x,y) | Pontos: p | Vidas: v | A bordo: b/capacidade |
   Entregues: e/total | Restantes: r`. **Mudança intencional.**
   _(verificação: manual)_
5. QUANDO o comando for lido O SISTEMA DEVE converter o texto em `Acao` e
   passar à sessão. Linha vazia não é comando: volta a pedir, sem mensagem,
   como na Fase 0. _(verificação: inspeção)_
6. QUANDO o turno devolver eventos O SISTEMA DEVE exibir, na ordem dos
   eventos, as frases:

   | Evento | Frase |
   |---|---|
   | MOVIMENTO | (nenhuma) |
   | EMBARQUE | `Passageiro <nome> embarcado com sucesso! +<pontos> pontos!` |
   | EMBARQUE_RECUSADO_SEM_PASSAGEIRO | `Nenhum passageiro nesta posição.` |
   | EMBARQUE_RECUSADO_NAVE_CHEIA | `Nave cheia! Não há espaço para mais passageiros.` |
   | ENTREGA | `<n> passageiro(s) entregue(s) na base!` **(frase nova)** |
   | COLISAO com vidas > 0 | `Alerta! Colisão detectada! Vidas restantes: <v>` |
   | COLISAO com 0 vidas | (nenhuma; o DERROTA_NAVE_DESTRUIDA a seguir mostra o GAME OVER) |
   | DERROTA_NAVE_DESTRUIDA | `GAME OVER! A nave foi destruída.` |
   | DERROTA_PONTUACAO_ZERADA | `Combustível/Pontuação zerada! Missão perdida.` |
   | VITORIA | o bloco de vitória da Fase 0 (`Main.java:181-184`), seguido das estatísticas (R4) |

   _(verificação: manual + inspeção)_
7. QUANDO a sessão devolver comando inválido O SISTEMA DEVE exibir
   "Comando inválido.". Para SAIR, exibe "Missão abortada pelo piloto.".
   _(verificação: manual)_
8. QUANDO a partida continuar, não houver mais passageiros no mapa e
   houver passageiros a bordo O SISTEMA DEVE exibir o alerta da Fase 0:
   `✨ ALERTA: Todos os passageiros resgatados! Retorne para a Plataforma de
   Pouso 'L' em (0,0) para completar a missão.` _(verificação: manual)_

### R3 — Ranking em arquivo

**História:** Como jogador, quero que o ranking continue salvo em
`ranking.json`, para não perder o histórico e poder trocar o armazenamento
sem mexer nas regras.

**Critérios de aceitação:**

1. O SISTEMA DEVE implementar `RankingRepository` em
   `missaomarte.infrastructure.ranking`, gravando e lendo `ranking.json` na
   pasta de onde o jogo é executado. O JSON é escrito e lido à mão.
   _(verificação: inspeção)_
2. O SISTEMA DEVE gravar no mesmo formato da Fase 0 (`Main.java:453-481`):
   chaves `name`, `score`, `dificuldade` (nome do enum), `passageirosColetados`,
   `dataHora` e `tempoJogo`. Um `ranking.json` gravado pela Fase 0 deve ser
   lido pela Fase 1, e vice-versa. _(verificação: manual)_
3. QUANDO o arquivo não existir ou não puder ser lido O SISTEMA DEVE
   devolver um ranking vazio, sem mensagem. _(verificação: inspeção)_
4. O SISTEMA DEVE devolver as entradas ordenadas por pontuação
   decrescente, como `parseRankingJson` (`Main.java:483-550`), preservando o
   ponto *b*. _(verificação: inspeção)_
5. QUANDO o ranking for limpo O SISTEMA DEVE apagar o arquivo.
   _(verificação: manual)_
6. SE gravar ou apagar falhar ENTÃO O SISTEMA DEVE lançar a exceção de
   ranking da `application` com a mensagem do erro. O `infrastructure` não
   escreve no console. _(verificação: inspeção)_

### R4 — Estatísticas e ranking no console

**Critérios de aceitação:**

1. QUANDO houver vitória O SISTEMA DEVE exibir as estatísticas da Fase 0
   (`Main.java:212-226`): pontuação final, movimentos, tempo de jogo e
   "Passageiros Resgatados", que agora conta os **entregues**. Também mostra
   "🏆 Novo recorde absoluto do sistema!" ou "Recorde atual a ser batido",
   conforme a regra da `application`. _(verificação: manual)_
2. QUANDO a vitória entrar no Top 5 O SISTEMA DEVE exibir "Parabéns! Você
   entrou para o Top 5 de pilotos!". SE a gravação falhar ENTÃO O SISTEMA
   DEVE exibir antes "Não foi possível salvar o ranking: <mensagem>", como
   na Fase 0. _(verificação: manual)_
3. QUANDO a opção 2 for escolhida O SISTEMA DEVE exibir o ranking no
   formato da Fase 0 (`Main.java:404-416`), ou "Nenhum registro encontrado.
   Seja o primeiro a jogar!". _(verificação: manual)_
4. QUANDO a opção 3 for escolhida O SISTEMA DEVE pedir confirmação. Com
   `s`/`sim`, limpa e mostra "Ranking resetado com sucesso!"; com outra
   resposta, mostra "Operação cancelada.". SE limpar falhar ENTÃO mostra
   "Erro ao resetar ranking: <mensagem>". _(verificação: manual)_

### R5 — Composição e build

**Critérios de aceitação:**

1. O SISTEMA DEVE ter um `missaomarte.Main` que só cria as dependências
   (`Scanner`, `Random`, `Clock`, repositório em arquivo, `RankingTop5`,
   `MissaoFactory`, `GameSession` e console) e inicia o console, sem regra
   e sem desenho. _(verificação: inspeção)_
2. O SISTEMA DEVE ter um console que não importa
   `missaomarte.infrastructure` e não contém regra de jogo: não calcula
   pontos, colisão, vitória nem a regra do ranking. _(verificação: inspeção)_
3. QUANDO `./build.sh` for executado em `fases/fase-01-refatoracao` O
   SISTEMA DEVE compilar tudo para `out/`. Com `./build.sh run`, compila e
   executa o jogo. SE a compilação falhar ENTÃO o script termina com código
   diferente de zero e não executa. _(verificação: manual)_
4. O SISTEMA DEVE ter `fases/fase-01-refatoracao/README.md` com requisitos,
   comandos, controles e a lista das mudanças intencionais da Fase 1.
   _(verificação: inspeção)_
5. QUANDO o `.workflow/check.sh` rodar O SISTEMA DEVE passar (compilação,
   camadas e Fase 0 intacta). _(verificação: gate)_

### R6 — Comportamento preservado e mudanças intencionais

**Critérios de aceitação:**

1. QUANDO o roteiro manual do design for executado O SISTEMA DEVE se
   comportar como a Fase 0 em todos os itens da seção 4 da
   `docs/analise-inicial.md`, exceto nas mudanças intencionais.
   _(verificação: manual)_
2. O SISTEMA DEVE apresentar estas mudanças intencionais, cada uma visível
   no roteiro:
   - desembarque automático na base, com a frase de entrega;
   - vitória só com todos os passageiros entregues;
   - "Entregues" na linha de status, com a conta fechando;
   - capacidade por dificuldade (4/3/2), com "Nave cheia!" alcançável.

   _(verificação: manual)_
