package missaomarte.gdx;

import java.util.Optional;

import com.badlogic.gdx.Input.Keys;

import missaomarte.application.Acao;

/**
 * Converte o código da tecla numa {@link Acao}. Só lê constantes inteiras de
 * {@code Input.Keys}, por isso roda em teste sem janela. Tecla desconhecida
 * devolve vazio e nunca chega à sessão (não gasta turno).
 */
public class MapeadorTeclas {

    public Optional<Acao> acao(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
                return Optional.of(Acao.CIMA);
            case Keys.S:
            case Keys.DOWN:
                return Optional.of(Acao.BAIXO);
            case Keys.A:
            case Keys.LEFT:
                return Optional.of(Acao.ESQUERDA);
            case Keys.D:
            case Keys.RIGHT:
                return Optional.of(Acao.DIREITA);
            case Keys.E:
                return Optional.of(Acao.EMBARCAR);
            case Keys.Q:
            case Keys.ESCAPE:
                return Optional.of(Acao.SAIR);
            default:
                return Optional.empty();
        }
    }
}
