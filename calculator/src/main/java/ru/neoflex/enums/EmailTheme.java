package ru.neoflex.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Перечисление возможных тем писем")
public enum EmailTheme {
    @Schema(description = "Уведомление") NOTIFICATION,
    @Schema(description = "Подтверждение") CONFIRMATION,
    @Schema(description = "Реклама") PROMOTION,
    @Schema(description = "Техническое сообщение") TECHNICAL
}
