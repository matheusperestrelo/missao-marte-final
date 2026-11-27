package missaomarte.servidor.infrastructure;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import missaomarte.servidor.application.EmissorDeToken;
import missaomarte.servidor.application.TokenEmitido;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public class JwtEmissorDeToken implements EmissorDeToken {

    static final Duration VALIDADE = Duration.ofHours(2);

    private final JwtEncoder encoder;
    private final Clock clock;

    public JwtEmissorDeToken(JwtEncoder encoder, Clock clock) {
        this.encoder = encoder;
        this.clock = clock;
    }

    @Override
    public TokenEmitido emitir(long usuarioId) {
        Instant agora = Instant.now(clock);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(usuarioId))
                .issuedAt(agora)
                .expiresAt(agora.plus(VALIDADE))
                .build();
        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(cabecalho, claims)).getTokenValue();
        return new TokenEmitido(token, VALIDADE.toSeconds());
    }
}
