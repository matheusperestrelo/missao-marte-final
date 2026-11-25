package missaomarte.servidor.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import missaomarte.servidor.api.validacao.TamanhoMaximoEmBytes;

public record CadastroRequest(
        @NotBlank(message = "é obrigatório")
        @Pattern(regexp = "^[A-Za-z0-9_-]{3,20}$",
                message = "deve ter de 3 a 20 caracteres: letras sem acento, números, _ e -")
        String nome,
        @NotBlank(message = "é obrigatória")
        @Size(min = 8, message = "deve ter pelo menos 8 caracteres")
        @TamanhoMaximoEmBytes(72)
        String senha) {

    @Override
    public String toString() {
        return "CadastroRequest[nome=" + nome + ", senha=***]";
    }
}
