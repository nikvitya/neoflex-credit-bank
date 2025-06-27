package ru.neoflex.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Типы занятости")
public enum EmploymentStatus {
    @Schema(description = "Работает по найму") EMPLOYED,
    @Schema(description = "Собственный бизнес") SELF_EMPLOYED,
    @Schema(description = "Безработный") UNEMPLOYED,
    @Schema(description = "Пенсионер") RETIRED
}
