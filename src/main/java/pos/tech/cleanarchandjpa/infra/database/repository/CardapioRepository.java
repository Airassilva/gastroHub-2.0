package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pos.tech.cleanarchandjpa.infra.database.entity.CardapioEntity;

import java.util.UUID;

public interface CardapioRepository extends JpaRepository<CardapioEntity, UUID> {
    Page<CardapioEntity> findAllByRestauranteId(UUID restauranteId, Pageable parametrosPag);
}
