package missaomarte.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Dubles.FabricaFixa;
import missaomarte.application.Dubles.RandomSequencia;
import missaomarte.application.Dubles.RelogioControlado;
import missaomarte.domain.Asteroide;
import missaomarte.domain.Engenheiro;
import missaomarte.domain.Inimigo;
import missaomarte.domain.Mapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Posicao;
import missaomarte.domain.Professor;

/**
 * Cada teste monta uma missão conhecida com a FabricaFixa. A nave sempre
 * começa na base (0,0). Colisões usam asteroides (parados), para não
 * depender do sorteio dos inimigos.
 */
class GameSessionTest {

    private final RelogioControlado relogio = new RelogioControlado(1_000);

    private GameSession sessao(Missao missao, Dificuldade dificuldade) {
        return sessao(missao, dificuldade, new Random(42));
    }

    private GameSession sessao(Missao missao, Dificuldade dificuldade, Random random) {
        GameSession sessao = new GameSession(new FabricaFixa(missao), random, relogio);
        sessao.iniciar("Ana", dificuldade, 5);
        return sessao;
    }

    private static List<TipoEvento> tipos(ResultadoTurno resultado) {
        return resultado.eventos().stream().map(EventoJogo::tipo).collect(Collectors.toList());
    }

    private static void contaFecha(Missao m) {
        assertEquals(m.getTotal(), m.getABordo() + m.getEntregues() + m.getRestantes(),
                "a bordo + entregues + restantes deve ser o total");
    }

    private static Missao missaoComProfessorEm(int x, int y, int tamanho) {
        Missao missao = new Missao(new Mapa(tamanho), new Nave(4));
        missao.adicionarPassageiro(new Professor("Dr. Silva", new Posicao(x, y)));
        return missao;
    }

    @Test
    @DisplayName("R5.1 início: pontos da dificuldade, 0 movimentos, 3 vidas")
    void r5_1_inicioDaPartida() {
        GameSession s = sessao(missaoComProfessorEm(1, 0, 5), Dificuldade.DIFICIL);

        assertEquals(45, s.getPontos());
        assertEquals(0, s.getMovimentos());
        assertEquals(3, s.getVidas());
        assertEquals("Ana", s.getPiloto());
        assertEquals(Dificuldade.DIFICIL, s.getDificuldade());
        assertEquals(ResultadoPartida.EM_ANDAMENTO, s.getResultado());
        assertFalse(s.isEncerrada());
    }

    @Test
    @DisplayName("R5.1 nome vazio vira Piloto Anônimo e tamanho < 1 vira 5")
    void r5_1_pilotoAnonimoETamanhoPadrao() {
        GameSession s = new GameSession(new MissaoFactory(new Random(1)), new Random(1), relogio);

        s.iniciar("   ", Dificuldade.FACIL, 0);

        assertEquals("Piloto Anônimo", s.getPiloto());
        assertEquals(5, s.getMissao().getMapa().getTamanho());
    }

    @Test
    @DisplayName("R5.2 movimento tira 1 ponto e conta 1 movimento, inclusive na borda (ponto g)")
    void r5_2_movimentoCustaUmPontoInclusiveNaBorda() {
        GameSession s = sessao(missaoComProfessorEm(1, 1, 1), Dificuldade.FACIL);

        ResultadoTurno r1 = s.jogar(Acao.CIMA);
        ResultadoTurno r2 = s.jogar(Acao.CIMA); // já está na borda (0,-1)

        assertEquals(List.of(TipoEvento.MOVIMENTO), tipos(r1));
        assertEquals(List.of(TipoEvento.MOVIMENTO), tipos(r2));
        assertEquals(new Posicao(0, -1), s.getMissao().getNave().posicao());
        assertEquals(28, s.getPontos());
        assertEquals(2, s.getMovimentos());
    }

    @Test
    @DisplayName("R5.3 embarque soma a pontuação, não conta movimento e gera EMBARQUE")
    void r5_3_embarque() {
        GameSession s = sessao(missaoComProfessorEm(1, 0, 5), Dificuldade.FACIL);
        s.jogar(Acao.DIREITA);

        ResultadoTurno r = s.jogar(Acao.EMBARCAR);

        assertEquals(List.of(new EventoJogo(TipoEvento.EMBARQUE, "Dr. Silva", 10)), r.eventos());
        assertEquals(29 + 10, s.getPontos());
        assertEquals(1, s.getMovimentos());
        assertEquals(1, s.getMissao().getABordo());
    }

    @Test
    @DisplayName("R5.4 embarque recusado sem passageiro na célula")
    void r5_4_semPassageiro() {
        GameSession s = sessao(missaoComProfessorEm(3, 3, 5), Dificuldade.FACIL);

        ResultadoTurno r = s.jogar(Acao.EMBARCAR);

        assertEquals(List.of(TipoEvento.EMBARQUE_RECUSADO_SEM_PASSAGEIRO), tipos(r));
        assertEquals(30, s.getPontos());
    }

