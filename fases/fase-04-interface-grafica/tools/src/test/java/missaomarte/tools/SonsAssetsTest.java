package missaomarte.tools;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import missaomarte.gdx.catalogo.Som;
import org.junit.jupiter.api.Test;

/** Confere os WAV commitados em core/assets contra o catálogo. */
class SonsAssetsTest {

    private static Wav ler(Som som) throws IOException {
        Path arquivo = ImagensAssetsTest.pastaAssets().resolve(som.caminho());
        assertTrue(arquivo.toFile().isFile(), "arquivo ausente: " + som.caminho());
        return Wav.ler(arquivo);
    }

    @Test
    void test_R4_1_cada_som_e_wav_pcm_mono_22050_hz_16_bits() throws IOException {
        assertEquals(8, Som.values().length);
        for (Som som : Som.values()) {
            Wav wav = ler(som);
            assertEquals(1, wav.formato(), som.name());
            assertEquals(1, wav.canais(), som.name());
            assertEquals(22050, wav.taxa(), som.name());
            assertEquals(16, wav.bits(), som.name());
        }
    }

    @Test
    void test_R4_2_efeitos_duram_de_0_05_a_2_s_e_a_musica_de_7_a_9_s() throws IOException {
        for (Som som : Som.values()) {
            double duracao = ler(som).duracao();
            if (som.musica()) {
                assertTrue(duracao >= 7 && duracao <= 9, "música: " + duracao);
            } else {
                assertTrue(duracao >= 0.05 && duracao <= 2, som.name() + ": " + duracao);
            }
        }
    }

    @Test
    void test_R4_3_os_sete_efeitos_sao_diferentes_entre_si() throws IOException {
        List<Som> efeitos = new ArrayList<>();
        for (Som som : Som.values()) {
            if (!som.musica()) {
                efeitos.add(som);
            }
        }
        assertEquals(7, efeitos.size());
        for (int a = 0; a < efeitos.size(); a++) {
            for (int b = a + 1; b < efeitos.size(); b++) {
                assertFalse(Arrays.equals(ler(efeitos.get(a)).dados(), ler(efeitos.get(b)).dados()),
                        efeitos.get(a) + " e " + efeitos.get(b) + " iguais");
            }
        }
    }

    @Test
    void test_R4_4_cada_som_comeca_e_termina_em_silencio() throws IOException {
        for (Som som : Som.values()) {
            short[] amostras = ler(som).amostras();
            assertTrue(amostras.length > 0, som.name());
            assertEquals(0, amostras[0], som.name());
            assertEquals(0, amostras[amostras.length - 1], som.name());
        }
    }

    @Test
    void test_R4_5_musica_tem_pico_menor_que_o_de_todos_os_efeitos() throws IOException {
        int picoMusica = ler(Som.MUSICA).pico();
        assertTrue(picoMusica > 0);
        for (Som som : Som.values()) {
            if (!som.musica()) {
                assertTrue(picoMusica < ler(som).pico(), som.name());
            }
        }
    }

    @Test
    void test_R4_1_todo_wav_da_pasta_pertence_ao_catalogo() throws IOException {
        try (var arquivos = Files.list(ImagensAssetsTest.pastaAssets().resolve("sons"))) {
            assertEquals(Som.values().length, arquivos.filter(p -> p.toString().endsWith(".wav")).count());
        }
    }

    @Test
    void test_R4_1_wav_escrito_pode_ser_lido_de_volta(@org.junit.jupiter.api.io.TempDir Path tmp) throws IOException {
        short[] amostras = {0, 100, -100, 0};
        Path arquivo = tmp.resolve("x.wav");

        GeradorSons.gravarWav(amostras, arquivo);

        assertArrayEquals(amostras, Wav.ler(arquivo).amostras());
    }
}
