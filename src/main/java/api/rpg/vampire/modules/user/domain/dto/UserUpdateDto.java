package api.rpg.vampire.modules.user.domain.dto;

import api.rpg.vampire.modules.role.Role;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserUpdateDto(
        @NotNull(message = "ID é um campo obrigatório!")
        UUID id,
        String username,
        String name,
        String discord,
        String phone,
        Boolean isActive,
        List<Role> roles
)
{
}
