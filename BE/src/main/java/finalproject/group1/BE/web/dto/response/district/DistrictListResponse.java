package finalproject.group1.BE.web.dto.response.district;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictListResponse {
    /**
     * district id
     */
    private int id;
    /**
     * district name
     */
    private String name;

}
