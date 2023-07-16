package finalproject.group1.BE.web.dto.request.category;

import finalproject.group1.BE.constant.Constants;
import finalproject.group1.BE.web.annotation.ValidFileExtension;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotEmpty
    private String name;
    @NotNull
    @ValidFileExtension(extension = Constants.VALID_IMAGE_FILE_EXTENSION
            ,message = "invalid file extension")
    private MultipartFile image;
}
