package missaomarte.application.ranking;

/** Falha ao salvar ou limpar o ranking, lançada pela implementação do repositório. */
public class RankingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RankingException(String mensagem) {
        super(mensagem);
    }

    public RankingException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
