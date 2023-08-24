package finalproject.group1.BE.web.dto.request.product;

import finalproject.group1.BE.web.dto.response.product.ProductStatisticDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisticCSVRequest {
    List<ProductStatisticDetailResponse> datas;
}
