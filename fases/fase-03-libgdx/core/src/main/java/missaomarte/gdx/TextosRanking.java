package missaomarte.gdx;

import java.util.ArrayList;
import java.util.List;

import missaomarte.application.ResumoPartida;
import missaomarte.application.ranking.RankingEntry;

/** Textos da tela de Ranking, sem libGDX. */
public final class TextosRanking {

    public static final String VAZIO = "Nenhum registro encontrado. Seja o primeiro a jogar!";
    public static final String ENTROU = "Parabéns! Você entrou para o Top 10!";
    public static final String NAO_ENTROU = "Sua pontuação não entrou no Top 10.";

    private TextosRanking() {
    }

    /** Uma linha por entrada, na ordem recebida, ou a frase de ranking vazio. */
    public static List<String> linhas(List<RankingEntry> entradas) {
        List<String> linhas = new ArrayList<>();
        if (entradas.isEmpty()) {
            linhas.add(VAZIO);
            return linhas;
        }
        int posicao = 1;
        for (RankingEntry e : entradas) {
            linhas.add(posicao + ". " + e.nome() + " - " + e.pontuacao() + " pts | " + e.dificuldade()
                    + " | Entregues: " + e.passageirosColetados() + " | Tempo: " + e.tempoJogo()
                    + "s | " + e.dataHora());
            posicao++;
        }
        return linhas;
    }

    /** Bloco de resumo mostrado acima do Top 10 depois de uma vitória. */
    public static List<String> resumo(ResumoPartida resumo, boolean entrou) {
        List<String> linhas = new ArrayList<>();
        linhas.add("Pontuação final: " + resumo.pontos() + " pontos");
        linhas.add("Movimentos: " + resumo.movimentos());
        linhas.add("Tempo de jogo: " + resumo.duracaoSegundos() + " segundos");
        linhas.add("Passageiros entregues: " + resumo.entregues());
        linhas.add(entrou ? ENTROU : NAO_ENTROU);
        return linhas;
    }
}
