package finalproject.group1.BE.web.dto.request;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.web.annotation.ValidFileExtension;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportRequest {
    @NotNull
    @ValidFileExtension(extension = Constants.VALID_IMPORT_FILE_EXTENSION
            ,message = "Not a csv file")
    MultipartFile csvFile;
}
