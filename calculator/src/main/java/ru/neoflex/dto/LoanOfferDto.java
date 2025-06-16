package ru.neoflex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Кредитное предложение")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfferDto {

    @Schema(description = "Уникальный идентификатор заявки")
    private UUID statementId;

    @Schema(description = "Запрошенная сумма кредита", example = "1000000")
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма кредита (с учетом страховки)", example = "110000.55")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "3060.00")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка по кредиту", example = "7")
    private BigDecimal rate;

    @Schema(description = "Включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Наличие зарплатного клиента в банке", example = "true")
    private Boolean isSalaryClient;
}
