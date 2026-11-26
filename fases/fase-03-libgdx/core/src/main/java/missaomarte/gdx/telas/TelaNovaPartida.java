package missaomarte.gdx.telas;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import missaomarte.application.Dificuldade;
import missaomarte.gdx.FormularioNovaPartida;
import missaomarte.gdx.FormularioNovaPartida.Etapa;
import missaomarte.gdx.MissaoMarteGame;
import missaomarte.gdx.ResultadoFormulario;
import missaomarte.gdx.TelaBase;

/** Nova partida: nome do piloto e dificuldade. O estado fica no {@link FormularioNovaPartida}. */
public class TelaNovaPartida extends TelaBase {

    private static final String DICA_NOME = "Enter confirma · vazio = Piloto Anônimo · Esc volta ao Menu";
    private static final String DICA_DIFICULDADE =
            "Setas (cima/baixo) escolhem · Enter inicia · Esc volta ao nome";

    private final FormularioNovaPartida formulario = new FormularioNovaPartida();

    public TelaNovaPartida(MissaoMarteGame game) {
        super(game);
    }

    @Override
    protected InputProcessor entrada() {
        return new InputAdapter() {
            @Override
            public boolean keyTyped(char caractere) {
                if (!ativa()) {
                    return false;
                }
                formulario.caractere(caractere);
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (!ativa()) {
                    return false;
                }
                switch (keycode) {
                    case Keys.BACKSPACE:
                        formulario.apagar();
                        return true;
                    case Keys.UP:
                        formulario.cima();
                        return true;
                    case Keys.DOWN:
                        formulario.baixo();
                        return true;
                    case Keys.ENTER:
                    case Keys.NUMPAD_ENTER:
                        executar(formulario.enter());
                        return true;
                    case Keys.ESCAPE:
                        executar(formulario.esc());
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    private void executar(ResultadoFormulario resultado) {
        switch (resultado) {
            case INICIAR:
                game.iniciarPartida(formulario.nome(), formulario.selecionada());
                break;
            case VOLTAR_MENU:
                game.irParaMenu();
                break;
            case CONTINUA:
            default:
                break;
        }
    }

    @Override
    protected void desenhar(float delta) {
        boolean noNome = formulario.etapa() == Etapa.NOME;
        Color corNome = noNome ? Color.YELLOW : Color.LIGHT_GRAY;
        Color corLista = noNome ? Color.LIGHT_GRAY : Color.YELLOW;

        game.getBatch().begin();
        escrever("Nova partida", 0, 540, MissaoMarteGame.LARGURA, Align.center, Color.WHITE, 2f);

        escrever("Nome do piloto:", 120, 440, 560, Align.left, corNome, 1.2f);
        escrever(formulario.nome() + (noNome ? "_" : ""), 120, 410, 560, Align.left, Color.WHITE, 1.4f);
        escrever(DICA_NOME, 120, 375, 560, Align.left, Color.GRAY, 1f);

        escrever("Dificuldade:", 120, 310, 560, Align.left, corLista, 1.2f);
        float y = 280;
        for (Dificuldade opcao : formulario.opcoes()) {
            boolean marcada = opcao == formulario.selecionada();
            String texto = (marcada ? "> " : "  ") + opcao;
            Color cor = marcada ? (noNome ? Color.WHITE : Color.YELLOW) : Color.GRAY;
            y -= escrever(texto, 140, y, 400, Align.left, cor, 1.4f) + 14;
        }
        if (!noNome) {
            escrever(DICA_DIFICULDADE, 120, 150, 560, Align.left, Color.GRAY, 1f);
        }
        game.getBatch().end();
    }
}
