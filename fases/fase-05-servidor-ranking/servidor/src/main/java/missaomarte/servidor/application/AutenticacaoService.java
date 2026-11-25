package missaomarte.servidor.application;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import missaomarte.servidor.domain.NomeUsuario;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioRepository repositorio;
    private final PasswordEncoder encoder;
    private final EmissorDeToken emissor;
    /** Hash de uma senha aleatória: gasta o mesmo tempo quando o usuário não existe. */
    private final String hashFalso;

    public AutenticacaoService(UsuarioRepository repositorio, PasswordEncoder encoder,
            EmissorDeToken emissor) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.emissor = emissor;
        byte[] aleatorio = new byte[32];
        new SecureRandom().nextBytes(aleatorio);
        this.hashFalso = encoder.encode(Base64.getEncoder().encodeToString(aleatorio));
    }

    public TokenEmitido entrar(String nome, String senha) {
        Optional<Usuario> usuario = repositorio.findByNomeNormalizado(NomeUsuario.normalizar(nome));
        if (usuario.isEmpty()) {
            encoder.matches(senha, hashFalso);
            throw new CredenciaisInvalidasException();
        }
        if (!encoder.matches(senha, usuario.get().getHashSenha())) {
            throw new CredenciaisInvalidasException();
        }
        return emissor.emitir(usuario.get().getId());
    }
}
