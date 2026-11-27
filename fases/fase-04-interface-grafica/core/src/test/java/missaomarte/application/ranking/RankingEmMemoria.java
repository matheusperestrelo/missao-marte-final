package missaomarte.application.ranking;

import java.util.ArrayList;
import java.util.List;

/** Ranking em memória para os testes; conta as gravações. */
final class RankingEmMemoria implements RankingRepository {

    final List<RankingEntry> entradas = new ArrayList<>();
    int salvamentos;

    @Override
    public List<RankingEntry> carregar() {
        return new ArrayList<>(entradas);
    }

    @Override
    public void salvar(List<RankingEntry> novas) {
        salvamentos++;
        entradas.clear();
        entradas.addAll(novas);
    }

    @Override
    public void limpar() {
        entradas.clear();
    }
}
