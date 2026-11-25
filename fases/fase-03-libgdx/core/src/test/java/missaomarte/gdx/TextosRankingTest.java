package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Dificuldade;
import missaomarte.application.ResumoPartida;
import missaomarte.application.ranking.RankingEntry;

class TextosRankingTest {

    @Test
    @DisplayName("R6.1 uma linha por entrada, com posição, nome, pontos, dificuldade, entregues, tempo e data")
    void r6_1_formatoDaLinha() {
        List<RankingEntry> entradas = List.of(
                new RankingEntry("Ana", 85, Dificuldade.FACIL, 4, "2026-11-20 14:30:00", 42),
                new RankingEntry("Bia", 70, Dificuldade.DIFICIL, 5, "2026-11-19 09:05:10", 120));

        assertEquals(List.of(
                "1. Ana - 85 pts | Fácil | Entregues: 4 | Tempo: 42s | 2026-11-20 14:30:00",
                "2. Bia - 70 pts | Difícil | Entregues: 5 | Tempo: 120s | 2026-11-19 09:05:10"),
                TextosRanking.linhas(entradas));
    }

    @Test
    @DisplayName("R6.1 numera até a 10ª posição")
    void r6_1_numeraAteDez() {
        List<RankingEntry> dez = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dez.add(new RankingEntry("P" + i, 100 - i, Dificuldade.MEDIO, 5, "x", 1));
        }

        List<String> linhas = TextosRanking.linhas(dez);

        assertEquals(10, linhas.size());
        assertEquals(true, linhas.get(9).startsWith("10. P9 - 91 pts"));
    }

    @Test
    @DisplayName("R6.2 ranking vazio mostra a frase de primeiro jogador")
    void r6_2_vazio() {
        assertEquals(List.of("Nenhum registro encontrado. Seja o primeiro a jogar!"),
                TextosRanking.linhas(List.of()));
    }

    @Test
    @DisplayName("R6.3 resumo com pontuação, movimentos, tempo, entregues e Parabéns quando entrou")
    void r6_3_resumoQueEntrou() {
        ResumoPartida resumo = new ResumoPartida("Ana", Dificuldade.MEDIO, 85, 17, 5, 42, true);

        assertEquals(List.of(
                "Pontuação final: 85 pontos",
                "Movimentos: 17",
                "Tempo de jogo: 42 segundos",
                "Passageiros entregues: 5",
                "Parabéns! Você entrou para o Top 10!"),
                TextosRanking.resumo(resumo, true));
    }

    @Test
    @DisplayName("R6.3 resumo diz que não entrou no Top 10 quando for o caso")
    void r6_3_resumoQueNaoEntrou() {
        ResumoPartida resumo = new ResumoPartida("Ana", Dificuldade.MEDIO, 3, 40, 4, 90, true);

        List<String> linhas = TextosRanking.resumo(resumo, false);

        assertEquals("Sua pontuação não entrou no Top 10.", linhas.get(linhas.size() - 1));
        assertEquals("Pontuação final: 3 pontos", linhas.get(0));
    }
}
