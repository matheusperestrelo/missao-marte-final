package missaomarte.servidor.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import missaomarte.servidor.application.EmissorDeToken;
import missaomarte.servidor.domain.PartidaRepository;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class ValidacaoPartidaApiTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    UsuarioRepository usuarios;
    @Autowired
    PartidaRepository partidas;
    @Autowired
    EmissorDeToken emissor;

    private String autorizacao;
    private long aceitas;

    @BeforeEach
    void preparar() {
        aceitas = 0;
        partidas.deleteAll();
        usuarios.deleteAll();
        Usuario ana = usuarios.save(new Usuario("ana", "hash", Instant.now()));
        autorizacao = "Bearer " + emissor.emitir(ana.getId()).token();
    }

    @AfterEach
    void limparPartidas() {
        // outras classes de teste apagam usuários e não conhecem a chave estrangeira
        partidas.deleteAll();
    }

    private static String corpo(String dificuldade, String resultado, String pontuacao,
            String duracao, String movimentos) {
        StringBuilder sb = new StringBuilder("{");
        String[][] campos = {{"dificuldade", dificuldade}, {"resultado", resultado},
                {"pontuacao", pontuacao}, {"duracaoSegundos", duracao}, {"movimentos", movimentos}};
        for (String[] c : campos) {
            if (c[1] != null) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                sb.append('"').append(c[0]).append("\":").append(c[1]);
            }
        }
        return sb.append('}').toString();
    }

    private static String valido() {
        return corpo("\"FACIL\"", "\"VITORIA\"", "100", "10", "5");
    }

    private ResultActions enviar(String corpo) throws Exception {
        return mvc.perform(post("/api/partidas").header("Authorization", autorizacao)
                .contentType(MediaType.APPLICATION_JSON).content(corpo));
    }

    private void aceitar(String corpo) throws Exception {
        enviar(corpo).andExpect(status().isCreated());
        aceitas++;
    }

    private void esperar400(ResultActions r) throws Exception {
        r.andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", startsWith("application/problem+json")))
                .andExpect(jsonPath("$.status").value(400));
        assertThat(partidas.count()).isEqualTo(aceitas);
    }

    private void esperar400ComCampo(ResultActions r, String campo) throws Exception {
        esperar400(r);
        r.andExpect(jsonPath("$.erros[0].campo").value(campo));
    }

    // ---------- R2.1 ----------

    @Test
    void r2_1_dificuldadeInvalidaDa400() throws Exception {
        for (String v : new String[] {"\"ABORTADA\"", "\"facil\"", "\"\"", "\"EXTREMO\""}) {
            esperar400(enviar(corpo(v, "\"VITORIA\"", "1", "1", "1")));
        }
    }

    @Test
    void r2_1_resultadoInvalidoDa400() throws Exception {
        for (String v : new String[] {"\"ABORTADA\"", "\"vitoria\"", "\"\""}) {
            esperar400(enviar(corpo("\"FACIL\"", v, "1", "1", "1")));
        }
    }

    @Test
    void r2_1_todasAsDificuldadesEResultadosValidosSaoAceitos() throws Exception {
        for (String d : new String[] {"FACIL", "MEDIO", "DIFICIL"}) {
            for (String r : new String[] {"VITORIA", "DERROTA"}) {
                aceitar(corpo("\"" + d + "\"", "\"" + r + "\"", "1", "1", "1"));
            }
        }
    }

    // ---------- R2.2 ----------

    @Test
    void r2_2_pontuacaoNosLimitesEAceitaEForaDeles400() throws Exception {
        aceitar(corpo("\"FACIL\"", "\"VITORIA\"", "-100000", "1", "1"));
        aceitar(corpo("\"FACIL\"", "\"VITORIA\"", "100000", "1", "1"));
        esperar400ComCampo(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "-100001", "1", "1")), "pontuacao");
        esperar400ComCampo(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "100001", "1", "1")), "pontuacao");
    }

    @Test
    void r2_2_duracaoNosLimitesEAceitaEForaDeles400() throws Exception {
        aceitar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "0", "1"));
        aceitar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "86400", "1"));
        esperar400ComCampo(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "-1", "1")), "duracaoSegundos");
        esperar400ComCampo(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "86401", "1")), "duracaoSegundos");
    }

    @Test
    void r2_2_movimentosNosLimitesEAceitaEForaDeles400() throws Exception {
        aceitar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1", "0"));
        aceitar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1", "100000"));
        esperar400ComCampo(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1", "-1")), "movimentos");
        esperar400ComCampo(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1", "100001")), "movimentos");
    }

    // ---------- R2.3 ----------

    @Test
    void r2_3_campoAusenteDa400() throws Exception {
        String[] nomes = {"dificuldade", "resultado", "pontuacao", "duracaoSegundos", "movimentos"};
        String[] valores = {"\"FACIL\"", "\"VITORIA\"", "1", "1", "1"};
        for (int i = 0; i < nomes.length; i++) {
            String[] v = valores.clone();
            v[i] = null;
            esperar400ComCampo(enviar(corpo(v[0], v[1], v[2], v[3], v[4])), nomes[i]);
        }
    }

    @Test
    void r2_3_campoNuloDa400() throws Exception {
        String[] nomes = {"dificuldade", "resultado", "pontuacao", "duracaoSegundos", "movimentos"};
        String[] valores = {"\"FACIL\"", "\"VITORIA\"", "1", "1", "1"};
        for (int i = 0; i < nomes.length; i++) {
            String[] v = valores.clone();
            v[i] = "null";
            esperar400ComCampo(enviar(corpo(v[0], v[1], v[2], v[3], v[4])), nomes[i]);
        }
    }

    // ---------- R2.4 ----------

    @Test
    void r2_4_textoNumCampoNumericoDa400() throws Exception {
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "\"abc\"", "1", "1")));
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "\"abc\"", "1")));
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1", "\"abc\"")));
    }

    @Test
    void r2_4_numeroComCasasDecimaisDa400() throws Exception {
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1.5", "1", "1")));
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1.5", "1")));
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "1", "1", "1.5")));
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "10.0", "1", "1")));
    }

    @Test
    void r2_4_corpoQueNaoEJsonDa400() throws Exception {
        esperar400(enviar("{isso nao e json"));
        esperar400(enviar(""));
    }

    // ---------- R2.5 ----------

    @Test
    void r2_5_doisCamposInvalidosDaoDoisItensEmErros() throws Exception {
        esperar400(enviar(corpo("\"FACIL\"", "\"VITORIA\"", "100001", "-1", "1")));
        enviar(corpo("\"FACIL\"", "\"VITORIA\"", "100001", "-1", "1"))
                .andExpect(jsonPath("$.erros.length()").value(2));
    }
}
