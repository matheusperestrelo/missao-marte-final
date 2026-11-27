package missaomarte.servidor.api;

import missaomarte.servidor.application.UsuarioAutenticado;
import missaomarte.servidor.domain.UsuarioRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** Entrega o {@link UsuarioAutenticado} às rotas, conferindo que o usuário do token existe. */
public class ResolvedorUsuarioAutenticado implements HandlerMethodArgumentResolver {

    private final UsuarioRepository usuarios;

    public ResolvedorUsuarioAutenticado(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public boolean supportsParameter(MethodParameter parametro) {
        return UsuarioAutenticado.class.equals(parametro.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parametro, ModelAndViewContainer container,
            NativeWebRequest request, WebDataBinderFactory binderFactory) {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || !(autenticacao.getPrincipal() instanceof Jwt jwt)) {
            throw new UsuarioNaoAutenticadoException();
        }
        long id;
        try {
            id = Long.parseLong(jwt.getSubject());
        } catch (NumberFormatException e) {
            throw new UsuarioNaoAutenticadoException();
        }
        if (!usuarios.existsById(id)) {
            throw new UsuarioNaoAutenticadoException();
        }
        return new UsuarioAutenticado(id);
    }
}
