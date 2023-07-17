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
    private Integer categoryId;
    /**
     * search_key to search
     */
    private String searchKey;
}
