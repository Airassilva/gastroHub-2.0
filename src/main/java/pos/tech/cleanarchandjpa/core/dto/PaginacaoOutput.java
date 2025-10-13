package pos.tech.cleanarchandjpa.core.dto;

import java.util.List;

public class PaginacaoOutput<T> {
    List<T> content;
    int page;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean hasNext;
    boolean hasPrevious;

    public PaginacaoOutput(List<T> content, int page, int i, long l, int i1, boolean b, boolean b1) {
        this.content = content;
        this.page = page;
        this.pageSize = i;
        this.totalElements = l;
        this.totalPages = i1;
        this.hasNext = b;
        this.hasPrevious = b1;
    }
}
