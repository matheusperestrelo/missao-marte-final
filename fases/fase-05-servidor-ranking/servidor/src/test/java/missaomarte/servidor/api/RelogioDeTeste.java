package missaomarte.servidor.api;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** Relógio controlável dos testes de API: parte do instante real e só anda quando mandado. */
public class RelogioDeTeste extends Clock {

    private volatile Instant agora = Instant.now();

    public void fixar(Instant instante) {
        this.agora = instante;
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return agora;
    }
}
