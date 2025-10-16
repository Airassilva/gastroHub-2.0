package pos.tech.cleanarchandjpa.infra.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    @EntityGraph(attributePaths = "tipoUsuario")
    Optional<UsuarioEntity>findById(UUID id);

    Optional<UsuarioEntity> findByCpf(String cpf);
    Optional<UsuarioEntity> findByCnpj(String cnpj);
    Page<UsuarioEntity> findAllByAtivoTrue(Pageable pageable);

    @EntityGraph(attributePaths = "tipoUsuario")
    @Query("SELECT u FROM UsuarioEntity u WHERE u.id = :usuarioId")
    Optional<UsuarioEntity> findByIdWithTipoUsuarioId(@Param("usuarioId") UUID usuarioId);

    void deleteById(UUID id);
}
