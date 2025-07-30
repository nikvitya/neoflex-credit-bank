package ru.neoflex.deal.model.jsonb;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AppliedOffer {
    private UUID statementId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
