package missaomarte.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import missaomarte.domain.Direcao;

class AcaoTest {

    @ParameterizedTest(name = "\"{0}\" vira {1}")
    @CsvSource({
            "w, CIMA", "W, CIMA",
            "s, BAIXO", "S, BAIXO",
            "a, ESQUERDA", "A, ESQUERDA",
            "d, DIREITA", "D, DIREITA",
            "c, EMBARCAR", "C, EMBARCAR",
            "q, SAIR", "Q, SAIR"
    })
    @DisplayName("R3.1 letras de comando em maiúscula e minúscula")
    void r3_1_letrasDeComando(String texto, Acao esperada) {
        assertEquals(esperada, Acao.deTexto(texto));
    }

    @Test
    @DisplayName("R3.1 só o primeiro caractere conta (ponto c: sair vira BAIXO)")
    void r3_1_soOPrimeiroCaractereConta() {
        assertEquals(Acao.BAIXO, Acao.deTexto("sair"));
        assertEquals(Acao.DIREITA, Acao.deTexto("direita"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"z", "1", " w", "?"})
    @DisplayName("R3.1 vazio, nulo e outros caracteres viram INVALIDA")
    void r3_1_invalidas(String texto) {
        assertEquals(Acao.INVALIDA, Acao.deTexto(texto));
    }

    @Test
    @DisplayName("movimentos têm direção; as demais ações não")
    void direcaoDasAcoes() {
        assertEquals(Direcao.CIMA, Acao.CIMA.direcao());
        assertTrue(Acao.DIREITA.ehMovimento());
        assertFalse(Acao.EMBARCAR.ehMovimento());
        assertNull(Acao.SAIR.direcao());
    }
}
