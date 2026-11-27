package missaomarte.gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import missaomarte.application.Acao;
import missaomarte.application.GameSession;
import missaomarte.application.ResultadoPartida;
import missaomarte.application.ResultadoTurno;
import missaomarte.application.ResumoPartida;
import missaomarte.application.ranking.RankingException;
import missaomarte.application.ranking.RankingService;

/**
 * Controle da tela da Partida, sem libGDX. A regra do jogo continua na
 * {@link GameSession} e a do ranking no {@link RankingService}; aqui ficam só
 * as mensagens do último turno, o registro único da vitória e o destino do Enter.
 */
public class ControlePartida {

    public static final String CONTINUAR = "Pressione Enter para continuar";
    public static final String ERRO_AO_SALVAR = "Não foi possível salvar o ranking: ";

    private final GameSession sessao;
    private final RankingService ranking;

    private List<String> mensagens = List.of();
    private boolean registrou;
    private boolean entrou;
    private String erroRanking;

    public ControlePartida(GameSession sessao, RankingService ranking) {
        this.sessao = sessao;
        this.ranking = ranking;
    }

    /**
     * Executa um turno para a ação. Ação vazia (tecla desconhecida) e partida já
     * encerrada não fazem nada: nenhum turno é gasto e nada é registrado.
     */
    public void tecla(Optional<Acao> acao) {
        if (acao.isEmpty() || terminada() || abandonada()) {
            return;
        }
        ResultadoTurno turno = sessao.jogar(acao.get());
        if (abandonada()) {
            return;
        }
        mensagens = MensagensPainel.traduzir(turno);
        if (sessao.getResultado() == ResultadoPartida.VITORIA && !registrou) {
            registrarVitoria();
        }
    }

    /** Enter: depois da vitória vai ao Ranking, depois da derrota ao Menu; antes do fim não faz nada. */
    public DestinoFim enter() {
        switch (sessao.getResultado()) {
            case VITORIA:
                return DestinoFim.RANKING;
            case DERROTA:
                return DestinoFim.MENU;
            default:
                return DestinoFim.NENHUM;
        }
    }

    public boolean abandonada() {
        return sessao.getResultado() == ResultadoPartida.ABORTADA;
    }

    /** Vitória ou derrota (o abandono sai direto da tela e não conta como fim). */
    public boolean terminada() {
        ResultadoPartida resultado = sessao.getResultado();
        return resultado == ResultadoPartida.VITORIA || resultado == ResultadoPartida.DERROTA;
    }

    /** Linhas de estado do painel (piloto, vidas, pontos...). */
    public List<String> estado() {
        return TextoPainel.estado(sessao);
    }

    /** Mensagens do último turno, mais o erro de gravação do ranking, se houve. */
    public List<String> mensagens() {
        List<String> todas = new ArrayList<>(mensagens);
        if (erroRanking != null) {
            todas.add(erroRanking);
        }
        return todas;
    }

    public Optional<String> alerta() {
        return MensagensPainel.alerta(sessao.getMissao(), sessao.isEncerrada());
    }

    public Optional<String> linhaFim() {
        return terminada() ? Optional.of(CONTINUAR) : Optional.empty();
    }

    public boolean entrouNoRanking() {
        return entrou;
    }

    public ResumoPartida resumo() {
        return sessao.resumo();
    }

    public GameSession sessao() {
        return sessao;
    }

    private void registrarVitoria() {
        registrou = true;
        try {
            entrou = ranking.registrar(sessao.resumo());
        } catch (RankingException e) {
            entrou = false;
            erroRanking = ERRO_AO_SALVAR + e.getMessage();
        }
    }
}
