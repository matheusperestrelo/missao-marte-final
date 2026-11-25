package missaomarte.domain;

public record Posicao(int x, int y) {

    public static final Posicao ORIGEM = new Posicao(0, 0);

    public Posicao mover(Direcao direcao) {
        return new Posicao(x + direcao.dx(), y + direcao.dy());
    }
}
