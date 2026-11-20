package missaomarte.domain;

public class Astronauta extends Passageiro {

    public Astronauta(String nome, Posicao posicao) {
        super(nome, posicao);
    }

    @Override
    public int pontuacao() {
        return 20;
    }

    @Override
    public TipoElemento tipo() {
        return TipoElemento.ASTRONAUTA;
    }
}
