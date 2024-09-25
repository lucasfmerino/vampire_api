package api.rpg.vampire.modules.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(
        @NotBlank(message = "Username é um campo obrigatório!")
        String username,

        @NotBlank(message = "Password é um campo obrigatório!")
        @Size(min = 6, max = 22, message = "A senha deve ter no mínimo 6 caracteres e no máximo 22 caracteres")
        String password
)
{
}
