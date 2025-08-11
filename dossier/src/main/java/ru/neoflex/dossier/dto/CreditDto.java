package ru.neoflex.dossier.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDto {
    @NotNull(message = "Credit amount is required")
    @Positive(message = "Credit amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Term is required")
    @Positive(message = "Term must be positive")
    private Integer term;

    @NotNull(message = "Monthly payment is required")
    @Positive(message = "Monthly payment must be positive")
    private BigDecimal monthlyPayment;

    @NotNull(message = "Rate is required")
    @Positive(message = "Rate must be positive")
    private BigDecimal rate;

    @NotNull(message = "PSK is required")
    private BigDecimal psk;

    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    @NotNull(message = "Payment schedule is required")
    private List<PaymentScheduleElementDto> paymentSchedule;
}
