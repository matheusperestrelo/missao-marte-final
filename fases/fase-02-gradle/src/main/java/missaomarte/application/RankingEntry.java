package missaomarte.application;

/** Linha do ranking: uma vitória registrada. */
public record RankingEntry(String nome, int pontuacao, Dificuldade dificuldade,
                           int passageirosColetados, String dataHora, long tempoJogo) {
}
