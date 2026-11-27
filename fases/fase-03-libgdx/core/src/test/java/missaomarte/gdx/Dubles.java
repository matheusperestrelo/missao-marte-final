package missaomarte.gdx;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import missaomarte.application.Dificuldade;
import missaomarte.application.GameSession;
import missaomarte.application.MissaoFactory;
import missaomarte.application.ranking.RankingEntry;
import missaomarte.application.ranking.RankingException;
import missaomarte.application.ranking.RankingRepository;
import missaomarte.application.ranking.RankingService;
import missaomarte.domain.Missao;

/** Dublês dos testes do pacote gdx (os de outros pacotes são package-private). */
final class Dubles {

    static final Clock RELOGIO = Clock.fixed(Instant.parse("2026-11-20T14:30:00Z"), ZoneOffset.UTC);

    private Dubles() {
    }

    /** Fábrica que devolve a missão montada pelo teste, ignorando dificuldade e tamanho. */
    static final class FabricaFixa extends MissaoFactory {
        private final Missao missao;

        FabricaFixa(Missao missao) {
            super(new Random(0));
            this.missao = missao;
        }

        @Override
        public Missao criar(Dificuldade dificuldade, int tamanho) {
            return missao;
        }
    }

    /** Ranking em memória; conta as gravações. */
    static final class RankingEmMemoria implements RankingRepository {
        final List<RankingEntry> entradas = new ArrayList<>();
        int salvamentos;

        @Override
        public List<RankingEntry> carregar() {
            return new ArrayList<>(entradas);
        }

        @Override
        public void salvar(List<RankingEntry> novas) {
            salvamentos++;
            entradas.clear();
            entradas.addAll(novas);
        }

        @Override
        public void limpar() {
            entradas.clear();
        }
    }

    /** Ranking cujo salvar e limpar sempre falham com "disco cheio". */
    static final class RankingQueFalha implements RankingRepository {
        @Override
        public List<RankingEntry> carregar() {
            return new ArrayList<>();
        }

        @Override
        public void salvar(List<RankingEntry> entradas) {
            throw new RankingException("disco cheio");
        }

        @Override
        public void limpar() {
            throw new RankingException("disco cheio");
        }
    }

    static RankingService servico(RankingRepository repositorio) {
        return new RankingService(repositorio, RELOGIO);
    }

    /** Sessão iniciada sobre a missão dada, com o relógio fixo (duração 0 s). */
    static GameSession sessao(Missao missao, Dificuldade dificuldade) {
        GameSession sessao = new GameSession(new FabricaFixa(missao), new Random(42), RELOGIO);
        sessao.iniciar("Ana", dificuldade, 5);
        return sessao;
    }
}
