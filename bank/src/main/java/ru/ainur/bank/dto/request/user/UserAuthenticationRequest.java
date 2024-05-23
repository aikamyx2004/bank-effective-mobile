package ru.ainur.bank.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Запрос на авторизацию")
public class UserAuthenticationRequest {
    @Schema(description = "Имя пользователя", example = "Inoor")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String username;

    @Schema(description = "Пароль", example = "your_password")
    @NotBlank(message = "Пароль не может быть пустыми")
    @Size(min = 5, max = 255, message = "Длина пароля должна быть от 5 до  255 символов")
    private String password;
}
