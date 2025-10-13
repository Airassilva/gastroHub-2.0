package pos.tech.cleanarchandjpa.core.domain;

public record ParametrosPag(
        int page,
        int pageSize,
        String sortBy,
        String sortDirection
) {
    public ParametrosPag {
        if (page < 0) throw new IllegalArgumentException("Page não pode ser negativa");
        if (pageSize < 1 || pageSize > 100) throw new IllegalArgumentException("PageSize deve estar entre 1 e 100");
    }
}
