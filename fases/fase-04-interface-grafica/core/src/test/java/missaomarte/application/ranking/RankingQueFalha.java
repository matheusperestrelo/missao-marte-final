package missaomarte.application.ranking;

import java.util.ArrayList;
import java.util.List;

/** Ranking cujo salvar e limpar sempre falham com "disco cheio". */
final class RankingQueFalha implements RankingRepository {

    @Override
    public List<RankingEntry> carregar() {
        return new ArrayList<>();
    }

    @Override
    public void salvar(List<RankingEntry> entradas) {
        throw new RankingException("disco cheio");
    }

    @Override
    public void limpar() {
        throw new RankingException("disco cheio");
    }
}
