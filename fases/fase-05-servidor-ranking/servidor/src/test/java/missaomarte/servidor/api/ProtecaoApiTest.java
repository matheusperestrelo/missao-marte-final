package missaomarte.servidor.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import javax.crypto.spec.SecretKeySpec;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import missaomarte.servidor.infrastructure.JwtEmissorDeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Import(RotaProtegidaDeTeste.class)
class ProtecaoApiTest {

    private static final String ROTA = "/api/teste/protegida";

    @Autowired
    MockMvc mvc;
    @Autowired
    UsuarioRepository repositorio;
    @Autowired
    Clock clock;
    @Value("${missaomarte.jwt.segredo}")
    String segredo;

    @BeforeEach
    void limpar() {
        repositorio.deleteAll();
    }

    private static JwtEncoder encoder(String segredo) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(
                new SecretKeySpec(segredo.getBytes(StandardCharsets.UTF_8), "HmacSHA256")));
    }

    private String token(String segredoDoToken, Clock relogio, long sub) {
        return new JwtEmissorDeToken(encoder(segredoDoToken), relogio).emitir(sub).token();
    }

    private long criarUsuario() {
        return repositorio.save(new Usuario("ana", "hash", Instant.now())).getId();
    }

    private void esperar401(ResultActions r) throws Exception {
        r.andExpect(status().isUnauthorized())
                .andExpect(header().string("Content-Type", startsWith("application/problem+json")))
                .andExpect(header().string("WWW-Authenticate", startsWith("Bearer")))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Não autorizado"))
                .andExpect(jsonPath("$.detail").value("Autenticação necessária ou token inválido."))
                .andExpect(header().doesNotExist("Set-Cookie"));
    }

    @Test
    void r5_2_rotaProtegidaSemTokenDa401() throws Exception {
        esperar401(mvc.perform(get(ROTA)));
    }

    @Test
    void r5_2_rotaInexistenteSemTokenDa401() throws Exception {
        esperar401(mvc.perform(get("/api/nao-existe")));
    }

    @Test
    void r5_3_tokenMalformadoDa401() throws Exception {
        esperar401(mvc.perform(get(ROTA).header("Authorization", "Bearer lixo.lixo.lixo")));
    }

    @Test
    void r5_3_assinaturaDeOutroSegredoDa401() throws Exception {
        long id = criarUsuario();
        String t = token("outro-segredo-totalmente-diferente-0123456789", clock, id);
        esperar401(mvc.perform(get(ROTA).header("Authorization", "Bearer " + t)));
    }

    @Test
    void r5_3_tokenExpiradoDa401() throws Exception {
        long id = criarUsuario();
        Clock passado = Clock.fixed(Instant.now().minusSeconds(3 * 3600), ZoneOffset.UTC);
        String t = token(segredo, passado, id);
        esperar401(mvc.perform(get(ROTA).header("Authorization", "Bearer " + t)));
    }

    @Test
    void r5_4_subInexistenteDa401() throws Exception {
        String t = token(segredo, clock, 987654L);
        esperar401(mvc.perform(get(ROTA).header("Authorization", "Bearer " + t)));
    }

    @Test
    void r5_5_tokenValidoChegaARotaComOId() throws Exception {
        long id = criarUsuario();
        String t = token(segredo, clock, id);
        mvc.perform(get(ROTA).header("Authorization", "Bearer " + t))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void r5_7_respostaProtegidaNaoUsaCookie() throws Exception {
        long id = criarUsuario();
        String t = token(segredo, clock, id);
        mvc.perform(get(ROTA).header("Authorization", "Bearer " + t))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Set-Cookie"));
    }

    @Test
    void r7_1_rotaInexistenteComTokenValidoDa404EmProblemDetail() throws Exception {
        long id = criarUsuario();
        String t = token(segredo, clock, id);
        mvc.perform(get("/api/nao-existe").header("Authorization", "Bearer " + t))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", startsWith("application/problem+json")));
    }

    @Test
    void r7_7_erroInternoDa500GenericoSemPilhaNemMensagem() throws Exception {
        long id = criarUsuario();
        String t = token(segredo, clock, id);
        mvc.perform(get("/api/teste/erro").header("Authorization", "Bearer " + t))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", startsWith("application/problem+json")))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("Erro interno no servidor."))
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(content().string(not(containsString("segredo-interno-da-excecao"))))
                .andExpect(content().string(not(containsString("java."))))
                .andExpect(content().string(not(containsString("missaomarte."))));
    }
}
