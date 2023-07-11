package finalproject.group1.BE.web.dto.response.Category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListResponse {
    private int id;
    @NotEmpty
    private String name;
}
