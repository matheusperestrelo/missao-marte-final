package missaomarte.gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import missaomarte.application.EventoJogo;
import missaomarte.application.ResultadoTurno;
import missaomarte.domain.Missao;

/**
 * Traduz os eventos do turno nas frases do painel: as mesmas do console da
 * Fase 2, sem emoji (a fonte padrão não os tem) e sem linhas de separador.
 */
public final class MensagensPainel {

    public static final String ALERTA_RESGATADOS =
            "ALERTA: Todos os passageiros resgatados! Retorne para a Plataforma de Pouso 'L' em (0,0)"
                    + " para completar a missão.";

    private MensagensPainel() {
    }

    public static List<String> traduzir(ResultadoTurno resultado) {
        List<String> frases = new ArrayList<>();
        for (EventoJogo evento : resultado.eventos()) {
            switch (evento.tipo()) {
                case EMBARQUE:
                    frases.add("Passageiro " + evento.passageiro()
                            + " embarcado com sucesso! +" + evento.valor() + " pontos!");
                    break;
                case EMBARQUE_RECUSADO_SEM_PASSAGEIRO:
                    frases.add("Nenhum passageiro nesta posição.");
                    break;
                case EMBARQUE_RECUSADO_NAVE_CHEIA:
                    frases.add("Nave cheia! Não há espaço para mais passageiros.");
                    break;
                case ENTREGA:
                    frases.add(evento.valor() + " passageiro(s) entregue(s) na base!");
                    break;
                case COLISAO:
                    if (evento.valor() > 0) {
                        frases.add("Alerta! Colisão detectada! Vidas restantes: " + evento.valor());
                    }
                    break;
                case DERROTA_NAVE_DESTRUIDA:
                    frases.add("GAME OVER! A nave foi destruída.");
                    break;
                case DERROTA_PONTUACAO_ZERADA:
                    frases.add("Combustível/Pontuação zerada! Missão perdida.");
                    break;
                case VITORIA:
                    frases.add("DECOLAGEM AUTORIZADA! Nave acoplada à plataforma em (0,0).");
                    frases.add("Retornando à órbita marciana com todos os passageiros. Missão cumprida!");
                    break;
                case MOVIMENTO:
                default:
                    break;
            }
        }
        return frases;
    }

    /** Aviso de "todos resgatados" (mesma condição do console): partida aberta, mapa vazio, gente a bordo. */
    public static Optional<String> alerta(Missao missao, boolean encerrada) {
        if (!encerrada && missao.getRestantes() == 0 && missao.getABordo() > 0) {
            return Optional.of(ALERTA_RESGATADOS);
        }
        return Optional.empty();
    }
}
