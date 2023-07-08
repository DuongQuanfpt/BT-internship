package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.web.annotation.UppercaseAndNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

public class UppercaseAndNumberValidator implements ConstraintValidator<UppercaseAndNumber, String> {

    @Override
    public void initialize(UppercaseAndNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        char currentChar;
        boolean uppercaseFlag = false;
        boolean numberFlag = false;

        for (int i = 0; i < password.length(); i++) {
            currentChar = password.charAt(i);
            //check if current character is a number
            if (Character.isDigit(currentChar)) {
                numberFlag = true;
            }
            //check if the current character is uppercase
            if (Character.isUpperCase(currentChar)) {
                uppercaseFlag = true;
            }
        }

        if (uppercaseFlag && numberFlag) {
            return true;
        }
        return false;
    }
}
