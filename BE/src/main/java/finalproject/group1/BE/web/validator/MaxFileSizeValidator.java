package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.web.annotation.MaxFileSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * MaxFileSize annotation validator
 */
public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {
    /**
     * max file size
     */
    double maxSize;
    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        maxSize = constraintAnnotation.maxFileSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        double size = file.getSize(); //byte
        size = (size /1024) /1024; //convert byte to megabyte

        //if file size larger than max size
        if ( size > maxSize){
           return false;
        }
        return true;
    }
}
