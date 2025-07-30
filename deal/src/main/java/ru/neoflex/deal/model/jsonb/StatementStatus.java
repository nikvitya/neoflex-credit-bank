package ru.neoflex.deal.model.jsonb;

import lombok.*;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;

import java.time.LocalDateTime;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StatementStatus {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}