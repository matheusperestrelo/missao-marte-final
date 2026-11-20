package missaomarte.domain;

public interface ElementoMapa {

    Posicao posicao();

    TipoElemento tipo();

    default char simbolo() {
        return tipo().simbolo();
    }
}
