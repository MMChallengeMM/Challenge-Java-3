package challenge.fiap.dtos;

import java.util.List;

public record SearchDto<T>(
        int page,
        int foundItens,
        int pageSize,
        List<T> data
){}
