package missaomarte.gdx;

import java.util.List;

import missaomarte.application.GameSession;
import missaomarte.domain.Missao;

/** As linhas de estado do painel, lidas da sessão; sem libGDX e sem regra. */
public final class TextoPainel {

    private TextoPainel() {
    }

    public static List<String> estado(GameSession sessao) {
        Missao missao = sessao.getMissao();
        return List.of(
                "Piloto: " + sessao.getPiloto(),
                "Dificuldade: " + sessao.getDificuldade(),
                "Vidas: " + sessao.getVidas(),
                "Pontos: " + sessao.getPontos(),
                "A bordo: " + missao.getABordo() + "/" + missao.getNave().getCapacidade(),
                "Entregues: " + missao.getEntregues() + "/" + missao.getTotal(),
                "Restantes: " + missao.getRestantes(),
                "Movimentos: " + sessao.getMovimentos());
    }
}
