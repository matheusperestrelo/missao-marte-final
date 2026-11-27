package missaomarte.lwjgl3;

import java.nio.file.Paths;
import java.time.Clock;
import java.util.Random;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import missaomarte.application.MissaoFactory;
import missaomarte.application.ranking.RankingService;
import missaomarte.gdx.MissaoMarteGame;
import missaomarte.infrastructure.ranking.FileRankingRepository;

/** Raiz de composição do desktop: cria as dependências e abre a janela. */
public class Lwjgl3Launcher {

    public static void main(String[] args) {
        Random random = new Random();
        Clock clock = Clock.systemDefaultZone();
        MissaoFactory fabrica = new MissaoFactory(random);
        RankingService ranking = new RankingService(
                new FileRankingRepository(Paths.get("ranking.json")), clock);

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Missão Marte Unifor");
        config.setWindowedMode(MissaoMarteGame.LARGURA, MissaoMarteGame.ALTURA);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new MissaoMarteGame(fabrica, random, clock, ranking), config);
    }
}
