package ru.neoflex.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.dto.CreditDto;
import ru.neoflex.dto.LoanOfferDto;
import ru.neoflex.dto.LoanStatementRequestDto;
import ru.neoflex.dto.ScoringDataDto;
import ru.neoflex.service.CreditService;
import ru.neoflex.service.LoanOfferService;

import java.util.List;

@Tag(name = "Кредитный калькулятор", description = "API по расчету параметров кредита и скорингу данных.")
@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final LoanOfferService loanOfferService;
    private final CreditService creditService;

    @Autowired
    public CalculatorController(LoanOfferService loanOfferService, CreditService creditService) {
        this.loanOfferService = loanOfferService;
        this.creditService = creditService;
    }

    @Operation(summary = "Расчёт возможных условий кредита.")
    @PostMapping("/offers")
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true)
                                                  @Valid @RequestBody LoanStatementRequestDto loanStatement) {
        return loanOfferService.calculateLoanOffers(loanStatement);
    }

    @Operation(summary = "Полный расчёт параметров кредита, скоринг и валидация присланных данных.")
    @PostMapping("/calc")
    public CreditDto calculateCredit(@Parameter(required = true)
                                     @Valid @RequestBody ScoringDataDto scoringData) {
        return creditService.calculateCredit(scoringData);
    }
}
