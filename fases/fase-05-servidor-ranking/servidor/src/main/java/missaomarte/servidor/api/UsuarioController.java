package missaomarte.servidor.api;

import jakarta.validation.Valid;
import java.net.URI;
import missaomarte.servidor.api.dto.CadastroRequest;
import missaomarte.servidor.api.dto.UsuarioResponse;
import missaomarte.servidor.application.CadastroService;
import missaomarte.servidor.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    private final CadastroService cadastro;

    public UsuarioController(CadastroService cadastro) {
        this.cadastro = cadastro;
    }

    @PostMapping("/api/usuarios")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroRequest pedido) {
        Usuario usuario = cadastro.cadastrar(pedido.nome(), pedido.senha());
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getId()))
                .body(new UsuarioResponse(usuario.getId(), usuario.getNome()));
    }
}
