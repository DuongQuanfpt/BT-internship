package finalproject.group1.BE.web.dto.response.product;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private int id;
    @NotEmpty
    private String sku;
    @NotEmpty
    private String name;
    @NotEmpty
    private String detailInfo;
    @NotEmpty
    private Float price;
    @NotEmpty
    private List<String> imagePath;
    @NotEmpty
    private List<String> imageName;
}
