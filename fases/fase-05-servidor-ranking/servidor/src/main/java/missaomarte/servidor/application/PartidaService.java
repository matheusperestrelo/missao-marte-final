package missaomarte.servidor.application;

import java.time.Clock;
import java.time.Instant;
import missaomarte.servidor.domain.Partida;
import missaomarte.servidor.domain.PartidaRepository;
import missaomarte.servidor.domain.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class PartidaService {

    private final PartidaRepository partidas;
    private final UsuarioRepository usuarios;
    private final Clock clock;

    public PartidaService(PartidaRepository partidas, UsuarioRepository usuarios, Clock clock) {
        this.partidas = partidas;
        this.usuarios = usuarios;
        this.clock = clock;
    }

    public PartidaRegistrada registrar(long usuarioId, NovaPartida dados) {
        Partida salva = partidas.save(new Partida(usuarios.getReferenceById(usuarioId),
                dados.dificuldade(), dados.resultado(), dados.pontuacao(), dados.duracaoSegundos(),
                dados.movimentos(), Instant.now(clock)));
        return new PartidaRegistrada(salva, null);
    }
}
