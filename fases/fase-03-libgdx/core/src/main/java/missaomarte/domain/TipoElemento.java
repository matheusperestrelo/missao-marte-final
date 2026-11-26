package missaomarte.domain;

public enum TipoElemento {
    NAVE('@', "Nave"),
    PROFESSOR('P', "Professor"),
    ENGENHEIRO('E', "Engenheiro"),
    ASTRONAUTA('T', "Astronauta"),
    ASTEROIDE('#', "Asteroide"),
    INIMIGO('X', "Inimigo"),
    BASE('L', "Plataforma de Pouso");

    private final char simbolo;
    private final String nome;

    TipoElemento(char simbolo, String nome) {
        this.simbolo = simbolo;
        this.nome = nome;
    }

    public char simbolo() {
        return simbolo;
    }

    public String nome() {
        return nome;
    }
}
