package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.CreditDto;
import ru.neoflex.dossier.exception.EmailServiceException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final FileCreator fileCreator;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        log.info("Send simple email");

        try {
            var message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            log.info("Message created");

            emailSender.send(message);
            log.info("Message sent");

        } catch (Exception exception) {
            log.error("Failed to send email to {}", to, exception);
            throw new EmailServiceException("Sending email exception", exception);
        }
    }

    @Async
    public void sendMessageWithAttachment(String to, String subject, String text, CreditDto creditDto) {
        log.info("Send email with attachment");
        File file = null;

        try {
            var message = emailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            log.info("Message created");

            file = new File(fileCreator.createTxtFile(creditDto).toString());

            var documents = new FileSystemResource(file);

            String attachmentName = documents.getFilename() != null
                    ? documents.getFilename()
                    : "document_" + UUID.randomUUID().toString();

            helper.addAttachment(attachmentName, documents);
            log.info("Attachment added");

            emailSender.send(message);
            log.info("Message sent");

        } catch (Exception exception) {
            log.error("Failed to send email to {}", to, exception);
            throw new EmailServiceException("Sending email exception", exception);
        } finally {
            if (file != null && file.exists()) {
                try {
                    Files.deleteIfExists(file.toPath());
                    log.debug("Temporary file deleted: {}", file.getAbsolutePath());
                } catch (IOException e) {
                    log.warn("Failed to delete temp file {}", file.getAbsolutePath(), e);
                }
            }
        }
    }
}
