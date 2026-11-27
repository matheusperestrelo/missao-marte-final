package missaomarte.servidor.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import missaomarte.servidor.api.dto.LoginRequest;
import missaomarte.servidor.api.dto.LoginResponse;
import missaomarte.servidor.domain.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class LoginApiTest {

    private static final String SENHA = "senha-boa-123";

    @Autowired
    MockMvc mvc;
    @Autowired
    UsuarioRepository repositorio;
    @Autowired
    JwtDecoder decoder;
    @Autowired
    JsonMapper json;

    @BeforeEach
    void limpar() {
        repositorio.deleteAll();
    }

    private long cadastrar(String nome) throws Exception {
        String corpo = mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"senha\":\"" + SENHA + "\"}"))
                .andReturn().getResponse().getContentAsString();
        return json.readTree(corpo).get("id").asLong();
    }

    private ResultActions login(String nome, String senha) throws Exception {
        return mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"" + nome + "\",\"senha\":\"" + senha + "\"}"));
    }

    private JsonNode corpo(ResultActions r) throws Exception {
        return json.readTree(r.andReturn().getResponse().getContentAsString());
    }

    private String token() throws Exception {
        return corpo(login("Matheus", SENHA)).get("token").asString();
    }

    @Test
    void r4_1_loginCorretoDa200ComTokenTipoEExpiracao() throws Exception {
        cadastrar("Matheus");
        login("Matheus", SENHA)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.expiraEmSegundos").value(7200))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void r4_2_loginComOutraCaixaNoNomeEAceito() throws Exception {
        cadastrar("Matheus");
        login("MATHEUS", SENHA).andExpect(status().isOk());
    }

    @Test
    void r4_3_senhaComOutraCaixaERecusada() throws Exception {
        cadastrar("Matheus");
        login("Matheus", SENHA.toUpperCase()).andExpect(status().isUnauthorized());
    }

    @Test
    void r4_4_tokenEJwtHs256ComSubIatExpESemDadosSensiveis() throws Exception {
        long id = cadastrar("Matheus");

        Jwt jwt = decoder.decode(token());

        assertThat(jwt.getHeaders().get("alg")).isEqualTo("HS256");
        assertThat(jwt.getSubject()).isEqualTo(String.valueOf(id));
        assertThat(jwt.getIssuedAt()).isNotNull();
        assertThat(Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt())).isEqualTo(Duration.ofHours(2));
        assertThat(jwt.getClaims().keySet()).containsOnly("sub", "iat", "exp");
    }

    @Test
    void r4_5_usuarioInexistenteESenhaErradaDaoOMesmo401() throws Exception {
        cadastrar("Matheus");
        ResultActions inexistente = login("ninguem", SENHA);
        ResultActions errada = login("Matheus", "senha-errada-1");

        inexistente.andExpect(status().isUnauthorized())
                .andExpect(header().string("Content-Type",
                        org.hamcrest.Matchers.startsWith("application/problem+json")))
                .andExpect(jsonPath("$.detail").value("Nome ou senha inválidos."));
        errada.andExpect(status().isUnauthorized());
        assertThat(corpo(inexistente)).isEqualTo(corpo(errada));
    }

    @Test
    void r4_7_nomeOuSenhaAusentesOuVaziosDao400EmProblemDetail() throws Exception {
        for (String c : new String[] {"{}", "{\"nome\":\"\",\"senha\":\"\"}", "{\"nome\":\"a\"}",
                "{\"senha\":\"x\"}"}) {
            mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(c))
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string("Content-Type",
                            org.hamcrest.Matchers.startsWith("application/problem+json")));
        }
    }

    @Test
    void r5_7_loginNaoEnviaCookie() throws Exception {
        cadastrar("Matheus");
        login("Matheus", SENHA).andExpect(header().doesNotExist("Set-Cookie"));
    }

    @Test
    void r6_1_tokenEmitidoEAceitoPeloDecoderComOSegredoConfigurado() throws Exception {
        cadastrar("Matheus");
        assertThat(decoder.decode(token()).getSubject()).isNotBlank();
    }

    @Test
    void r7_4_toStringDeLoginRequestELoginResponseNaoMostraSenhaNemToken() {
        assertThat(new LoginRequest("matheus", "segredo-123").toString()).doesNotContain("segredo-123");
        assertThat(new LoginResponse("tok-abc", "Bearer", 7200).toString()).doesNotContain("tok-abc");
    }

    @Test
    void r7_5_logDeCadastroELoginNaoTemSenhaNemToken(CapturedOutput saida) throws Exception {
        cadastrar("Matheus");
        String token = token();

        assertThat(saida.getAll()).doesNotContain(SENHA).doesNotContain(token);
    }
}
