package missaomarte.servidor.application;

import java.util.List;
import missaomarte.servidor.domain.LinhaRanking;
import missaomarte.servidor.domain.PartidaRepository;
import missaomarte.servidor.domain.RankingGlobal;
import missaomarte.servidor.domain.Resultado;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

    private final PartidaRepository partidas;

    public RankingService(PartidaRepository partidas) {
        this.partidas = partidas;
    }

    public List<LinhaRanking> top10() {
        return RankingGlobal.calcular(
                partidas.findByResultadoAndPontuacaoGreaterThan(Resultado.VITORIA, 0));
    }
}
