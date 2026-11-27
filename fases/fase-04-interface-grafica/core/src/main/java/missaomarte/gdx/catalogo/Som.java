package missaomarte.gdx.catalogo;

/**
 * Catálogo dos sons do jogo: caminho em {@code core/assets} e se é a música.
 * Fonte única para o gerador, os testes e o jogo. Não usa nada do libGDX.
 */
public enum Som {
    MOVER("mover"),
    SELECIONAR("selecionar"),
    EMBARCAR("embarcar"),
    ENTREGAR("entregar"),
    COLISAO("colisao"),
    VITORIA("vitoria"),
    DERROTA("derrota"),
    MUSICA("musica");

    private final String caminho;

    Som(String nome) {
        this.caminho = "sons/" + nome + ".wav";
    }

    /** Caminho relativo a {@code core/assets}. */
    public String caminho() {
        return caminho;
    }

    /** Verdadeiro só para a música de fundo; os demais são efeitos. */
    public boolean musica() {
        return this == MUSICA;
    }
}
