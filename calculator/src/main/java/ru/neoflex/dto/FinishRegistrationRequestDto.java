package ru.neoflex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.enums.Gender;
import ru.neoflex.enums.MaritalStatus;

import java.time.LocalDate;

import static ru.neoflex.util.DateConstant.DATE_PATTERN;

@Schema(description = "DTO для завершения регистрации клиента")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishRegistrationRequestDto {

    @Schema(
            description = "Пол клиента",
            example = "MALE",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Gender gender;

    @Schema(
            description = "Семейное положение",
            example = "MARRIED",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private MaritalStatus maritalStatus;

    @Schema(
            description = "Количество иждивенцев",
            example = "2",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer dependentAmount;

    @Schema(
            description = "Дата выдачи паспорта",
            example = "2020-05-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate passportIssueDate;

    @Schema(
            description = "Отделение, выдавшее паспорт",
            example = "ОУФМС России по г. Москве",
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String passportIssueBranch;

    @Schema(
            description = "Информация о занятости",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EmploymentDto employment;

    @Schema(
            description = "Номер банковского счета",
            example = "40817810099910004321",
            pattern = "^[0-9]{20}$",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String accountNumber;
}
