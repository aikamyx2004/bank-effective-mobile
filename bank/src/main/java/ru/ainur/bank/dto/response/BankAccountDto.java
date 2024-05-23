package ru.ainur.bank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Банковский счет")
public class BankAccountDto {
    private Long id;
    private BigDecimal balance;
    private BigDecimal initialDeposit;
}
