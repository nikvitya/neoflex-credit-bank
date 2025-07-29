package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "График ежемесячных платежей по кредиту")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {

    @Schema(description = "Номер платежа", example = "10")
    private Integer number;

    @Schema(description = "Дата платежа", example = "2025-06-28")
    private LocalDate date;

    @Schema(description = "Ежемесячный платеж", example = "20000")
    private BigDecimal totalPayment;

    @Schema(description = "Сумма процентов", example = "100")
    private BigDecimal interestPayment;

    @Schema(description = "Основной долг", example = "10000")
    private BigDecimal debtPayment;

    @Schema(description = "Остаток долга", example = "250000")
    private BigDecimal remainingDebt;
}
