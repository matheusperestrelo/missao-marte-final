package missaomarte.servidor.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "é obrigatório") String nome,
        @NotBlank(message = "é obrigatória") String senha) {

    @Override
    public String toString() {
        return "LoginRequest[nome=" + nome + ", senha=***]";
    }
}
