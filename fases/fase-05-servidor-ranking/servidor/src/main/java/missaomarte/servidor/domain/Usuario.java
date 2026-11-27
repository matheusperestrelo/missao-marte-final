package missaomarte.servidor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String nomeNormalizado;

    @Column(nullable = false, length = 60)
    private String hashSenha;

    @Column(nullable = false)
    private Instant criadoEm;

    protected Usuario() {
    }

    public Usuario(String nome, String hashSenha, Instant criadoEm) {
        this.nome = nome;
        this.nomeNormalizado = NomeUsuario.normalizar(nome);
        this.hashSenha = hashSenha;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeNormalizado() {
        return nomeNormalizado;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    @Override
    public String toString() {
        return "Usuario[id=" + id + ", nome=" + nome + "]";
    }
}
