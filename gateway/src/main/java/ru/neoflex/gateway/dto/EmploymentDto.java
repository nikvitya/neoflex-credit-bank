package ru.neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.EmploymentStatus;

import javax.swing.text.Position;
import java.math.BigDecimal;

import static ru.neoflex.gateway.util.StringPatterns.INN;

@Schema(description = "Сведения о текущей работе")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDto {

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    @NotNull(message = "Необходимо выбрать рабочий статус")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707123456")
    @NotNull(message = "Необходимо заполнить ИНН работодателя")
    @Pattern(regexp = INN, message = "ИНН работодателя должен содержать 10 или 12 цифр")
    private String employerInn;

    @Schema(description = "Размер заработной платы", example = "70000")
    @NotNull(message = "Необходимо ввести размер заработной платы")
    @PositiveOrZero(message = "Размер заработной платы должен быть больше или равен 0")
    private BigDecimal salary;

    @Schema(description = "Позиция на работе", example = "TOP_MANAGER")
    @NotNull(message = "Необходимо выбрать позицию на текущем месте работы")
    private Position position;

    @Schema(description = "Общий стаж работы", example = "256")
    @NotNull(message = "Необходимо ввести общий стаж работы в месяцах")
    @PositiveOrZero(message = "Общий стаж работы должен быть больше или равен 0")
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы", example = "12")
    @NotNull(message = "Необходимо ввести стаж на текущем месте работы в месяцах")
    @PositiveOrZero(message = "Стаж на текущем месте работы должен быть больше или равен 0")
    private Integer workExperienceCurrent;
}
