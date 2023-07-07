package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderDetail_tbl")
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

//    @Column(name = "order_id", nullable = false)
//    private int orderId;
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
