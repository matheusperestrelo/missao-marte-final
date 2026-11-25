package missaomarte.domain;

public class Base implements ElementoMapa {

    @Override
    public Posicao posicao() {
        return Posicao.ORIGEM;
    }

    @Override
    public TipoElemento tipo() {
        return TipoElemento.BASE;
    }
}
