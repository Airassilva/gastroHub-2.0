package pos.tech.cleanarchandjpa.core.domain;

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
