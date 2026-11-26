package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Input.Keys;

import missaomarte.application.Acao;

/** Só usa constantes de Input.Keys: não abre janela nem inicializa o Gdx. */
class MapeadorTeclasTest {

    private final MapeadorTeclas mapeador = new MapeadorTeclas();

    private void esperar(Acao acao, int... teclas) {
        for (int tecla : teclas) {
            assertEquals(Optional.of(acao), mapeador.acao(tecla), "tecla " + Keys.toString(tecla));
        }
    }

    @Test
    @DisplayName("R1.1 W e seta para cima movem para cima")
    void r1_1_cima() {
        esperar(Acao.CIMA, Keys.W, Keys.UP);
    }

    @Test
    @DisplayName("R1.2 S e seta para baixo movem para baixo")
    void r1_2_baixo() {
        esperar(Acao.BAIXO, Keys.S, Keys.DOWN);
    }

    @Test
    @DisplayName("R1.3 A e seta para a esquerda movem para a esquerda")
    void r1_3_esquerda() {
        esperar(Acao.ESQUERDA, Keys.A, Keys.LEFT);
    }

    @Test
    @DisplayName("R1.4 D e seta para a direita movem para a direita")
    void r1_4_direita() {
        esperar(Acao.DIREITA, Keys.D, Keys.RIGHT);
    }

    @Test
    @DisplayName("R1.5 E embarca")
    void r1_5_embarcar() {
        esperar(Acao.EMBARCAR, Keys.E);
    }

    @Test
    @DisplayName("R1.6 Q e Esc abandonam")
    void r1_6_sair() {
        esperar(Acao.SAIR, Keys.Q, Keys.ESCAPE);
    }

    @Test
    @DisplayName("R1.7 qualquer outra tecla (C, Enter, números, espaço) não vira ação")
    void r1_7_outrasTeclasSaoIgnoradas() {
        int[] outras = {Keys.C, Keys.ENTER, Keys.NUMPAD_ENTER, Keys.NUM_1, Keys.NUM_0, Keys.SPACE,
                Keys.TAB, Keys.F1, Keys.M, Keys.BACKSPACE, Keys.UNKNOWN};
        for (int tecla : outras) {
            assertEquals(Optional.empty(), mapeador.acao(tecla), "tecla " + tecla);
        }
    }

    @Test
    @DisplayName("tecla desconhecida nunca vira Acao.INVALIDA: não chega à sessão")
    void nuncaDevolveInvalida() {
        for (int tecla = 0; tecla < 300; tecla++) {
            mapeador.acao(tecla).ifPresent(acao ->
                    assertEquals(false, acao == Acao.INVALIDA, "INVALIDA vazou"));
        }
    }
}
