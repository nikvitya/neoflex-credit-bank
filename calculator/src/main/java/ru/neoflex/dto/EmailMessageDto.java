package ru.neoflex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.enums.EmailTheme;


@Schema(description = "DTO для отправки email сообщения")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto {
    @Schema(
            description = "Email адрес получателя",
            example = "user@example.com"
    )
    private String address;

    @Schema(
            description = "Тема письма",
            example = "NOTIFICATION"
    )
    private EmailTheme theme;

    @Schema(
            description = "ID связанного заявления/документа",
            example = "12345"
    )
    private Long statementId;

    @Schema(
            description = "Текст сообщения",
            example = "Уважаемый клиент, ваш кредит одобрен!"
    )
    private String text;
}
