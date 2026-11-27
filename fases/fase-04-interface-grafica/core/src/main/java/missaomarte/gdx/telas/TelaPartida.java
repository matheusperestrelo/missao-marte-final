package missaomarte.gdx.telas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;

import missaomarte.domain.ElementoMapa;
import missaomarte.domain.TipoElemento;
import missaomarte.gdx.ControlePartida;
import missaomarte.gdx.CoresElemento;
import missaomarte.gdx.DestinoFim;
import missaomarte.gdx.MapaGeometria;
import missaomarte.gdx.MapeadorTeclas;
import missaomarte.gdx.MissaoMarteGame;
import missaomarte.gdx.TelaBase;

/**
 * Partida: converte tecla em {@code Acao} (via {@link MapeadorTeclas} e {@link ControlePartida})
 * e desenha o estado da sessão. Não tem regra de jogo.
 */
public class TelaPartida extends TelaBase {

    private static final float LADO = 48f;
    private static final float ORIGEM_X = 16f;
    private static final float ORIGEM_Y = 36f;
    private static final float PAINEL_X = 560f;
    private static final float PAINEL_LARGURA = 230f;
    private static final float PAINEL_TOPO = 580f;
    private static final float MARGEM_ELEMENTO = 3f;
    private static final float MARGEM_NAVE = 8f;
    private static final Color COR_FUNDO_GRADE = new Color(0.10f, 0.10f, 0.18f, 1f);
    private static final Color COR_LINHA_GRADE = new Color(0.30f, 0.30f, 0.42f, 1f);

    private final ControlePartida controle;
    private final MapeadorTeclas mapeador = new MapeadorTeclas();
    private final MapaGeometria geometria;

    public TelaPartida(MissaoMarteGame game, ControlePartida controle) {
        super(game);
        this.controle = controle;
        this.geometria = MapaGeometria.de(controle.sessao().getMissao().getMapa(), ORIGEM_X, ORIGEM_Y, LADO);
    }

    @Override
    protected InputProcessor entrada() {
        return new InputAdapter() {
            // Um turno por toque de tecla: o libGDX não repete keyDown ao segurar a tecla.
            @Override
            public boolean keyDown(int keycode) {
                if (!ativa()) {
                    return false;
                }
                if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
                    navegar(controle.enter());
                    return true;
                }
                controle.tecla(mapeador.acao(keycode));
                if (controle.abandonada()) {
                    game.irParaMenu();
                }
                return true;
            }
        };
    }

    private void navegar(DestinoFim destino) {
        switch (destino) {
            case RANKING:
                game.irParaRanking(controle.resumo(), controle.entrouNoRanking());
                break;
            case MENU:
                game.irParaMenu();
                break;
            case NENHUM:
            default:
                break;
        }
    }

    @Override
    protected void desenhar(float delta) {
        List<ElementoMapa> elementos = elementosEmOrdemDeDesenho();
        desenharGrade();
        desenharElementos(elementos);
        desenharTextos(elementos);
    }

    // Base primeiro, nave por último: a nave aparece por cima do que estiver na mesma célula.
    private List<ElementoMapa> elementosEmOrdemDeDesenho() {
        List<ElementoMapa> ordem = new ArrayList<>(controle.sessao().getMissao().elementos());
        ordem.sort((a, b) -> Integer.compare(camada(a.tipo()), camada(b.tipo())));
        return ordem;
    }

    private static int camada(TipoElemento tipo) {
        if (tipo == TipoElemento.BASE) {
            return 0;
        }
        return tipo == TipoElemento.NAVE ? 2 : 1;
    }

    private void desenharGrade() {
        ShapeRenderer shapes = game.getShapeRenderer();
        shapes.begin(ShapeType.Filled);
        shapes.setColor(COR_FUNDO_GRADE);
        shapes.rect(geometria.origemX(), geometria.origemY(), geometria.larguraGrade(), geometria.alturaGrade());
        shapes.end();

        shapes.begin(ShapeType.Line);
        shapes.setColor(COR_LINHA_GRADE);
        int n = geometria.celulasPorLado();
        for (int i = 0; i <= n; i++) {
            float deslocamento = i * geometria.lado();
            shapes.line(geometria.origemX() + deslocamento, geometria.origemY(),
                    geometria.origemX() + deslocamento, geometria.origemY() + geometria.alturaGrade());
            shapes.line(geometria.origemX(), geometria.origemY() + deslocamento,
                    geometria.origemX() + geometria.larguraGrade(), geometria.origemY() + deslocamento);
        }
        shapes.end();
    }

    private void desenharElementos(List<ElementoMapa> elementos) {
        ShapeRenderer shapes = game.getShapeRenderer();
        shapes.begin(ShapeType.Filled);
        for (ElementoMapa elemento : elementos) {
            float margem = elemento.tipo() == TipoElemento.NAVE ? MARGEM_NAVE : MARGEM_ELEMENTO;
            shapes.setColor(CoresElemento.de(elemento.tipo()));
            shapes.rect(geometria.x(elemento.posicao().x()) + margem,
                    geometria.y(elemento.posicao().y()) + margem,
                    geometria.lado() - 2 * margem, geometria.lado() - 2 * margem);
        }
        shapes.end();
    }

    private void desenharTextos(List<ElementoMapa> elementos) {
        game.getBatch().begin();
        float meioDaFonte = game.getFonte().getCapHeight() / 2f;
        for (ElementoMapa elemento : elementos) {
            escrever(String.valueOf(elemento.simbolo()),
                    geometria.x(elemento.posicao().x()),
                    geometria.y(elemento.posicao().y()) + geometria.lado() / 2f + meioDaFonte,
                    geometria.lado(), Align.center, Color.BLACK, 1f);
        }
        desenharPainel();
        game.getBatch().end();
    }

    private void desenharPainel() {
        float y = PAINEL_TOPO;
        for (String linha : controle.estado()) {
            y -= escrever(linha, PAINEL_X, y, PAINEL_LARGURA, Align.left, Color.WHITE, 1f) + 8;
        }
        y -= 12;
        for (String mensagem : controle.mensagens()) {
            y -= escrever(mensagem, PAINEL_X, y, PAINEL_LARGURA, Align.left, Color.LIGHT_GRAY, 1f) + 8;
        }
        if (controle.alerta().isPresent()) {
            y -= escrever(controle.alerta().get(), PAINEL_X, y, PAINEL_LARGURA, Align.left, Color.YELLOW, 1f) + 8;
        }
        if (controle.linhaFim().isPresent()) {
            escrever(controle.linhaFim().get(), PAINEL_X, y - 6, PAINEL_LARGURA, Align.left, Color.CYAN, 1.1f);
        }
        escrever("W/A/S/D ou setas movem · E embarca · Q ou Esc sai", PAINEL_X, 70, PAINEL_LARGURA,
                Align.left, Color.GRAY, 1f);
    }
}
