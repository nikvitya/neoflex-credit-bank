package ru.neoflex.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип изменения статуса")
public enum StatusChangeType {
    @Schema(description = "Автоматическое изменение") AUTOMATIC,
    @Schema(description = "Ручное изменение оператором") MANUAL,
    @Schema(description = "Изменение клиентом") CLIENT_ACTION,
    @Schema(description = "Системное изменение") SYSTEM
}