package challenge.fiap.dtos;

public record SearchResponse<T>(
        PageResponse<T> pageResponse,
        String orderby,
        boolean ascending
) {

}
