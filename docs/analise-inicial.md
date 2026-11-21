# Fase 0: análise do jogo original (Missão Marte Unifor)

Aluno: Matheus Correia · Data: 14/11/2026 · Issue #2
Código analisado: `fases/fase-00-console/src` (10 classes, `Main.java` com 569 linhas)

## 1. Como compilar e executar

Requisito: JDK 17. A partir da raiz do repositório:

```bash
cd fases/fase-00-console
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

O `ranking.json` é criado na pasta de onde o jogo roda. Ele e o `out/` ficam fora do Git. O `src/README.md` da Fase 0 está desatualizado (cita `fase-0-oopuro` e PowerShell) e não foi alterado.

## 2. Classes

| Classe | Função |
| --- | --- |
| `Main` | Menu, entrada, regras da partida, criação do cenário, desenho do mapa, estatísticas e ranking (JSON escrito e lido à mão) |
| `Missao` | Guarda nave, passageiros, asteroides e inimigos; detecta colisão e embarca |
| `Nave` | Posição (começa em 0,0), capacidade 5, 3 vidas, passageiros a bordo |
| `Passageiro`, `Professor`, `Engenheiro`, `Astronauta` | Passageiro vale 10, 15 ou 20 pontos conforme o tipo |
| `Asteroide`, `Inimigo` | Perigos; o inimigo anda 1 casa por turno em uma das 4 direções |
| `Dificuldade` | Enum `FACIL`, `MEDIO`, `DIFICIL` |

## 3. Fluxo e regras

**Fluxo:** menu (1 jogar, 2 ranking, 3 resetar, 4 sair) → nome, dificuldade e tamanho do mapa → Enter para decolar → turnos até o fim → volta ao menu.

**Turno:** desenha o mapa e lê um comando: `w/a/s/d` move (−1 ponto), `c` embarca, `q` aborta. Depois de `w/a/s/d` ou `c`: inimigos andam, colisão, pontuação e vitória são checadas, nessa ordem.

| Regra | Comportamento |
| --- | --- |
| Dificuldade | Fácil: 4 passageiros, 1 asteroide, 1 inimigo, 30 pontos. Médio: 5, 2, 2, 20 pontos. Difícil: 5, 3, 3, 45 pontos |
| Passageiros | Professor `P` 10, Engenheiro `E` 15, Astronauta `T` 20 (o Fácil não tem astronauta) |
| Colisão | Perigo na célula da nave tira 1 de 3 vidas; a nave não se move. Com 0 vidas: GAME OVER |
| Derrota | 0 vidas ou pontuação menor ou igual a 0 |
| Vitória | Todos embarcados e nave em (0,0) |
| Mapa | De `-N` a `+N` (padrão 5). Símbolos: `@` nave, `#` asteroide, `X` inimigo, `L` base, `.` vazio |
| Ranking | Top 5 por pontuação, só vitórias com pontuação maior que 0, salvo em `ranking.json` |

## 4. Comportamentos a preservar

- [ ] Menu de 4 opções; opção inválida mostra mensagem e volta ao menu.
- [ ] Nome vazio vira "Piloto Anônimo"; dificuldade desconhecida vira Médio; mapa inválido vira 5.
- [ ] Pontuação inicial e quantidades por dificuldade da seção 3; nenhum objeto começa na mesma célula nem na (0,0).
- [ ] Movimento limitado às bordas, custando 1 ponto por `w/a/s/d`; `c` é gratuito.
- [ ] Embarque soma 10, 15 ou 20 pontos; sem passageiro mostra "Nenhum passageiro nesta posição."
- [ ] Colisão, GAME OVER, pontuação zerada e vitória como na seção 3.
- [ ] Ranking recarregado após cada partida; limpar pede confirmação (`s`/`sim`) e apaga o arquivo; `n` cancela.
- [ ] `q` aborta a partida; opção 4 encerra; Ctrl+D encerra no menu e na partida.

## 5. Pontos de atenção (decisão: preservar e registrar, exceto as mudanças exigidas pela Fase 1 (desembarque na base, vitória por passageiros entregues, indicadores a bordo + entregues + restantes = total, capacidade coerente))

Quem mudar algum destes na Fase 1 documenta como mudança intencional. "Confirmado" = verificado rodando o jogo com entrada automatizada; os demais vêm do código.

| # | Ponto | Origem |
| --- | --- | --- |
| a | O menu só aparece depois da primeira linha digitada, e essa linha vira a opção escolhida | confirmado |
| b | Nome de piloto com vírgula faz a entrada sumir do ranking, sem aviso | confirmado |
| c | Só o 1º caractere do comando conta: `sair` move a nave para baixo | confirmado |
| d | Mapa de tamanho 1 com Médio ou Difícil trava (mais objetos que células livres) | confirmado |
| e | "Nave cheia" é inalcançável (capacidade 5, no máximo 5 passageiros) | código |
| f | O Difícil começa com mais pontos (45) que o Fácil (30) | código |
| g | Bater na borda gasta 1 ponto; `c` também é um turno (inimigos andam) | código |
| h | Gastar o último ponto ao chegar na base com todos embarcados é derrota | código |

## 6. Roteiro de testes manuais

Executado por Matheus Correia, JDK 17, código da Fase 0 sem alterações. Todos os casos conferem com a seção 3.

| Caso | Resultado |
| --- | --- |
| Menu: opção válida e opção inválida (ex.: 9) | OK |
| Dificuldades Fácil, Média e Difícil | OK |
| Embarque com passageiro e sem passageiro | OK |
| Colisão (perde vida) e GAME OVER com 0 vidas | OK |
| Pontuação zerada: missão perdida | OK |
| Vitória: estatísticas e `ranking.json` criado | OK |
| Ranking: consulta, limpar com `s`, cancelar com `n` | OK |
| Sair: `q` na partida e opção 4 | OK |
| Fim da entrada (Ctrl+D) no menu e durante a partida | OK |
| Nome vazio, mapa inválido e comando desconhecido | OK |

## 7. Critérios de aceite da issue #2

- [x] Comandos para compilar e executar documentados
- [x] Fluxo e regras principais descritos
- [x] Lista de comportamentos a preservar
- [x] Roteiro de testes manuais executado e registrado
- [x] `fases/fase-00-console` intacta (só compilada e executada)
