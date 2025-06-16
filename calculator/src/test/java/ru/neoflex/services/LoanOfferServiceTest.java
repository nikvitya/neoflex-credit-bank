package ru.neoflex.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.dto.LoanOfferDto;
import ru.neoflex.dto.LoanStatementRequestDto;
import ru.neoflex.service.AnnuityCalculatorService;
import ru.neoflex.service.LoanOfferService;
import ru.neoflex.service.RateCalculatorService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceTest {
    @Mock
    private AnnuityCalculatorService annuityCalculator;
    @Mock
    private RateCalculatorService rateCalculator;
    @InjectMocks
    private LoanOfferService loanOfferService;

    private BigDecimal amount = BigDecimal.valueOf(100000);
    private BigDecimal amountWihInsurance = BigDecimal.valueOf(105000);
    private int term = 12;
    private LoanStatementRequestDto loanStatement = LoanStatementRequestDto.builder()
            .amount(amount)
            .term(term)
            .build();
    private BigDecimal monthlyPayment1 = BigDecimal.valueOf(9215.66);
    private BigDecimal monthlyPayment2 = BigDecimal.valueOf(9526.74);
    private BigDecimal monthlyPayment3 = BigDecimal.valueOf(9168);
    private BigDecimal monthlyPayment4 = BigDecimal.valueOf(9477.12);
    private BigDecimal rate1 = BigDecimal.valueOf(19);
    private BigDecimal rate2 = BigDecimal.valueOf(16);
    private BigDecimal rate3 = BigDecimal.valueOf(18);
    private BigDecimal rate4 = BigDecimal.valueOf(15);

    @Test
    void calculateLoanOffers() {
        when(annuityCalculator.calculateTotalAmount(amount, true)).thenReturn(amount);
        when(rateCalculator.calculateRate(false, false)).thenReturn(rate1);
        when(annuityCalculator.calculateMonthlyPayment(amount, term, rate1)).thenReturn(monthlyPayment1);
        when(annuityCalculator.calculateTotalAmount(amount, true)).thenReturn(amountWihInsurance);
        when(rateCalculator.calculateRate(true, false)).thenReturn(rate2);
        when(annuityCalculator.calculateMonthlyPayment(amountWihInsurance, term, rate2)).thenReturn(monthlyPayment2);
        when(annuityCalculator.calculateTotalAmount(amount, true)).thenReturn(amountWihInsurance);
        when(rateCalculator.calculateRate(true, true)).thenReturn(rate4);
        when(annuityCalculator.calculateMonthlyPayment(amountWihInsurance, term, rate4)).thenReturn(monthlyPayment4);
        when(annuityCalculator.calculateTotalAmount(amount, false)).thenReturn(amount);
        when(rateCalculator.calculateRate(false, true)).thenReturn(rate3);
        when(annuityCalculator.calculateMonthlyPayment(amount, term, rate3)).thenReturn(monthlyPayment3);

        List<LoanOfferDto> loanOffers = loanOfferService.calculateLoanOffers(loanStatement);

        assertEquals(4, loanOffers.size());
        assertEquals(rate1, loanOffers.get(0).getRate());
        assertEquals(rate3, loanOffers.get(1).getRate());
        assertEquals(rate2, loanOffers.get(2).getRate());
        assertEquals(rate4, loanOffers.get(3).getRate());
        assertEquals(amount, loanOffers.get(0).getTotalAmount());
        assertEquals(amount, loanOffers.get(1).getTotalAmount());
        assertEquals(amountWihInsurance, loanOffers.get(2).getTotalAmount());
        assertEquals(amountWihInsurance, loanOffers.get(3).getTotalAmount());
        assertEquals(monthlyPayment1, loanOffers.get(0).getMonthlyPayment());
        assertEquals(monthlyPayment3, loanOffers.get(1).getMonthlyPayment());
        assertEquals(monthlyPayment2, loanOffers.get(2).getMonthlyPayment());
        assertEquals(monthlyPayment4, loanOffers.get(3).getMonthlyPayment());
        verify(annuityCalculator, times(2)).calculateTotalAmount(amount, false);
        verify(annuityCalculator, times(2)).calculateTotalAmount(amount, true);
        verify(rateCalculator, times(1)).calculateRate(false, false);
        verify(rateCalculator, times(1)).calculateRate(true, false);
        verify(rateCalculator, times(1)).calculateRate(false, true);
        verify(rateCalculator, times(1)).calculateRate(true, true);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amount, term, rate1);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amountWihInsurance, term, rate2);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amount, term, rate3);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amountWihInsurance, term, rate4);
    }
}
