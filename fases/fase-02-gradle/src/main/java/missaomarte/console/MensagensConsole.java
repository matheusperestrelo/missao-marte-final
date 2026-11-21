package missaomarte.console;

import java.util.ArrayList;
import java.util.List;

import missaomarte.application.EventoJogo;
import missaomarte.application.ResultadoTurno;

/** Traduz os eventos de um turno nas frases do console. */
public class MensagensConsole {

    static final String SEPARADOR =
            "================================================================";

    public List<String> traduzir(ResultadoTurno resultado) {
        List<String> frases = new ArrayList<>();
        for (EventoJogo evento : resultado.eventos()) {
            switch (evento.tipo()) {
                case MOVIMENTO:
                    break;
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
                    frases.add("\n" + SEPARADOR);
                    frases.add("🚀 DECOLAGEM AUTORIZADA! Nave acoplada à plataforma em (0,0).");
                    frases.add("Retornando à órbita marciana com todos os passageiros. Missão cumprida!");
                    frases.add(SEPARADOR);
                    break;
                default:
                    break;
            }
        }
        return frases;
    }
}
