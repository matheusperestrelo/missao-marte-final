package missaomarte.gdx;

import java.util.List;

import missaomarte.application.Dificuldade;

/**
 * Formulário da tela Nova partida, sem libGDX: primeiro o nome do piloto
 * (até 20 caracteres) e depois a dificuldade, escolhida com as setas.
 * O nome vazio vira "Piloto Anônimo" na {@code GameSession}, não aqui.
 */
public class FormularioNovaPartida {

    public static final int MAX_NOME = 20;

    public enum Etapa { NOME, DIFICULDADE }

    private static final List<Dificuldade> OPCOES = List.of(Dificuldade.values());

    private final StringBuilder nome = new StringBuilder();
    private Etapa etapa = Etapa.NOME;
    private int selecionada = 0; // começa em FACIL

    /** Caractere digitado; só vale na etapa do nome, e ignora controle e o que passar de 20. */
    public void caractere(char c) {
        if (etapa != Etapa.NOME || nome.length() >= MAX_NOME) {
            return;
        }
        if (Character.isISOControl(c) || Character.isSurrogate(c)) {
            return;
        }
        nome.append(c);
    }

    /** Backspace: apaga o último caractere do nome, se houver. */
    public void apagar() {
        if (etapa == Etapa.NOME && nome.length() > 0) {
            nome.setLength(nome.length() - 1);
        }
    }

    public void cima() {
        if (etapa == Etapa.DIFICULDADE && selecionada > 0) {
            selecionada--;
        }
    }

    public void baixo() {
        if (etapa == Etapa.DIFICULDADE && selecionada < OPCOES.size() - 1) {
            selecionada++;
        }
    }

    public ResultadoFormulario enter() {
        if (etapa == Etapa.NOME) {
            etapa = Etapa.DIFICULDADE;
            return ResultadoFormulario.CONTINUA;
        }
        return ResultadoFormulario.INICIAR;
    }

    public ResultadoFormulario esc() {
        if (etapa == Etapa.DIFICULDADE) {
            etapa = Etapa.NOME;
            return ResultadoFormulario.CONTINUA;
        }
        return ResultadoFormulario.VOLTAR_MENU;
    }

    public Etapa etapa() {
        return etapa;
    }

    public String nome() {
        return nome.toString();
    }

    public List<Dificuldade> opcoes() {
        return OPCOES;
    }

    public Dificuldade selecionada() {
        return OPCOES.get(selecionada);
    }
}
