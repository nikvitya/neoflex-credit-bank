package ru.neoflex.deal.model.jsonb;

import lombok.*;
import ru.neoflex.deal.enums.EmploimentPosition;
import ru.neoflex.deal.enums.EmploymentStatus;

import java.math.BigDecimal;

@Builder
@ToString
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentData {
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploimentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
