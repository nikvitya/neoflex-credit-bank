package ru.neoflex.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Перечисление возможных статусов заявления")
public enum StatementStatus {
    @Schema(description = "Новое заявление") NEW,
    @Schema(description = "В обработке") IN_PROGRESS,
    @Schema(description = "Одобрено") APPROVED,
    @Schema(description = "Отклонено") REJECTED,
    @Schema(description = "Отменено клиентом") CANCELLED
}
