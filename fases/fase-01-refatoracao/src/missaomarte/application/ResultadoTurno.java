package missaomarte.application;

import java.util.List;

/** Resultado de um turno: eventos na ordem em que aconteceram. */
public record ResultadoTurno(List<EventoJogo> eventos, boolean comandoInvalido) {
}
