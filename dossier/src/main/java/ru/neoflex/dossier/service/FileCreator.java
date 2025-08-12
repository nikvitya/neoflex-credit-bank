package ru.neoflex.dossier.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.CreditDto;
import ru.neoflex.dossier.dto.PaymentScheduleElementDto;
import ru.neoflex.dossier.exception.FileCreatorException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.neoflex.dossier.util.DateConstant.DATE_PATTERN;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileCreator {
    private final Validator validator;

    public Path createTxtFile(CreditDto creditDto) {
        log.info("Create temporary file with txt format");

        validateDto(creditDto);

        Path tempFile;

        try {
            tempFile = Files.createTempFile("loan_documents", ".txt");

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8, StandardOpenOption.WRITE))) {
                writer.write("Credit Amount: " + creditDto.getAmount() + "\n");
                writer.write("Term: " + creditDto.getTerm() + "\n");
                writer.write("Monthly Payment: " + creditDto.getMonthlyPayment() + "\n");
                writer.write("Rate: " + creditDto.getRate() + "\n");
                writer.write("PSK: " + creditDto.getPsk() + "\n");

                writer.write(Boolean.TRUE.equals(creditDto.getIsInsuranceEnabled()) ?
                        "Insurance enabled\n" : "Insurance is not enabled\n");

                writer.write(Boolean.TRUE.equals(creditDto.getIsSalaryClient()) ?
                        "Salary Client - Yes\n\n" : "Salary Client - No\n\n");

                writer.write("Number,Date,Total Payment,Interest Payment,Debt Payment,Remaining Debt\n");
                if (creditDto.getPaymentSchedule() != null && !creditDto.getPaymentSchedule().isEmpty()) {
                    for (PaymentScheduleElementDto payment : creditDto.getPaymentSchedule()) {
                        writer.write(payment.getNumber() + ",");
                        writer.write(payment.getDate() != null
                                ? payment.getDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + ","
                                : "N/A,");
                        writer.write(payment.getTotalPayment() + ",");
                        writer.write(payment.getInterestPayment() + ",");
                        writer.write(payment.getDebtPayment() + ",");
                        writer.write(payment.getRemainingDebt() + "\n");
                    }
                } else {
                    writer.write("No payment schedule available\n");
                }
                log.info("File created successfully.");
            }
        } catch (IOException exception) {
            log.error("File creating exception: ", exception);
            throw new FileCreatorException("Failed to create file", exception);
        }

        return tempFile;
    }

    private void validateDto(CreditDto creditDto) {
        if (creditDto == null) {
            log.error("CreditDto is null. Cannot create file.");
            throw new FileCreatorException("Credit data is missing");
        }

        Set<ConstraintViolation<CreditDto>> violations = validator.validate(creditDto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            log.error("Validation failed: {}", errorMsg);
            throw new FileCreatorException("Invalid credit data: " + errorMsg);
        }
    }
}
