package missaomarte.application;

import missaomarte.domain.Direcao;

public enum Acao {
    CIMA(Direcao.CIMA),
    BAIXO(Direcao.BAIXO),
    ESQUERDA(Direcao.ESQUERDA),
    DIREITA(Direcao.DIREITA),
    EMBARCAR(null),
    SAIR(null),
    INVALIDA(null);

    private final Direcao direcao;

    Acao(Direcao direcao) {
        this.direcao = direcao;
    }

    /** Converte o texto digitado considerando só o primeiro caractere. */
    public static Acao deTexto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return INVALIDA;
        }
        switch (Character.toLowerCase(texto.charAt(0))) {
            case 'w': return CIMA;
            case 's': return BAIXO;
            case 'a': return ESQUERDA;
            case 'd': return DIREITA;
            case 'c': return EMBARCAR;
            case 'q': return SAIR;
            default: return INVALIDA;
        }
    }

    /** Direção do movimento, ou nulo para as ações que não são de movimento. */
    public Direcao direcao() {
        return direcao;
    }

    public boolean ehMovimento() {
        return direcao != null;
    }
}
