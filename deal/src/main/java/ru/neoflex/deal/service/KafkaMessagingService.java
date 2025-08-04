package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.EmailMessageDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingService {

    private final KafkaTemplate<String , Object> kafkaTemplate;

    public void sendMessage(String sendClientTopic, EmailMessageDto emailMessageDto) {
        log.info("Send message to kafka = {}", emailMessageDto);
        kafkaTemplate.send(sendClientTopic, emailMessageDto.getStatementId(), emailMessageDto);
        log.info("Message sent to kafka topic = {}", sendClientTopic);
    }
}
