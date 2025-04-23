package challenge.fiap.dtos;

import java.util.HashMap;

public record SearchResponse<T>(
        PageResponse<T> pageResponse,
        HashMap<String, Object> filters,
        String orderBy,
        boolean ascending
) {

}
