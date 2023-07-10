package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.web.annotation.ValidFileExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileExtensionValidator implements ConstraintValidator<ValidFileExtension, MultipartFile> {
    String validExtension;
    @Override
    public void initialize(ValidFileExtension constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        validExtension = constraintAnnotation.extension();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFile.getOriginalFilename().endsWith(validExtension)){
            return true;
        }
        return false;
    }
}
