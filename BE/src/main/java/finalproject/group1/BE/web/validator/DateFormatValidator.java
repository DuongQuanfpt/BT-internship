package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.commons.ValidateCommons;
import finalproject.group1.BE.web.annotation.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        return ValidateCommons.isValidDate(date);
    }
}
