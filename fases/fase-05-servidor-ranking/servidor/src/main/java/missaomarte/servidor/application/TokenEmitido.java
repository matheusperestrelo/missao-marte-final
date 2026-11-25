package missaomarte.servidor.application;

public record TokenEmitido(String token, long expiraEmSegundos) {

    @Override
    public String toString() {
        return "TokenEmitido[token=***, expiraEmSegundos=" + expiraEmSegundos + "]";
    }
}
