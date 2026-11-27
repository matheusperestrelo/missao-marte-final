package missaomarte.servidor.api;

import java.util.List;
import missaomarte.servidor.api.dto.LinhaRankingResponse;
import missaomarte.servidor.application.RankingService;
import missaomarte.servidor.application.UsuarioAutenticado;
import missaomarte.servidor.domain.Partida;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    private final RankingService servico;

    public RankingController(RankingService servico) {
        this.servico = servico;
    }

    @GetMapping("/api/ranking")
    public List<LinhaRankingResponse> ranking(UsuarioAutenticado usuario) {
        return servico.top10().stream().map(linha -> {
            Partida p = linha.partida();
            return new LinhaRankingResponse(linha.posicao(), p.getUsuario().getNome(),
                    p.getPontuacao(), p.getDificuldade(), p.getDuracaoSegundos(), p.getMovimentos(),
                    p.getDataHora());
        }).toList();
    }
}
