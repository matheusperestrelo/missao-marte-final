package missaomarte.servidor.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SaudeApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void r2_1_saudeSemTokenResponde200ComStatusOk() throws Exception {
        mvc.perform(get("/api/saude"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"ok\"}", true));
    }

    @Test
    void r2_2_saudeComTokenInvalidoResponde200() throws Exception {
        mvc.perform(get("/api/saude").header("Authorization", "Bearer lixo.lixo.lixo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void r5_2_rotaInexistenteSemTokenDa401EmProblemDetail() throws Exception {
        mvc.perform(get("/api/nao-existe"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("Content-Type", org.hamcrest.Matchers.startsWith("application/problem+json")))
                .andExpect(header().string("WWW-Authenticate", org.hamcrest.Matchers.startsWith("Bearer")))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Não autorizado"))
                .andExpect(jsonPath("$.detail").value("Autenticação necessária ou token inválido."))
                .andExpect(header().doesNotExist("Set-Cookie"));
    }
}
