package ru.neoflex.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Перечисление возможных семейных положений")
public enum MaritalStatus {
    SINGLE,
    MARRIED,
    DIVORCED,
    WIDOWED
}
