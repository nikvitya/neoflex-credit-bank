package ru.neoflex.statement.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface MinEighteenYearsBeforeDate {
    String message() default "Введите дату рождения, не позднее 18 лет с текущего дня";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
