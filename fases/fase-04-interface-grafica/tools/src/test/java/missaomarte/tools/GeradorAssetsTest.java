package missaomarte.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import missaomarte.gdx.catalogo.Imagem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GeradorAssetsTest {

    @Test
    void test_R2_2_gera_todas_as_imagens_do_catalogo_criando_as_pastas(@TempDir Path tmp) throws IOException {
        Path saida = tmp.resolve("nova/assets");

        GeradorAssets.gerar(saida);

        for (Imagem imagem : Imagem.values()) {
            assertTrue(saida.resolve(imagem.caminho()).toFile().isFile(), imagem.caminho());
        }
    }

    @Test
    void test_R2_3_duas_execucoes_produzem_os_mesmos_pixels(@TempDir Path tmp) throws IOException {
        Path a = tmp.resolve("a");
        Path b = tmp.resolve("b");

        GeradorAssets.gerar(a);
        GeradorAssets.gerar(b);

        for (Imagem imagem : Imagem.values()) {
            assertMesmosPixels(a.resolve(imagem.caminho()), b.resolve(imagem.caminho()));
        }
    }

    @Test
    void test_R2_4_assets_commitados_sao_iguais_ao_que_o_gerador_produz(@TempDir Path tmp) throws IOException {
        GeradorAssets.gerar(tmp);

        for (Imagem imagem : Imagem.values()) {
            assertMesmosPixels(tmp.resolve(imagem.caminho()),
                    ImagensAssetsTest.pastaAssets().resolve(imagem.caminho()));
        }
    }

    static void assertMesmosPixels(Path esperado, Path atual) throws IOException {
        BufferedImage x = ImageIO.read(esperado.toFile());
        BufferedImage y = ImageIO.read(atual.toFile());
        assertEquals(x.getWidth(), y.getWidth(), atual.toString());
        assertEquals(x.getHeight(), y.getHeight(), atual.toString());
        for (int py = 0; py < x.getHeight(); py++) {
            for (int px = 0; px < x.getWidth(); px++) {
                assertEquals(x.getRGB(px, py), y.getRGB(px, py), atual + " em " + px + "," + py);
            }
        }
    }
}
