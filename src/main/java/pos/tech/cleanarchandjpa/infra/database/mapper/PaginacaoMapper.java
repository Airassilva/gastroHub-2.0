package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.PaginacaoOutput;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioResponseDTO;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginacaoMapper {

    public static <T> PaginacaoOutput<T> fromDomain(PaginacaoResult<T> result) {
        return new PaginacaoOutput<>(
                result.content(),
                result.page(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    public static PaginacaoOutput<UsuarioResponseDTO> fromDto(PaginacaoResult<Usuario> result) {
        List<UsuarioResponseDTO> dtos = result.content().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail()))
                .toList();
        return new PaginacaoOutput<>(
                dtos,
                result.page(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }
}
