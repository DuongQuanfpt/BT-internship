package finalproject.group1.BE.web.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    int id;
    int productId;
    String productName;
    String imagePath;
    String imageName;
    int quantity;
    float price;
    float totalPrice;
}
