package missaomarte.servidor.api;

import org.springframework.security.core.AuthenticationException;

/** Token válido, mas o usuário do {@code sub} não existe (ou o {@code sub} não é um id). */
public class UsuarioNaoAutenticadoException extends AuthenticationException {

    public UsuarioNaoAutenticadoException() {
        super("Usuário do token não encontrado.");
    }
}
