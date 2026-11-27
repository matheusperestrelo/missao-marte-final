package missaomarte.servidor.application;

public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException() {
        super("Nome ou senha inválidos.");
    }
}
