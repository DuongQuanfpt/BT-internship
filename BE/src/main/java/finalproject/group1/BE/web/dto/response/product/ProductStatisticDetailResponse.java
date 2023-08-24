package finalproject.group1.BE.web.dto.response.product;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatisticDetailResponse {
    private int id;
    private String sku;
    private String productName;
    private long viewCount;
    private long favoriteCount;
    private long favoriteRemovalCount;
    private long addedToCartCount;
    private long orderCount;
    private long orderQuantity;
    private Float orderViewPercentage;
}
