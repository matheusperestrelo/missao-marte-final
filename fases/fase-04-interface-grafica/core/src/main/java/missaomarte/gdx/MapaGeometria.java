package missaomarte.gdx;

import missaomarte.domain.Mapa;

/**
 * Converte a célula do domínio em pixels. É aqui que fica a inversão do eixo Y:
 * no domínio {@code y} cresce para baixo; no libGDX, para cima. A menor linha
 * ({@code y} mínimo) vai para o topo e a menor coluna ({@code x} mínimo) para a
 * esquerda. As posições devolvidas são do canto inferior esquerdo da célula.
 */
public class MapaGeometria {

    private final int min;
    private final int max;
    private final float origemX;
    private final float origemY;
    private final float lado;

    public MapaGeometria(int min, int max, float origemX, float origemY, float lado) {
        this.min = min;
        this.max = max;
        this.origemX = origemX;
        this.origemY = origemY;
        this.lado = lado;
    }

    /** Geometria de um mapa; {@code origemX/Y} é o canto inferior esquerdo da grade. */
    public static MapaGeometria de(Mapa mapa, float origemX, float origemY, float lado) {
        return new MapaGeometria(mapa.getMin(), mapa.getMax(), origemX, origemY, lado);
    }

    public float x(int celulaX) {
        return origemX + (celulaX - min) * lado;
    }

    public float y(int celulaY) {
        return origemY + (max - celulaY) * lado;
    }

    public float lado() {
        return lado;
    }

    public float origemX() {
        return origemX;
    }

    public float origemY() {
        return origemY;
    }

    public int celulasPorLado() {
        return max - min + 1;
    }

    public float larguraGrade() {
        return celulasPorLado() * lado;
    }

    public float alturaGrade() {
        return celulasPorLado() * lado;
    }
}
