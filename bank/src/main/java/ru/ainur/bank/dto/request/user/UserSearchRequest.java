package ru.ainur.bank.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Запрос на поиск пользователей")
public class UserSearchRequest {

    @Schema(description = "Дата рождения для фильтрации пользователей, у которых дата рождения больше указанной")
    private LocalDate dateOfBirth;

    @Schema(description = "Телефон для фильтрации пользователей по точному совпадению")
    private String phone;

    @Schema(description = "ФИО для фильтрации пользователей по начальной части ФИО")
    private String fullName;

    @Schema(description = "Email для фильтрации пользователей по точному совпадению")
    private String email;
}
