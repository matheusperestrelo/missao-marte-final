package missaomarte.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MapaTest {

    private final Mapa mapa = new Mapa(5);

    @Test
    @DisplayName("R2.3 posições da borda estão no mapa")
    void r2_3_bordasEstaoContidas() {
        assertTrue(mapa.contem(new Posicao(-5, -5)));
        assertTrue(mapa.contem(new Posicao(5, 5)));
        assertTrue(mapa.contem(new Posicao(-5, 5)));
        assertTrue(mapa.contem(Posicao.ORIGEM));
    }

    @Test
    @DisplayName("R2.3 posições de fora não estão no mapa")
    void r2_3_foraDaBordaNaoEstaContido() {
        assertFalse(mapa.contem(new Posicao(6, 0)));
        assertFalse(mapa.contem(new Posicao(0, -6)));
        assertFalse(mapa.contem(new Posicao(-6, 6)));
    }
}
