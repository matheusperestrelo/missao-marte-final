# Design: fase1-console-infra

## Abordagem

O console é dividido em três classes pequenas. O `JogoConsole` cuida do menu
e do laço de leitura, o `DesenhoMapa` monta o texto do mapa e o
`MensagensConsole` traduz eventos em frases. O ranking em arquivo é a lógica
de `loadRanking`/`saveRanking`/`parseRankingJson` da Fase 0, movida para uma
classe com o caminho recebido no construtor. O `Main` monta tudo.

## Componentes afetados

Todos novos, em `fases/fase-01-refatoracao/`:

| Arquivo | Responsabilidade |
|---|---|
| `src/missaomarte/Main.java` | Composição: `new Scanner(System.in)`, `new Random()`, `Clock.systemDefaultZone()`, `new FileRankingRepository(Paths.get("ranking.json"))`, `new RankingTop5(repo, clock)`, `new MissaoFactory(random)`, `new GameSession(fabrica, random, clock)`, `new JogoConsole(scanner, System.out, sessao, ranking).executar()` |
| `src/missaomarte/console/JogoConsole.java` | Boas-vindas, menu, `lerLinha` (igual a `Main.java:394-402`), leitura de nome, dificuldade e tamanho, laço da partida, estatísticas, exibir e limpar ranking |
| `src/missaomarte/console/DesenhoMapa.java` | `String desenhar(GameSession)`: cabeçalho, grade, legenda (`TipoElemento.values()` + `.=Vazio`), comandos e passageiros na superfície; `String linhaStatus(GameSession)` |
| `src/missaomarte/console/MensagensConsole.java` | `List<String> traduzir(ResultadoTurno)`: tabela de R2.6 (`switch` sobre `TipoEvento`) |
| `src/missaomarte/infrastructure/ranking/FileRankingRepository.java` | `implements RankingRepository`; `FileRankingRepository(Path)`; `carregar`/`salvar`/`limpar` com o formato e o parser da Fase 0; `IOException` → `RankingException` |
| `build.sh` | `cd` para a pasta do script; `rm -rf out`; `javac --release 17 -encoding UTF-8 -d out $(find src -name '*.java')`; `set -e`; com o argumento `run`, executa `java -cp out missaomarte.Main` |
| `README.md` | Requisitos (JDK 17), `./build.sh` e `./build.sh run`, controles, mudanças intencionais em relação à Fase 0 |

`.gitignore` da fase: criado na spec 1 (`out/`, `ranking.json`).

## Fluxo

**Laço da partida** (`JogoConsole`), equivalente a `Main.java:81-209`:

```
nome, dificuldade (Dificuldade.deString), tamanho (parse; erro → mensagem e 5)
"Iniciando missão…" / "Pressione Enter para decolar!" (EOF → "Missão não iniciada.")
sessao.iniciar(nome, dificuldade, tamanho)
enquanto !sessao.isEncerrada():
  imprimir desenho.desenhar(sessao) e desenho.linhaStatus(sessao)
  linha = lerLinha("Comando (w/s/a/d/c/q): ", "")
  se linha vazia: se EOF → "Entrada encerrada. Missão abortada." e sai; senão continua
  r = sessao.jogar(Acao.deTexto(linha))
  se r.comandoInvalido() → "Comando inválido."; continua
  se sessao.getResultado() == ABORTADA → "Missão abortada pelo piloto."; sai
  imprimir mensagens.traduzir(r)    // VITORIA dispara o bloco de vitória e as estatísticas
  se !encerrada e missao.getRestantes()==0 e missao.getABordo()>0 → alerta de retorno
```

O EOF no meio da partida só encerra o laço do console. A sessão fica
abandonada e é reiniciada na próxima partida com `iniciar`, como na Fase 0,
que não registra nada nesse caso.

**Vitória:** o bloco de vitória, depois as estatísticas com
`ranking.recorde()` e `ranking.seriaNovoRecorde(pontos)` calculados
**antes** de registrar, como a Fase 0 (que usa a lista carregada antes de
adicionar). Em seguida `ranking.registrar(sessao.resumo())`:
`true` → "Parabéns!…"; `RankingException` → "Não foi possível salvar o
ranking: <msg>" seguido de "Parabéns!…". A Fase 0 também mostrava o
"Parabéns" depois de falhar ao salvar.

