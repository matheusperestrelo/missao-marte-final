package missaomarte.gdx.telas;

import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import missaomarte.application.ResumoPartida;
import missaomarte.gdx.MissaoMarteGame;
import missaomarte.gdx.TelaBase;
import missaomarte.gdx.TextosRanking;

/**
 * Top 10 e, depois de uma vitória, o resumo da partida. Só mostra: quem registrou
 * a pontuação e informou se ela entrou foi a tela da Partida.
 */
public class TelaRanking extends TelaBase {

    private final Optional<ResumoPartida> resumo;
    private final boolean entrou;
    private List<String> linhasResumo = List.of();
    private List<String> linhasRanking = List.of();

    public TelaRanking(MissaoMarteGame game, Optional<ResumoPartida> resumo, boolean entrou) {
        super(game);
        this.resumo = resumo;
        this.entrou = entrou;
    }

    @Override
    public void show() {
        super.show();
        linhasResumo = resumo.map(r -> TextosRanking.resumo(r, entrou)).orElse(List.of());
        linhasRanking = TextosRanking.linhas(game.getRanking().top10());
    }

    @Override
    protected InputProcessor entrada() {
        return new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (!ativa()) {
                    return false;
                }
                if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER || keycode == Keys.ESCAPE) {
                    game.irParaMenu();
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    protected void desenhar(float delta) {
        game.getBatch().begin();
        float y = 570;
        if (!linhasResumo.isEmpty()) {
            for (String linha : linhasResumo) {
                y -= escrever(linha, 20, y, 760, Align.left, corDaLinha(linha), 1.2f) + 8;
            }
            y -= 14;
        }
        y -= escrever("Ranking Top 10", 20, y, 760, Align.left, Color.WHITE, 1.8f) + 14;
        for (String linha : linhasRanking) {
            y -= escrever(linha, 20, y, 760, Align.left, Color.LIGHT_GRAY, 1f) + 8;
        }
        escrever("Enter ou Esc volta ao Menu", 20, 30, 760, Align.left, Color.GRAY, 1f);
        game.getBatch().end();
    }

    private static Color corDaLinha(String linha) {
        if (linha.equals(TextosRanking.ENTROU)) {
            return Color.GREEN;
        }
        if (linha.equals(TextosRanking.NAO_ENTROU)) {
            return Color.ORANGE;
        }
        return Color.WHITE;
    }
}
