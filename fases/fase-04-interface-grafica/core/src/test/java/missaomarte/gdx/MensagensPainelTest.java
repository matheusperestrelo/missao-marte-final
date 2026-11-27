package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Dificuldade;
import missaomarte.application.EventoJogo;
import missaomarte.application.ResultadoTurno;
import missaomarte.application.TipoEvento;
import missaomarte.domain.Mapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Posicao;
import missaomarte.domain.Professor;

class MensagensPainelTest {

    private static List<String> traduzir(EventoJogo... eventos) {
        return MensagensPainel.traduzir(new ResultadoTurno(List.of(eventos), false));
    }

    @Test
    @DisplayName("R4.2 movimento sem outro evento não gera mensagem")
    void r4_2_movimento() {
        assertTrue(traduzir(new EventoJogo(TipoEvento.MOVIMENTO, null, 0)).isEmpty());
    }

    @Test
    @DisplayName("R4.2 embarque com nome e pontos")
    void r4_2_embarque() {
        assertEquals(List.of("Passageiro Dr. Silva embarcado com sucesso! +10 pontos!"),
                traduzir(new EventoJogo(TipoEvento.EMBARQUE, "Dr. Silva", 10)));
    }

    @Test
    @DisplayName("R4.2 embarque recusado: sem passageiro e nave cheia")
    void r4_2_embarqueRecusado() {
        assertEquals(List.of("Nenhum passageiro nesta posição."),
                traduzir(new EventoJogo(TipoEvento.EMBARQUE_RECUSADO_SEM_PASSAGEIRO, null, 0)));
        assertEquals(List.of("Nave cheia! Não há espaço para mais passageiros."),
                traduzir(new EventoJogo(TipoEvento.EMBARQUE_RECUSADO_NAVE_CHEIA, null, 0)));
    }

    @Test
    @DisplayName("R4.2 entrega na base")
    void r4_2_entrega() {
        assertEquals(List.of("2 passageiro(s) entregue(s) na base!"),
                traduzir(new EventoJogo(TipoEvento.ENTREGA, null, 2)));
    }

    @Test
    @DisplayName("R4.2 colisão mostra as vidas restantes, só com vidas > 0")
    void r4_2_colisao() {
        assertEquals(List.of("Alerta! Colisão detectada! Vidas restantes: 2"),
                traduzir(new EventoJogo(TipoEvento.COLISAO, null, 2)));
        assertTrue(traduzir(new EventoJogo(TipoEvento.COLISAO, null, 0)).isEmpty(),
                "com 0 vidas só aparece o GAME OVER");
    }

    @Test
    @DisplayName("R4.2 derrotas")
    void r4_2_derrotas() {
        assertEquals(List.of("GAME OVER! A nave foi destruída."),
                traduzir(new EventoJogo(TipoEvento.DERROTA_NAVE_DESTRUIDA, null, 0)));
        assertEquals(List.of("Combustível/Pontuação zerada! Missão perdida."),
                traduzir(new EventoJogo(TipoEvento.DERROTA_PONTUACAO_ZERADA, null, 0)));
    }

    @Test
    @DisplayName("R4.2 vitória: duas frases, sem emoji e sem separador")
    void r4_2_vitoria() {
        List<String> frases = traduzir(new EventoJogo(TipoEvento.VITORIA, null, 0));

        assertEquals(List.of(
                "DECOLAGEM AUTORIZADA! Nave acoplada à plataforma em (0,0).",
                "Retornando à órbita marciana com todos os passageiros. Missão cumprida!"), frases);
    }

    @Test
    @DisplayName("R4.2 as mensagens seguem a ordem dos eventos e nenhuma tem emoji, separador ou quebra de linha")
    void r4_2_ordemELimpeza() {
        List<String> frases = traduzir(
                new EventoJogo(TipoEvento.MOVIMENTO, null, 0),
                new EventoJogo(TipoEvento.ENTREGA, null, 1),
                new EventoJogo(TipoEvento.COLISAO, null, 1),
                new EventoJogo(TipoEvento.VITORIA, null, 0));

        assertEquals(4, frases.size());
        assertTrue(frases.get(0).startsWith("1 passageiro(s)"));
        assertTrue(frases.get(1).startsWith("Alerta!"));
        for (String frase : frases) {
            assertTrue(frase.chars().allMatch(c -> c < 0x2000), "sem emoji: " + frase);
            assertTrue(!frase.contains("====") && !frase.contains("\n"), frase);
        }
    }

    @Test
    @DisplayName("R4.4 alerta quando não há passageiros no mapa, há a bordo e a partida continua")
    void r4_4_alerta() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));
        Professor professor = new Professor("Dr. Silva", new Posicao(1, 0));
        missao.adicionarPassageiro(professor);

        assertEquals(Optional.empty(), MensagensPainel.alerta(missao, false), "ainda há passageiro no mapa");

        missao.embarcar(professor);

        assertEquals(Optional.of("ALERTA: Todos os passageiros resgatados! Retorne para a Plataforma de Pouso"
                + " 'L' em (0,0) para completar a missão."), MensagensPainel.alerta(missao, false));
        assertEquals(Optional.empty(), MensagensPainel.alerta(missao, true), "partida encerrada");
    }

    @Test
    @DisplayName("R4.4 sem ninguém a bordo não há alerta")
    void r4_4_semNinguemABordo() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));

        assertEquals(Optional.empty(), MensagensPainel.alerta(missao, false));
    }

    @Test
    @DisplayName("Dificuldade usada nos testes existe (sanidade)")
    void sanidade() {
        assertEquals("Fácil", Dificuldade.FACIL.toString());
    }
}
