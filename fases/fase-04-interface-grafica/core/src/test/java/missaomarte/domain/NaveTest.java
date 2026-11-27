package missaomarte.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NaveTest {

    @Test
    @DisplayName("R2.4 a nave fica parada ao tentar sair do mapa")
    void r2_4_naveFicaParadaNaBorda() {
        Mapa mapa = new Mapa(1);
        Nave nave = new Nave(2);

        nave.mover(Direcao.DIREITA, mapa);
        nave.mover(Direcao.DIREITA, mapa);

        assertEquals(new Posicao(1, 0), nave.posicao());
    }

    @Test
    @DisplayName("R2.5 embarca até a capacidade e recusa com a nave cheia")
    void r2_5_embarcaAteACapacidadeERecusaCheia() {
        Nave nave = new Nave(2);
        Passageiro a = new Professor("A", new Posicao(1, 0));
        Passageiro b = new Engenheiro("B", new Posicao(2, 0));
        Passageiro c = new Astronauta("C", new Posicao(3, 0));

        assertTrue(nave.embarcar(a));
        assertTrue(nave.embarcar(b));
        assertTrue(nave.estaCheia());
        assertFalse(nave.embarcar(c));
        assertEquals(List.of(a, b), nave.getABordo());
    }

    @Test
    @DisplayName("R2.6 desembarcar todos devolve os passageiros e libera a capacidade")
    void r2_6_desembarcarTodosLiberaCapacidade() {
        Nave nave = new Nave(1);
        Passageiro a = new Professor("A", new Posicao(1, 0));
        nave.embarcar(a);

        List<Passageiro> desembarcados = nave.desembarcarTodos();

        assertEquals(List.of(a), desembarcados);
        assertTrue(nave.getABordo().isEmpty());
        assertFalse(nave.estaCheia());
    }

    @Test
    @DisplayName("perder vida não passa de zero")
    void perderVidaNaoFicaNegativa() {
        Nave nave = new Nave(1);
        for (int i = 0; i < 5; i++) {
            nave.perderVida();
        }
        assertEquals(0, nave.getVidas());
    }
}
