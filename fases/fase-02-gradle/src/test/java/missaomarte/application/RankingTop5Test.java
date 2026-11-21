package missaomarte.application;

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

import missaomarte.application.Dubles.RankingEmMemoria;
import missaomarte.application.Dubles.RankingQueFalha;

class RankingTop5Test {

    private final Clock relogio = Clock.fixed(Instant.parse("2026-11-20T14:30:00Z"), ZoneOffset.UTC);
    private final RankingEmMemoria repo = new RankingEmMemoria();
    private final RankingTop5 ranking = new RankingTop5(repo, relogio);

    private static ResumoPartida vitoria(String piloto, int pontos) {
        return new ResumoPartida(piloto, Dificuldade.MEDIO, pontos, 10, 5, 42, true);
    }

    private List<String> nomes() {
        return repo.entradas.stream().map(RankingEntry::nome).collect(Collectors.toList());
    }

    @Test
    @DisplayName("R6.2 derrota ou pontuação <= 0 não entram e não salvam")
    void r6_2_recusaDerrotaEPontuacaoNaoPositiva() {
        assertFalse(ranking.registrar(new ResumoPartida("A", Dificuldade.MEDIO, 50, 1, 1, 1, false)));
        assertFalse(ranking.registrar(vitoria("B", 0)));
        assertFalse(ranking.registrar(vitoria("C", -3)));
        assertEquals(0, repo.salvamentos);
    }

    @Test
    @DisplayName("R6.3 com menos de 5 aceita; com 5, só pontuação maior que a última")
    void r6_3_limiteDeCinco() {
        for (int i = 1; i <= 5; i++) {
            assertTrue(ranking.registrar(vitoria("P" + i, i * 10)));
        }
        assertFalse(ranking.registrar(vitoria("Empate", 10)), "igual à última é recusado");
        assertFalse(ranking.registrar(vitoria("Menor", 5)));
        assertTrue(ranking.registrar(vitoria("Maior", 11)));
        assertEquals(5, repo.entradas.size());
        assertFalse(nomes().contains("P1"));
    }

    @Test
    @DisplayName("R6.4 ordem decrescente, empate depois das antigas, máximo 5, data do Clock")
    void r6_4_ordemEmpateEData() {
        ranking.registrar(vitoria("A", 30));
        ranking.registrar(vitoria("B", 50));
        ranking.registrar(vitoria("C", 30));

        assertEquals(List.of("B", "A", "C"), nomes());
        RankingEntry primeira = repo.entradas.get(0);
        assertEquals("2026-11-20 14:30:00", primeira.dataHora());
        assertEquals(new RankingEntry("B", 50, Dificuldade.MEDIO, 5, "2026-11-20 14:30:00", 42), primeira);
    }

    @Test
    @DisplayName("R6.5 recorde e novo recorde")
    void r6_5_recorde() {
        assertEquals(Optional.empty(), ranking.recorde());
        assertFalse(ranking.seriaNovoRecorde(100), "sem recorde > 0 não há novo recorde");

        ranking.registrar(vitoria("A", 40));
        ranking.registrar(vitoria("B", 60));

        assertEquals("B", ranking.recorde().orElseThrow().nome());
        assertTrue(ranking.seriaNovoRecorde(61));
        assertFalse(ranking.seriaNovoRecorde(60));
    }

    @Test
    @DisplayName("listar lê o repositório e limpar esvazia")
    void listarELimpar() {
        ranking.registrar(vitoria("A", 40));
        assertEquals(1, ranking.listar().size());

        ranking.limpar();

        assertTrue(ranking.listar().isEmpty());
    }

    @Test
    @DisplayName("R6.6 falha ao salvar ou limpar propaga RankingException com a mensagem")
    void r6_6_falhaDoRepositorio() {
        RankingTop5 comFalha = new RankingTop5(new RankingQueFalha(), relogio);

        RankingException aoSalvar = assertThrows(RankingException.class, () -> comFalha.registrar(vitoria("A", 10)));
        RankingException aoLimpar = assertThrows(RankingException.class, comFalha::limpar);

        assertEquals("disco cheio", aoSalvar.getMessage());
        assertEquals("disco cheio", aoLimpar.getMessage());
    }
}
