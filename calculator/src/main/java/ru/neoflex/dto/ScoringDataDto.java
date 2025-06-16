package ru.neoflex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.enums.Gender;
import ru.neoflex.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.util.DateConstant.DATE_PATTERN;

@Schema(description = "Полные данные для скоринга и окончательного расчёта параметров кредита")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoringDataDto {

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

    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @Schema(description = "Дата рождения", example = "1980-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта", example = "1990-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение выдачи паспорта", example = "ОВД кировского района города Пензы")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "0")
    private Integer dependentAmount;

    @Schema(description = "Сведения о текущей работе")
    private EmploymentDto employment;

    @Schema(description = "Номер расчетного счёта", example = "40817810100007408755")
    private String accountNumber;

    @Schema(description = "Включена ли страховка", example = "false")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Наличие зарплатного клиента в банке", example = "false")
    private Boolean isSalaryClient;


}
