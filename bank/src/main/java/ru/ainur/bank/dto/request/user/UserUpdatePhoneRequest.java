package ru.ainur.bank.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdatePhoneRequest {
    @Schema(description = "Новый телефонный номер", example = "+79528125252")
    @Size(min = 2, max = 16, message = "Телефонный номер должен содержать от 2 до 16 символов")
    @NotBlank(message = "Телефонный номер не может быть пустыми")
    private String phone;
}
