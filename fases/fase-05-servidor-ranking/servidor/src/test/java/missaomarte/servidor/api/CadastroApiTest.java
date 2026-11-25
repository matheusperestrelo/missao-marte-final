package missaomarte.servidor.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import missaomarte.servidor.api.dto.CadastroRequest;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class CadastroApiTest {

    private static final String SENHA_OK = "senha-boa-123";

    @Autowired
    MockMvc mvc;
    @Autowired
    UsuarioRepository repositorio;
    @Autowired
    PasswordEncoder encoder;

    @BeforeEach
    void limpar() {
        repositorio.deleteAll();
    }

    private ResultActions cadastrar(String corpo) throws Exception {
        return mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(corpo));
    }

    private ResultActions cadastrar(String nome, String senha) throws Exception {
        return cadastrar("{\"nome\":\"" + nome + "\",\"senha\":\"" + senha + "\"}");
    }

    private void esperaProblema400(ResultActions r) throws Exception {
        r.andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type",
                        org.hamcrest.Matchers.startsWith("application/problem+json")));
    }

    @Test
    void r3_1_cadastroValidoDa201ComIdENomeComoEnviado() throws Exception {
        cadastrar("Matheus", SENHA_OK)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Matheus"))
                .andExpect(header().exists("Location"));
    }

    @Test
    void r3_2_respostaTemSoIdENome() throws Exception {
        String corpo = cadastrar("Matheus", SENHA_OK).andReturn().getResponse().getContentAsString();
        assertThat(corpo).doesNotContain(SENHA_OK).doesNotContain("$2");
        cadastrar("Outro", SENHA_OK).andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void r3_3_senhaGuardadaComoHashBcryptDeCustoDez() throws Exception {
        cadastrar("Matheus", SENHA_OK).andExpect(status().isCreated());
        Usuario u = repositorio.findByNomeNormalizado("matheus").orElseThrow();
        assertThat(u.getHashSenha()).matches("^\\$2[aby]\\$10\\$.{53}$");
        assertThat(u.getHashSenha()).isNotEqualTo(SENHA_OK);
        assertThat(encoder.matches(SENHA_OK, u.getHashSenha())).isTrue();
    }

    @Test
    void r3_4_mesmaSenhaGeraHashesDiferentes() throws Exception {
        cadastrar("ana", SENHA_OK).andExpect(status().isCreated());
        cadastrar("bia", SENHA_OK).andExpect(status().isCreated());
        String a = repositorio.findByNomeNormalizado("ana").orElseThrow().getHashSenha();
        String b = repositorio.findByNomeNormalizado("bia").orElseThrow().getHashSenha();
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void r3_5_guardaNomeNormalizadoEDataDeCriacao() throws Exception {
        cadastrar("MaTheus", SENHA_OK).andExpect(status().isCreated());
        Usuario u = repositorio.findByNomeNormalizado("matheus").orElseThrow();
        assertThat(u.getNome()).isEqualTo("MaTheus");
        assertThat(u.getNomeNormalizado()).isEqualTo("matheus");
        assertThat(u.getCriadoEm()).isNotNull();
    }

    @Test
    void r3_6_nomeRepetidoComOutraCaixaDa409SemCriarOutro() throws Exception {
        cadastrar("Matheus", SENHA_OK).andExpect(status().isCreated());
        for (String repetido : new String[] {"matheus", "MATHEUS"}) {
            cadastrar(repetido, SENHA_OK)
                    .andExpect(status().isConflict())
                    .andExpect(header().string("Content-Type",
                            org.hamcrest.Matchers.startsWith("application/problem+json")))
                    .andExpect(jsonPath("$.detail").value("Já existe um usuário com esse nome."));
        }
        assertThat(repositorio.count()).isEqualTo(1);
    }

    @Test
    void r3_7_nomeNosLimitesDeTamanho() throws Exception {
        esperaProblema400(cadastrar("ab", SENHA_OK));
        cadastrar("abc", SENHA_OK).andExpect(status().isCreated());
        cadastrar("a".repeat(20), SENHA_OK).andExpect(status().isCreated());
        esperaProblema400(cadastrar("a".repeat(21), SENHA_OK));
    }

    @Test
    void r3_7_nomeComCaracteresInvalidosOuAusenteDa400() throws Exception {
        esperaProblema400(cadastrar("com espaco", SENHA_OK));
        esperaProblema400(cadastrar("João", SENHA_OK));
        esperaProblema400(cadastrar("nome@x", SENHA_OK));
        esperaProblema400(cadastrar("", SENHA_OK));
        esperaProblema400(cadastrar("{\"senha\":\"" + SENHA_OK + "\"}"));
        cadastrar("a_b-9", SENHA_OK).andExpect(status().isCreated());
        assertThat(repositorio.count()).isEqualTo(1);
    }

    @Test
    void r3_8_senhaCom7Recusa8Aceita() throws Exception {
        esperaProblema400(cadastrar("ana", "1234567"));
        cadastrar("ana", "12345678").andExpect(status().isCreated());
    }

    @Test
    void r3_9_senhaMaximaDe72BytesEmUtf8() throws Exception {
        cadastrar("ana", "a".repeat(72)).andExpect(status().isCreated());
        esperaProblema400(cadastrar("bia", "a".repeat(73)));
        // 40 caracteres, 80 bytes
        esperaProblema400(cadastrar("cris", "é".repeat(40)));
        cadastrar("dani", "é".repeat(36)).andExpect(status().isCreated());
    }

    @Test
    void r3_10_senhaAusenteVaziaOuJsonMalformadoDa400() throws Exception {
        esperaProblema400(cadastrar("{\"nome\":\"ana\"}"));
        esperaProblema400(cadastrar("ana", ""));
        esperaProblema400(cadastrar("{nome:"));
        assertThat(repositorio.count()).isZero();
    }

    @Test
    void r7_2_doisCamposInvalidosDaoDoisItensEmErros() throws Exception {
        cadastrar("a b", "123")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.length()").value(2))
                .andExpect(jsonPath("$.erros[?(@.campo=='nome')]").exists())
                .andExpect(jsonPath("$.erros[?(@.campo=='senha')]").exists())
                .andExpect(jsonPath("$.erros[0].mensagem").isString());
    }

    @Test
    void r7_2_campoComVariasViolacoesGeraUmItemSo() throws Exception {
        cadastrar("", SENHA_OK)
                .andExpect(jsonPath("$.erros.length()").value(1))
                .andExpect(jsonPath("$.erros[0].campo").value("nome"));
    }

    @Test
    void r7_3_senhaInvalidaNaoApareceNoCorpoDoErro() throws Exception {
        String curta = cadastrar("a b", "segredo").andReturn().getResponse().getContentAsString();
        assertThat(curta).doesNotContain("segredo");
        String longa = "x".repeat(73);
        String corpo = cadastrar("ana", longa).andReturn().getResponse().getContentAsString();
        assertThat(corpo).doesNotContain(longa);
    }

    @Test
    void r7_3_nomeInvalidoNaoRepeteValorRejeitadoNoCorpo() throws Exception {
        String corpo = cadastrar("nome@estranho", SENHA_OK).andReturn().getResponse().getContentAsString();
        assertThat(corpo).doesNotContain("nome@estranho").doesNotContain(SENHA_OK);
    }

    @Test
    void r7_4_toStringDoCadastroNaoMostraASenha() {
        String texto = new CadastroRequest("ana", "minha-senha-secreta").toString();
        assertThat(texto).doesNotContain("minha-senha-secreta").contains("ana").contains("***");
    }

    @Test
    void r7_1_jsonMalformadoEhProblemJson() throws Exception {
        cadastrar("{nome:")
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }
}
