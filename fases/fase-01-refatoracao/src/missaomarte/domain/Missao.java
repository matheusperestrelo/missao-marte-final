package missaomarte.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Missao {

    private final Mapa mapa;
    private final Nave nave;
    private final Base base = new Base();
    private final List<Passageiro> passageirosNoMapa = new ArrayList<>();
    private final List<Passageiro> entregues = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Inimigo> inimigos = new ArrayList<>();
    private int total;

    public Missao(Mapa mapa, Nave nave) {
        this.mapa = mapa;
        this.nave = nave;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public Nave getNave() {
        return nave;
    }

    public void adicionarPassageiro(Passageiro passageiro) {
        passageirosNoMapa.add(passageiro);
        total++;
    }

    public void adicionarAsteroide(Asteroide asteroide) {
        asteroides.add(asteroide);
    }

    public void adicionarInimigo(Inimigo inimigo) {
        inimigos.add(inimigo);
    }

    public boolean estaOcupada(Posicao posicao) {
        return elementos().stream()
                .anyMatch(e -> e != base && e.posicao().equals(posicao));
    }

    public Passageiro passageiroNaPosicaoDaNave() {
        for (Passageiro passageiro : passageirosNoMapa) {
            if (passageiro.posicao().equals(nave.posicao())) {
                return passageiro;
            }
        }
        return null;
    }

    public boolean embarcar(Passageiro passageiro) {
        if (!passageirosNoMapa.contains(passageiro) || !nave.embarcar(passageiro)) {
            return false;
        }
        passageirosNoMapa.remove(passageiro);
        return true;
    }

    public int desembarcarNaBase() {
        if (!nave.posicao().equals(base.posicao())) {
            return 0;
        }
        List<Passageiro> desembarcados = nave.desembarcarTodos();
        entregues.addAll(desembarcados);
        return desembarcados.size();
    }

    public void moverInimigos(Supplier<Direcao> direcoes) {
        for (Inimigo inimigo : inimigos) {
            inimigo.mover(direcoes.get(), mapa);
        }
    }

    public boolean haColisao() {
        return asteroides.stream().anyMatch(a -> a.colideCom(nave.posicao()))
                || inimigos.stream().anyMatch(i -> i.colideCom(nave.posicao()));
    }

    public int getABordo() {
        return nave.getABordo().size();
    }

    public int getEntregues() {
        return entregues.size();
    }

    public int getRestantes() {
        return passageirosNoMapa.size();
    }

    public int getTotal() {
        return total;
    }

    public boolean todosEntregues() {
        return entregues.size() == total;
    }

    public List<Passageiro> getPassageirosNoMapa() {
        return List.copyOf(passageirosNoMapa);
    }

    public List<ElementoMapa> elementos() {
        List<ElementoMapa> elementos = new ArrayList<>();
        elementos.add(nave);
        elementos.addAll(passageirosNoMapa);
        elementos.addAll(asteroides);
        elementos.addAll(inimigos);
        elementos.add(base);
        return List.copyOf(elementos);
    }
}