**Desenho de uma célula:** percorrer `missao.elementos()` e usar o
`simbolo()` do primeiro cuja `posicao()` seja a célula; sem elemento, `.`.

## Decisões e alternativas descartadas

- **Decisão:** três classes no console.
  **Alternativa descartada:** um `JogoConsole` único. Seria um novo `Main`
  gigante, o problema 1 da `fase-00-arquitetura.md`.
- **Decisão:** o desenho percorre `elementos()` por célula, O(células ×
  elementos). Com no máximo ~11 elementos, a simplicidade vale mais que a
  eficiência.
- **Decisão:** o `FileRankingRepository` recebe o `Path`. Na Fase 2, o
  teste com `@TempDir` (#9) depende disso.
- **Decisão:** a falha de gravação é lançada como `RankingException` e
  tratada no console. O `infrastructure` não imprime, e as frases da Fase 0
  ficam no console.
- **Decisão:** só `build.sh`.
  **Alternativa descartada:** `build.ps1`, porque a equipe usa macOS e
  Linux.

## Riscos e impactos

- **Frases:** qualquer diferença de texto ou espaçamento em relação à Fase
  0 aparece no roteiro manual. O caminho mais seguro é copiar as strings do
  `Main.java` da Fase 0, que é código da própria equipe e não do repositório
  do professor.
- **Compatibilidade do `ranking.json`:** o arquivo é compatível entre as
  fases. Rodar a Fase 0 e a Fase 1 na mesma pasta compartilha o ranking.
- **Composição:** o `Main` cria o `Random` sem semente, então o jogo
  continua imprevisível como na Fase 0.

## Estratégia de testes

Sem JUnit na Fase 1. A verificação é:

- **gate:** `.workflow/check.sh` (compilação, camadas e Fase 0 intacta);
- **busca:** `grep -rn "instanceof\|infrastructure" fases/fase-01-refatoracao/src/missaomarte/console`
  sem resultado;
- **roteiro manual**, executado com `./build.sh run` e registrado nas notas
  da última tarefa, com o resultado de cada caso:

| # | Caso | Esperado |
|---|---|---|
| 1 | Menu: opção 9, depois 4 | "Opção inválida…", depois sai com a frase de despedida |
| 2 | Fácil, Médio e Difícil | Pontos iniciais 30/20/45; 4/5/5 passageiros; capacidade 4/3/2 na linha de status |
| 3 | `c` numa célula sem passageiro | "Nenhum passageiro nesta posição." |
| 4 | Embarcar um passageiro | "+10/15/20 pontos", "A bordo" sobe e "Restantes" desce |
| 5 | Difícil: embarcar 2 e tentar o 3º | "Nave cheia!…" |
| 6 | Voltar à base com passageiros | "N passageiro(s) entregue(s) na base!", "A bordo" zera, "Entregues" sobe |
| 7 | Todos embarcados, fora da base | Alerta de retorno da Fase 0 |
| 8 | Entregar o último na base | Bloco de vitória, estatísticas e Top 5; `ranking.json` criado |
| 9 | Colidir | "Alerta! Colisão…"; com 0 vidas, "GAME OVER!…" |
| 10 | Gastar todos os pontos | "Combustível/Pontuação zerada!…" |
| 11 | Ranking (opção 2), com e sem registros | Formato da Fase 0 |
| 12 | Limpar com `s`, depois limpar com `n` | "Ranking resetado…" e "Operação cancelada." |
| 13 | `q` na partida | "Missão abortada pelo piloto." |
| 14 | Ctrl+D no menu, antes de decolar e na partida | As três frases de R1.5 |
| 15 | Nome vazio, mapa `abc`, comando `z` | "Piloto Anônimo", "Entrada inválida…(5)", "Comando inválido." |
| 16 | `ranking.json` gravado pela Fase 0 | Lido e exibido pela Fase 1 |
| 17 | A conta da linha de status em todos os casos acima | a bordo + entregues + restantes = total |
