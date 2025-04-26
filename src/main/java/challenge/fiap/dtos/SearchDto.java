package challenge.fiap.dtos;

import java.util.HashMap;

public record SearchDto<T>(
        PageDto<T> pageDto,
        HashMap<String, Object> filters,
        String orderBy,
        boolean ascending
) {

}
