package missaomarte.servidor.infrastructure;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import org.junit.jupiter.api.Test;

class SegredoJwtTest {

    private static final String MENSAGEM = "MISSAOMARTE_JWT_SEGREDO precisa ter pelo menos 32 bytes.";

    @Test
    void r6_2_ausenteGeraSegredoAleatorioDe32Bytes() {
        SegredoJwt segredo = SegredoJwt.resolver(null, new SecureRandom());
        assertTrue(segredo.aleatorio());
        assertEquals(32, segredo.bytes().length);
    }

    @Test
    void r6_2_vazioGeraSegredoAleatorioDe32Bytes() {
        SegredoJwt segredo = SegredoJwt.resolver("", new SecureRandom());
        assertTrue(segredo.aleatorio());
        assertEquals(32, segredo.bytes().length);
    }

    @Test
    void r6_3_com31BytesImpedeAInicializacaoSemMostrarOValor() {
        String valor = "a".repeat(31);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> SegredoJwt.resolver(valor, new SecureRandom()));
        assertEquals(MENSAGEM, e.getMessage());
        assertFalse(e.getMessage().contains(valor));
    }

    @Test
    void r6_3_contaBytesEmUtf8NaoCaracteres() {
        // 16 caracteres, 32 bytes em UTF-8
        String valor = "é".repeat(16);
        assertEquals(32, valor.getBytes(StandardCharsets.UTF_8).length);
        assertFalse(SegredoJwt.resolver(valor, new SecureRandom()).aleatorio());
        // 15 caracteres, 30 bytes
        assertThrows(IllegalStateException.class,
                () -> SegredoJwt.resolver("é".repeat(15), new SecureRandom()));
    }

    @Test
    void r6_1_com32BytesUsaOsBytesDoValor() {
        String valor = "b".repeat(32);
        SegredoJwt segredo = SegredoJwt.resolver(valor, new SecureRandom());
        assertFalse(segredo.aleatorio());
        assertArrayEquals(valor.getBytes(StandardCharsets.UTF_8), segredo.bytes());
    }

    @Test
    void r6_4_toStringNaoMostraOsBytes() {
        String valor = "segredo-que-nao-pode-vazar-0123456789";
        SegredoJwt segredo = SegredoJwt.resolver(valor, new SecureRandom());
        assertFalse(segredo.toString().contains(valor));
        assertFalse(segredo.toString().contains("segredo"));
    }
}
