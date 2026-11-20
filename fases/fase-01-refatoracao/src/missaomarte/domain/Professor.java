package missaomarte.domain;

public class Professor extends Passageiro {

    public Professor(String nome, Posicao posicao) {
        super(nome, posicao);
    }

    @Override
    public int pontuacao() {
        return 10;
    }

    @Override
    public TipoElemento tipo() {
        return TipoElemento.PROFESSOR;
    }
}
