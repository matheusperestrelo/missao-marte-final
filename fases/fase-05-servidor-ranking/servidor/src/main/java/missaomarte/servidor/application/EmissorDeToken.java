package missaomarte.servidor.application;

/** Porta para emitir o token de acesso de um usuário. */
public interface EmissorDeToken {

    TokenEmitido emitir(long usuarioId);
}
