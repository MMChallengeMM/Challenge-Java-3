package challenge.fiap.dtos;

import java.util.List;

public record PageDto<T>(
        int page,
        int pageSize,
        int totalItens,
        List<T> itens) {
}
