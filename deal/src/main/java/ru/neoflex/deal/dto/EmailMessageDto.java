package ru.neoflex.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.Theme;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto {
    private String address;
    private Theme theme;
    private String statementId;
}