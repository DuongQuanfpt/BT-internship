package finalproject.group1.BE.web.dto.request.Category;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotEmpty
    private String name;
}
