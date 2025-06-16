package ru.neoflex.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dto.ScoringDataDto;
import ru.neoflex.enums.EmploymentStatus;
import ru.neoflex.exception.ScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.neoflex.util.ScoringConstant.MAX_AGE_FOR_CREDIT;
import static ru.neoflex.util.ScoringConstant.MAX_LOAN_AMOUNT_IN_MONTH_SALARY;
import static ru.neoflex.util.ScoringConstant.MIN_AGE_FOR_CREDIT;
import static ru.neoflex.util.ScoringConstant.MIN_CURRENT_WORKING_EXPERIENCE;
import static ru.neoflex.util.ScoringConstant.MIN_TOTAL_WORKING_EXPERIENCE;

@Service
@Slf4j
public class ScoringService {
    public void scoring(ScoringDataDto scoringData) {
        log.info("Scoring: scoringData = {}", scoringData);

        List<String> reasonsForRefusal = new ArrayList<>();
        var employmentDto = scoringData.getEmployment();
        var maxYearsAgo = LocalDate.now().minusYears(MAX_AGE_FOR_CREDIT);
        var minYearsAgo = LocalDate.now().minusYears(MIN_AGE_FOR_CREDIT);

        if (employmentDto.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            reasonsForRefusal.add("Working status unemployed");
        }

        if (scoringData.getAmount().compareTo(employmentDto.getSalary().multiply(BigDecimal.valueOf(MAX_LOAN_AMOUNT_IN_MONTH_SALARY))) > 0) {
            reasonsForRefusal.add((String.format("The loan amount is more than %s month salaries",
                    String.valueOf(MAX_LOAN_AMOUNT_IN_MONTH_SALARY))));
        }

        if (scoringData.getBirthdate().isBefore(maxYearsAgo)) {
            reasonsForRefusal.add((String.format("Age over than %s years",
                    String.valueOf(MAX_AGE_FOR_CREDIT))));
        } else if (scoringData.getBirthdate().isAfter(minYearsAgo)) {
            reasonsForRefusal.add((String.format("Age less than %s years",
                    String.valueOf(MIN_AGE_FOR_CREDIT))));
        }

        if (employmentDto.getWorkExperienceTotal() < MIN_TOTAL_WORKING_EXPERIENCE) {
            reasonsForRefusal.add((String.format("Total experience less than %s months",
                    String.valueOf(MIN_TOTAL_WORKING_EXPERIENCE))));
        }

        if (employmentDto.getWorkExperienceCurrent() < MIN_CURRENT_WORKING_EXPERIENCE) {
            reasonsForRefusal.add((String.format("Current experience less than %s months",
                    String.valueOf(MIN_CURRENT_WORKING_EXPERIENCE))));
        }

        if (!reasonsForRefusal.isEmpty()) {
            throw new ScoringException(String.format("Scoring result - rejection. Reasons: %s",
                    String.join(", ", reasonsForRefusal)));
        } else {
            log.info("Scoring was successful!");
        }
    }
}
