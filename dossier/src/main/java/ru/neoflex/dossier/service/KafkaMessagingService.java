package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.EmailMessageDto;
import ru.neoflex.dossier.feign.DealFeignClient;

import static ru.neoflex.dossier.enums.Status.DOCUMENT_CREATED;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingService {
    private final EmailService emailService;
    private final DealFeignClient dealFeignClient;
    private static final String MESSAGE_CONSUMED = "Message consumed {}";

    @KafkaListener(topics = "finish-registration",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessageDto"})
    public void sendEmailWithFinishRegistration(@Payload EmailMessageDto emailMessageDto) {
        log.info(MESSAGE_CONSUMED, emailMessageDto);

        var text = "Ваша заявка предварительно одобрена, завершите оформление.";
        emailService.sendSimpleMessage(emailMessageDto.getAddress(), "Завершите оформление", text);
    }

    @KafkaListener(topics = "create-documents",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessageDto"})
    public void sendEmailWithCreateDocuments(EmailMessageDto emailMessageDto) {
        log.info(MESSAGE_CONSUMED, emailMessageDto);

        var text = "Ваша заявка окончательно одобрена.\n[Сформировать документы.](ссылка)";
        emailService.sendSimpleMessage(emailMessageDto.getAddress(), "Заявка на кредит одобрена", text);
    }

    @KafkaListener(topics = "send-documents",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessageDto"})
    public void sendEmailWithSendDocuments(EmailMessageDto emailMessageDto) {
        log.info(MESSAGE_CONSUMED, emailMessageDto);

        dealFeignClient.saveNewStatementStatus(emailMessageDto.getStatementId(), DOCUMENT_CREATED);

        var statementDto = dealFeignClient.findStatementById(emailMessageDto.getStatementId());
        var creditDto = statementDto.getCredit();

        var text = "Документы по кредиту.\n[Запрос на согласие с условиями.](ссылка)";
        emailService.sendMessageWithAttachment(emailMessageDto.getAddress(), "Документы по кредиту", text, creditDto);
    }

    @KafkaListener(topics = "send-ses",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessageDto"})
    public void sendEmailWithSendSes(EmailMessageDto emailMessageDto) {
        log.info(MESSAGE_CONSUMED, emailMessageDto);

        var statementDto = dealFeignClient.findStatementById(emailMessageDto.getStatementId());
        String sesCode = statementDto.getSesCode();

        var text = "Код подтверждения " + sesCode + ".\n[Подписать документы.](ссылка)";
        emailService.sendSimpleMessage(emailMessageDto.getAddress(), "Подпишите документы по кредиту", text);
    }

    @KafkaListener(topics = "credit-issued",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessageDto"})
    public void sendEmailWithCreditIssued(EmailMessageDto emailMessageDto) {
        log.info(MESSAGE_CONSUMED, emailMessageDto);

        var text = "Кредит выдан.";
        emailService.sendSimpleMessage(emailMessageDto.getAddress(), "Кредит выдан", text);
    }

    @KafkaListener(topics = "statement-denied",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessageDto"})
    public void sendEmailWithStatementDenied(EmailMessageDto emailMessageDto) {
        log.info(MESSAGE_CONSUMED, emailMessageDto);

        var text = "Заявка на кредит отклонена.\nОбратитесь в отделение банка за дополнительной информацией.";
        emailService.sendSimpleMessage(emailMessageDto.getAddress(), "Заявка на кредит отклонена", text);
    }
}
