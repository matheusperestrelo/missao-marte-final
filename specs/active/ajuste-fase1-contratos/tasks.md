# Tarefas: ajuste-fase1-contratos

**Issue:** #13 · **Fase:** 1 · **Depende de:** — · **Branch:** `chore/issue-13-fase1-contratos`

Ajuste só de documentação: cria `docs/contratos.md` (raiz do repositório)
descrevendo os contratos que as três trilhas compartilham. A parte da Fase 1
descreve o código **como ele está** em `fases/fase-01-refatoracao/src/missaomarte/`
(fonte de verdade; idêntico ao de `fases/fase-02-gradle/src/main/java/`).
A parte da Fase 5 é uma **proposta a combinar**, não decisão.

- [ ] 1. Escrever `docs/contratos.md` com os contratos da Fase 1 e a proposta da Fase 5
  - status: todo
  - Não alterar nenhum arquivo fora de `docs/contratos.md`: nada de código,
    testes, `fases/` ou outros docs. O `check.sh` deve continuar passando.
  - Conferir cada assinatura no código antes de escrever; se algo da lista
    abaixo divergir do código, vale o código e a divergência vai para a seção
    "Diferenças em relação ao plano".
  - Seções obrigatórias (critérios da issue #13):
    1. **Domínio (`missaomarte.domain`), como implementado:** `Posicao`
       (record `x`, `y`; `ORIGEM`; `mover(Direcao)` devolve nova instância),
       `Direcao` (dx/dy), `Mapa(tamanho)` com `contem(Posicao)`,
       `getMin/getMax/getTamanho`, `ElementoMapa` (`posicao()`, `tipo()`,
       `simbolo()` padrão vindo do tipo), `TipoElemento` (símbolo e nome de
       cada constante), `Nave(capacidade)`, `Passageiro` abstrata (Professor
       10, Engenheiro 15, Astronauta 20), `Perigo` (`Asteroide`, `Inimigo`),
       `Base` e `Missao` (métodos públicos).
    2. **`Acao`** (CIMA, BAIXO, ESQUERDA, DIREITA, EMBARCAR, SAIR, INVALIDA),
       `Acao.deTexto(String)` lendo só o primeiro caractere, sem diferenciar
       maiúsculas; mapeamento do console `w/s/a/d`, `c`, `q`; `direcao()` e
       `ehMovimento()`.
    3. **Turno:** `ResultadoPartida` (EM_ANDAMENTO, VITORIA, DERROTA,
       ABORTADA); os 9 valores de `TipoEvento`; record
       `EventoJogo(tipo, passageiro, valor)`; record
       `ResultadoTurno(eventos, comandoInvalido)`; comando inválido e SAIR não
       geram eventos.
    4. **Sessão e cenário:** `GameSession(MissaoFactory, Random, Clock)` com
       `iniciar(piloto, dificuldade, tamanho)`, `jogar(Acao)`, getters de
       estado (`getMissao`, `getPiloto`, `getDificuldade`, `getPontos`,
       `getVidas`, `getMovimentos`, `getDuracaoSegundos`, `getResultado`,
       `isEncerrada`) e `resumo()` → `ResumoPartida`;
       `MissaoFactory(Random).criar(Dificuldade, tamanho)`; tabela da
       `Dificuldade` (pontuação inicial, passageiros, asteroides, inimigos,
       capacidade 4/3/2) e `deString`. Registrar que a sessão não tem guarda
       contra `jogar` depois do fim nem consulta antes de `iniciar`.
    5. **Ranking:** `RankingEntry(nome, pontuacao, dificuldade,
       passageirosColetados, dataHora, tempoJogo)`, `RankingRepository`
       (carregar, salvar, limpar; a implementação devolve a lista ordenada com
       no máximo 5), `RankingException`, `RankingTop5` (listar,
       registrar(ResumoPartida), recorde, seriaNovoRecorde, limpar),
       `FileRankingRepository(Path)`. Nota: na Fase 3 a regra vira
       `RankingService` com Top 10 em `application.ranking` (issue #15).
    6. **Diferenças em relação ao plano:** não existe record `Placar` (a
       sessão expõe o estado por getters); o método é `jogar`, não
       `executar`; `ElementoMapa` faz o papel de `ObjetoRenderizavel`.
    7. **API do servidor (Fase 5, a combinar):** POST /api/usuarios
       {nome, senha}; POST /api/auth/login {nome, senha} →
       {token, tipo, expiraEmSegundos}; POST /api/partidas {dificuldade,
       resultado, pontuacao, duracaoSegundos, movimentos};
       GET /api/partidas/minhas?pagina&tamanho; GET /api/ranking;
       GET /api/saude (público); enums FACIL/MEDIO/DIFICIL e VITORIA/DERROTA;
       erros em ProblemDetail. Marcar a seção como proposta.
    8. **Porta `ServidorDoJogo` (Fase 5, a combinar):** cadastrar, entrar,
       enviarPartida e carregarRanking, com `Callback<T>` e
       `Resposta<T>(valor, erro, naoAutorizado)`. Marcar como proposta.
  - Fora desta tarefa (são da equipe, não do agente): aprovação dos três
    integrantes e revisão do PR por outro integrante. Deixar no fim do
    documento uma seção "Aprovação" com os três nomes em aberto.
  - _Requisitos: nenhum (ajuste)_
  - notas:
