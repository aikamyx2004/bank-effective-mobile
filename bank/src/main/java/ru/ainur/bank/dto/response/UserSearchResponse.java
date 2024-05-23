package ru.ainur.bank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Ответ на запрос поиска пользователей")
public class UserSearchResponse {
    @Schema(description = "Список найденных пользователей")
    private List<UserDto> users;

    @Schema(description = "Общее количество найденных пользователей")
    private long totalUsers;

    @Schema(description = "Общее количество станиц из найденных пользователей")
    private long totalPages;
}
