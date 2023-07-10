package finalproject.group1.BE.web.dto.request;

import finalproject.group1.BE.constant.Constants;
import finalproject.group1.BE.web.annotation.ValidFileExtension;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddProductRequest {
    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9-]{0,51}")
    private String sku;

    @NotEmpty
    private String name;

    @NotEmpty
    @Size(max = 1000)
    private String detailInfo;

    @NotNull
    private Integer category_id;

    @NotNull
    @Digits(integer=15, fraction=3)
    private BigDecimal price;

    @NotNull
    @ValidFileExtension(extension = Constants.VALID_IMAGE_FILE_EXTENSION
            ,message = "invalid file extension")
    private MultipartFile thumbnailImage;

    @NotNull
//    @ValidFileExtension(extension = Constants.VALID_IMAGE_FILE_EXTENSION
//            ,message = "invalid file extension")
    private List<MultipartFile> detailImage;

}
