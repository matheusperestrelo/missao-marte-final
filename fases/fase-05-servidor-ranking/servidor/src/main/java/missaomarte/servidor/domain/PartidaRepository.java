package missaomarte.servidor.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    Page<Partida> findByUsuarioId(Long usuarioId, Pageable pageable);
}
