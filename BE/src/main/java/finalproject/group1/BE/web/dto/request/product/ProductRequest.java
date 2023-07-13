package finalproject.group1.BE.web.dto.request.product;

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

/**
 * dto for add and update product
 */
@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {
    /**
     * product code
     */
    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9-]{0,51}")
    private String sku;

    /**
     * product name
     */
    @NotEmpty
    private String name;

    /**
     * product detail description
     */
    @NotEmpty
    @Size(max = 1000)
    private String detail_info;

    /**
     * id of category
     */
    @NotNull
    private Integer category_id;

    /**
     * product price
     */
    @NotNull
    @Digits(integer=15, fraction=3)
    private BigDecimal price;

    /**
     * product thumbnail image
     */
    @NotNull
    @ValidFileExtension(extension = Constants.VALID_IMAGE_FILE_EXTENSION
            ,message = "invalid file extension")
    private MultipartFile thumbnailImage;

    /**
     * product detail image
     */
    @NotNull
    @ValidFileExtension(extension = Constants.VALID_IMAGE_FILE_EXTENSION
            ,message = "invalid file extension")
    private List<MultipartFile> detailImage;

}
