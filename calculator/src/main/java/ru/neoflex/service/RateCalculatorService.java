package ru.neoflex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.config.RateConfiguration;
import ru.neoflex.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static ru.neoflex.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.enums.Gender.FEMALE;
import static ru.neoflex.enums.Gender.MALE;
import static ru.neoflex.enums.MaritalStatus.DIVORCED;
import static ru.neoflex.enums.MaritalStatus.MARRIED;
import static ru.neoflex.enums.Position.MID_MANAGER;
import static ru.neoflex.enums.Position.TOP_MANAGER;
import static ru.neoflex.util.BigDecimalConstant.THREE;
import static ru.neoflex.util.BigDecimalConstant.TWO;
import static ru.neoflex.util.ScoringConstant.MAX_AGE_FOR_CREDIT;
import static ru.neoflex.util.ScoringConstant.MIN_AGE_FOR_CREDIT;

@Slf4j
@RequiredArgsConstructor
@Service
public class RateCalculatorService {

    private final RateConfiguration rateConfiguration;

    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        log.info("Calculating rate: isInsuranceEnabled = {}, isSalaryClient = {}", isInsuranceEnabled, isSalaryClient);

        var rate = BigDecimal.valueOf(rateConfiguration.getRate());

        if (Boolean.TRUE.equals(isInsuranceEnabled)) {
            rate = rate.subtract(THREE);
        }

        if (Boolean.TRUE.equals(isSalaryClient)) {
            rate = rate.subtract(BigDecimal.ONE);
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateFinalRate(ScoringDataDto scoringData, BigDecimal previousRate) {
        log.info("Final calculating rate: scoringData = {}, rate = {}", scoringData, previousRate);

        var rate = previousRate;
        var employmentDto = scoringData.getEmployment();
        var gender = scoringData.getGender();
        var birthdate = scoringData.getBirthdate();
        var maritalStatus = scoringData.getMaritalStatus();

        if (employmentDto.getEmploymentStatus() == SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.ONE);
        }

        if (employmentDto.getEmploymentStatus() == SELF_EMPLOYED) {
            rate = rate.add(TWO);
        }

        if (employmentDto.getPosition() == MID_MANAGER) {
            rate = rate.subtract(TWO);
        }

        if (employmentDto.getPosition() == TOP_MANAGER) {
            rate = rate.subtract(THREE);
        }

        if (maritalStatus == MARRIED) {
            rate = rate.subtract(THREE);
        }

        if (maritalStatus == DIVORCED) {
            rate = rate.add(BigDecimal.ONE);
        }

        if (gender == FEMALE && isOlderAndYounger(MIN_AGE_FOR_CREDIT, MAX_AGE_FOR_CREDIT, birthdate)) {
            rate = rate.subtract(THREE);

        }

        if (gender == MALE && isOlderAndYounger(MIN_AGE_FOR_CREDIT, MAX_AGE_FOR_CREDIT, birthdate)) {
            rate = rate.subtract(THREE);
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isOlderAndYounger(int minAge, int maxAge, LocalDate birthdate) {
        var maxBirthdate = LocalDate.now().minusYears(minAge);
        var minBirthdate = LocalDate.now().minusYears(maxAge + 1L);
        return !birthdate.isAfter(maxBirthdate) && birthdate.isAfter(minBirthdate);
    }
}
