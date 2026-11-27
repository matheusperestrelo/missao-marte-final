package missaomarte.gdx;

import com.badlogic.gdx.graphics.Color;

import missaomarte.domain.TipoElemento;

/** Cor de cada tipo de elemento do mapa (apresentação; a regra não depende disso). */
public final class CoresElemento {

    private CoresElemento() {
    }

    public static Color de(TipoElemento tipo) {
        switch (tipo) {
            case NAVE:
                return new Color(0.25f, 0.90f, 0.90f, 1f);
            case PROFESSOR:
                return new Color(0.45f, 0.85f, 0.40f, 1f);
            case ENGENHEIRO:
                return new Color(1.00f, 0.65f, 0.20f, 1f);
            case ASTRONAUTA:
                return new Color(1.00f, 0.90f, 0.30f, 1f);
            case ASTEROIDE:
                return new Color(0.65f, 0.65f, 0.70f, 1f);
            case INIMIGO:
                return new Color(0.95f, 0.35f, 0.35f, 1f);
            case BASE:
            default:
                return new Color(0.45f, 0.60f, 1.00f, 1f);
        }
    }
}
