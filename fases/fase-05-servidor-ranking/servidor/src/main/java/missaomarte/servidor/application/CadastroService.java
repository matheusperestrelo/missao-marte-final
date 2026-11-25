package missaomarte.servidor.application;

import java.time.Clock;
import java.time.Instant;
import missaomarte.servidor.domain.NomeUsuario;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CadastroService {

    private final UsuarioRepository repositorio;
    private final PasswordEncoder encoder;
    private final Clock clock;

    public CadastroService(UsuarioRepository repositorio, PasswordEncoder encoder, Clock clock) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.clock = clock;
    }

    public Usuario cadastrar(String nome, String senha) {
        if (repositorio.existsByNomeNormalizado(NomeUsuario.normalizar(nome))) {
            throw new NomeJaExisteException();
        }
        Usuario usuario = new Usuario(nome, encoder.encode(senha), Instant.now(clock));
        try {
            return repositorio.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new NomeJaExisteException();
        }
    }
}
