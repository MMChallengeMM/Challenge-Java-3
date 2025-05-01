package challenge.fiap.dtos;

import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    boolean deleted,
    int accessLevel,
    String sector
){}

