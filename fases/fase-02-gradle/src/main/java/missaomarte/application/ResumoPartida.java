package missaomarte.application;

/** Dados finais de uma partida, usados pelo ranking e pelas estatísticas. */
public record ResumoPartida(String piloto, Dificuldade dificuldade, int pontos,
                            int movimentos, int entregues, long duracaoSegundos,
                            boolean vitoria) {
}
