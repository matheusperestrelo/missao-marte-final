package missaomarte.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import missaomarte.domain.ElementoMapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Passageiro;
import missaomarte.domain.Posicao;
import missaomarte.domain.TipoElemento;

/** Nunca usa tamanho 1 no Médio/Difícil: a criação não termina (ponto d). */
class MissaoFactoryTest {

    private static long contar(Missao missao, TipoElemento tipo) {
        return missao.elementos().stream().filter(e -> e.tipo() == tipo).count();
    }

    @ParameterizedTest
    @EnumSource(Dificuldade.class)
    @DisplayName("R4.1 quantidades e capacidade da dificuldade")
    void r4_1_quantidadesDaDificuldade(Dificuldade d) {
        Missao missao = new MissaoFactory(new Random(42)).criar(d, 5);

        long passageiros = contar(missao, TipoElemento.PROFESSOR)
                + contar(missao, TipoElemento.ENGENHEIRO) + contar(missao, TipoElemento.ASTRONAUTA);
        assertEquals(d.passageiros(), passageiros);
        assertEquals(d.passageiros(), missao.getTotal());
        assertEquals(d.asteroides(), contar(missao, TipoElemento.ASTEROIDE));
        assertEquals(d.inimigos(), contar(missao, TipoElemento.INIMIGO));
        assertEquals(d.capacidadeNave(), missao.getNave().getCapacidade());
    }

    @Test
    @DisplayName("R4.2 objetos em células diferentes e fora da base, para várias sementes e tamanhos")
    void r4_2_semSobreposicaoEForaDaBase() {
        for (long semente = 0; semente < 50; semente++) {
            for (int tamanho = 2; tamanho <= 6; tamanho++) {
                for (Dificuldade d : Dificuldade.values()) {
                    Missao missao = new MissaoFactory(new Random(semente)).criar(d, tamanho);
                    Set<Posicao> ocupadas = new HashSet<>();
                    for (ElementoMapa e : missao.elementos()) {
                        if (e.tipo() == TipoElemento.NAVE || e.tipo() == TipoElemento.BASE) {
                            continue;
                        }
                        assertFalse(e.posicao().equals(Posicao.ORIGEM), "objeto na base");
                        assertTrue(missao.getMapa().contem(e.posicao()), "objeto fora do mapa");
                        assertTrue(ocupadas.add(e.posicao()), "duas coisas na mesma célula");
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("R4.3 sequência de nomes e tipos dos passageiros")
    void r4_3_sequenciaDePassageiros() {
        Missao missao = new MissaoFactory(new Random(7)).criar(Dificuldade.MEDIO, 5);

        List<Passageiro> passageiros = missao.getPassageirosNoMapa();

        assertEquals(List.of("Dr. Silva", "Eng. Rosa", "Dr. Lima", "Eng. Carlos", "Ast. Maria"),
                passageiros.stream().map(Passageiro::getNome).collect(Collectors.toList()));
        assertEquals(List.of(TipoElemento.PROFESSOR, TipoElemento.ENGENHEIRO, TipoElemento.PROFESSOR,
                        TipoElemento.ENGENHEIRO, TipoElemento.ASTRONAUTA),
                passageiros.stream().map(Passageiro::tipo).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("R4.4 mesma semente, mesmo cenário")
    void r4_4_mesmaSementeMesmoCenario() {
        Missao a = new MissaoFactory(new Random(123)).criar(Dificuldade.DIFICIL, 5);
        Missao b = new MissaoFactory(new Random(123)).criar(Dificuldade.DIFICIL, 5);

        assertEquals(descrever(a), descrever(b));
    }

    private static List<String> descrever(Missao missao) {
        return missao.elementos().stream()
                .map(e -> e.tipo() + "@" + e.posicao())
                .collect(Collectors.toList());
    }
}
