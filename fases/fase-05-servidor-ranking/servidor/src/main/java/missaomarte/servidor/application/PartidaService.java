package missaomarte.servidor.application;

import java.time.Clock;
import java.time.Instant;
import missaomarte.servidor.domain.LinhaRanking;
import missaomarte.servidor.domain.Partida;
import missaomarte.servidor.domain.PartidaRepository;
import missaomarte.servidor.domain.RankingGlobal;
import missaomarte.servidor.domain.Resultado;
import missaomarte.servidor.domain.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return new PartidaRegistrada(salva, posicaoNoRanking(salva));
    }

    private Integer posicaoNoRanking(Partida salva) {
        if (salva.getResultado() != Resultado.VITORIA || salva.getPontuacao() <= 0) {
            return null;
        }
        return RankingGlobal.calcular(
                        partidas.findByResultadoAndPontuacaoGreaterThan(Resultado.VITORIA, 0))
                .stream()
                .filter(linha -> linha.partida().getId().equals(salva.getId()))
                .map(LinhaRanking::posicao)
                .findFirst()
                .orElse(null);
    }

    public Page<Partida> historico(long usuarioId, int pagina, int tamanho) {
        Sort ordem = Sort.by(Sort.Direction.DESC, "dataHora").and(Sort.by(Sort.Direction.DESC, "id"));
        return partidas.findByUsuarioId(usuarioId, PageRequest.of(pagina, tamanho, ordem));
    }
}
