package missaomarte.servidor.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    Page<Partida> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = "usuario")
    List<Partida> findByResultadoAndPontuacaoGreaterThan(Resultado resultado, int pontuacao);
}
