package missaomarte.servidor.api.dto;

import java.time.Instant;
import missaomarte.servidor.domain.Dificuldade;
import missaomarte.servidor.domain.Resultado;

public record PartidaResponse(long id, Dificuldade dificuldade, Resultado resultado,
        int pontuacao, int duracaoSegundos, int movimentos, Instant dataHora) {
}
