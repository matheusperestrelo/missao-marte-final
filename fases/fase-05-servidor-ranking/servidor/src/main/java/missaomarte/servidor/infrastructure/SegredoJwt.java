package missaomarte.servidor.infrastructure;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/** Resolução pura do segredo de assinatura do JWT. */
public final class SegredoJwt {

    static final int MINIMO_BYTES = 32;

    private final byte[] bytes;
    private final boolean aleatorio;

    private SegredoJwt(byte[] bytes, boolean aleatorio) {
        this.bytes = bytes;
        this.aleatorio = aleatorio;
    }

    public static SegredoJwt resolver(String valorDaVariavel, SecureRandom random) {
        if (valorDaVariavel == null || valorDaVariavel.isEmpty()) {
            byte[] gerado = new byte[MINIMO_BYTES];
            random.nextBytes(gerado);
            return new SegredoJwt(gerado, true);
        }
        byte[] valor = valorDaVariavel.getBytes(StandardCharsets.UTF_8);
        if (valor.length < MINIMO_BYTES) {
            throw new IllegalStateException(
                    "MISSAOMARTE_JWT_SEGREDO precisa ter pelo menos 32 bytes.");
        }
        return new SegredoJwt(valor, false);
    }

    public byte[] bytes() {
        return bytes.clone();
    }

    public boolean aleatorio() {
        return aleatorio;
    }

    @Override
    public String toString() {
        return "SegredoJwt[aleatorio=" + aleatorio + ", bytes=***]";
    }
}
