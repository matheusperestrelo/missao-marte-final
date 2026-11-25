package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Acao;
import missaomarte.application.Dificuldade;
import missaomarte.application.GameSession;
import missaomarte.domain.Mapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Posicao;
import missaomarte.domain.Professor;

class TextoPainelTest {

    private static Missao missao() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));
        missao.adicionarPassageiro(new Professor("Dr. Silva", new Posicao(1, 0)));
        missao.adicionarPassageiro(new Professor("Dr. Lima", new Posicao(3, 3)));
        return missao;
    }

    @Test
    @DisplayName("R4.1 as oito linhas do painel no início da partida")
    void r4_1_estadoInicial() {
        GameSession sessao = Dubles.sessao(missao(), Dificuldade.FACIL);

        assertEquals(List.of(
                "Piloto: Ana",
                "Dificuldade: Fácil",
                "Vidas: 3",
                "Pontos: 30",
                "A bordo: 0/4",
                "Entregues: 0/2",
                "Restantes: 2",
                "Movimentos: 0"), TextoPainel.estado(sessao));
    }

    @Test
    @DisplayName("R4.1 os valores acompanham a sessão depois dos turnos")
    void r4_1_estadoDepoisDeJogar() {
        GameSession sessao = Dubles.sessao(missao(), Dificuldade.FACIL);
        sessao.jogar(Acao.DIREITA);
        sessao.jogar(Acao.EMBARCAR);

        assertEquals(List.of(
                "Piloto: Ana",
                "Dificuldade: Fácil",
                "Vidas: 3",
                "Pontos: 39",
                "A bordo: 1/4",
                "Entregues: 0/2",
                "Restantes: 1",
                "Movimentos: 1"), TextoPainel.estado(sessao));
    }

    @Test
    @DisplayName("R4.1 nome vazio aparece como Piloto Anônimo, como a sessão decide")
    void r4_1_pilotoAnonimo() {
        GameSession sessao = new GameSession(new Dubles.FabricaFixa(missao()), new java.util.Random(1), Dubles.RELOGIO);
        sessao.iniciar("   ", Dificuldade.MEDIO, 5);

        assertEquals("Piloto: Piloto Anônimo", TextoPainel.estado(sessao).get(0));
        assertEquals("Dificuldade: Médio", TextoPainel.estado(sessao).get(1));
    }
}
