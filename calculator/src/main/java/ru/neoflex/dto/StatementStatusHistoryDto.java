package ru.neoflex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.enums.StatementStatus;
import ru.neoflex.enums.StatusChangeType;

import java.time.LocalDateTime;

import static ru.neoflex.util.DateConstant.DATE_PATTERN;

@Schema(description = "DTO для истории изменения статуса заявления")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementStatusHistoryDto {
    @Schema(
            description = "Текущий статус заявления",
            example = "APPROVED"
    )
    private StatementStatus status;

    @Schema(
            description = "Дата и время изменения статуса",
            example = "2023-12-15 14:30:45"
    )
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime time;

    @Schema(
            description = "Тип изменения статуса",
            example = "AUTOMATIC"
    )
    private StatusChangeType changeType;
}
