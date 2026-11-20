package missaomarte.domain;

public class Inimigo extends Perigo {

    public Inimigo(Posicao posicao) {
        super(posicao);
    }

    @Override
    public TipoElemento tipo() {
        return TipoElemento.INIMIGO;
    }

    public void mover(Direcao direcao, Mapa mapa) {
        Posicao destino = posicao.mover(direcao);
        if (mapa.contem(destino)) {
            posicao = destino;
        }
    }
}
