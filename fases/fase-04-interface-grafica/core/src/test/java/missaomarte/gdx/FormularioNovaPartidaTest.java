package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Dificuldade;
import missaomarte.gdx.FormularioNovaPartida.Etapa;

class FormularioNovaPartidaTest {

    private final FormularioNovaPartida form = new FormularioNovaPartida();

    private void digitar(String texto) {
        for (char c : texto.toCharArray()) {
            form.caractere(c);
        }
    }

    @Test
    @DisplayName("R3.1 começa vazio, na etapa do nome")
    void r3_1_inicio() {
        assertEquals("", form.nome());
        assertEquals(Etapa.NOME, form.etapa());
    }

    @Test
    @DisplayName("R3.2 aceita até 20 caracteres, com acentos e números, e ignora o resto")
    void r3_2_limiteDeVinte() {
        digitar("Ação 01 de teste Marte-Unifor extra");

        assertEquals(20, form.nome().length());
        assertEquals("Ação 01 de teste Mar", form.nome());
    }

    @Test
    @DisplayName("R3.2 ignora os caracteres de controle que o LWJGL3 emite (\\b, \\t, DEL, \\n) e \\r")
    void r3_2_ignoraControle() {
        digitar("A\b\t\u007f\n\rB");

        assertEquals("AB", form.nome());
    }

    @Test
    @DisplayName("R3.3 backspace apaga o último caractere; sem nome, não faz nada")
    void r3_3_backspace() {
        form.apagar();
        assertEquals("", form.nome());

        digitar("Ana");
        form.apagar();

        assertEquals("An", form.nome());
    }

    @Test
    @DisplayName("R3.3 apagar com 20 caracteres libera espaço para digitar de novo")
    void r3_3_apagarLiberaEspaco() {
        digitar("12345678901234567890");
        form.apagar();
        form.caractere('X');

        assertEquals("1234567890123456789X", form.nome());
    }

    @Test
    @DisplayName("R3.4 Enter no nome passa para a dificuldade, mesmo com nome vazio")
    void r3_4_enterNoNome() {
        assertEquals(ResultadoFormulario.CONTINUA, form.enter());

        assertEquals(Etapa.DIFICULDADE, form.etapa());
        assertEquals("", form.nome());
    }

    @Test
    @DisplayName("R3.5 Esc no nome volta ao Menu")
    void r3_5_escNoNome() {
        assertEquals(ResultadoFormulario.VOLTAR_MENU, form.esc());
    }

    @Test
    @DisplayName("R4.1 lista Fácil, Médio, Difícil, nessa ordem, com Fácil selecionado")
    void r4_1_listaEPadrao() {
        assertEquals(List.of(Dificuldade.FACIL, Dificuldade.MEDIO, Dificuldade.DIFICIL), form.opcoes());
        assertEquals(Dificuldade.FACIL, form.selecionada());
        form.enter();
        assertEquals(Dificuldade.FACIL, form.selecionada());
    }

    @Test
    @DisplayName("R4.2 seta para baixo avança e para na última (sem dar a volta)")
    void r4_2_baixoComBorda() {
        form.enter();

        form.baixo();
        assertEquals(Dificuldade.MEDIO, form.selecionada());
        form.baixo();
        assertEquals(Dificuldade.DIFICIL, form.selecionada());
        form.baixo();
        assertEquals(Dificuldade.DIFICIL, form.selecionada());
    }

    @Test
    @DisplayName("R4.3 seta para cima volta e para na primeira (sem dar a volta)")
    void r4_3_cimaComBorda() {
        form.enter();
        form.cima();
        assertEquals(Dificuldade.FACIL, form.selecionada());

        form.baixo();
        form.baixo();
        form.cima();
        assertEquals(Dificuldade.MEDIO, form.selecionada());
    }

    @Test
    @DisplayName("R4.4 Esc na dificuldade volta ao nome e mantém o que foi digitado")
    void r4_4_escNaDificuldade() {
        digitar("Ana");
        form.enter();
        form.baixo();

        assertEquals(ResultadoFormulario.CONTINUA, form.esc());

        assertEquals(Etapa.NOME, form.etapa());
        assertEquals("Ana", form.nome());
        // Voltando para a dificuldade, a escolha anterior continua.
        form.enter();
        assertEquals(Dificuldade.MEDIO, form.selecionada());
    }

    @Test
    @DisplayName("R4.5 Enter na dificuldade manda iniciar com o nome e a dificuldade escolhidos")
    void r4_5_enterInicia() {
        digitar("Ana");
        form.enter();
        form.baixo();
        form.baixo();

        assertEquals(ResultadoFormulario.INICIAR, form.enter());

        assertEquals("Ana", form.nome());
        assertEquals(Dificuldade.DIFICIL, form.selecionada());
    }

    @Test
    @DisplayName("R4.7 na dificuldade, letras, números e backspace não mudam o nome")
    void r4_7_teclasDeTextoIgnoradasNaDificuldade() {
        digitar("Ana");
        form.enter();

        digitar("xyz123");
        form.apagar();

        assertEquals("Ana", form.nome());
    }

    @Test
    @DisplayName("setas no nome não mudam a dificuldade")
    void setasNoNomeNaoMudamADificuldade() {
        form.baixo();
        form.baixo();

        assertEquals(Dificuldade.FACIL, form.selecionada());
    }
}
