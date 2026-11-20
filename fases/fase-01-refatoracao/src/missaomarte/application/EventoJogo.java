package missaomarte.application;

/**
 * Algo que aconteceu no turno.
 * {@code passageiro} só é preenchido no EMBARQUE (nulo nos demais).
 * {@code valor} é pontos (EMBARQUE), quantidade (ENTREGA) ou vidas
 * restantes (COLISAO); 0 nos demais.
 */
public record EventoJogo(TipoEvento tipo, String passageiro, int valor) {
}
