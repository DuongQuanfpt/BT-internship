package finalproject.group1.BE.web.annotation;

import finalproject.group1.BE.web.validator.DateFormatValidator;
import finalproject.group1.BE.web.validator.UppercaseAndNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateFormat {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
