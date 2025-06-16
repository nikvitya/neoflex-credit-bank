package ru.neoflex.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.config.RateConfiguration;
import ru.neoflex.dto.PaymentScheduleElementDto;
import ru.neoflex.service.AnnuityCalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnnuityCalculatorServiceTest {
    @Mock
    private RateConfiguration rateConfiguration;
    @InjectMocks
    private AnnuityCalculatorService annuityCalculator;

    private BigDecimal amount = BigDecimal.valueOf(100000);
    private BigDecimal amountWihInsurance = BigDecimal.valueOf(105000.00);
    private BigDecimal rate = BigDecimal.valueOf(19);
    private int term = 2;
    private BigDecimal monthlyPayment = BigDecimal.valueOf(51190.61);
    private PaymentScheduleElementDto paymentScheduleElementDto0 = PaymentScheduleElementDto.builder()
            .number(0)
            .date(LocalDate.now())
            .totalPayment(BigDecimal.ZERO)
            .interestPayment(BigDecimal.ZERO)
            .debtPayment(BigDecimal.ZERO)
            .remainingDebt(amount)
            .build();
    private PaymentScheduleElementDto paymentScheduleElementDto1 = PaymentScheduleElementDto.builder()
            .number(1)
            .date(LocalDate.now().plusMonths(1))
            .totalPayment(monthlyPayment)
            .interestPayment(BigDecimal.valueOf(1583.33))
            .debtPayment(BigDecimal.valueOf(49607.28))
            .remainingDebt(BigDecimal.valueOf(50392.72))
            .build();
    private PaymentScheduleElementDto paymentScheduleElementDto2 = PaymentScheduleElementDto.builder()
            .number(2)
            .date(LocalDate.now().plusMonths(2))
            .totalPayment(monthlyPayment)
            .interestPayment(BigDecimal.valueOf(797.88))
            .debtPayment(BigDecimal.valueOf(50392.72))
            .remainingDebt(BigDecimal.ZERO)
            .build();
    private List<PaymentScheduleElementDto> paymentSchedule =
            List.of(paymentScheduleElementDto0, paymentScheduleElementDto1, paymentScheduleElementDto2);

    @Test
    void calculateTotalAmount() {
        Double insuranceRate = 5.00;
        when(rateConfiguration.getInsuranceRate()).thenReturn(insuranceRate);

        var currentAmount = annuityCalculator.calculateTotalAmount(amount, true);

        assertEquals(amountWihInsurance.setScale(2), currentAmount);
    }

    @Test
    void calculateMonthlyPayment() {
        var monthlyPaymentCurrent = annuityCalculator.calculateMonthlyPayment(amount, term, rate);

        assertEquals(monthlyPayment, monthlyPaymentCurrent);
    }

    @Test
    void calculatePaymentSchedule() {
        List<PaymentScheduleElementDto> currentSchedule = annuityCalculator.calculatePaymentSchedule(
                amount, term, rate, monthlyPayment);

        assertEquals(paymentSchedule.size(), currentSchedule.size());
        assertEquals(paymentSchedule.size(), currentSchedule.size());
        assertEquals(BigDecimal.valueOf(1583.33), currentSchedule.get(1).getInterestPayment());
        assertEquals(BigDecimal.valueOf(49607.28), currentSchedule.get(1).getDebtPayment());
        assertEquals(BigDecimal.valueOf(50392.72), currentSchedule.get(1).getRemainingDebt());
        assertEquals(BigDecimal.valueOf(797.88), currentSchedule.get(2).getInterestPayment());
        assertEquals(BigDecimal.valueOf(50392.72), currentSchedule.get(2).getDebtPayment());
        assertEquals(BigDecimal.ZERO, currentSchedule.get(2).getRemainingDebt());
    }

    @Test
    void calculatePsk() {
        var currentPsk = annuityCalculator.calculatePsk(paymentSchedule, amount, term);

        assertEquals(BigDecimal.valueOf(14.287), currentPsk);
    }
}