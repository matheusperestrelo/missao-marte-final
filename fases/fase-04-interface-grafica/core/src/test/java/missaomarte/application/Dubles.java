package missaomarte.application;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;

import missaomarte.domain.Missao;

/** Dublês escritos à mão para os testes (sem Mockito). */
final class Dubles {

    private Dubles() {
    }

    /** Fábrica que devolve a Missao montada pelo teste, ignorando dificuldade e tamanho. */
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

    /** Relógio que só anda quando o teste manda. */
    static final class RelogioControlado extends Clock {
        private long millis;

        RelogioControlado(long millisIniciais) {
            this.millis = millisIniciais;
        }

        void avancar(long ms) {
            millis += ms;
        }

        @Override
        public long millis() {
            return millis;
        }

        @Override
        public Instant instant() {
            return Instant.ofEpochMilli(millis);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }
    }

    /** Random que devolve uma sequência fixa em nextInt(int): 0 direita, 1 esquerda, 2 baixo, 3 cima. */
    static final class RandomSequencia extends Random {
        private static final long serialVersionUID = 1L;
        private final int[] valores;
        private int i;

        RandomSequencia(int... valores) {
            this.valores = valores;
        }

        @Override
        public int nextInt(int limite) {
            return valores[i++ % valores.length];
        }
    }
}
