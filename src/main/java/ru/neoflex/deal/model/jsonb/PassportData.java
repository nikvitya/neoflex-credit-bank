package ru.neoflex.deal.model.jsonb;

import lombok.*;

import java.time.LocalDate;

@Builder
@ToString
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PassportData {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

    public PassportData(String series, String number) {
        this.series = series;
        this.number = number;
    }
}