    @Test
    @DisplayName("R5.4 nave cheia com a capacidade do Difícil (2)")
    void r5_4_naveCheia() {
        Missao missao = new Missao(new Mapa(5), new Nave(Dificuldade.DIFICIL.capacidadeNave()));
        missao.adicionarPassageiro(new Professor("P1", new Posicao(1, 0)));
        missao.adicionarPassageiro(new Professor("P2", new Posicao(2, 0)));
        missao.adicionarPassageiro(new Engenheiro("E3", new Posicao(3, 0)));
        GameSession s = sessao(missao, Dificuldade.DIFICIL);

        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        s.jogar(Acao.DIREITA);
        ResultadoTurno r = s.jogar(Acao.EMBARCAR);

        assertEquals(List.of(TipoEvento.EMBARQUE_RECUSADO_NAVE_CHEIA), tipos(r));
        assertEquals(2, missao.getABordo());
        assertEquals(1, missao.getRestantes());
    }

    @Test
    @DisplayName("R5.5 e R5.6 entrega automática na base, sem pontos extras, com a conta fechando")
    void r5_5_entregaAutomaticaNaBase() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));
        missao.adicionarPassageiro(new Professor("Dr. Silva", new Posicao(1, 0)));
        missao.adicionarPassageiro(new Engenheiro("Eng. Rosa", new Posicao(4, 4)));
        GameSession s = sessao(missao, Dificuldade.FACIL);
        contaFecha(missao);

        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        contaFecha(missao);
        int pontosAntes = s.getPontos();
        ResultadoTurno r = s.jogar(Acao.ESQUERDA);

        assertEquals(List.of(new EventoJogo(TipoEvento.MOVIMENTO, null, 0),
                new EventoJogo(TipoEvento.ENTREGA, null, 1)), r.eventos());
        assertEquals(pontosAntes - 1, s.getPontos());
        assertEquals(0, missao.getABordo());
        assertEquals(1, missao.getEntregues());
        contaFecha(missao);
        assertFalse(s.isEncerrada());
    }

    @Test
    @DisplayName("R5.7 colisão tira 1 vida; com 0 vidas, derrota por nave destruída")
    void r5_7_colisaoENaveDestruida() {
        Missao missao = missaoComProfessorEm(4, 4, 5);
        missao.adicionarAsteroide(new Asteroide(new Posicao(1, 0)));
        GameSession s = sessao(missao, Dificuldade.FACIL);

        ResultadoTurno r1 = s.jogar(Acao.DIREITA);
        assertEquals(List.of(new EventoJogo(TipoEvento.MOVIMENTO, null, 0),
                new EventoJogo(TipoEvento.COLISAO, null, 2)), r1.eventos());
        assertEquals(2, s.getVidas());

        s.jogar(Acao.ESQUERDA);
        s.jogar(Acao.DIREITA);
        s.jogar(Acao.ESQUERDA);
        ResultadoTurno r3 = s.jogar(Acao.DIREITA);

        assertEquals(List.of(TipoEvento.MOVIMENTO, TipoEvento.COLISAO, TipoEvento.DERROTA_NAVE_DESTRUIDA), tipos(r3));
        assertEquals(0, r3.eventos().get(1).valor());
        assertEquals(ResultadoPartida.DERROTA, s.getResultado());
        assertTrue(s.isEncerrada());
    }

    @Test
    @DisplayName("R5.8 nave destruída e pontuação zerada no mesmo turno geram as duas derrotas, nessa ordem")
    void r5_8_duasDerrotasNaOrdem() {
        Missao missao = missaoComProfessorEm(-1, 1, 1);
        missao.adicionarAsteroide(new Asteroide(new Posicao(1, 0)));
        GameSession s = sessao(missao, Dificuldade.MEDIO); // 20 pontos

        s.jogar(Acao.DIREITA);  // colisão 1 → 19
        s.jogar(Acao.ESQUERDA); // 18
        s.jogar(Acao.DIREITA);  // colisão 2 → 17
        s.jogar(Acao.ESQUERDA); // 16
        for (int i = 0; i < 14; i++) {
            s.jogar(Acao.CIMA); // (0,-1) e depois batendo na borda → 2
        }
        s.jogar(Acao.BAIXO);    // volta a (0,0) → 1
        assertEquals(1, s.getPontos());

        ResultadoTurno r = s.jogar(Acao.DIREITA); // colisão 3 e pontos 0

        assertEquals(List.of(TipoEvento.MOVIMENTO, TipoEvento.COLISAO,
                TipoEvento.DERROTA_NAVE_DESTRUIDA, TipoEvento.DERROTA_PONTUACAO_ZERADA), tipos(r));
        assertEquals(ResultadoPartida.DERROTA, s.getResultado());
    }

    @Test
    @DisplayName("R5.9 pontuação zerada é derrota mesmo entregando o último passageiro (ponto h)")
    void r5_9_pontuacaoZeradaAoEntregarOUltimo() {
        GameSession s = sessao(missaoComProfessorEm(1, 0, 1), Dificuldade.MEDIO); // 20 pontos

        s.jogar(Acao.DIREITA);  // 19
        s.jogar(Acao.EMBARCAR); // 29
        for (int i = 0; i < 27; i++) {
            s.jogar(Acao.CIMA); // (1,-1) e borda → 2
        }
        s.jogar(Acao.BAIXO);    // (1,0) → 1
        ResultadoTurno r = s.jogar(Acao.ESQUERDA); // entrega o último com 0 pontos

        assertEquals(List.of(TipoEvento.MOVIMENTO, TipoEvento.ENTREGA, TipoEvento.DERROTA_PONTUACAO_ZERADA), tipos(r));
        assertEquals(ResultadoPartida.DERROTA, s.getResultado());
        assertTrue(s.getMissao().todosEntregues());
    }

    @Test
    @DisplayName("R5.10 vitória só quando todos forem entregues")
    void r5_10_vitoriaSoComTodosEntregues() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));
        missao.adicionarPassageiro(new Professor("P1", new Posicao(1, 0)));
        missao.adicionarPassageiro(new Professor("P2", new Posicao(2, 0)));
        GameSession s = sessao(missao, Dificuldade.FACIL);

        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        assertEquals(0, missao.getRestantes());
        assertFalse(s.isEncerrada(), "todos embarcados, mas ninguém entregue: a partida continua");

        s.jogar(Acao.ESQUERDA);
        ResultadoTurno r = s.jogar(Acao.ESQUERDA);

        assertEquals(List.of(TipoEvento.MOVIMENTO, TipoEvento.ENTREGA, TipoEvento.VITORIA), tipos(r));
        assertEquals(ResultadoPartida.VITORIA, s.getResultado());
    }

    @Test
    @DisplayName("R5.11 ordem dos eventos: ação, entrega, colisão, vitória")
    void r5_11_ordemDosEventos() {
        Missao missao = missaoComProfessorEm(1, 0, 5);
        missao.adicionarAsteroide(new Asteroide(Posicao.ORIGEM)); // na base, de propósito
        GameSession s = sessao(missao, Dificuldade.FACIL);

        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        ResultadoTurno r = s.jogar(Acao.ESQUERDA);

        assertEquals(List.of(TipoEvento.MOVIMENTO, TipoEvento.ENTREGA, TipoEvento.COLISAO, TipoEvento.VITORIA), tipos(r));
    }

    @Test
    @DisplayName("R5.12 INVALIDA não muda nada; SAIR aborta sem mover inimigos")
    void r5_12_invalidaESair() {
        Missao missao = missaoComProfessorEm(4, 4, 5);
        Inimigo inimigo = new Inimigo(new Posicao(-3, -3));
        missao.adicionarInimigo(inimigo);
        GameSession s = sessao(missao, Dificuldade.FACIL, new RandomSequencia(0));

        ResultadoTurno invalida = s.jogar(Acao.INVALIDA);

        assertTrue(invalida.comandoInvalido());
        assertTrue(invalida.eventos().isEmpty());
        assertEquals(30, s.getPontos());
        assertEquals(0, s.getMovimentos());
        assertEquals(Posicao.ORIGEM, missao.getNave().posicao());
        assertEquals(new Posicao(-3, -3), inimigo.posicao());

        ResultadoTurno sair = s.jogar(Acao.SAIR);

        assertFalse(sair.comandoInvalido());
        assertTrue(sair.eventos().isEmpty());
        assertEquals(ResultadoPartida.ABORTADA, s.getResultado());
        assertEquals(new Posicao(-3, -3), inimigo.posicao());
    }

    @Test
    @DisplayName("inimigo anda na direção sorteada (0 = direita) depois da ação")
    void inimigoAndaNaDirecaoSorteada() {
        Missao missao = missaoComProfessorEm(4, 4, 5);
        Inimigo inimigo = new Inimigo(new Posicao(-3, -3));
        missao.adicionarInimigo(inimigo);
        GameSession s = sessao(missao, Dificuldade.FACIL, new RandomSequencia(0, 3));

        s.jogar(Acao.EMBARCAR);
        assertEquals(new Posicao(-2, -3), inimigo.posicao());
        s.jogar(Acao.EMBARCAR);
        assertEquals(new Posicao(-2, -4), inimigo.posicao());
    }

    @Test
    @DisplayName("R5.13 a duração congela no fim da partida")
    void r5_13_duracaoCongelada() {
        GameSession s = sessao(missaoComProfessorEm(4, 4, 5), Dificuldade.FACIL);
        relogio.avancar(5_999);
        assertEquals(5, s.getDuracaoSegundos());

        s.jogar(Acao.SAIR);
        relogio.avancar(60_000);

        assertEquals(5, s.getDuracaoSegundos());
    }

    @Test
    @DisplayName("R5.14 resumo da partida encerrada")
    void r5_14_resumo() {
        GameSession s = sessao(missaoComProfessorEm(1, 0, 5), Dificuldade.FACIL);
        relogio.avancar(3_000);
        s.jogar(Acao.DIREITA);
        s.jogar(Acao.EMBARCAR);
        s.jogar(Acao.ESQUERDA);

        assertEquals(new ResumoPartida("Ana", Dificuldade.FACIL, 38, 2, 1, 3, true), s.resumo());
    }
}
