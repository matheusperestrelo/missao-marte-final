package missaomarte.gdx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import missaomarte.application.Acao;
import missaomarte.application.Dificuldade;
import missaomarte.application.GameSession;
import missaomarte.application.ranking.RankingEntry;
import missaomarte.domain.Asteroide;
import missaomarte.domain.Mapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Posicao;
import missaomarte.domain.Professor;
import missaomarte.gdx.Dubles.RankingEmMemoria;
import missaomarte.gdx.Dubles.RankingQueFalha;

/**
 * Cenários montados à mão (sem sorteio): a missão de vitória tem um professor em (1,0),
 * a de derrota tem um asteroide em (1,0). Nenhum teste abre janela ou usa OpenGL.
 */
class ControlePartidaTest {

    private final RankingEmMemoria repo = new RankingEmMemoria();

    // ---------- cenários ----------

    private static Missao missaoDeVitoria() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));
        missao.adicionarPassageiro(new Professor("Dr. Silva", new Posicao(1, 0)));
        return missao;
    }

    private static Missao missaoDeDerrota() {
        Missao missao = new Missao(new Mapa(5), new Nave(4));
        missao.adicionarPassageiro(new Professor("Dr. Silva", new Posicao(3, 3)));
        missao.adicionarAsteroide(new Asteroide(new Posicao(1, 0)));
        return missao;
    }

    private ControlePartida controle(Missao missao) {
        GameSession sessao = Dubles.sessao(missao, Dificuldade.FACIL);
        return new ControlePartida(sessao, Dubles.servico(repo));
    }

    private static void jogar(ControlePartida c, Acao... acoes) {
        for (Acao acao : acoes) {
            c.tecla(Optional.of(acao));
        }
    }

    /** Direita, embarcar, esquerda: entrega o professor na base e vence com 38 pontos. */
    private static void vencer(ControlePartida c) {
        jogar(c, Acao.DIREITA, Acao.EMBARCAR, Acao.ESQUERDA);
    }

    /** Move para o asteroide e fica em cima dele até perder as 3 vidas. */
    private static void perder(ControlePartida c) {
        jogar(c, Acao.DIREITA, Acao.EMBARCAR, Acao.EMBARCAR);
    }

    // ---------- R1.7: tecla ignorada ----------

    @Test
    @DisplayName("R1.7 tecla sem ação não gasta turno: pontos, movimentos e mensagens não mudam")
    void r1_7_teclaVaziaNaoGastaTurno() {
        ControlePartida c = controle(missaoDeVitoria());
        jogar(c, Acao.DIREITA, Acao.EMBARCAR);
        List<String> antes = c.mensagens();
        int pontos = c.sessao().getPontos();
        int movimentos = c.sessao().getMovimentos();

        c.tecla(Optional.empty());
        c.tecla(Optional.empty());

        assertEquals(pontos, c.sessao().getPontos());
        assertEquals(movimentos, c.sessao().getMovimentos());
        assertEquals(antes, c.mensagens());
        assertEquals(new Posicao(1, 0), c.sessao().getMissao().getNave().posicao());
    }

    @Test
    @DisplayName("R1.1 a ação chega à sessão: um turno por tecla")
    void r1_umTurnoPorTecla() {
        ControlePartida c = controle(missaoDeVitoria());

        jogar(c, Acao.DIREITA);

        assertEquals(1, c.sessao().getMovimentos());
        assertEquals(29, c.sessao().getPontos());
        assertEquals(new Posicao(1, 0), c.sessao().getMissao().getNave().posicao());
    }

    // ---------- R4: mensagens e alerta ----------

    @Test
    @DisplayName("R4.2 e R4.3 as mensagens são as do último turno; um movimento simples as apaga")
    void r4_3_mensagensTrocamACadaTurno() {
        ControlePartida c = controle(missaoDeVitoria());
        jogar(c, Acao.DIREITA);
        assertTrue(c.mensagens().isEmpty(), "movimento sem outro evento não gera mensagem");

        jogar(c, Acao.EMBARCAR);
        assertEquals(List.of("Passageiro Dr. Silva embarcado com sucesso! +10 pontos!"), c.mensagens());

        jogar(c, Acao.ESQUERDA);
        assertFalse(c.mensagens().contains("Passageiro Dr. Silva embarcado com sucesso! +10 pontos!"));
        assertTrue(c.mensagens().get(0).startsWith("1 passageiro(s) entregue(s)"));
    }

    @Test
    @DisplayName("R4.4 alerta quando o último passageiro embarca e a partida continua")
    void r4_4_alertaDeResgatados() {
        ControlePartida c = controle(missaoDeVitoria());
        assertEquals(Optional.empty(), c.alerta());

        jogar(c, Acao.DIREITA, Acao.EMBARCAR);

        assertTrue(c.alerta().orElseThrow().startsWith("ALERTA: Todos os passageiros resgatados!"));

        jogar(c, Acao.ESQUERDA);

        assertEquals(Optional.empty(), c.alerta(), "depois da vitória não há mais alerta");
    }

    // ---------- R5.1: vitória ----------

    @Test
    @DisplayName("R5.1 vitória registra a partida no ranking uma única vez, no turno da vitória")
    void r5_1_vitoriaRegistraUmaVez() {
        ControlePartida c = controle(missaoDeVitoria());
        jogar(c, Acao.DIREITA, Acao.EMBARCAR);
        assertEquals(0, repo.salvamentos, "antes da vitória não registra");

        jogar(c, Acao.ESQUERDA);

        assertTrue(c.terminada());
        assertEquals(1, repo.salvamentos);
        assertEquals(1, repo.entradas.size());
        assertEquals(new RankingEntry("Ana", 38, Dificuldade.FACIL, 1, "2026-11-20 14:30:00", 0),
                repo.entradas.get(0));
        assertTrue(c.entrouNoRanking());
        assertEquals(Optional.of("Pressione Enter para continuar"), c.linhaFim());
        assertTrue(c.mensagens().contains("Retornando à órbita marciana com todos os passageiros. Missão cumprida!"));
    }

    @Test
    @DisplayName("R5.3 depois do fim todas as teclas são ignoradas e nada é registrado de novo")
    void r5_3_aposOFimSoEnter() {
        ControlePartida c = controle(missaoDeVitoria());
        vencer(c);
        int pontos = c.sessao().getPontos();
        int movimentos = c.sessao().getMovimentos();
        List<String> mensagens = c.mensagens();

        jogar(c, Acao.CIMA, Acao.BAIXO, Acao.EMBARCAR, Acao.SAIR, Acao.DIREITA);
        c.tecla(Optional.empty());

        assertEquals(pontos, c.sessao().getPontos());
        assertEquals(movimentos, c.sessao().getMovimentos());
        assertEquals(mensagens, c.mensagens());
        assertEquals(1, repo.salvamentos, "o registro não se repete");
        assertEquals(1, repo.entradas.size());
        assertFalse(c.abandonada(), "Q depois do fim não vira abandono");
    }

    @Test
    @DisplayName("R5.4 Enter depois da vitória leva ao Ranking com o resumo da partida")
    void r5_4_enterAposVitoria() {
        ControlePartida c = controle(missaoDeVitoria());
        vencer(c);

        assertEquals(DestinoFim.RANKING, c.enter());

        assertEquals(DestinoFim.RANKING, c.enter(), "Enter repetido não registra de novo");
        assertEquals(1, repo.salvamentos);
        assertEquals(38, c.resumo().pontos());
        assertEquals(2, c.resumo().movimentos());
        assertEquals(1, c.resumo().entregues());
        assertTrue(c.resumo().vitoria());
        assertTrue(c.entrouNoRanking());
    }

    @Test
    @DisplayName("R5.4 vitória que não bate a 10ª colocada: informa que não entrou")
    void r5_4_vitoriaQueNaoEntra() {
        for (int i = 0; i < 10; i++) {
            repo.entradas.add(new RankingEntry("P" + i, 100 + i, Dificuldade.FACIL, 4, "x", 1));
        }
        ControlePartida c = controle(missaoDeVitoria());

        vencer(c);

        assertEquals(DestinoFim.RANKING, c.enter());
        assertFalse(c.entrouNoRanking());
        assertEquals(0, repo.salvamentos);
        assertTrue(c.mensagens().stream().noneMatch(m -> m.startsWith("Não foi possível")));
    }

    // ---------- R5.2 e R5.5: derrota ----------

    @Test
    @DisplayName("R5.2 derrota mostra o motivo e o aviso de continuar, e não registra nada")
    void r5_2_derrotaNaoRegistra() {
        ControlePartida c = controle(missaoDeDerrota());

        perder(c);

        assertTrue(c.terminada());
        assertEquals(0, repo.salvamentos);
        assertTrue(repo.entradas.isEmpty());
        assertTrue(c.mensagens().contains("GAME OVER! A nave foi destruída."));
        assertEquals(Optional.of("Pressione Enter para continuar"), c.linhaFim());
        assertFalse(c.entrouNoRanking());
    }

    @Test
    @DisplayName("R5.2 a colisão com vidas restantes aparece antes do game over")
    void r5_2_colisaoComVidas() {
        ControlePartida c = controle(missaoDeDerrota());

        jogar(c, Acao.DIREITA);
        assertEquals(List.of("Alerta! Colisão detectada! Vidas restantes: 2"), c.mensagens());
        jogar(c, Acao.EMBARCAR);
        assertEquals(List.of("Nenhum passageiro nesta posição.",
                "Alerta! Colisão detectada! Vidas restantes: 1"), c.mensagens());
        assertFalse(c.terminada());
    }

    @Test
    @DisplayName("R5.5 Enter depois da derrota volta ao Menu")
    void r5_5_enterAposDerrota() {
        ControlePartida c = controle(missaoDeDerrota());
        perder(c);

        assertEquals(DestinoFim.MENU, c.enter());
    }

    @Test
    @DisplayName("R5.3 depois da derrota as teclas também são ignoradas")
    void r5_3_aposADerrota() {
        ControlePartida c = controle(missaoDeDerrota());
        perder(c);
        int movimentos = c.sessao().getMovimentos();

        jogar(c, Acao.ESQUERDA, Acao.CIMA);

        assertEquals(movimentos, c.sessao().getMovimentos());
    }

    @Test
    @DisplayName("Enter antes do fim não faz nada")
    void enterAntesDoFim() {
        ControlePartida c = controle(missaoDeVitoria());

        assertEquals(DestinoFim.NENHUM, c.enter());
        jogar(c, Acao.DIREITA);
        assertEquals(DestinoFim.NENHUM, c.enter());
        assertEquals(Optional.empty(), c.linhaFim());
    }

    // ---------- R5.6: abandono ----------

    @Test
    @DisplayName("R5.6 abandonar (Q ou Esc) não registra no ranking nem mostra tela de fim")
    void r5_6_abandono() {
        ControlePartida c = controle(missaoDeVitoria());
        jogar(c, Acao.DIREITA, Acao.EMBARCAR);

        jogar(c, Acao.SAIR);

        assertTrue(c.abandonada());
        assertFalse(c.terminada(), "abandono sai direto, sem a tela de fim");
        assertEquals(0, repo.salvamentos);
        assertEquals(Optional.empty(), c.linhaFim());
        assertEquals(DestinoFim.NENHUM, c.enter());
    }

    @Test
    @DisplayName("R5.6 depois de abandonar, outras teclas não fazem mais nada")
    void r5_6_aposAbandono() {
        ControlePartida c = controle(missaoDeVitoria());
        jogar(c, Acao.SAIR);
        int movimentos = c.sessao().getMovimentos();

        jogar(c, Acao.DIREITA, Acao.EMBARCAR);

        assertEquals(movimentos, c.sessao().getMovimentos());
        assertTrue(c.abandonada());
    }

    // ---------- R5.7: falha do ranking ----------

    @Test
    @DisplayName("R5.7 falha ao registrar a vitória: mensagem no painel, jogo segue e não entrou")
    void r5_7_falhaAoRegistrar() {
        GameSession sessao = Dubles.sessao(missaoDeVitoria(), Dificuldade.FACIL);
        ControlePartida c = new ControlePartida(sessao, Dubles.servico(new RankingQueFalha()));

        vencer(c);

        assertTrue(c.terminada());
        assertFalse(c.entrouNoRanking());
        assertTrue(c.mensagens().contains("Não foi possível salvar o ranking: disco cheio"));
        assertEquals(DestinoFim.RANKING, c.enter());
        // Tentar de novo depois do fim não repete o registro (a partida já está encerrada).
        jogar(c, Acao.CIMA);
        assertEquals(1, c.mensagens().stream().filter(m -> m.startsWith("Não foi possível")).count());
    }

    // ---------- R5.8: partida nova começa do zero ----------

    @Test
    @DisplayName("R5.8 um controle novo (sessão nova) começa sem mensagens e com registro permitido")
    void r5_8_partidaNovaComecaDoZero() {
        ControlePartida primeira = controle(missaoDeVitoria());
        vencer(primeira);
        assertEquals(1, repo.salvamentos);

        ControlePartida segunda = controle(missaoDeVitoria());

        assertTrue(segunda.mensagens().isEmpty());
        assertFalse(segunda.terminada());
        assertEquals(30, segunda.sessao().getPontos());
        vencer(segunda);
        assertEquals(2, repo.salvamentos);
        assertEquals(2, repo.entradas.size());
    }

    // ---------- estado do painel ----------

    @Test
    @DisplayName("R4.1 o estado do painel vem da sessão")
    void r4_1_estadoDoPainel() {
        ControlePartida c = controle(missaoDeVitoria());
        jogar(c, Acao.DIREITA);

        assertEquals("Pontos: 29", c.estado().get(3));
        assertEquals("Movimentos: 1", c.estado().get(7));
    }
}
