package missaomarte.servidor.domain;

import java.util.Locale;

public final class NomeUsuario {

    private NomeUsuario() {
    }

    public static String normalizar(String nome) {
        return nome.toLowerCase(Locale.ROOT);
    }
}
