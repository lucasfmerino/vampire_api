package api.rpg.vampire.modules.user.domain.dto;

import api.rpg.vampire.modules.role.Role;
import api.rpg.vampire.modules.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserDisplayDto(
        UUID id,
        String username,
        String email,
        String name,
        String discord,
        String phone,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        List<Role> roles
)
{
    public UserDisplayDto(User user)
    {
        this(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getDiscord(),
                user.getPhone(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt(),
                user.getRoles()
        );
    }
}
