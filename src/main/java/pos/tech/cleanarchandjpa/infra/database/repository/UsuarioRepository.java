package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCpf(String cpf);
    Optional<UsuarioEntity> findByCnpj(String cnpj);
}
