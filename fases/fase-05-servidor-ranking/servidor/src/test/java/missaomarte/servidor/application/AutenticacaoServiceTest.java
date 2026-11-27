package missaomarte.servidor.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AutenticacaoServiceTest {

    /** PasswordEncoder simulado que registra cada conferência de senha. */
    static class EncoderEspiao implements PasswordEncoder {
        final List<String> conferidos = new ArrayList<>();

        @Override
        public String encode(CharSequence raw) {
            return "hash:" + raw;
        }

        @Override
        public boolean matches(CharSequence raw, String encoded) {
            conferidos.add(encoded);
            return false;
        }
    }

    private static UsuarioRepository repositorioVazio() {
        return (UsuarioRepository) Proxy.newProxyInstance(
                UsuarioRepository.class.getClassLoader(), new Class<?>[] {UsuarioRepository.class},
                (proxy, metodo, args) -> {
                    if (metodo.getName().equals("findByNomeNormalizado")) {
                        return Optional.<Usuario>empty();
                    }
                    throw new UnsupportedOperationException(metodo.getName());
                });
    }

    @Test
    void r4_6_usuarioInexistenteAindaConfereASenhaContraUmHashFalso() {
        EncoderEspiao encoder = new EncoderEspiao();
        AutenticacaoService servico = new AutenticacaoService(repositorioVazio(), encoder,
                id -> new TokenEmitido("t", 1));

        assertThrows(CredenciaisInvalidasException.class, () -> servico.entrar("ninguem", "qualquer-senha"));

        assertEquals(1, encoder.conferidos.size());
        assertTrue(encoder.conferidos.get(0).startsWith("hash:"));
    }
}
