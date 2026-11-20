# Missão Marte Unifor — Fase 1 (refatoração em camadas)

Jogo de exploração e resgate em Marte, em modo texto. O jogador pilota a nave
num mapa, embarca passageiros, entrega todos na plataforma de pouso e evita
asteroides e inimigos. A Fase 1 reorganiza a Fase 0 (`../fase-00-console`) em
camadas (`domain`, `application`, `console`, `infrastructure`), mantendo o
comportamento, exceto nas mudanças intencionais listadas abaixo.

## Requisitos

- JDK 17 ou superior (`javac` e `java` no `PATH`).
- macOS ou Linux (o script de build é `bash`).

## Comandos

Execute a partir de `fases/fase-01-refatoracao`:

```bash
./build.sh        # compila tudo para out/
./build.sh run    # compila e executa o jogo
```

Se a compilação falhar, o script termina com código diferente de zero e não
executa o jogo.

O ranking é gravado em `ranking.json`, na pasta de onde o jogo é executado.
O formato é o mesmo da Fase 0: um `ranking.json` gravado por uma fase é lido
pela outra.

## Controles

- `w`, `s`, `a`, `d`: mover a nave (cima, baixo, esquerda, direita).
- `c`: embarcar o passageiro na posição atual.
- `q`: abortar a missão.
- Menu: `1` nova missão, `2` ver o Top 5, `3` limpar o ranking (pede
  confirmação), `4` sair.
- Ctrl+D (fim da entrada) encerra o programa com segurança.

Capacidade da nave: 4 passageiros (fácil), 3 (médio) e 2 (difícil).

## Mudanças intencionais em relação à Fase 0

1. **Desembarque automático na base:** ao chegar à plataforma `L` com
   passageiros a bordo, eles são entregues, com a frase
   `N passageiro(s) entregue(s) na base!`.
2. **Vitória só com todos os passageiros entregues**, e não apenas embarcados.
3. **Linha de status com "Entregues":**
   `Nave em (x,y) | Pontos: p | Vidas: v | A bordo: b/capacidade | Entregues: e/total | Restantes: r`,
   com a conta a bordo + entregues + restantes = total.
4. **Capacidade por dificuldade (4/3/2)**, de modo que a mensagem
   `Nave cheia!` pode acontecer em qualquer nível.

Os pontos de atenção *a* (o menu só aparece depois da primeira linha lida) e
*b* (nome com vírgula some do ranking) da análise inicial foram preservados.

## Estrutura

```text
src/missaomarte/
├── Main.java                 composição das dependências
├── domain/                   regras do jogo
├── application/              sessão, ranking Top 5 e portas
├── console/                  menu, desenho do mapa e mensagens
└── infrastructure/ranking/   ranking em arquivo JSON
```
