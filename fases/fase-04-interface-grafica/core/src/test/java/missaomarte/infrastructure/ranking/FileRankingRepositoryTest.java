package missaomarte.infrastructure.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import missaomarte.application.Dificuldade;
import missaomarte.application.ranking.RankingEntry;

/** Só grava dentro da pasta temporária do JUnit (@TempDir). */
class FileRankingRepositoryTest {

    @TempDir
    Path pasta;

    @Test
    @DisplayName("R6.7 salvar e ler de volta, em ordem decrescente")
    void r6_7_salvarELer() {
        FileRankingRepository repo = new FileRankingRepository(pasta.resolve("ranking.json"));
        RankingEntry a = new RankingEntry("Ana", 30, Dificuldade.FACIL, 4, "2026-11-20 10:00:00", 15);
        RankingEntry b = new RankingEntry("Bia \"B\"", 50, Dificuldade.DIFICIL, 5, "2026-11-20 11:00:00", 20);

        repo.salvar(List.of(a, b));

        assertEquals(List.of(b, a), repo.carregar());
    }

    @Test
    @DisplayName("R6.7 arquivo inexistente lê como lista vazia")
    void r6_7_arquivoInexistente() {
        FileRankingRepository repo = new FileRankingRepository(pasta.resolve("nao-existe.json"));

        assertTrue(repo.carregar().isEmpty());
    }

    @Test
    @DisplayName("R6.7 limpar apaga o arquivo")
    void r6_7_limparApagaOArquivo() {
        Path arquivo = pasta.resolve("ranking.json");
        FileRankingRepository repo = new FileRankingRepository(arquivo);
        repo.salvar(List.of(new RankingEntry("Ana", 30, Dificuldade.FACIL, 4, "x", 1)));
        assertTrue(Files.exists(arquivo));

        repo.limpar();

        assertFalse(Files.exists(arquivo));
        assertTrue(repo.carregar().isEmpty());
    }

    @Test
    @DisplayName("R6.8 lê um ranking.json gravado pela Fase 0")
    void r6_8_formatoDaFase0() throws IOException {
        Path arquivo = pasta.resolve("ranking.json");
        String json = "[{\"name\":\"Fase0\",\"score\":42,\"dificuldade\":\"FACIL\",\"passageirosColetados\":4,"
                + "\"dataHora\":\"2026-11-01 10:00:00\",\"tempoJogo\":30},"
                + "{\"name\":\"Outro\",\"score\":77,\"dificuldade\":\"DIFICIL\",\"passageirosColetados\":5,"
                + "\"dataHora\":\"2026-11-02 09:30:00\",\"tempoJogo\":61}]";
        Files.write(arquivo, json.getBytes(StandardCharsets.UTF_8));

        List<RankingEntry> lidas = new FileRankingRepository(arquivo).carregar();

        assertEquals(List.of(
                new RankingEntry("Outro", 77, Dificuldade.DIFICIL, 5, "2026-11-02 09:30:00", 61),
                new RankingEntry("Fase0", 42, Dificuldade.FACIL, 4, "2026-11-01 10:00:00", 30)), lidas);
    }
}
