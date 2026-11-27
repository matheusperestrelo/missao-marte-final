package missaomarte.application.ranking;

import java.util.List;

/** Porta de persistência do ranking; implementada fora da application. */
public interface RankingRepository {
    List<RankingEntry> carregar();

    void salvar(List<RankingEntry> entradas);

    void limpar();
}
