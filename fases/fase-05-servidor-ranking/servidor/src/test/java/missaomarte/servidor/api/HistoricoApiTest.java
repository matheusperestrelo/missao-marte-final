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
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ConfiguracaoRelogioDeTeste.class)
class HistoricoApiTest {

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
        ana = criarUsuario("ana");
    }

    @AfterEach
    void limparPartidas() {
        // outras classes de teste apagam usuários e não conhecem a chave estrangeira
        partidas.deleteAll();
    }

    private Usuario criarUsuario(String nome) {
        return usuarios.save(new Usuario(nome, "hash", Instant.now()));
    }

    private Partida salvar(Usuario dono, int pontuacao, Instant dataHora) {
        return partidas.save(new Partida(dono, Dificuldade.MEDIO, Resultado.VITORIA, pontuacao, 60,
                20, dataHora));
    }

    private ResultActions minhas(Usuario quem, String consulta) throws Exception {
        return mvc.perform(get("/api/partidas/minhas" + consulta)
                .header("Authorization", "Bearer " + emissor.emitir(quem.getId()).token()));
    }

    @Test
    void r3_1_respondeComPaginaEItensNoFormato() throws Exception {
        salvar(ana, 500, BASE);

        minhas(ana, "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.itens[0].id").isNumber())
                .andExpect(jsonPath("$.itens[0].dificuldade").value("MEDIO"))
                .andExpect(jsonPath("$.itens[0].resultado").value("VITORIA"))
                .andExpect(jsonPath("$.itens[0].pontuacao").value(500))
                .andExpect(jsonPath("$.itens[0].duracaoSegundos").value(60))
                .andExpect(jsonPath("$.itens[0].movimentos").value(20))
                .andExpect(jsonPath("$.itens[0].dataHora").value("2026-03-04T05:06:07Z"))
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanho").value(10))
                .andExpect(jsonPath("$.totalItens").value(1))
                .andExpect(jsonPath("$.totalPaginas").value(1));
    }

    @Test
    void r3_2_devolveSoAsPartidasDoDono() throws Exception {
        Usuario bia = criarUsuario("bia");
        salvar(ana, 111, BASE);
        salvar(bia, 222, BASE);
        salvar(bia, 333, BASE);

        minhas(ana, "")
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.itens[0].pontuacao").value(111))
                .andExpect(jsonPath("$.totalItens").value(1));
        minhas(bia, "").andExpect(jsonPath("$.totalItens").value(2));
    }

    @Test
    void r3_3_ordenaPorDataHoraDecrescenteEIdDecrescenteNoEmpate() throws Exception {
        salvar(ana, 1, BASE);
        salvar(ana, 2, BASE.plusSeconds(100));
        salvar(ana, 3, BASE.plusSeconds(50));
        salvar(ana, 4, BASE.plusSeconds(100));

        minhas(ana, "")
                .andExpect(jsonPath("$.itens[0].pontuacao").value(4))
                .andExpect(jsonPath("$.itens[1].pontuacao").value(2))
                .andExpect(jsonPath("$.itens[2].pontuacao").value(3))
                .andExpect(jsonPath("$.itens[3].pontuacao").value(1));
    }

    @Test
    void r3_4_semParametrosUsaPagina0Tamanho10() throws Exception {
        for (int i = 0; i < 12; i++) {
            salvar(ana, i, BASE.plusSeconds(i));
        }

        minhas(ana, "")
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanho").value(10))
                .andExpect(jsonPath("$.itens", hasSize(10)))
                .andExpect(jsonPath("$.totalItens").value(12))
                .andExpect(jsonPath("$.totalPaginas").value(2));
    }

    @Test
    void r3_5_devolveAFatiaECertaEOsTotais() throws Exception {
        for (int i = 0; i < 23; i++) {
            salvar(ana, i, BASE.plusSeconds(i));
        }

        int[] esperados = {10, 10, 3};
        for (int pagina = 0; pagina < 3; pagina++) {
            minhas(ana, "?pagina=" + pagina + "&tamanho=10")
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pagina").value(pagina))
                    .andExpect(jsonPath("$.tamanho").value(10))
                    .andExpect(jsonPath("$.itens", hasSize(esperados[pagina])))
                    .andExpect(jsonPath("$.totalItens").value(23))
                    .andExpect(jsonPath("$.totalPaginas").value(3));
        }
        // a página 0 começa na mais recente (pontuação 22)
        minhas(ana, "?pagina=0&tamanho=10").andExpect(jsonPath("$.itens[0].pontuacao").value(22));
        minhas(ana, "?pagina=2&tamanho=10").andExpect(jsonPath("$.itens[2].pontuacao").value(0));
    }

    @Test
    void r3_6_paginaAlemDoFimDa200ComItensVazios() throws Exception {
        for (int i = 0; i < 3; i++) {
            salvar(ana, i, BASE.plusSeconds(i));
        }

        minhas(ana, "?pagina=5")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(0)))
                .andExpect(jsonPath("$.pagina").value(5))
                .andExpect(jsonPath("$.totalItens").value(3))
                .andExpect(jsonPath("$.totalPaginas").value(1));
    }

    @Test
    void r3_7_paginaNegativaDa400CitandoOParametro() throws Exception {
        minhas(ana, "?pagina=-1")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("pagina"));
    }

    @Test
    void r3_7_tamanhoForaDaFaixaDa400CitandoOParametro() throws Exception {
        minhas(ana, "?tamanho=0")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("tamanho"));
        minhas(ana, "?tamanho=51")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("tamanho"));
    }

    @Test
    void r3_7_parametroNaoInteiroDa400CitandoOParametro() throws Exception {
        minhas(ana, "?tamanho=abc")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("tamanho"));
        minhas(ana, "?pagina=1.5")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("pagina"));
    }

    @Test
    void r3_7_limitesDeTamanhoSaoAceitos() throws Exception {
        salvar(ana, 1, BASE);
        salvar(ana, 2, BASE.plusSeconds(1));

        minhas(ana, "?tamanho=1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.totalPaginas").value(2));
        minhas(ana, "?tamanho=50")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(2)));
    }

    @Test
    void r3_8_semPartidasDa200Vazio() throws Exception {
        minhas(ana, "")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(0)))
                .andExpect(jsonPath("$.totalItens").value(0))
                .andExpect(jsonPath("$.totalPaginas").value(0));
    }

    @Test
    void r3_9_semTokenDa401() throws Exception {
        mvc.perform(get("/api/partidas/minhas")).andExpect(status().isUnauthorized());
    }

    @Test
    void r3_9_tokenInvalidoDa401() throws Exception {
        mvc.perform(get("/api/partidas/minhas").header("Authorization", "Bearer lixo.lixo.lixo"))
                .andExpect(status().isUnauthorized());
    }
}
