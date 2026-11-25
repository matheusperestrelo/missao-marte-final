package missaomarte.servidor.api;

import java.util.List;
import missaomarte.servidor.domain.UsuarioRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class ConfiguracaoWeb implements WebMvcConfigurer {

    private final UsuarioRepository usuarios;

    ConfiguracaoWeb(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ResolvedorUsuarioAutenticado(usuarios));
    }
}
