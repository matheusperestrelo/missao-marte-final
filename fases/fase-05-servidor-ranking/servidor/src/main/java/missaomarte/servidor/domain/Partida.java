package missaomarte.servidor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "partidas", indexes = {
        @Index(name = "idx_partidas_usuario_data", columnList = "usuario_id, dataHora"),
        @Index(name = "idx_partidas_resultado_pontuacao", columnList = "resultado, pontuacao")})
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Dificuldade dificuldade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Resultado resultado;

    @Column(nullable = false)
    private int pontuacao;

    @Column(nullable = false)
    private int duracaoSegundos;

    @Column(nullable = false)
    private int movimentos;

    @Column(nullable = false)
    private Instant dataHora;

    protected Partida() {
    }

    public Partida(Usuario usuario, Dificuldade dificuldade, Resultado resultado, int pontuacao,
            int duracaoSegundos, int movimentos, Instant dataHora) {
        this.usuario = usuario;
        this.dificuldade = dificuldade;
        this.resultado = resultado;
        this.pontuacao = pontuacao;
        this.duracaoSegundos = duracaoSegundos;
        this.movimentos = movimentos;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public int getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public int getMovimentos() {
        return movimentos;
    }

    public Instant getDataHora() {
        return dataHora;
    }
}
