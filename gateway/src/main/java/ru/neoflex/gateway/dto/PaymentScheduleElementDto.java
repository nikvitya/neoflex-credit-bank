package ru.neoflex.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.gateway.util.DateConstant.DATE_PATTERN;

@Schema(description = "График ежемесячных платежей по кредиту")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {

    @Schema(description = "Номер платежа", example = "1")
    private Integer number;

    @Schema(description = "Дата платежа", example = "2025-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate date;

    @Schema(description = "Ежемесячный платеж", example = "2500")
    private BigDecimal totalPayment;

    @Schema(description = "Сумма процентов", example = "10")
    private BigDecimal interestPayment;

    @Schema(description = "Основной долг", example = "100000")
    private BigDecimal debtPayment;

    @Schema(description = "Остаток долга", example = "97500")
    private BigDecimal remainingDebt;
}

