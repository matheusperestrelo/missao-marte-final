package missaomarte.application;

public enum Dificuldade {
    FACIL("Fácil", 30, 4, 1, 1, 4),
    MEDIO("Médio", 20, 5, 2, 2, 3),
    DIFICIL("Difícil", 45, 5, 3, 3, 2);

    private final String rotulo;
    private final int pontuacaoInicial;
    private final int passageiros;
    private final int asteroides;
    private final int inimigos;
    private final int capacidadeNave;

    Dificuldade(String rotulo, int pontuacaoInicial, int passageiros,
                int asteroides, int inimigos, int capacidadeNave) {
        this.rotulo = rotulo;
        this.pontuacaoInicial = pontuacaoInicial;
        this.passageiros = passageiros;
        this.asteroides = asteroides;
        this.inimigos = inimigos;
        this.capacidadeNave = capacidadeNave;
    }

    public int pontuacaoInicial() { return pontuacaoInicial; }
    public int passageiros() { return passageiros; }
    public int asteroides() { return asteroides; }
    public int inimigos() { return inimigos; }
    public int capacidadeNave() { return capacidadeNave; }

    public static Dificuldade deString(String s) {
        if (s == null) return MEDIO;
        switch (s.trim().toLowerCase()) {
            case "facil":
            case "fácil":
                return FACIL;
            case "dificil":
            case "difícil":
                return DIFICIL;
            default:
                return MEDIO;
        }
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
