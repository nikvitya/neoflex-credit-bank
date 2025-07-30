package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.EmploimentPosition;
import ru.neoflex.deal.enums.EmploymentStatus;


import java.math.BigDecimal;

@Schema(description = "Сведения о текущей работе")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDto {
    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя",
            example = "7707123456",
            pattern = "^[0-9]{10,12}$",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String employerInn;

    @Schema(description = "Размер заработной платы", example = "100000")
    private BigDecimal salary;

    @Schema(description = "Позиция на работе", example = "TOP_MANAGER")
    private EmploimentPosition position;

    @Schema(description = "Общий стаж работы", example = "256")
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы", example = "12")
    private Integer workExperienceCurrent;


}
