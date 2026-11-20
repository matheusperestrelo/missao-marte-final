package missaomarte;

import java.nio.file.Paths;
import java.time.Clock;
import java.util.Random;
import java.util.Scanner;

import missaomarte.application.GameSession;
import missaomarte.application.MissaoFactory;
import missaomarte.application.RankingTop5;
import missaomarte.console.JogoConsole;
import missaomarte.infrastructure.ranking.FileRankingRepository;

/** Composição: cria as dependências e inicia o console. */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        Clock clock = Clock.systemDefaultZone();
        FileRankingRepository repositorio = new FileRankingRepository(Paths.get("ranking.json"));
        RankingTop5 ranking = new RankingTop5(repositorio, clock);
        MissaoFactory fabrica = new MissaoFactory(random);
        GameSession sessao = new GameSession(fabrica, random, clock);
        new JogoConsole(scanner, System.out, sessao, ranking).executar();
    }
}
