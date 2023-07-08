package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.web.annotation.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    @Value("${validDateFormat}")
    String dateFormat;

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        DateFormat format;

        try {
            format = new SimpleDateFormat(dateFormat);
            format.parse(date);
        } catch (ParseException | IllegalArgumentException| NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
