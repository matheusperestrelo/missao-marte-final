package missaomarte.gdx.catalogo;

import java.util.Map;

/**
 * Catálogo das imagens do jogo: caminho em {@code core/assets}, quadros e
 * dimensões de cada quadro. Fonte única para o gerador, os testes e o jogo.
 * Não usa nada do libGDX.
 */
public enum Imagem {
    FUNDO("fundo", 1, 800, 600, false),
    TERRENO("terreno", 1, 48, 48, false),
    NAVE("nave", 1, 48, 48, false),
    PROPULSOR("propulsor", 2, 48, 48, true),
    BASE("base", 2, 48, 48, true),
    INIMIGO("inimigo", 2, 48, 48, true),
    ASTEROIDE("asteroide", 1, 48, 48, false),
    PROFESSOR("professor", 1, 48, 48, false),
    ENGENHEIRO("engenheiro", 1, 48, 48, false),
    ASTRONAUTA("astronauta", 1, 48, 48, false),
    GENERICA("generica", 1, 48, 48, false),
    EXPLOSAO("explosao", 4, 48, 48, false),
    VIDA_CHEIA("vida_cheia", 1, 24, 24, false),
    VIDA_VAZIA("vida_vazia", 1, 24, 24, false),
    VAGA_OCUPADA("vaga_ocupada", 1, 24, 24, false),
    VAGA_LIVRE("vaga_livre", 1, 24, 24, false);

    private static final Map<Character, Imagem> POR_SIMBOLO = Map.of(
            '@', NAVE,
            'P', PROFESSOR,
            'E', ENGENHEIRO,
            'T', ASTRONAUTA,
            '#', ASTEROIDE,
            'X', INIMIGO,
            'L', BASE);

    private final String caminho;
    private final int quadros;
    private final int larguraQuadro;
    private final int alturaQuadro;
    private final boolean emLaco;

    Imagem(String nome, int quadros, int larguraQuadro, int alturaQuadro, boolean emLaco) {
        this.caminho = "imagens/" + nome + ".png";
        this.quadros = quadros;
        this.larguraQuadro = larguraQuadro;
        this.alturaQuadro = alturaQuadro;
        this.emLaco = emLaco;
    }

    /** Caminho relativo a {@code core/assets}. */
    public String caminho() {
        return caminho;
    }

    public int quadros() {
        return quadros;
    }

    public int larguraQuadro() {
        return larguraQuadro;
    }

    public int alturaQuadro() {
        return alturaQuadro;
    }

    public boolean emLaco() {
        return emLaco;
    }

    /** Imagem do elemento com este símbolo; desconhecido devolve {@link #GENERICA}. */
    public static Imagem doSimbolo(char simbolo) {
        return POR_SIMBOLO.getOrDefault(simbolo, GENERICA);
    }
}
