package api.rpg.vampire.modules.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserPasswordUpdateDto(
        @NotNull(message = "ID é um campo obrigatório!")
        UUID id,

//        @NotBlank(message = "Username é um campo obrigatório!")
//        String username,

        @NotNull(message = "A antiga senha é um campo obrigatório!")
        String oldPassword,

        @NotNull(message = "Senha é um campo obrigatório!")
        @Size(min = 6, max = 22, message = "A senha deve ter no mínimo 6 caracteres e no máximo 22 caracteres")
        String newPassword
)
{
}
