package pos.tech.cleanarchandjpa.core.dto.paginacao;

import java.util.List;

public record PaginacaoResult<T>(
        List<T> content,
        int page,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
