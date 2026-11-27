package missaomarte.servidor.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** Troca o {@code Clock} da aplicação por um {@link RelogioDeTeste}. */
@TestConfiguration
public class ConfiguracaoRelogioDeTeste {

    @Bean
    @Primary
    RelogioDeTeste relogioDeTeste() {
        return new RelogioDeTeste();
    }
}
