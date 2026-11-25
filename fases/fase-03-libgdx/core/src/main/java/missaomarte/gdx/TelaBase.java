package missaomarte.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Base das telas: registra o ciclo de vida no log, mantém o viewport e liga
 * e desliga o {@link InputProcessor} em {@code show()} e {@code hide()}.
 * As telas concretas só implementam {@link #desenhar(float)} e, se precisarem
 * de teclado, {@link #entrada()}.
 */
public abstract class TelaBase extends ScreenAdapter {

    private static final String TAG = "Tela";

    protected final MissaoMarteGame game;

    private boolean ativa;

    protected TelaBase(MissaoMarteGame game) {
        this.game = game;
    }

    /** Desenha o quadro. O viewport e as matrizes de projeção já estão aplicados. */
    protected abstract void desenhar(float delta);

    /** Entrada de teclado da tela. Por padrão não trata nada. */
    protected InputProcessor entrada() {
        return new InputAdapter();
    }

    /**
     * Verdadeiro entre {@code show()} e {@code hide()}. O libGDX pode entregar eventos que
     * já estavam na fila ao processador antigo; as telas ignoram esses eventos.
     */
    protected final boolean ativa() {
        return ativa;
    }

    /**
     * Escreve texto com quebra de linha na largura dada e devolve a altura usada.
     * O batch precisa estar entre {@code begin()} e {@code end()}. Restaura cor e escala da fonte.
     */
    protected float escrever(String texto, float x, float y, float largura, int alinhamento,
                             Color cor, float escala) {
        BitmapFont fonte = game.getFonte();
        fonte.getData().setScale(escala);
        fonte.setColor(cor);
        GlyphLayout layout = fonte.draw(game.getBatch(), texto, x, y, largura, alinhamento, true);
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(1f);
        return layout.height;
    }

    @Override
    public void show() {
        ativa = true;
        registrar("show");
        Gdx.input.setInputProcessor(entrada());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.12f, 1f);
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getCamera().combined);
        game.getShapeRenderer().setProjectionMatrix(game.getCamera().combined);
        desenhar(delta);
    }

    @Override
    public void resize(int largura, int altura) {
        registrar("resize " + largura + "x" + altura);
        game.getViewport().update(largura, altura, true);
    }

    @Override
    public void pause() {
        registrar("pause");
    }

    @Override
    public void resume() {
        registrar("resume");
    }

    @Override
    public void hide() {
        ativa = false;
        registrar("hide");
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        registrar("dispose");
    }

    private void registrar(String evento) {
        Gdx.app.log(TAG, getClass().getSimpleName() + " " + evento);
    }
}
