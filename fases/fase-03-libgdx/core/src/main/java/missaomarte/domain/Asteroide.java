package missaomarte.domain;

public class Asteroide extends Perigo {

    public Asteroide(Posicao posicao) {
        super(posicao);
    }

    @Override
    public TipoElemento tipo() {
        return TipoElemento.ASTEROIDE;
    }
}
