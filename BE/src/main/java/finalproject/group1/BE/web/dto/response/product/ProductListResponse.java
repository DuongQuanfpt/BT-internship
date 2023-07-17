package finalproject.group1.BE.web.dto.response.product;

import finalproject.group1.BE.web.dto.response.PageableDTO;
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

    List<ProductResponse> productResponses;
    PageableDTO pageableDTO;
}
