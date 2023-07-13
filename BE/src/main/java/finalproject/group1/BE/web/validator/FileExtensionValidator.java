package finalproject.group1.BE.web.validator;

import finalproject.group1.BE.web.annotation.ValidFileExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileExtensionValidator implements ConstraintValidator<ValidFileExtension, Object> {
    String validExtension;
    @Override
    public void initialize(ValidFileExtension constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        validExtension = constraintAnnotation.extension();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        if(object instanceof MultipartFile){
           MultipartFile multipartFile = (MultipartFile) object;
            if(multipartFile.getOriginalFilename().endsWith(validExtension)){
               isValid.set(true);
            }
        } else if (object instanceof List) {
            List<Object> objects = (List<Object>) object;
            objects.stream().forEach(o -> {
                if (o instanceof MultipartFile){
                    MultipartFile multipartFile = (MultipartFile) o;
                    if(multipartFile.getOriginalFilename().endsWith(validExtension)){
                        isValid.set(true);
                    }
                }
            });
        }

        return isValid.get();
    }
}
