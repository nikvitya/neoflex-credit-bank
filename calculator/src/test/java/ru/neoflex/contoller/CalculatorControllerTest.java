package ru.neoflex.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.controller.CalculatorController;
import ru.neoflex.dto.LoanStatementRequestDto;
import ru.neoflex.dto.ScoringDataDto;
import ru.neoflex.service.CreditService;
import ru.neoflex.service.LoanOfferService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CalculatorController.class)
@Import(CalculatorControllerTest.TestConfig.class)
class CalculatorControllerTest {
    @Autowired
    private LoanOfferService loanOfferService;
    @Autowired
    private CreditService creditService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @TestConfiguration // Внутренний конфиг для моков
    static class TestConfig {
        @Bean
        @Primary // Переопределяем реальный бин
        public LoanOfferService loanOfferService() {
            return Mockito.mock(LoanOfferService.class);
        }

        @Bean
        @Primary
        public CreditService creditService() {
            return Mockito.mock(CreditService.class);
        }
    }


    @Test
    void calculateLoanOffers() throws Exception {
        when(loanOfferService.calculateLoanOffers(new LoanStatementRequestDto())).thenReturn(List.of());

        mvc.perform(post("/calculator/offers")
                        .content(mapper.writeValueAsString(new LoanStatementRequestDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit() throws Exception {
        when(creditService.calculateCredit(new ScoringDataDto())).thenReturn(any());

        mvc.perform(post("/calculator/calc")
                        .content(mapper.writeValueAsString(new ScoringDataDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
