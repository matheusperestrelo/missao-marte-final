package missaomarte.gdx;

import java.time.Clock;
import java.util.Optional;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import missaomarte.application.Dificuldade;
import missaomarte.application.GameSession;
import missaomarte.application.MissaoFactory;
import missaomarte.application.ResumoPartida;
import missaomarte.application.ranking.RankingService;
import missaomarte.gdx.telas.TelaMenu;
import missaomarte.gdx.telas.TelaNovaPartida;
import missaomarte.gdx.telas.TelaPartida;
import missaomarte.gdx.telas.TelaRanking;

/**
 * Ponto de entrada do libGDX: cria os recursos gráficos compartilhados em
 * {@code create()} e os libera em {@code dispose()}. As dependências do jogo
 * chegam pelo construtor; quem as cria é o launcher (raiz de composição).
 */
public class MissaoMarteGame extends Game {

    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    /** Mapa fixo de 11×11 células (de -5 a +5); a tela de Nova partida não pergunta o tamanho. */
    public static final int TAMANHO_MAPA = 5;

    private final MissaoFactory fabrica;
    private final Random random;
    private final Clock clock;
    private final RankingService ranking;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont fonte;
    private Recursos recursos;

    public MissaoMarteGame(MissaoFactory fabrica, Random random, Clock clock, RankingService ranking) {
        this.fabrica = fabrica;
        this.random = random;
        this.clock = clock;
        this.ranking = ranking;
    }

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA, ALTURA, camera);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        fonte = new BitmapFont();
        recursos = new Recursos();
        recursos.carregar();
        setScreen(new TelaMenu(this));
    }

    @Override
    public void dispose() {
        super.dispose(); // esconde a tela atual (hide)
        if (getScreen() != null) {
            getScreen().dispose();
        }
        batch.dispose();
        shapeRenderer.dispose();
        fonte.dispose();
        recursos.dispose();
    }

    // ---------- Navegação ----------

    public void irParaMenu() {
        trocarTela(new TelaMenu(this));
    }

    public void irParaNovaPartida() {
        trocarTela(new TelaNovaPartida(this));
    }

    /** Ranking aberto pelo Menu, sem resumo de partida. */
    public void irParaRanking() {
        trocarTela(new TelaRanking(this, Optional.empty(), false));
    }

    /** Ranking aberto depois de uma vitória, com o resumo e a informação se entrou no Top 10. */
    public void irParaRanking(ResumoPartida resumo, boolean entrou) {
        trocarTela(new TelaRanking(this, Optional.of(resumo), entrou));
    }

    /** Cria uma sessão nova (partida do zero) e abre a tela da Partida. */
    public void iniciarPartida(String piloto, Dificuldade dificuldade) {
        GameSession sessao = new GameSession(fabrica, random, clock);
        sessao.iniciar(piloto, dificuldade, TAMANHO_MAPA);
        trocarTela(new TelaPartida(this, new ControlePartida(sessao, ranking)));
    }

    /** Fecha o jogo; o libGDX chama {@code dispose()} e libera os recursos. */
    public void sair() {
        Gdx.app.exit();
    }

    // As telas são leves e recriadas a cada navegação; a anterior é liberada aqui.
    private void trocarTela(Screen nova) {
        Screen anterior = getScreen();
        setScreen(nova);
        if (anterior != null) {
            anterior.dispose();
        }
    }

    // ---------- Recursos e dependências compartilhados ----------

    public MissaoFactory getFabrica() {
        return fabrica;
    }

    public Random getRandom() {
        return random;
    }

    public Clock getClock() {
        return clock;
    }

    public RankingService getRanking() {
        return ranking;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public Recursos getRecursos() {
        return recursos;
    }

    public BitmapFont getFonte() {
        return fonte;
    }
}
