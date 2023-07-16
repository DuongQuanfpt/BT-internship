package finalproject.group1.BE.web.dto.request.product;

import finalproject.group1.BE.domain.entities.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListRequest {
    /**
     * category (categoryId) to search
     */
    private Category category;
    /**
     * product sku to search
     */
    private String sku;
    /**
     * product name to search
     */
    private String name;
}
