package finalproject.group1.BE.web.dto.request.category;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.web.annotation.MaxFileSize;
import finalproject.group1.BE.web.annotation.ValidFileExtension;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * dto request update category
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    /**
     * category new name
     */
    @NotEmpty
    private String name;

    /**
     * category new image
     */
    @ValidFileExtension(extension = Constants.VALID_IMAGE_FILE_EXTENSION
            ,message = "invalid file extension")
    @MaxFileSize(maxFileSize = 2 , message = "file too large (<2MB)")
    private MultipartFile image;
}

