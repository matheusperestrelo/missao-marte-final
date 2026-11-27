package missaomarte.servidor.api;

import jakarta.validation.Valid;
import java.net.URI;
import missaomarte.servidor.api.dto.NovaPartidaRequest;
import missaomarte.servidor.api.dto.PartidaRegistradaResponse;
import missaomarte.servidor.application.NovaPartida;
import missaomarte.servidor.application.PartidaRegistrada;
import missaomarte.servidor.application.PartidaService;
import missaomarte.servidor.application.UsuarioAutenticado;
import missaomarte.servidor.domain.Partida;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartidaController {

    private final PartidaService servico;

    public PartidaController(PartidaService servico) {
        this.servico = servico;
    }

    @PostMapping("/api/partidas")
    public ResponseEntity<PartidaRegistradaResponse> registrar(UsuarioAutenticado usuario,
            @Valid @RequestBody NovaPartidaRequest pedido) {
        PartidaRegistrada registrada = servico.registrar(usuario.id(), new NovaPartida(
                pedido.dificuldade(), pedido.resultado(), pedido.pontuacao(),
                pedido.duracaoSegundos(), pedido.movimentos()));
        Partida p = registrada.partida();
        return ResponseEntity.created(URI.create("/api/partidas/" + p.getId()))
                .body(new PartidaRegistradaResponse(p.getId(), p.getDificuldade(), p.getResultado(),
                        p.getPontuacao(), p.getDuracaoSegundos(), p.getMovimentos(),
                        p.getDataHora(), registrada.posicaoRanking()));
    }
}
