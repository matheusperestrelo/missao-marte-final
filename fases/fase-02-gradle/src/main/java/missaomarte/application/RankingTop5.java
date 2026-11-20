package missaomarte.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Regra do Top 5: só vitórias com pontuação positiva entram, ordenadas por pontos. */
public class RankingTop5 {
    private static final int MAXIMO = 5;
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RankingRepository repositorio;
    private final Clock clock;

    public RankingTop5(RankingRepository repositorio, Clock clock) {
        this.repositorio = repositorio;
        this.clock = clock;
    }

    public List<RankingEntry> listar() {
        return repositorio.carregar();
    }

    /** Registra a partida; devolve true se a entrada entrou no ranking. */
    public boolean registrar(ResumoPartida resumo) {
        if (!resumo.vitoria() || resumo.pontos() <= 0) return false;
        List<RankingEntry> lista = new ArrayList<>(repositorio.carregar());
        if (lista.size() >= MAXIMO && resumo.pontos() <= lista.get(MAXIMO - 1).pontuacao()) {
            return false;
        }
        lista.add(new RankingEntry(resumo.piloto(), resumo.pontos(), resumo.dificuldade(),
                resumo.entregues(), LocalDateTime.now(clock).format(FORMATO),
                resumo.duracaoSegundos()));
        lista.sort(Comparator.comparingInt(RankingEntry::pontuacao).reversed());
        List<RankingEntry> top = new ArrayList<>(lista.subList(0, Math.min(MAXIMO, lista.size())));
        repositorio.salvar(top);
        return true;
    }

    public Optional<RankingEntry> recorde() {
        List<RankingEntry> lista = repositorio.carregar();
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    public boolean seriaNovoRecorde(int pontos) {
        int atual = recorde().map(RankingEntry::pontuacao).orElse(0);
        return atual > 0 && pontos > atual;
    }

    public void limpar() {
        repositorio.limpar();
    }
}
