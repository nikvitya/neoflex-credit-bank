package ru.neoflex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.util.DateConstant.DATE_PATTERN;

@Schema(description = "Заявка на кредит")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatementRequestDto {
    @Schema(description = "Запрашиваемая сумма кредита", example = "100000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;

    @Schema(description = "Электронная почта", example = "hello@mail.ru")
    @Email
    private String email;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "9700")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "000000")
    private String passportNumber;
}
