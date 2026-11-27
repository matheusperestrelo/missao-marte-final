package missaomarte.servidor.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import missaomarte.servidor.application.EmissorDeToken;
import missaomarte.servidor.domain.Dificuldade;
import missaomarte.servidor.domain.Partida;
import missaomarte.servidor.domain.PartidaRepository;
import missaomarte.servidor.domain.Resultado;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ConfiguracaoRelogioDeTeste.class)
class RegistroPartidaApiTest {

    private static final String CORPO_VALIDO = """
            {"dificuldade":"MEDIO","resultado":"VITORIA","pontuacao":1200,
             "duracaoSegundos":95,"movimentos":40}""";

    @Autowired
    MockMvc mvc;
    @Autowired
    UsuarioRepository usuarios;
    @Autowired
    PartidaRepository partidas;
    @Autowired
    EmissorDeToken emissor;
    @Autowired
    RelogioDeTeste relogio;

    @BeforeEach
    void limpar() {
        partidas.deleteAll();
        usuarios.deleteAll();
        relogio.fixar(Instant.now());
    }

    private Usuario criarUsuario(String nome) {
        return usuarios.save(new Usuario(nome, "hash", Instant.now()));
    }

    private String bearer(Usuario usuario) {
        return "Bearer " + emissor.emitir(usuario.getId()).token();
    }

    private ResultActions registrar(String autorizacao, String corpo) throws Exception {
        var req = post("/api/partidas").contentType(MediaType.APPLICATION_JSON).content(corpo);
        if (autorizacao != null) {
            req = req.header("Authorization", autorizacao);
        }
        return mvc.perform(req);
    }

    @Test
    void r1_1_registroValidoDa201ComOsCamposDaPartida() throws Exception {
        Usuario ana = criarUsuario("ana");

        registrar(bearer(ana), CORPO_VALIDO)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/partidas/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.dificuldade").value("MEDIO"))
                .andExpect(jsonPath("$.resultado").value("VITORIA"))
                .andExpect(jsonPath("$.pontuacao").value(1200))
                .andExpect(jsonPath("$.duracaoSegundos").value(95))
                .andExpect(jsonPath("$.movimentos").value(40))
                .andExpect(jsonPath("$.dataHora").isString())
                .andExpect(jsonPath("$.posicaoRanking").value((Object) null));
    }

    @Test
    void r1_2_partidaFicaComODonoDoTokenEIgnoraCamposAMais() throws Exception {
        Usuario ana = criarUsuario("ana");
        Usuario bia = criarUsuario("bia");
        String corpo = """
                {"dificuldade":"FACIL","resultado":"DERROTA","pontuacao":10,
                 "duracaoSegundos":5,"movimentos":3,
                 "usuarioId":%d,"nome":"bia","id":999,"dataHora":"2000-01-01T00:00:00Z"}"""
                .formatted(bia.getId());

        registrar(bearer(ana), corpo)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(org.hamcrest.Matchers.not(999)));

        List<Partida> salvas = partidas.findAll();
        assertThat(salvas).hasSize(1);
        assertThat(salvas.get(0).getUsuario().getId()).isEqualTo(ana.getId());
        assertThat(salvas.get(0).getDataHora()).isAfter(Instant.parse("2001-01-01T00:00:00Z"));
    }

    @Test
    void r1_3_dataHoraEDoServidorEmUtcIso8601() throws Exception {
        Usuario ana = criarUsuario("ana");
        String autorizacao = bearer(ana);
        Instant fixo = Instant.parse("2026-03-04T05:06:07.123Z");
        relogio.fixar(fixo);

        registrar(autorizacao, CORPO_VALIDO)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataHora").value("2026-03-04T05:06:07.123Z"));
    }

    @Test
    void r1_7_semTokenDa401ENaoRegistra() throws Exception {
        criarUsuario("ana");

        registrar(null, CORPO_VALIDO).andExpect(status().isUnauthorized());

        assertThat(partidas.count()).isZero();
    }

    @Test
    void r1_7_tokenInvalidoDa401ENaoRegistra() throws Exception {
        criarUsuario("ana");

        registrar("Bearer lixo.lixo.lixo", CORPO_VALIDO).andExpect(status().isUnauthorized());

        assertThat(partidas.count()).isZero();
    }

    @Test
    void r5_1_partidaSalvaPodeSerLidaComTodosOsCampos() throws Exception {
        Usuario ana = criarUsuario("ana");
        String autorizacao = bearer(ana);
        Instant fixo = Instant.parse("2026-03-04T05:06:07Z");
        relogio.fixar(fixo);

        registrar(autorizacao, CORPO_VALIDO).andExpect(status().isCreated());

        Partida p = partidas.findAll().get(0);
        assertThat(p.getId()).isNotNull();
        assertThat(p.getUsuario().getId()).isEqualTo(ana.getId());
        assertThat(p.getDificuldade()).isEqualTo(Dificuldade.MEDIO);
        assertThat(p.getResultado()).isEqualTo(Resultado.VITORIA);
        assertThat(p.getPontuacao()).isEqualTo(1200);
        assertThat(p.getDuracaoSegundos()).isEqualTo(95);
        assertThat(p.getMovimentos()).isEqualTo(40);
        assertThat(p.getDataHora().truncatedTo(ChronoUnit.SECONDS)).isEqualTo(fixo);
    }
}
