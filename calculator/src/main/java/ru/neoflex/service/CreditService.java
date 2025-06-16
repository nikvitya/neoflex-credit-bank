package ru.neoflex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dto.CreditDto;
import ru.neoflex.dto.PaymentScheduleElementDto;
import ru.neoflex.dto.ScoringDataDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditService {
    private final ScoringService scoringService;
    private final AnnuityCalculatorService annuityCalculatorService;
    private final RateCalculatorService rateCalculatorService;

    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        log.info("Validation full data and calculation credit parameters {}", scoringData);

        scoringService.scoring(scoringData);

        var rate = rateCalculatorService.calculateRate(
                scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        rate = rateCalculatorService.calculateFinalRate(
                scoringData, rate);

        var amount = annuityCalculatorService.calculateTotalAmount(
                scoringData.getAmount(), scoringData.getIsInsuranceEnabled());

        var monthlyPayment = annuityCalculatorService.calculateMonthlyPayment(
                amount, scoringData.getTerm(), rate);

        List<PaymentScheduleElementDto> paymentSchedule = annuityCalculatorService.calculatePaymentSchedule(
                amount, scoringData.getTerm(), rate, monthlyPayment);

        var psk = annuityCalculatorService.calculatePsk(
                paymentSchedule, scoringData.getAmount(), scoringData.getTerm());

        return CreditDto.builder()
                .amount(amount)
                .term(scoringData.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }
}
