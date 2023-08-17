package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.commons.ValidateCommons;
import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UppercaseAndNumberValidator implements ConstraintValidator<UppercaseAndNumber, String> {

    @Override
    public void initialize(UppercaseAndNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return ValidateCommons.hasUpperCaseAndNumber(password);
    }
}
