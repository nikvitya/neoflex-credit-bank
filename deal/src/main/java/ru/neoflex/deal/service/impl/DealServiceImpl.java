package ru.neoflex.deal.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.EmailMessageDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.exception.ScoringException;
import ru.neoflex.deal.feign.CalculatorFeignClient;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.OfferMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.StatementStatus;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.repository.StatementRepository;
import ru.neoflex.deal.service.ClientService;
import ru.neoflex.deal.service.DealService;
import ru.neoflex.deal.service.KafkaMessagingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.neoflex.deal.enums.ApplicationStatus.*;
import static ru.neoflex.deal.enums.ChangeType.AUTOMATIC;
import static ru.neoflex.deal.enums.Theme.CREATE_DOCUMENTS;
import static ru.neoflex.deal.enums.Theme.FINISH_REGISTRATION;
import static ru.neoflex.deal.enums.Theme.STATEMENT_DENIED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DealServiceImpl implements DealService {
    private final CalculatorFeignClient calculatorFeignClient;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final ScoringDataMapper scoringDataMapper;
    private final ClientService clientService;
    private final KafkaMessagingService kafkaMessagingService;

    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Calculate loan offers in dealService: loanStatement = {}", loanStatement);

        var client = clientService.saveClient(loanStatement);

        var statement = new Statement(client, LocalDateTime.now(), new ArrayList<>());
        var savedStatement = statementRepository.save(statement);
        log.info("Statement saved = {}", savedStatement);

        saveStatus(savedStatement, PREAPPROVAL);

        List<LoanOfferDto> offers = calculatorFeignClient.getLoanOffers(loanStatement);
        offers.forEach(offer -> offer.setStatementId(savedStatement.getId()));
        log.info("Offers get from CalculatorMS: {}",
                offers.stream().map(LoanOfferDto::toString).collect(Collectors.joining(", ")));

        return offers;
    }

    @Override
    public void selectLoanOffers(LoanOfferDto loanOffer) {
        log.info("Select one loan offer = {}", loanOffer);

        var statement = findStatementById(loanOffer.getStatementId());
        log.info("Statement has found = {}", statement);

        saveStatus(statement, CLIENT_DENIED);

        var appliedOffer = offerMapper.toAppliedOffer(loanOffer);
        statement.setAppliedOffer(appliedOffer);
        log.info("Statement with selected offer saved = {}", findStatementById(statement.getId()));

        var emailMessage = EmailMessageDto.builder()
                .address(statement.getClient().getEmail())
                .theme(FINISH_REGISTRATION)
                .statementId(String.valueOf(loanOffer.getStatementId()))
                .build();
        kafkaMessagingService.sendMessage("finish-registration", emailMessage);

    }

    @Override
    public void finishRegistration(String statementId, FinishRegistrationRequestDto finishRegistration) {
        log.info("Finish registration: statementId = {}, finishRegistration = {}", statementId, finishRegistration);

        var statement = findStatementById(UUID.fromString(statementId));
        log.info("Statement found = {}", statement);

        var offer = statement.getAppliedOffer();
        var client = statement.getClient();
        var passportData = client.getPassport().getPassportData();
        var scoringData = scoringDataMapper.toScoringDataDto(finishRegistration, offer, client, passportData);
        log.info("ScoringData prepared = {}", scoringData);

        CreditDto creditDto = null;
        try {
            creditDto = calculatorFeignClient.calculateCredit(scoringData);
            log.info("CreditDto get from CalculatorMS = {}", creditDto);

        } catch (ScoringException exception) {
            log.info(exception.getMessage());

            var emailMessage = EmailMessageDto.builder()
                    .address(statement.getClient().getEmail())
                    .theme(STATEMENT_DENIED)
                    .statementId(statementId)
                    .build();
            kafkaMessagingService.sendMessage("statement-denied", emailMessage);
        }

        if (creditDto != null) {
            var credit = creditMapper.toCredit(creditDto);
            var savedCredit = creditRepository.save(credit);
            statement.setCredit(savedCredit);
            log.info("Credit saved = {}", savedCredit);

            clientService.finishRegistration(client, finishRegistration);

            var emailMessage = EmailMessageDto.builder()
                    .address(statement.getClient().getEmail())
                    .theme(CREATE_DOCUMENTS)
                    .statementId(statementId)
                    .build();
            kafkaMessagingService.sendMessage("create-documents", emailMessage);
        }
    }

    private void saveStatus(Statement statement, ApplicationStatus status) {
        log.info("Save new statement status = {}", status);

        statement.setStatus(status);
        log.info("Status in statement saved = {}", statement.getStatus());

        var statementStatus = new StatementStatus(status, LocalDateTime.now(), AUTOMATIC);
        List<StatementStatus> history = statement.getStatusHistory();
        history.add(statementStatus);
        log.info("Status saved in history: {}",
                statement.getStatusHistory().stream().map(StatementStatus::toString).collect(Collectors.joining(", ")));
    }

    private Statement findStatementById(UUID id) {
        return statementRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", id)));
    }
}
