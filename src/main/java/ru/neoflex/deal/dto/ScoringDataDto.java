package ru.neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.annotation.MinEighteenYearsBeforeDate;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.deal.util.DateConstant.DATE_PATTERN;
import static ru.neoflex.deal.util.StringPatterns.*;
import static ru.neoflex.deal.util.StringPatterns.ACCOUNT_NUMBER;

@Schema(description = "Полные данные для скоринга и окончательного расчёта параметров кредита")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoringDataDto {

    @Schema(description = "Запрашиваемая сумма кредита", example = "100000")
    @NotNull(message = "Необходимо указать сумму кредита")
    @Min(value = 50000, message = "Сумма кредита должна быть больше или равна 50 000.00 рублей")
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

    @Schema(description = "Пол", example = "MALE")
    @NotNull(message = "Необходимо выбрать пол")
    private Gender gender;

    @Schema(description = "Дата рождения", example = "1988-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    @NotNull(message = "Необходимо указать дату рождения")
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

    @Schema(description = "Дата выдачи паспорта", example = "1988-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    @NotNull(message = "Необходимо указать дату выдачи паспорта")
    @PastOrPresent(message = "Дата выдачи паспорта должна быть не позже текущей даты")
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение выдачи паспорта", example = "ОУФМС России по Чувашкой Республике")
    @NotBlank(message = "Необходимо указать отделение выдачи паспорта")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "MARRIED")
    @NotNull(message = "Необходимо выбрать семейное положение")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "0")
    @NotNull(message = "Необходимо заполнить количество иждивенцев")
    @PositiveOrZero(message = "Необходимо указать 0 или больше")
    private Integer dependentAmount;

    @Schema(description = "Сведения о текущей работе")
    @NotNull(message = "Необходимо указать сведения о текущей работе")
    @Valid
    private EmploymentDto employment;

    @Schema(description = "Номер расчетного счёта", example = "12345678910000000000")
    @NotBlank(message = "Необходимо ввести номер расчетного счёта")
    @Pattern(regexp = ACCOUNT_NUMBER, message = "Счёт должен содержать 20 цифр")
    private String accountNumber;

    @Schema(description = "Включена ли страховка", example = "true")
    @NotNull(message = "Необходимо указать включить ли страховку")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Наличие зарплатного клиента в банке", example = "true")
    @NotNull(message = "Необходимо указать наличие зарплатного клиента")
    private Boolean isSalaryClient;
}
