package ru.neoflex.deal.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface MinEighteenYearsBeforeDate {
    String message() default "Введите дату рождения, не позднее 18 лет с текущего дня";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
