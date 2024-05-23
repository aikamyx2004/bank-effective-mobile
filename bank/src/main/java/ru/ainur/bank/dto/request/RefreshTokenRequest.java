package ru.ainur.bank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на обновление токена")
public class RefreshTokenRequest {
    @Schema(description = "Токен для обновления", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjIyNTA2Nz...")
    private String token;
}
