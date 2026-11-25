package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Dificuldade;
import missaomarte.application.ranking.RankingEntry;
import missaomarte.gdx.Dubles.RankingEmMemoria;
import missaomarte.gdx.Dubles.RankingQueFalha;

class ControleMenuTest {

    private final RankingEmMemoria repo = new RankingEmMemoria();
    private final ControleMenu menu = new ControleMenu(Dubles.servico(repo));

    private void comUmaEntrada() {
        repo.entradas.add(new RankingEntry("Ana", 50, Dificuldade.FACIL, 4, "2026-11-01 10:00:00", 30));
    }

    @Test
    @DisplayName("R1.2 tecla 1 pede Nova partida")
    void r1_2_umAbreNovaPartida() {
        assertEquals(ComandoMenu.NOVA_PARTIDA, menu.caractere('1'));
    }

    @Test
    @DisplayName("R1.3 tecla 2 pede o Ranking")
    void r1_3_doisAbreRanking() {
        assertEquals(ComandoMenu.RANKING, menu.caractere('2'));
    }

    @Test
    @DisplayName("R1.4 tecla 4 pede para sair")
    void r1_4_quatroSai() {
        assertEquals(ComandoMenu.SAIR, menu.caractere('4'));
    }

    @Test
    @DisplayName("R1.5 outras teclas são ignoradas, sem mensagem nem confirmação")
    void r1_5_outrasTeclasIgnoradas() {
        for (char c : new char[] {'a', 'x', '5', '0', ' ', 's', 'n', '\n'}) {
            assertEquals(ComandoMenu.NADA, menu.caractere(c), "tecla '" + (int) c + "'");
        }
        assertFalse(menu.confirmando());
        assertTrue(menu.mensagem().isEmpty());
        assertEquals(ComandoMenu.NADA, menu.esc());
    }

    @Test
    @DisplayName("R2.1 tecla 3 abre a confirmação e ainda não limpa")
    void r2_1_tresPedeConfirmacao() {
        comUmaEntrada();

        assertEquals(ComandoMenu.NADA, menu.caractere('3'));

        assertTrue(menu.confirmando());
        assertEquals(1, repo.entradas.size());
        assertEquals("Limpar o ranking? S confirma, N ou Esc cancela", ControleMenu.PERGUNTA_LIMPAR);
    }

    @Test
    @DisplayName("R2.2 S (maiúsculo ou minúsculo) limpa o ranking e mostra \"Ranking limpo.\"")
    void r2_2_sLimpa() {
        for (char s : new char[] {'s', 'S'}) {
            comUmaEntrada();
            ControleMenu m = new ControleMenu(Dubles.servico(repo));
            m.caractere('3');

            m.caractere(s);

            assertTrue(repo.entradas.isEmpty());
            assertFalse(m.confirmando());
            assertEquals(Optional.of("Ranking limpo."), m.mensagem());
        }
    }

    @Test
    @DisplayName("R2.3 N e Esc cancelam, mostram \"Operação cancelada.\" e não limpam")
    void r2_3_nEEscCancelam() {
        comUmaEntrada();
        menu.caractere('3');
        menu.caractere('N');
        assertEquals(Optional.of("Operação cancelada."), menu.mensagem());
        assertFalse(menu.confirmando());

        menu.caractere('3');
        menu.esc();
        assertEquals(Optional.of("Operação cancelada."), menu.mensagem());
        assertFalse(menu.confirmando());

        assertEquals(1, repo.entradas.size());
    }

    @Test
    @DisplayName("R2.4 com a confirmação aberta as outras teclas, inclusive 1, 2 e 4, são ignoradas")
    void r2_4_teclasIgnoradasNaConfirmacao() {
        menu.caractere('3');

        for (char c : new char[] {'1', '2', '4', 'x', '3'}) {
            assertEquals(ComandoMenu.NADA, menu.caractere(c));
            assertTrue(menu.confirmando(), "continua perguntando depois de '" + c + "'");
        }
    }

    @Test
    @DisplayName("R2.5 falha ao limpar mostra a mensagem do erro e o menu continua")
    void r2_5_falhaAoLimpar() {
        ControleMenu comFalha = new ControleMenu(Dubles.servico(new RankingQueFalha()));
        comFalha.caractere('3');

        assertEquals(ComandoMenu.NADA, comFalha.caractere('s'));

        assertEquals(Optional.of("Não foi possível limpar o ranking: disco cheio"), comFalha.mensagem());
        assertFalse(comFalha.confirmando());
    }

    @Test
    @DisplayName("R2.6 qualquer tecla depois da mensagem a apaga e vale como opção normal")
    void r2_6_teclaDepoisDaMensagem() {
        menu.caractere('3');
        menu.caractere('s');
        assertTrue(menu.mensagem().isPresent());

        assertEquals(ComandoMenu.NOVA_PARTIDA, menu.caractere('1'));
        assertTrue(menu.mensagem().isEmpty());

        // Esc e uma tecla qualquer também apagam a mensagem.
        menu.caractere('3');
        menu.caractere('n');
        assertTrue(menu.mensagem().isPresent());
        menu.esc();
        assertTrue(menu.mensagem().isEmpty());

        menu.caractere('3');
        menu.caractere('n');
        menu.caractere('x');
        assertTrue(menu.mensagem().isEmpty());
    }

    @Test
    @DisplayName("R2.6 depois da mensagem, a tecla 3 abre a confirmação de novo")
    void r2_6_tresDepoisDaMensagem() {
        menu.caractere('3');
        menu.caractere('n');

        menu.caractere('3');

        assertTrue(menu.confirmando());
        assertTrue(menu.mensagem().isEmpty());
    }
}
