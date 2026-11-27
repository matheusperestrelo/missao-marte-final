package missaomarte.gdx.catalogo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ImagemTest {

    @Test
    void test_R5_2_arroba_devolve_nave() {
        assertEquals(Imagem.NAVE, Imagem.doSimbolo('@'));
    }

    @Test
    void test_R5_2_p_devolve_professor() {
        assertEquals(Imagem.PROFESSOR, Imagem.doSimbolo('P'));
    }

    @Test
    void test_R5_2_e_devolve_engenheiro() {
        assertEquals(Imagem.ENGENHEIRO, Imagem.doSimbolo('E'));
    }

    @Test
    void test_R5_2_t_devolve_astronauta() {
        assertEquals(Imagem.ASTRONAUTA, Imagem.doSimbolo('T'));
    }

    @Test
    void test_R5_2_cerquilha_devolve_asteroide() {
        assertEquals(Imagem.ASTEROIDE, Imagem.doSimbolo('#'));
    }

    @Test
    void test_R5_2_x_devolve_inimigo() {
        assertEquals(Imagem.INIMIGO, Imagem.doSimbolo('X'));
    }

    @Test
    void test_R5_2_l_devolve_base() {
        assertEquals(Imagem.BASE, Imagem.doSimbolo('L'));
    }

    @Test
    void test_R5_3_simbolo_desconhecido_devolve_generica() {
        assertEquals(Imagem.GENERICA, Imagem.doSimbolo('?'));
        assertEquals(Imagem.GENERICA, Imagem.doSimbolo(' '));
        assertEquals(Imagem.GENERICA, Imagem.doSimbolo('Z'));
    }
}
