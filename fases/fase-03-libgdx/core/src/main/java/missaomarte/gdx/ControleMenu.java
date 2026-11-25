package missaomarte.gdx;

import java.util.Optional;

import missaomarte.application.ranking.RankingException;
import missaomarte.application.ranking.RankingService;

/**
 * Navegação do Menu e confirmação de limpar o ranking, sem libGDX.
 * A tela só converte o evento do teclado em chamada e executa o comando devolvido.
 */
public class ControleMenu {

    public static final String PERGUNTA_LIMPAR = "Limpar o ranking? S confirma, N ou Esc cancela";
    public static final String RANKING_LIMPO = "Ranking limpo.";
    public static final String CANCELADO = "Operação cancelada.";
    public static final String ERRO_AO_LIMPAR = "Não foi possível limpar o ranking: ";

    private enum Estado { NORMAL, CONFIRMANDO_LIMPEZA }

    private final RankingService ranking;
    private Estado estado = Estado.NORMAL;
    private String mensagem;

    public ControleMenu(RankingService ranking) {
        this.ranking = ranking;
    }

    /** Tecla de caractere (letra, número). Devolve o comando que o jogo deve executar. */
    public ComandoMenu caractere(char c) {
        // Qualquer tecla apaga a mensagem anterior e depois conta como tecla normal.
        mensagem = null;
        if (estado == Estado.CONFIRMANDO_LIMPEZA) {
            tratarConfirmacao(c);
            return ComandoMenu.NADA;
        }
        switch (c) {
            case '1':
                return ComandoMenu.NOVA_PARTIDA;
            case '2':
                return ComandoMenu.RANKING;
            case '3':
                estado = Estado.CONFIRMANDO_LIMPEZA;
                return ComandoMenu.NADA;
            case '4':
                return ComandoMenu.SAIR;
            default:
                return ComandoMenu.NADA;
        }
    }

    /** Tecla Esc: cancela a confirmação; fora dela só apaga a mensagem. */
    public ComandoMenu esc() {
        mensagem = null;
        if (estado == Estado.CONFIRMANDO_LIMPEZA) {
            cancelar();
        }
        return ComandoMenu.NADA;
    }

    public boolean confirmando() {
        return estado == Estado.CONFIRMANDO_LIMPEZA;
    }

    public Optional<String> mensagem() {
        return Optional.ofNullable(mensagem);
    }

    private void tratarConfirmacao(char c) {
        char minuscula = Character.toLowerCase(c);
        if (minuscula == 's') {
            estado = Estado.NORMAL;
            try {
                ranking.limpar();
                mensagem = RANKING_LIMPO;
            } catch (RankingException e) {
                mensagem = ERRO_AO_LIMPAR + e.getMessage();
            }
        } else if (minuscula == 'n') {
            cancelar();
        }
        // Qualquer outra tecla é ignorada enquanto a pergunta está aberta.
    }

    private void cancelar() {
        estado = Estado.NORMAL;
        mensagem = CANCELADO;
    }
}
