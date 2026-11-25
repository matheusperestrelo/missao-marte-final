package missaomarte.gdx.telas;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import missaomarte.gdx.ComandoMenu;
import missaomarte.gdx.ControleMenu;
import missaomarte.gdx.MissaoMarteGame;
import missaomarte.gdx.TelaBase;

/** Menu principal. Converte o teclado em chamadas ao {@link ControleMenu} e desenha o estado dele. */
public class TelaMenu extends TelaBase {

    private static final String[] OPCOES = {"1 - Jogar", "2 - Ranking", "3 - Limpar ranking", "4 - Sair"};

    private final ControleMenu controle;

    public TelaMenu(MissaoMarteGame game) {
        super(game);
        this.controle = new ControleMenu(game.getRanking());
    }

    @Override
    protected InputProcessor entrada() {
        return new InputAdapter() {
            @Override
            public boolean keyTyped(char caractere) {
                if (!ativa()) {
                    return false;
                }
                executar(controle.caractere(caractere));
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (!ativa() || keycode != Keys.ESCAPE) {
                    return false;
                }
                executar(controle.esc());
                return true;
            }
        };
    }

    private void executar(ComandoMenu comando) {
        switch (comando) {
            case NOVA_PARTIDA:
                game.irParaNovaPartida();
                break;
            case RANKING:
                game.irParaRanking();
                break;
            case SAIR:
                game.sair();
                break;
            case NADA:
            default:
                break;
        }
    }

    @Override
    protected void desenhar(float delta) {
        game.getBatch().begin();
        escrever("Missão Marte Unifor", 0, 520, MissaoMarteGame.LARGURA, Align.center, Color.WHITE, 2.2f);

        float y = 400;
        for (String opcao : OPCOES) {
            y -= escrever(opcao, 300, y, 300, Align.left, Color.WHITE, 1.4f) + 16;
        }

        if (controle.confirmando()) {
            escrever(ControleMenu.PERGUNTA_LIMPAR, 0, 200, MissaoMarteGame.LARGURA, Align.center,
                    Color.YELLOW, 1.2f);
        } else {
            controle.mensagem().ifPresent(texto -> escrever(texto, 0, 200, MissaoMarteGame.LARGURA,
                    Align.center, Color.GREEN, 1.2f));
        }
        game.getBatch().end();
    }
}
