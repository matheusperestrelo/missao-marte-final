package missaomarte.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import javax.imageio.ImageIO;
import missaomarte.gdx.catalogo.Imagem;
import org.junit.jupiter.api.Test;

/** Confere os PNG commitados em core/assets contra o catálogo. */
class ImagensAssetsTest {

    private static final Set<Imagem> OPACAS = EnumSet.of(Imagem.FUNDO, Imagem.TERRENO);

    static Path pastaAssets() {
        return Paths.get(System.getProperty("missaomarte.assets"));
    }

    private static BufferedImage ler(Imagem imagem) throws IOException {
        File arquivo = pastaAssets().resolve(imagem.caminho()).toFile();
        assertTrue(arquivo.isFile(), "arquivo ausente: " + imagem.caminho());
        BufferedImage lida = ImageIO.read(arquivo);
        assertNotNull(lida, "PNG ilegível: " + imagem.caminho());
        return lida;
    }

    @Test
    void test_R3_1_cada_imagem_existe_com_as_dimensoes_do_catalogo() throws IOException {
        for (Imagem imagem : Imagem.values()) {
            BufferedImage lida = ler(imagem);
            assertEquals(imagem.quadros() * imagem.larguraQuadro(), lida.getWidth(), imagem.name());
            assertEquals(imagem.alturaQuadro(), lida.getHeight(), imagem.name());
        }
    }

    @Test
    void test_R3_1_dimensoes_da_tabela_da_spec() {
        assertEquals(800, Imagem.FUNDO.larguraQuadro());
        assertEquals(600, Imagem.FUNDO.alturaQuadro());
        assertEquals(2, Imagem.PROPULSOR.quadros());
        assertEquals(4, Imagem.EXPLOSAO.quadros());
        assertEquals(24, Imagem.VIDA_CHEIA.larguraQuadro());
        assertEquals(24, Imagem.VAGA_LIVRE.alturaQuadro());
        assertEquals(48, Imagem.NAVE.larguraQuadro());
    }

    @Test
    void test_R3_2_animacao_e_uma_faixa_horizontal_de_quadros_iguais_em_tamanho() throws IOException {
        for (Imagem imagem : Imagem.values()) {
            if (imagem.quadros() > 1) {
                BufferedImage lida = ler(imagem);
                assertEquals(imagem.quadros() * imagem.larguraQuadro(), lida.getWidth(), imagem.name());
                assertEquals(imagem.alturaQuadro(), lida.getHeight(), imagem.name());
            }
        }
    }

    @Test
    void test_R3_3_elementos_tem_canto_transparente_e_fundo_e_terreno_sao_opacos() throws IOException {
        for (Imagem imagem : Imagem.values()) {
            BufferedImage lida = ler(imagem);
            int alfaCanto = (lida.getRGB(0, 0) >>> 24) & 0xFF;
            if (OPACAS.contains(imagem)) {
                assertEquals(255, alfaCanto, imagem.name() + " deve ser opaco");
                assertEquals(255, (lida.getRGB(lida.getWidth() - 1, lida.getHeight() - 1) >>> 24) & 0xFF,
                        imagem.name() + " deve ser opaco");
            } else {
                assertEquals(0, alfaCanto, imagem.name() + " deve ter canto transparente");
            }
        }
    }

    @Test
    void test_R3_4_quadros_de_cada_animacao_sao_diferentes_entre_si() throws IOException {
        for (Imagem imagem : Imagem.values()) {
            if (imagem.quadros() < 2) {
                continue;
            }
            BufferedImage lida = ler(imagem);
            for (int a = 0; a < imagem.quadros(); a++) {
                for (int b = a + 1; b < imagem.quadros(); b++) {
                    assertTrue(!quadroIgual(lida, imagem, a, b),
                            imagem.name() + ": quadros " + a + " e " + b + " iguais");
                }
            }
        }
    }

    private static boolean quadroIgual(BufferedImage lida, Imagem imagem, int a, int b) {
        int largura = imagem.larguraQuadro();
        for (int y = 0; y < imagem.alturaQuadro(); y++) {
            for (int x = 0; x < largura; x++) {
                if (lida.getRGB(a * largura + x, y) != lida.getRGB(b * largura + x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    void test_R3_1_todo_png_da_pasta_pertence_ao_catalogo() throws IOException {
        Path pasta = pastaAssets().resolve("imagens");
        try (var arquivos = Files.list(pasta)) {
            long total = arquivos.filter(p -> p.toString().endsWith(".png")).count();
            assertEquals(Imagem.values().length, total);
        }
    }
}
