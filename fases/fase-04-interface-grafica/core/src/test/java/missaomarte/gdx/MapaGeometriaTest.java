package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.domain.Mapa;

class MapaGeometriaTest {

    private static final float DELTA = 0.001f;

    // Mapa 11x11 (-5..5), células de 48 px, grade com o canto inferior esquerdo em (16, 36).
    private final MapaGeometria geo = MapaGeometria.de(new Mapa(5), 16f, 36f, 48f);

    @Test
    @DisplayName("R3.2 menor x e menor y: ponto mais à esquerda e mais alto da grade")
    void r3_2_cantoSuperiorEsquerdo() {
        float xMin = geo.x(-5);
        float yTopo = geo.y(-5);

        assertEquals(16f, xMin, DELTA);
        assertEquals(36f + 10 * 48f, yTopo, DELTA);
        for (int c = -5; c <= 5; c++) {
            assertTrue(geo.x(c) >= xMin, "nenhuma coluna fica à esquerda da menor");
            assertTrue(geo.y(c) <= yTopo, "nenhuma linha fica acima da menor");
        }
    }

    @Test
    @DisplayName("R3.3 maior x e maior y: ponto mais à direita e mais baixo da grade")
    void r3_3_cantoInferiorDireito() {
        assertEquals(16f + 10 * 48f, geo.x(5), DELTA);
        assertEquals(36f, geo.y(5), DELTA);
    }

    @Test
    @DisplayName("R3.4 a origem (0,0) é o centro da grade: 6ª coluna e 6ª linha")
    void r3_4_origemNoCentro() {
        assertEquals(16f + 5 * 48f, geo.x(0), DELTA);
        assertEquals(36f + 5 * 48f, geo.y(0), DELTA);
        // 5 células de cada lado: o centro da célula central é o centro da grade.
        assertEquals(geo.origemX() + geo.larguraGrade() / 2f, geo.x(0) + geo.lado() / 2f, DELTA);
        assertEquals(geo.origemY() + geo.alturaGrade() / 2f, geo.y(0) + geo.lado() / 2f, DELTA);
    }

    @Test
    @DisplayName("R3.5 y + 1 desce uma célula; x + 1 vai uma célula para a direita")
    void r3_5_passos() {
        assertEquals(-48f, geo.y(1) - geo.y(0), DELTA, "y maior no domínio é mais baixo na tela");
        assertEquals(48f, geo.x(1) - geo.x(0), DELTA);
        assertEquals(0f, geo.y(3) - geo.y(3), DELTA);
        assertEquals(0f, geo.x(2) - geo.x(2), DELTA);
    }

    @Test
    @DisplayName("tamanho da grade: 11 células de 48 px = 528 px, dentro da área 800x600")
    void tamanhoDaGrade() {
        assertEquals(11, geo.celulasPorLado());
        assertEquals(528f, geo.larguraGrade(), DELTA);
        assertEquals(528f, geo.alturaGrade(), DELTA);
        assertTrue(geo.origemX() + geo.larguraGrade() <= 560f, "não invade o painel que começa em x = 560");
        assertTrue(geo.origemY() + geo.alturaGrade() <= 600f);
    }

    @Test
    @DisplayName("funciona com outro tamanho de mapa (limites min e max do domínio)")
    void outroTamanho() {
        MapaGeometria pequena = MapaGeometria.de(new Mapa(1), 0f, 0f, 10f);

        assertEquals(3, pequena.celulasPorLado());
        assertEquals(20f, pequena.y(-1), DELTA);
        assertEquals(0f, pequena.y(1), DELTA);
        assertEquals(10f, pequena.y(0), DELTA);
    }
}
