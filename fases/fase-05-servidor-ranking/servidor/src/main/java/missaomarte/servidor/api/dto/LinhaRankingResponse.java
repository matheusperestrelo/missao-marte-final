package missaomarte.servidor.api.dto;

import java.time.Instant;
import missaomarte.servidor.domain.Dificuldade;

public record LinhaRankingResponse(int posicao, String nome, int pontuacao, Dificuldade dificuldade,
        int duracaoSegundos, int movimentos, Instant dataHora) {
}
