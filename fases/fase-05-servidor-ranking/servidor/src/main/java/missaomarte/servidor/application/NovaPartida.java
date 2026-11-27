package missaomarte.servidor.application;

import missaomarte.servidor.domain.Dificuldade;
import missaomarte.servidor.domain.Resultado;

/** Dados já validados de uma partida a registrar. */
public record NovaPartida(Dificuldade dificuldade, Resultado resultado, int pontuacao,
        int duracaoSegundos, int movimentos) {
}
