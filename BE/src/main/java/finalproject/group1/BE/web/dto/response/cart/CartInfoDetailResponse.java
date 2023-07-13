package finalproject.group1.BE.web.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartInfoDetailResponse {
    Integer id;

    Integer productId;

    String productName;

    String imagePath;

    String imageName;

    Integer quantity;

    Float price;

    Float totalPrice;
}
