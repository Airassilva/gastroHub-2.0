package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pos.tech.cleanarchandjpa.infra.database.entity.TipoUsuarioEntity;

import java.util.UUID;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, UUID> {
    Page<TipoUsuarioEntity> findAll(Pageable pageable);
    TipoUsuarioEntity findByUsuariosId(UUID id);
}
