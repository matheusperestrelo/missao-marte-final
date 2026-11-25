package missaomarte.servidor.api;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class ConfiguracaoSeguranca {

    @Bean
    SecurityFilterChain filtros(HttpSecurity http, JsonMapper jsonMapper) throws Exception {
        EntradaNaoAutenticada entrada = new EntradaNaoAutenticada(jsonMapper);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/saude").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o
                        .jwt(Customizer.withDefaults())
                        .bearerTokenResolver(resolvedorIgnorandoRotasPublicas())
                        .authenticationEntryPoint(entrada)
                        .accessDeniedHandler((req, res, ex) -> entrada.commence(req, res, null)))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(entrada)
                        .accessDeniedHandler((req, res, ex) -> entrada.commence(req, res, null)));
        return http.build();
    }

    /** Nas rotas liberadas o token (mesmo inválido) é ignorado, em vez de virar 401. */
    private static BearerTokenResolver resolvedorIgnorandoRotasPublicas() {
        BearerTokenResolver padrao = new DefaultBearerTokenResolver();
        OrRequestMatcher publicas = new OrRequestMatcher(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/usuarios"),
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/auth/login"),
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/api/saude"),
                PathPatternRequestMatcher.withDefaults().matcher("/error"));
        return request -> publicas.matches(request) ? null : padrao.resolve(request);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
