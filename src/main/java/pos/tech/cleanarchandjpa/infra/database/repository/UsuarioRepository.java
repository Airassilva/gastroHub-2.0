package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity>findById(UUID id);
    Optional<UsuarioEntity> findByCpf(String cpf);
    Optional<UsuarioEntity> findByCnpj(String cnpj);
    Page<UsuarioEntity> findAllAtivo(Pageable pageable);
    void deleteById(UUID id);
}
