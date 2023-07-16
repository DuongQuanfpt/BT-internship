package finalproject.group1.BE.web.dto.response.city;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityListResponse {
    private int id;
    @NotEmpty
    private String name;
}
