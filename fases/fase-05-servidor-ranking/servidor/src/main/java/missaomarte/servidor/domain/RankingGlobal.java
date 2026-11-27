package missaomarte.servidor.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Ranking global: melhor vitória de cada jogador, Top 10, calculado em memória. */
public final class RankingGlobal {

    public static final int LIMITE = 10;

    /** Pontuação maior, menor duração, partida mais antiga, menor id. */
    public static final Comparator<Partida> ORDEM = Comparator
            .comparingInt(Partida::getPontuacao).reversed()
            .thenComparingInt(Partida::getDuracaoSegundos)
            .thenComparing(Partida::getDataHora)
            .thenComparing(Partida::getId);

    private RankingGlobal() {
    }

    public static List<LinhaRanking> calcular(List<Partida> candidatas) {
        List<Partida> ordenadas = candidatas.stream()
                .filter(p -> p.getResultado() == Resultado.VITORIA && p.getPontuacao() > 0)
                .sorted(ORDEM)
                .toList();

        Set<Long> jogadoresVistos = new HashSet<>();
        List<LinhaRanking> linhas = new ArrayList<>();
        for (Partida p : ordenadas) {
            if (linhas.size() == LIMITE) {
                break;
            }
            if (jogadoresVistos.add(p.getUsuario().getId())) {
                linhas.add(new LinhaRanking(linhas.size() + 1, p));
            }
        }
        return linhas;
    }
}
