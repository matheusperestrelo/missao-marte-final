package missaomarte.domain;

public abstract class Perigo implements ElementoMapa {

    protected Posicao posicao;

    protected Perigo(Posicao posicao) {
        this.posicao = posicao;
    }

    @Override
    public Posicao posicao() {
        return posicao;
    }

    public boolean colideCom(Posicao outra) {
        return posicao.equals(outra);
    }
}
