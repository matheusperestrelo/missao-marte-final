package missaomarte.servidor.infrastructure;

import java.security.SecureRandom;
import java.time.Clock;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class ConfiguracaoJwt {

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguracaoJwt.class);

    @Bean
    SegredoJwt segredoJwt(@Value("${missaomarte.jwt.segredo:}") String valor) {
        SegredoJwt segredo = SegredoJwt.resolver(valor, new SecureRandom());
        if (segredo.aleatorio()) {
            LOG.warn("MISSAOMARTE_JWT_SEGREDO não definido: usando um segredo aleatório. "
                    + "Os tokens deixam de valer quando o servidor reiniciar.");
        }
        return segredo;
    }

    @Bean
    SecretKey chaveJwt(SegredoJwt segredo) {
        return new SecretKeySpec(segredo.bytes(), "HmacSHA256");
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey chaveJwt, Clock clock) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(chaveJwt)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        JwtTimestampValidator tempo = new JwtTimestampValidator();
        tempo.setClock(clock);
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(tempo));
        return decoder;
    }
}
