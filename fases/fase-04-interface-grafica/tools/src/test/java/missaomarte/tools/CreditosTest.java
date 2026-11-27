package missaomarte.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import missaomarte.gdx.catalogo.Imagem;
import missaomarte.gdx.catalogo.Som;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CreditosTest {

    @Test
    void test_R2_5_creditos_listam_cada_arquivo_e_o_comando_de_geracao() throws IOException {
        String texto = Files.readString(ImagensAssetsTest.pastaAssets().resolve("CREDITOS.md"),
                StandardCharsets.UTF_8);

        for (Imagem imagem : Imagem.values()) {
            assertTrue(texto.contains(imagem.caminho()), "faltou nos créditos: " + imagem.caminho());
        }
        for (Som som : Som.values()) {
            assertTrue(texto.contains(som.caminho()), "faltou nos créditos: " + som.caminho());
        }
        assertTrue(texto.contains("programa próprio"));
        assertTrue(texto.contains("./gradlew :tools:gerarAssets"));
    }

    @Test
    void test_R2_5_creditos_commitados_sao_os_que_o_gerador_escreve(@TempDir Path tmp) throws IOException {
        GeradorAssets.gerar(tmp);

        assertEquals(Files.readString(tmp.resolve("CREDITOS.md"), StandardCharsets.UTF_8),
                Files.readString(ImagensAssetsTest.pastaAssets().resolve("CREDITOS.md"), StandardCharsets.UTF_8));
    }
}
