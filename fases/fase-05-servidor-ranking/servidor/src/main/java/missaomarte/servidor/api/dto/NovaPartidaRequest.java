package missaomarte.servidor.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import missaomarte.servidor.domain.Dificuldade;
import missaomarte.servidor.domain.Resultado;

public record NovaPartidaRequest(
        @NotNull Dificuldade dificuldade,
        @NotNull Resultado resultado,
        @NotNull @Min(-100000) @Max(100000) Integer pontuacao,
        @NotNull @Min(0) @Max(86400) Integer duracaoSegundos,
        @NotNull @Min(0) @Max(100000) Integer movimentos) {
}
