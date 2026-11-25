package missaomarte.servidor.application;

public class NomeJaExisteException extends RuntimeException {

    public NomeJaExisteException() {
        super("Já existe um usuário com esse nome.");
    }
}
