package missaomarte.application.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Dificuldade;
import missaomarte.application.ResumoPartida;

/** Regras do Top 10, com repositório em memória e relógio fixo. */
class RankingServiceTest {

    private final Clock relogio = Clock.fixed(Instant.parse("2026-11-20T14:30:00Z"), ZoneOffset.UTC);
    private final RankingEmMemoria repo = new RankingEmMemoria();
    private final RankingService ranking = new RankingService(repo, relogio);

    private static ResumoPartida vitoria(String piloto, int pontos) {
        return new ResumoPartida(piloto, Dificuldade.MEDIO, pontos, 10, 5, 42, true);
    }

    private static RankingEntry entrada(String nome, int pontos) {
        return new RankingEntry(nome, pontos, Dificuldade.FACIL, 4, "2026-11-01 10:00:00", 30);
    }

    private List<String> nomes() {
        return repo.entradas.stream().map(RankingEntry::nome).collect(Collectors.toList());
    }

    private void encher(int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            assertTrue(ranking.registrar(vitoria("P" + i, i * 10)));
        }
    }

    // ---------- R2: listar ----------

    @Test
    @DisplayName("R2.1 top10 ordena do maior para o menor e corta em 10, mesmo se o repositório devolver mais")
    void r2_1_top10OrdenadoECortado() {
        for (int i = 1; i <= 12; i++) {
            repo.entradas.add(entrada("E" + i, i * 5));
        }

        List<RankingEntry> top = ranking.top10();

        assertEquals(10, top.size());
        assertEquals(60, top.get(0).pontuacao());
        assertEquals(15, top.get(9).pontuacao());
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).pontuacao() >= top.get(i).pontuacao());
        }
    }

    @Test
    @DisplayName("R2.2 sem entradas devolve lista vazia")
    void r2_2_vazio() {
        assertTrue(ranking.top10().isEmpty());
    }

    @Test
    @DisplayName("R2.3 recorde é a primeira entrada, ou nada se estiver vazio")
    void r2_3_recorde() {
        assertEquals(Optional.empty(), ranking.recorde());

        ranking.registrar(vitoria("A", 40));
        ranking.registrar(vitoria("B", 60));

        assertEquals("B", ranking.recorde().orElseThrow().nome());
    }

    // ---------- R3: registrar ----------

    @Test
    @DisplayName("R3.1 derrota e abandono não entram, informam false e não gravam")
    void r3_1_recusaPartidaSemVitoria() {
        assertFalse(ranking.registrar(new ResumoPartida("A", Dificuldade.MEDIO, 50, 1, 1, 1, false)));

        assertEquals(0, repo.salvamentos);
        assertTrue(repo.entradas.isEmpty());
    }

    @Test
    @DisplayName("R3.2 pontuação zero ou negativa não entra e não grava")
    void r3_2_recusaPontuacaoNaoPositiva() {
        assertFalse(ranking.registrar(vitoria("B", 0)));
        assertFalse(ranking.registrar(vitoria("C", -3)));

        assertEquals(0, repo.salvamentos);
    }

    @Test
    @DisplayName("R3.3 com menos de 10 entradas, vitória positiva entra e informa true")
    void r3_3_entraComMenosDeDez() {
        assertTrue(ranking.registrar(vitoria("A", 1)));

        assertEquals(1, repo.salvamentos);
        assertEquals(List.of("A"), nomes());
    }

    @Test
    @DisplayName("R3.4 lista cheia: igual ou menor que a 10ª é recusada; maior entra e a 10ª sai")
    void r3_4_listaCheiaSoAceitaMaiorQueODecimo() {
        encher(10); // 10, 20, ... 100; a 10ª colocada tem 10 pontos
        int gravacoes = repo.salvamentos;

        assertFalse(ranking.registrar(vitoria("Empate", 10)), "igual à 10ª é recusado");
        assertFalse(ranking.registrar(vitoria("Menor", 5)));
        assertEquals(gravacoes, repo.salvamentos, "recusados não gravam");
        assertEquals(10, repo.entradas.size());

        assertTrue(ranking.registrar(vitoria("Maior", 11)));

        assertEquals(10, repo.entradas.size());
        assertFalse(nomes().contains("P1"), "a antiga 10ª saiu");
        assertTrue(nomes().contains("Maior"));
    }

    @Test
    @DisplayName("R3.5 empate em pontos: a nova fica depois das antigas")
    void r3_5_empateDepoisDasAntigas() {
        ranking.registrar(vitoria("A", 30));
        ranking.registrar(vitoria("B", 50));
        ranking.registrar(vitoria("C", 30));

        assertEquals(List.of("B", "A", "C"), nomes());
    }

    @Test
    @DisplayName("R3.6 grava nome, pontos, dificuldade, entregues, tempo e data do relógio injetado")
    void r3_6_camposDaEntrada() {
        ranking.registrar(new ResumoPartida("Ana", Dificuldade.DIFICIL, 77, 12, 5, 61, true));

        assertEquals(new RankingEntry("Ana", 77, Dificuldade.DIFICIL, 5, "2026-11-20 14:30:00", 61),
                repo.entradas.get(0));
    }

    @Test
    @DisplayName("R3 nunca grava mais de 10, mesmo se o arquivo já tinha mais")
    void r3_nuncaGravaMaisDeDez() {
        for (int i = 1; i <= 12; i++) {
            repo.entradas.add(entrada("E" + i, i * 5));
        }

        assertTrue(ranking.registrar(vitoria("Novo", 1000)));

        assertEquals(10, repo.entradas.size());
        assertEquals("Novo", repo.entradas.get(0).nome());
    }

    // ---------- R4: limpar ----------

    @Test
    @DisplayName("R4.1 limpar apaga tudo e o top10 seguinte vem vazio")
    void r4_1_limpar() {
        ranking.registrar(vitoria("A", 40));
        assertEquals(1, ranking.top10().size());

        ranking.limpar();

        assertTrue(ranking.top10().isEmpty());
    }

    // ---------- R5: falha da persistência ----------

    @Test
    @DisplayName("R5.1 falha ao gravar ou limpar propaga RankingException com a mensagem original")
    void r5_1_falhaDoRepositorio() {
        RankingService comFalha = new RankingService(new RankingQueFalha(), relogio);

        RankingException aoSalvar = assertThrows(RankingException.class, () -> comFalha.registrar(vitoria("A", 10)));
        RankingException aoLimpar = assertThrows(RankingException.class, comFalha::limpar);

        assertEquals("disco cheio", aoSalvar.getMessage());
        assertEquals("disco cheio", aoLimpar.getMessage());
    }
}
