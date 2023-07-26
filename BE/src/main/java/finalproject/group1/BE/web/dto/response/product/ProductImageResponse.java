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
public class ProductImageResponse {
    @NotEmpty
    private List<String> path;
    @NotEmpty
    private List<String> name;
}
