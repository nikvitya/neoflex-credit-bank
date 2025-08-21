package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.*;
import ru.neoflex.deal.feign.CalculatorFeignClient;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.OfferMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.AppliedOffer;
import ru.neoflex.deal.model.jsonb.PassportData;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.repository.StatementRepository;
import ru.neoflex.deal.service.impl.DealServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.neoflex.deal.enums.ApplicationStatus.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceImplTest {
    @Mock
    private StatementRepository statementRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private CalculatorFeignClient calculatorFeignClient;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private CreditMapper creditMapper;
    @Mock
    private ScoringDataMapper scoringDataMapper;
    @InjectMocks
    private DealServiceImpl dealService;
    @Mock
    private KafkaMessagingService kafkaMessagingService;

    private final UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");
    private final PassportData passportData = new PassportData();
    private final Passport passport = new Passport(passportData);
    private final Client client = Client.builder().passport(passport).build();
    private final Statement statement = Statement.builder()
            .id(id)
            .client(client)
            .creationDate(LocalDateTime.now())
            .statusHistory(new ArrayList<>())
            .build();
    private final FinishRegistrationRequestDto finishRegistration = new FinishRegistrationRequestDto();

    @Test
    void calculateLoanOffers() {
        final List<LoanOfferDto> offers = List.of(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());
        when(clientService.saveClient(any())).thenReturn(client);
        when(statementRepository.save(any())).thenReturn(statement);
        when(calculatorFeignClient.getLoanOffers(any())).thenReturn(offers);

        final List<LoanOfferDto> actualOffers = dealService.calculateLoanOffers(new LoanStatementRequestDto());

        verify(clientService, times(1)).saveClient(any());
        verify(statementRepository, times(1)).save(any());
        verify(calculatorFeignClient, times(1)).getLoanOffers(any());
        for (int i = 0; i < offers.size(); i++) {
            assertEquals(statement.getId(), actualOffers.get(i).getStatementId());
        }
        assertEquals(PREAPPROVAL, statement.getStatus());
        assertEquals(1, statement.getStatusHistory().size());
        assertEquals(PREAPPROVAL, statement.getStatusHistory().get(0).getStatus());
    }

    @Test
    void selectLoanOffers_whenStatementFound_thenStatusAndOfferSaved() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(offerMapper.toAppliedOffer(any())).thenReturn(new AppliedOffer());

        assertDoesNotThrow(() -> dealService.selectLoanOffers(new LoanOfferDto()));

        verify(statementRepository, times(2)).findById(any());
        verify(offerMapper, times(1)).toAppliedOffer(any());
        assertEquals(CLIENT_DENIED, statement.getStatus());
        assertEquals(1, statement.getStatusHistory().size());
        assertEquals(CLIENT_DENIED, statement.getStatusHistory().get(0).getStatus());
    }


    @Test
    void selectLoanOffers_whenStatementNotFound_thenThrowsException() {
        final var loanOfferDto = LoanOfferDto.builder().statementId(id).build();
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        final EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                dealService.selectLoanOffers(loanOfferDto));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
    }


    @Test
    void finishRegistration_whenEntitiesFound_thenNoExceptions() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(scoringDataMapper.toScoringDataDto(any(), any(), any(), any())).thenReturn(new ScoringDataDto());
        when(calculatorFeignClient.calculateCredit(any())).thenReturn(new CreditDto());
        when(creditMapper.toCredit(any())).thenReturn(new Credit());
        when(creditRepository.save(any())).thenReturn(new Credit());

        assertDoesNotThrow(() -> dealService.finishRegistration(String.valueOf(id), finishRegistration));

        verify(statementRepository, times(1)).findById(any());
        verify(scoringDataMapper, times(1)).toScoringDataDto(any(), any(), any(), any());
        verify(calculatorFeignClient, times(1)).calculateCredit(any());
        verify(creditMapper, times(1)).toCredit(any());
        verify(creditRepository, times(1)).save(any());
    }


    @Test
    void finishRegistration_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        final EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                dealService.finishRegistration(String.valueOf(id), finishRegistration));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
    }
}
