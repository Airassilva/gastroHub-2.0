package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;

import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<EnderecoEntity, UUID> {
}
