package missaomarte.servidor.application;

import missaomarte.servidor.domain.Partida;

/** A partida salva e a posição dela no ranking (nula quando não entrou). */
public record PartidaRegistrada(Partida partida, Integer posicaoRanking) {
}
