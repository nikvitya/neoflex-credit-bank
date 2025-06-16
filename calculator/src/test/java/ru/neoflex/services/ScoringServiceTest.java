package ru.neoflex.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.dto.EmploymentDto;
import ru.neoflex.dto.ScoringDataDto;
import ru.neoflex.exception.ScoringException;
import ru.neoflex.service.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.neoflex.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.enums.EmploymentStatus.UNEMPLOYED;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {
    private ScoringService scoringService = new ScoringService();
    private LocalDate birthdateSixtyFiveYearsAge = LocalDate.now().minusYears(65);
    private LocalDate birthdateTwentyYearsAge = LocalDate.now().minusYears(20);
    private EmploymentDto employment = EmploymentDto.builder()
            .employmentStatus(SELF_EMPLOYED)
            .salary(BigDecimal.valueOf(300000))
            .workExperienceTotal(25)
            .workExperienceCurrent(15)
            .build();
    private ScoringDataDto scoringData = ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(100000))
            .birthdate(LocalDate.of(1986, 11, 16))
            .employment(employment)
            .build();

    @Test
    void testScoring_whenValidData_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenUnemployedStatus_thenThrowsException() {
        employment.setEmploymentStatus(UNEMPLOYED);

        ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Scoring result - rejection. Reasons: Working status unemployed", exception.getMessage());
    }

    @Test
    void scoring_whenAmountExceeds25Salaries_thenThrowsException() {
        BigDecimal smallSalary = BigDecimal.valueOf(3000);
        employment.setSalary(smallSalary);

        ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Scoring result - rejection. Reasons: The loan amount is more than 24 month salaries", exception.getMessage());
    }

    @Test
    void scoring_whenAgeMoreSixtyFiveYearsAgo_thenThrowsException() {
        var birthdateSixtyFiveYearsAgoYearsAgeMinusDay = birthdateSixtyFiveYearsAge.minusDays(1);
        scoringData.setBirthdate(birthdateSixtyFiveYearsAgoYearsAgeMinusDay);

        ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Scoring result - rejection. Reasons: Age over than 65 years", exception.getMessage());
    }

    @Test
    void scoring_whenAgeSixtyFive_thenNoExceptionThrown() {
        scoringData.setBirthdate(birthdateSixtyFiveYearsAge);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenAgeLessSixtyFive_thenNoExceptionThrown() {
        var birthdateSixtyFiveYearsAgoYearsAgePlusDay = birthdateSixtyFiveYearsAge.plusDays(1);
        scoringData.setBirthdate(birthdateSixtyFiveYearsAgoYearsAgePlusDay);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenAgeLessTwenty_thenThrowsException() {
        var birthdateTwentyYearsAgoYearsAgePlusDay = birthdateTwentyYearsAge.plusDays(1);
        scoringData.setBirthdate(birthdateTwentyYearsAgoYearsAgePlusDay);

        ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Scoring result - rejection. Reasons: Age less than 20 years", exception.getMessage());
    }

    @Test
    void scoring_whenAgeTwenty_thenNoExceptionThrown() {
        scoringData.setBirthdate(birthdateTwentyYearsAge);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenAgeMoreTwenty_thenNoExceptionThrown() {
        var birthdateTwentyYearsAgoMinusDay = birthdateTwentyYearsAge.minusDays(1);
        scoringData.setBirthdate(birthdateTwentyYearsAgoMinusDay);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }


    @Test
    void scoring_whenWorkExperienceTotalLessEighteen_thenThrowsException() {
        employment.setWorkExperienceTotal(17);

        ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Scoring result - rejection. Reasons: Total experience less than 18 months", exception.getMessage());
    }

    @Test
    void scoring_whenWorkExperienceTotalIsEighteen_thenNoExceptionThrown() {
        employment.setWorkExperienceTotal(18);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenWorkExperienceCurrentLess_thenThrowsException() {
        employment.setWorkExperienceCurrent(2);

        ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Scoring result - rejection. Reasons: Current experience less than 12 months", exception.getMessage());
    }

    @Test
    void scoring_whenWorkExperienceCurrentIsTwentyThree_thenNoExceptionThrown() {
        employment.setWorkExperienceCurrent(23);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }
}
