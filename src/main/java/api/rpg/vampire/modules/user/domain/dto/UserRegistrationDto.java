package api.rpg.vampire.modules.user.domain.dto;

import api.rpg.vampire.modules.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserRegistrationDto(
        @NotBlank(message = "Usuário é um campo obrigatório!")
        String username,

        @NotBlank(message = "E-mail é um campo obrigatório!")
        @Email(message = "E-mail inválido!")
        String email,

        @NotBlank(message = "Password é um campo obrigatório!")
        @Size(min = 6, max = 20, message = "A senha deve ter no mínimo 6 caracteres e no máximo 20 caracteres")
        String password,

        @NotBlank(message = "Nome é um campo obrigatório!")
        String name,

        String discord,

        String phone,

        @NotNull
        List<Role> roles
)
{
}
