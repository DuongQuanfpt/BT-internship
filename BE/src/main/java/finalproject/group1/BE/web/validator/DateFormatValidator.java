package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.web.annotation.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
//    @Value("${validDateFormat}")
    String dateFormat = Constants.VALID_DATE_FORMAT;

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {

        try {
            DateFormat format = new SimpleDateFormat(dateFormat);
            format.setLenient(false);
            format.parse(date);

        } catch (ParseException | IllegalArgumentException| NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
