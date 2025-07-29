package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.service.DealService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.neoflex.deal.enums.EmploimentPosition.TOP_MANAGER;
import static ru.neoflex.deal.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.deal.enums.Gender.MALE;
import static ru.neoflex.deal.enums.MaritalStatus.MARRIED;

@WebMvcTest(controllers = DealController.class)
public class DealControllerTest {
    @MockitoBean
    private DealService dealService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void calculateLoanOffers() throws Exception {
        final var loanStatement = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .email("ivan@ya.ru")
                .birthdate(LocalDate.of(1980, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        when(dealService.calculateLoanOffers(loanStatement)).thenReturn(List.of());


        mvc.perform(post("/deal/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void selectLoanOffers() throws Exception {
        final var offer = LoanOfferDto.builder()
                .statementId(UUID.fromString("12345678-1234-5678-1234-567812345678"))
                .totalAmount(BigDecimal.valueOf(100000))
                .monthlyPayment(BigDecimal.valueOf(10000))
                .rate(BigDecimal.valueOf(19))
                .build();

        mvc.perform(post("/deal/offer/select")
                        .content(mapper.writeValueAsString(offer))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit() throws Exception {
        final EmploymentDto employmentDto = EmploymentDto.builder()
                .employmentStatus(SELF_EMPLOYED)
                .employerInn("2120000000")
                .salary(BigDecimal.valueOf(10000))
                .position(TOP_MANAGER)
                .workExperienceTotal(123)
                .workExperienceCurrent(12)
                .build();
        final FinishRegistrationRequestDto finishRegistration = FinishRegistrationRequestDto.builder()
                .gender(MALE)
                .maritalStatus(MARRIED)
                .dependentAmount(0)
                .passportIssueDate(LocalDate.of(1980,1,1))
                .passportIssueBranch("УФМС России по ...")
                .employment(employmentDto)
                .accountNumber("12345678900000000000")
                .build();

        mvc.perform(post("/deal/calculate/12345678-1234-5678-1234-567812345678")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
