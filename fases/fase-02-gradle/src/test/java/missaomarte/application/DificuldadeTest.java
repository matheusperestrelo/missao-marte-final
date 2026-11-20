package missaomarte.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DificuldadeTest {

    @ParameterizedTest(name = "{0}: pontos {1}, passageiros {2}, asteroides {3}, inimigos {4}, capacidade {5}")
    @CsvSource({
            "FACIL,   30, 4, 1, 1, 4",
            "MEDIO,   20, 5, 2, 2, 3",
            "DIFICIL, 45, 5, 3, 3, 2"
    })
    @DisplayName("R3.2 parâmetros de cada dificuldade")
    void r3_2_parametros(Dificuldade d, int pontos, int passageiros, int asteroides, int inimigos, int capacidade) {
        assertEquals(pontos, d.pontuacaoInicial());
        assertEquals(passageiros, d.passageiros());
        assertEquals(asteroides, d.asteroides());
        assertEquals(inimigos, d.inimigos());
        assertEquals(capacidade, d.capacidadeNave());
    }

    @ParameterizedTest(name = "\"{0}\" vira {1}")
    @CsvSource(value = {
            "facil|FACIL", "fácil|FACIL", "  FACIL  |FACIL", "Fácil|FACIL",
            "dificil|DIFICIL", "difícil|DIFICIL", "DIFÍCIL|DIFICIL",
            "medio|MEDIO", "qualquer|MEDIO", "''|MEDIO"
    }, delimiter = '|')
    @DisplayName("R3.3 conversão do texto, com e sem acento")
    void r3_3_deString(String texto, Dificuldade esperada) {
        assertEquals(esperada, Dificuldade.deString(texto));
    }

    @Test
    @DisplayName("R3.3 nulo vira MEDIO")
    void r3_3_nuloViraMedio() {
        assertEquals(Dificuldade.MEDIO, Dificuldade.deString(null));
    }

    @Test
    @DisplayName("R3.3 nomes de exibição")
    void r3_3_nomesDeExibicao() {
        assertEquals("Fácil", Dificuldade.FACIL.toString());
        assertEquals("Médio", Dificuldade.MEDIO.toString());
        assertEquals("Difícil", Dificuldade.DIFICIL.toString());
    }
}
