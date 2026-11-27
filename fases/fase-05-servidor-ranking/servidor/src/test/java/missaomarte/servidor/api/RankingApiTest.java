package missaomarte.servidor.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import missaomarte.servidor.application.EmissorDeToken;
import missaomarte.servidor.domain.Dificuldade;
import missaomarte.servidor.domain.Partida;
import missaomarte.servidor.domain.PartidaRepository;
import missaomarte.servidor.domain.Resultado;
import missaomarte.servidor.domain.Usuario;
import missaomarte.servidor.domain.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class RankingApiTest {

    private static final Instant BASE = Instant.parse("2026-03-04T05:06:07Z");

    @Autowired
    MockMvc mvc;
    @Autowired
    UsuarioRepository usuarios;
    @Autowired
    PartidaRepository partidas;
    @Autowired
    EmissorDeToken emissor;

    Usuario ana;

    @BeforeEach
    void limpar() {
        partidas.deleteAll();
        usuarios.deleteAll();
        ana = criarUsuario("Ana_Maria");
    }

    @AfterEach
    void limparPartidas() {
        // outras classes de teste apagam usuários e não conhecem a chave estrangeira
        partidas.deleteAll();
    }

    private Usuario criarUsuario(String nome) {
        return usuarios.save(new Usuario(nome, "hash", Instant.now()));
    }

    private Partida salvar(Usuario dono, Resultado resultado, int pontuacao, int duracao) {
        return partidas.save(new Partida(dono, Dificuldade.DIFICIL, resultado, pontuacao, duracao, 33,
                BASE));
    }

    private ResultActions ranking() throws Exception {
        return mvc.perform(get("/api/ranking")
                .header("Authorization", "Bearer " + emissor.emitir(ana.getId()).token()));
    }

    @Test
    void r4_1_linhaTemOsCamposEOsValoresDaPartida() throws Exception {
        salvar(ana, Resultado.VITORIA, 800, 75);

        ranking()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].posicao").value(1))
                .andExpect(jsonPath("$[0].nome").value("Ana_Maria"))
                .andExpect(jsonPath("$[0].pontuacao").value(800))
                .andExpect(jsonPath("$[0].dificuldade").value("DIFICIL"))
                .andExpect(jsonPath("$[0].duracaoSegundos").value(75))
                .andExpect(jsonPath("$[0].movimentos").value(33))
                .andExpect(jsonPath("$[0].dataHora").value("2026-03-04T05:06:07Z"));
    }

    @Test
    void r4_2_derrotaEVitoriaComPontuacaoZeroFicamFora() throws Exception {
        Usuario bia = criarUsuario("bia");
        Usuario caio = criarUsuario("caio");
        salvar(ana, Resultado.DERROTA, 9000, 10);
        salvar(bia, Resultado.VITORIA, 0, 10);
        salvar(caio, Resultado.VITORIA, 5, 10);

        ranking()
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("caio"));
    }

    @Test
    void r4_3_jogadorComTresVitoriasApareceUmaVezComAMelhor() throws Exception {
        salvar(ana, Resultado.VITORIA, 100, 10);
        salvar(ana, Resultado.VITORIA, 300, 10);
        salvar(ana, Resultado.VITORIA, 200, 10);

        ranking()
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].pontuacao").value(300));
    }

    @Test
    void r4_4_ordenaPorPontuacaoEDesempataPorDuracao() throws Exception {
        Usuario bia = criarUsuario("bia");
        Usuario caio = criarUsuario("caio");
        salvar(ana, Resultado.VITORIA, 100, 50);
        salvar(bia, Resultado.VITORIA, 200, 90);
        salvar(caio, Resultado.VITORIA, 100, 20);

        ranking()
                .andExpect(jsonPath("$[0].nome").value("bia"))
                .andExpect(jsonPath("$[1].nome").value("caio"))
                .andExpect(jsonPath("$[2].nome").value("Ana_Maria"));
    }

    @Test
    void r4_5_devolveNoMaximoDezLinhasComPosicoesSequenciais() throws Exception {
        for (int i = 1; i <= 12; i++) {
            salvar(criarUsuario("jogador" + i), Resultado.VITORIA, 100 + i, 20);
        }

        ranking()
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].posicao").value(1))
                .andExpect(jsonPath("$[9].posicao").value(10))
                .andExpect(jsonPath("$[0].pontuacao").value(112))
                .andExpect(jsonPath("$[9].pontuacao").value(103));
    }

    @Test
    void r4_6_semVitoriasDevolveListaVazia() throws Exception {
        salvar(ana, Resultado.DERROTA, 500, 10);

        ranking()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void r4_7_linhaTemSomenteAsChavesEsperadas() throws Exception {
        salvar(ana, Resultado.VITORIA, 800, 75);

        String corpo = ranking().andReturn().getResponse().getContentAsString();
        JsonNode linha = new ObjectMapper().readTree(corpo).get(0);

        java.util.Set<String> chaves = new java.util.TreeSet<>();
        linha.propertyNames().forEach(chaves::add);
        org.assertj.core.api.Assertions.assertThat(chaves).containsExactlyInAnyOrder("posicao", "nome",
                "pontuacao", "dificuldade", "duracaoSegundos", "movimentos", "dataHora");
    }

    @Test
    void r4_8_semTokenDa401() throws Exception {
        mvc.perform(get("/api/ranking")).andExpect(status().isUnauthorized());
    }

    @Test
    void r4_8_tokenInvalidoDa401() throws Exception {
        mvc.perform(get("/api/ranking").header("Authorization", "Bearer lixo.lixo.lixo"))
                .andExpect(status().isUnauthorized());
    }
}
