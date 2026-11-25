package missaomarte.domain;

import java.util.ArrayList;
import java.util.List;

public class Nave implements ElementoMapa {

    private static final int VIDAS_INICIAIS = 3;

    private final int capacidade;
    private final List<Passageiro> aBordo = new ArrayList<>();
    private Posicao posicao = Posicao.ORIGEM;
    private int vidas = VIDAS_INICIAIS;

    public Nave(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public Posicao posicao() {
        return posicao;
    }

    @Override
    public TipoElemento tipo() {
        return TipoElemento.NAVE;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getVidas() {
        return vidas;
    }

    public void mover(Direcao direcao, Mapa mapa) {
        Posicao destino = posicao.mover(direcao);
        if (mapa.contem(destino)) {
            posicao = destino;
        }
    }

    public boolean estaCheia() {
        return aBordo.size() >= capacidade;
    }

    public boolean embarcar(Passageiro passageiro) {
        if (estaCheia()) {
            return false;
        }
        aBordo.add(passageiro);
        return true;
    }

    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public List<Passageiro> desembarcarTodos() {
        List<Passageiro> desembarcados = new ArrayList<>(aBordo);
        aBordo.clear();
        return desembarcados;
    }

    public List<Passageiro> getABordo() {
        return List.copyOf(aBordo);
    }
}
