package finalproject.group1.BE.web.annotation;

import finalproject.group1.BE.web.validator.MaxFileSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * custom annotation to validate file size
 */
@Documented
@Constraint(validatedBy = MaxFileSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {
    /**
     * max file size in megabytes
     * @return
     */
    double maxFileSize() default 2;
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
