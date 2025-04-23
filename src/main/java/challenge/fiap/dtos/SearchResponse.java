package challenge.fiap.dtos;

import challenge.fiap.models.Failure;

public record SearchResponse<T>(
        PageResponse<T> pageResponse,
        String type,
        Integer startYear,
        Integer endYear,
        String orderBy,
        boolean ascending
) {

}
