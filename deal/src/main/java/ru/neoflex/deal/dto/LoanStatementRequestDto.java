package ru.neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.annotation.MinEighteenYearsBeforeDate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.deal.util.DateConstant.DATE_PATTERN;
import static ru.neoflex.deal.util.StringPatterns.PASSPORT_NUMBER;
import static ru.neoflex.deal.util.StringPatterns.PASSPORT_SERIES;

@Schema(description = "Заявка на кредит")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatementRequestDto {

    @Schema(description = "Запрашиваемая сумма кредита", example = "100000")
    @NotNull(message = "Необходимо указать сумму кредита")
    @Min(value = 30000, message = "Сумма кредита должна быть больше или равна 30 000.00 рублей")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "6")
    @NotNull(message = "Необходимо указать срок кредита в месяцах")
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6ти месяцам")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    @NotBlank(message = "Необходимо указать имя")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    @NotBlank(message = "Необходимо указать фамилию")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;

    @Schema(description = "Электронная почта", example = "ivan@mail.ru")
    @NotBlank(message = "Необходимо указать электронную почту")
    @Email(message = "Поле email имеет некорректный формат")
    private String email;

    @Schema(description = "Дата рождения", example = "1988-01-01")
    @NotNull(message = "Необходимо указать дату рождения")
    @JsonFormat(pattern = DATE_PATTERN)
    @MinEighteenYearsBeforeDate(message = "Кредит выдаётся лицам, достигшим 18-ти лет")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    @NotBlank(message = "Необходимо указать серию паспорта")
    @Pattern(regexp = PASSPORT_SERIES, message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    @NotBlank(message = "Необходимо указать номер паспорта")
    @Pattern(regexp = PASSPORT_NUMBER, message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;
}