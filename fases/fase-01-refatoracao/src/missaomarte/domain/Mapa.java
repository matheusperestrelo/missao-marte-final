package missaomarte.domain;

public class Mapa {

    private final int tamanho;

    public Mapa(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getMin() {
        return -tamanho;
    }

    public int getMax() {
        return tamanho;
    }

    public boolean contem(Posicao posicao) {
        return posicao.x() >= getMin() && posicao.x() <= getMax()
                && posicao.y() >= getMin() && posicao.y() <= getMax();
    }
}
