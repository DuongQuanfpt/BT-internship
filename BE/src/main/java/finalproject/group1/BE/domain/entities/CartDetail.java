package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CartDetail_tbl")
@Data
public class CartDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

//    @Column(name = "cart_id", nullable = false)
//    private int cartId;
//
//    @Column(name = "product_id", nullable = false)
//    private int productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "total_price", nullable = false)
    private Float totalPrice;
}
