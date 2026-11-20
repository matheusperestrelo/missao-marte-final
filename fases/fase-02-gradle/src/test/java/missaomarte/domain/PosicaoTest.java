package missaomarte.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PosicaoTest {

    @Test
    @DisplayName("R2.1 mover devolve nova posição e mantém a original")
    void r2_1_moverDevolveNovaInstanciaEMantemAOriginal() {
        Posicao original = new Posicao(2, 3);

        Posicao movida = original.mover(Direcao.DIREITA);

        assertEquals(new Posicao(3, 3), movida);
        assertEquals(new Posicao(2, 3), original);
        assertEquals(new Posicao(2, 2), original.mover(Direcao.CIMA));
        assertEquals(new Posicao(2, 4), original.mover(Direcao.BAIXO));
        assertEquals(new Posicao(1, 3), original.mover(Direcao.ESQUERDA));
    }

    @Test
    @DisplayName("R2.2 igualdade e hashCode pelas coordenadas")
    void r2_2_igualdadeEHashCodePelasCoordenadas() {
        assertEquals(new Posicao(1, -1), new Posicao(1, -1));
        assertEquals(new Posicao(1, -1).hashCode(), new Posicao(1, -1).hashCode());
        assertNotEquals(new Posicao(1, -1), new Posicao(-1, 1));
        assertEquals(new Posicao(0, 0), Posicao.ORIGEM);
    }
}
