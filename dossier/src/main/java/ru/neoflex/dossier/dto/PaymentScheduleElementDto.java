package ru.neoflex.dossier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.dossier.util.DateConstant.DATE_PATTERN;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {
    private Integer number;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}
