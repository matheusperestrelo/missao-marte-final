package missaomarte.servidor.api;

import missaomarte.servidor.application.UsuarioAutenticado;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rota que só existe nos testes: devolve o id recebido. */
@RestController
class RotaProtegidaDeTeste {

    record Resposta(long id) {
    }

    @GetMapping("/api/teste/protegida")
    Resposta protegida(UsuarioAutenticado usuario) {
        return new Resposta(usuario.id());
    }

    @GetMapping("/api/teste/erro")
    Resposta erro() {
        throw new RuntimeException("segredo-interno-da-excecao");
    }
}
