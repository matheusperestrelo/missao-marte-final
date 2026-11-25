package missaomarte.servidor.api;

import jakarta.validation.Valid;
import missaomarte.servidor.api.dto.LoginRequest;
import missaomarte.servidor.api.dto.LoginResponse;
import missaomarte.servidor.application.AutenticacaoService;
import missaomarte.servidor.application.TokenEmitido;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticacaoController {

    private final AutenticacaoService autenticacao;

    public AutenticacaoController(AutenticacaoService autenticacao) {
        this.autenticacao = autenticacao;
    }

    @PostMapping("/api/auth/login")
    public LoginResponse entrar(@Valid @RequestBody LoginRequest pedido) {
        TokenEmitido emitido = autenticacao.entrar(pedido.nome(), pedido.senha());
        return new LoginResponse(emitido.token(), "Bearer", emitido.expiraEmSegundos());
    }
}
