package ru.neoflex.statement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.feign.DealFeignClient;

import java.util.List;

@Tag(name = "Заявка", description = "API по прескорингу данных, расчёту возможных условий кредита и выбору одного из них.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/statement")
public class StatementController {

    private final DealFeignClient dealFeignClient;

    /**
     * Validate data and calculate 4 loan offers.
     */
    @Operation(summary = "Прескоринг данных и расчёт возможных условий кредита.")
    @PostMapping
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true) @Valid @RequestBody
                                                  LoanStatementRequestDto loanStatement) {
        return dealFeignClient.calculateLoanOffers(loanStatement);
    }

    /**
     * Select one loan offer.
     */
    @Operation(summary = "Выбор одного из предложений по кредиту.")
    @PostMapping("/offer")
    public void selectLoanOffers(@Parameter(required = true) @Valid @RequestBody LoanOfferDto loanOffer) {
        dealFeignClient.selectLoanOffers(loanOffer);
    }
}
