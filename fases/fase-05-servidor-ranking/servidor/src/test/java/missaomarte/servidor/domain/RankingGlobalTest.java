package missaomarte.servidor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RankingGlobalTest {

    private static final Instant BASE = Instant.parse("2026-03-04T05:06:07Z");

    private static Usuario usuario(long id) {
        Usuario u = new Usuario("jogador" + id, "hash", BASE);
        ReflectionTestUtils.setField(u, "id", id);
        return u;
    }

    private static Partida partida(long id, Usuario dono, Resultado resultado, int pontuacao,
            int duracao, Instant dataHora) {
        Partida p = new Partida(dono, Dificuldade.MEDIO, resultado, pontuacao, duracao, 10, dataHora);
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }

    private static Partida vitoria(long id, Usuario dono, int pontuacao, int duracao, Instant dataHora) {
        return partida(id, dono, Resultado.VITORIA, pontuacao, duracao, dataHora);
    }

    private static List<Long> ids(List<LinhaRanking> linhas) {
        return linhas.stream().map(l -> l.partida().getId()).toList();
    }

    @Test
    void r4_2_ignoraDerrotaEVitoriaSemPontuacaoPositiva() {
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                partida(1, usuario(1), Resultado.DERROTA, 9000, 10, BASE),
                vitoria(2, usuario(2), 0, 10, BASE),
                vitoria(3, usuario(3), -5, 10, BASE),
                vitoria(4, usuario(4), 1, 10, BASE)));

        assertThat(ids(r)).containsExactly(4L);
    }

    @Test
    void r4_3_mantemUmaLinhaPorJogadorComAMelhorPartida() {
        Usuario a = usuario(1);
        Usuario b = usuario(2);
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(1, a, 100, 10, BASE),
                vitoria(2, a, 300, 10, BASE),
                vitoria(3, a, 200, 10, BASE),
                vitoria(4, b, 250, 10, BASE)));

        assertThat(ids(r)).containsExactly(2L, 4L);
    }

    @Test
    void r4_4_ordenaPorPontuacaoDecrescente() {
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(1, usuario(1), 100, 10, BASE),
                vitoria(2, usuario(2), 300, 10, BASE),
                vitoria(3, usuario(3), 200, 10, BASE)));

        assertThat(ids(r)).containsExactly(2L, 3L, 1L);
    }

    @Test
    void r4_4_empateDePontuacaoDesempataPorMenorDuracao() {
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(1, usuario(1), 100, 50, BASE),
                vitoria(2, usuario(2), 100, 20, BASE),
                vitoria(3, usuario(3), 100, 30, BASE)));

        assertThat(ids(r)).containsExactly(2L, 3L, 1L);
    }

    @Test
    void r4_4_empateDePontuacaoEDuracaoDesempataPelaPartidaMaisAntiga() {
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(1, usuario(1), 100, 20, BASE.plusSeconds(20)),
                vitoria(2, usuario(2), 100, 20, BASE),
                vitoria(3, usuario(3), 100, 20, BASE.plusSeconds(10))));

        assertThat(ids(r)).containsExactly(2L, 3L, 1L);
    }

    @Test
    void r4_4_empateTotalDesempataPeloMenorId() {
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(9, usuario(1), 100, 20, BASE),
                vitoria(3, usuario(2), 100, 20, BASE),
                vitoria(5, usuario(3), 100, 20, BASE)));

        assertThat(ids(r)).containsExactly(3L, 5L, 9L);
    }

    @Test
    void r4_4_melhorPartidaDoJogadorUsaOMesmoCriterioDeDesempate() {
        Usuario a = usuario(1);
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(1, a, 100, 40, BASE),
                vitoria(2, a, 100, 20, BASE.plusSeconds(5)),
                vitoria(3, a, 100, 20, BASE)));

        assertThat(ids(r)).containsExactly(3L);
    }

    @Test
    void r4_5_cortaEmDezLinhasComPosicoesDeUmADez() {
        List<Partida> todas = new ArrayList<>();
        for (long i = 1; i <= 12; i++) {
            todas.add(vitoria(i, usuario(i), (int) (i * 10), 20, BASE));
        }

        List<LinhaRanking> r = RankingGlobal.calcular(todas);

        assertThat(r).hasSize(10);
        assertThat(r.stream().map(LinhaRanking::posicao).toList())
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertThat(ids(r)).containsExactly(12L, 11L, 10L, 9L, 8L, 7L, 6L, 5L, 4L, 3L);
    }

    @Test
    void r4_5_empateTotalNaoRepetePosicao() {
        List<LinhaRanking> r = RankingGlobal.calcular(List.of(
                vitoria(1, usuario(1), 100, 20, BASE),
                vitoria(2, usuario(2), 100, 20, BASE)));

        assertThat(r.stream().map(LinhaRanking::posicao).toList()).containsExactly(1, 2);
    }

    @Test
    void r4_6_listaVaziaDaListaVazia() {
        assertThat(RankingGlobal.calcular(List.of())).isEmpty();
    }
}
