package finalproject.group1.BE.web.dto.request.district;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictRequest {
    /**
     * cityId to search the districts belong to this city
     */
    private int cityId;
}
