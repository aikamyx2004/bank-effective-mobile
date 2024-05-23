package ru.ainur.bank.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Запрос на регистрацию")
public class UserRegistrationRequest {
    @Schema(description = "Имя пользователя", example = "Inoor")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Пароль", example = "your_password")
    @NotBlank(message = "Пароль не может быть пустыми")
    @Size(min = 5, max = 255, message = "Длина пароля должна быть от 5 до  255 символов")
    private String password;

    @Schema(description = "ФИО", example = "Мухтаров Айнур Дамирович")
    @Size(min = 2, max = 255, message = "ФИО должно содержать от 2 до 255 символов")
    @NotBlank(message = "ФИО не может быть пустыми")
    private String fullName;

    @Schema(description = "Адрес электронной почты", example = "aikamyx2004@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Телефонный номер", example = "+79528125252")
    @Size(min = 2, max = 16, message = "Телефонный номер должен содержать от 2 до 16 символов")
    @NotBlank(message = "Телефонный номер не может быть пустыми")
    private String phone;

    @Schema(description = "Имя пользователя", example = "Inoor")
    @NotNull
    private LocalDate dateOfBirth;

    @Schema(description = "Депозит", example = "1000")
    @DecimalMin(value = "0.0")
    private BigDecimal initialDeposit;
}
