package missaomarte.domain;

public abstract class Passageiro implements ElementoMapa {

    private final String nome;
    private final Posicao posicao;

    protected Passageiro(String nome, Posicao posicao) {
        this.nome = nome;
        this.posicao = posicao;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public Posicao posicao() {
        return posicao;
    }

    public abstract int pontuacao();

    @Override
    public abstract TipoElemento tipo();
}
