package missaomarte.console;

import missaomarte.application.GameSession;
import missaomarte.domain.ElementoMapa;
import missaomarte.domain.Mapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Passageiro;
import missaomarte.domain.Posicao;
import missaomarte.domain.TipoElemento;

/** Monta o texto do mapa, da legenda e da linha de status. */
public class DesenhoMapa {

    private static final String NL = System.lineSeparator();

    public String desenhar(GameSession sessao) {
        Missao missao = sessao.getMissao();
        Mapa mapa = missao.getMapa();
        int min = mapa.getMin();
        int max = mapa.getMax();
        StringBuilder sb = new StringBuilder();

        sb.append(NL);
        sb.append(String.format("Mapa da Missão (Pontos: %d) - Piloto: %s%n",
                sessao.getPontos(), sessao.getPiloto()));
        sb.append("    ");
        for (int x = min; x <= max; x++) {
            sb.append(String.format(" %2d", x));
        }
        sb.append(NL).append("    ");
        for (int x = min; x <= max; x++) {
            sb.append(" __");
        }
        sb.append(NL);

        for (int y = min; y <= max; y++) {
            sb.append(String.format("%3d|", y));
            for (int x = min; x <= max; x++) {
                sb.append(String.format(" %2c", simboloEm(missao, new Posicao(x, y))));
            }
            sb.append(NL);
        }

        sb.append(legenda()).append(NL);
        sb.append("Comandos: w/s/a/d (mover), c (embarcar), q (sair)").append(NL);
        sb.append("Passageiros na superfície marciana:");
        for (Passageiro p : missao.getPassageirosNoMapa()) {
            sb.append(NL).append(String.format(" - %s (%s) em (%d,%d)",
                    p.getNome(), p.tipo().nome(), p.posicao().x(), p.posicao().y()));
        }
        return sb.toString();
    }

    public String linhaStatus(GameSession sessao) {
        Missao missao = sessao.getMissao();
        Posicao nave = missao.getNave().posicao();
        return String.format(
                "Nave em (%d,%d) | Pontos: %d | Vidas: %d | A bordo: %d/%d | Entregues: %d/%d | Restantes: %d",
                nave.x(), nave.y(), sessao.getPontos(), sessao.getVidas(),
                missao.getABordo(), missao.getNave().getCapacidade(),
                missao.getEntregues(), missao.getTotal(), missao.getRestantes());
    }

    private static char simboloEm(Missao missao, Posicao posicao) {
        for (ElementoMapa elemento : missao.elementos()) {
            if (elemento.posicao().equals(posicao)) {
                return elemento.simbolo();
            }
        }
        return '.';
    }

    private static String legenda() {
        StringBuilder sb = new StringBuilder("Legenda: ");
        for (TipoElemento tipo : TipoElemento.values()) {
            sb.append(tipo.simbolo()).append('=').append(tipo.nome()).append(", ");
        }
        return sb.append(".=Vazio").toString();
    }
}
