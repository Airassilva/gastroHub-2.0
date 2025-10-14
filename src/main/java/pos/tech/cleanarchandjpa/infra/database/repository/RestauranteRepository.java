package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pos.tech.cleanarchandjpa.infra.database.entity.RestauranteEntity;

import java.util.UUID;

public interface RestauranteRepository extends JpaRepository<RestauranteEntity, UUID> {
    Page<RestauranteEntity> findAll(Pageable pageable);
}
