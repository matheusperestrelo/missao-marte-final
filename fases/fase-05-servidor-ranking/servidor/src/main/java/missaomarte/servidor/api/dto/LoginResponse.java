package missaomarte.servidor.api.dto;

public record LoginResponse(String token, String tipo, long expiraEmSegundos) {

    @Override
    public String toString() {
        return "LoginResponse[token=***, tipo=" + tipo + ", expiraEmSegundos=" + expiraEmSegundos + "]";
    }
}
