package ru.neoflex.statement.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<MinEighteenYearsBeforeDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        LocalDate date = LocalDate.now().minusYears(18);
        return !birthdate.isAfter(date);
    }
}
