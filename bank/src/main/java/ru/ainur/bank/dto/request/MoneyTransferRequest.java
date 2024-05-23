package ru.ainur.bank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Запрос на перевод денег")
public class MoneyTransferRequest {
    @Schema(description = "ID счета получателя", example = "2")
    @NotNull(message = "ID счета получателя не может быть пустым")
    private Long toAccountId;

    @Schema(description = "Сумма перевода", example = "100")
    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма перевода должна быть больше нуля")
    @NotNull(message = "Сумма перевода не может быть пустой")
    private BigDecimal amount;
}