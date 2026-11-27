package missaomarte.application.ranking;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import missaomarte.application.ResumoPartida;

/**
 * Regra do Top 10: só vitórias com pontuação positiva entram, em ordem
 * decrescente de pontos. Com a lista cheia, só entra quem passar da 10ª.
 * As telas consultam, registram e limpam o ranking só por aqui.
 */
public class RankingService {

    public static final int MAXIMO = 10;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RankingRepository repositorio;
    private final Clock clock;

    public RankingService(RankingRepository repositorio, Clock clock) {
        this.repositorio = repositorio;
        this.clock = clock;
    }

    /** As melhores entradas, da maior para a menor pontuação, no máximo {@value #MAXIMO}. */
    public List<RankingEntry> top10() {
        return limitar(ordenar(repositorio.carregar()));
    }

    /**
     * Registra a partida e devolve se ela entrou no Top 10. Derrota, abandono
     * e pontuação menor ou igual a zero não entram e não gravam nada.
     *
     * @throws RankingException se o repositório falhar ao gravar
     */
    public boolean registrar(ResumoPartida resumo) {
        if (!resumo.vitoria() || resumo.pontos() <= 0) {
            return false;
        }
        List<RankingEntry> atuais = top10();
        if (atuais.size() >= MAXIMO && resumo.pontos() <= atuais.get(MAXIMO - 1).pontuacao()) {
            return false;
        }
        List<RankingEntry> novas = new ArrayList<>(atuais);
        novas.add(new RankingEntry(resumo.piloto(), resumo.pontos(), resumo.dificuldade(),
                resumo.entregues(), LocalDateTime.now(clock).format(FORMATO),
                resumo.duracaoSegundos()));
        repositorio.salvar(limitar(ordenar(novas)));
        return true;
    }

    /** A maior pontuação gravada, ou nada se o ranking estiver vazio. */
    public Optional<RankingEntry> recorde() {
        List<RankingEntry> lista = top10();
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    /** @throws RankingException se o repositório falhar ao limpar */
    public void limpar() {
        repositorio.limpar();
    }

    // Ordenação estável: em caso de empate, as entradas antigas ficam na frente.
    private static List<RankingEntry> ordenar(List<RankingEntry> entradas) {
        List<RankingEntry> copia = new ArrayList<>(entradas);
        copia.sort(Comparator.comparingInt(RankingEntry::pontuacao).reversed());
        return copia;
    }

    private static List<RankingEntry> limitar(List<RankingEntry> ordenadas) {
        return new ArrayList<>(ordenadas.subList(0, Math.min(MAXIMO, ordenadas.size())));
    }
}
