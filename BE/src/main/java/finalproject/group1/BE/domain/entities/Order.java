package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_tbl")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_id", nullable = false)
    private String displayId;

//    @Column(name = "user_id", nullable = false)
//    private int userId;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "total_price", nullable = false)
    private Float totalPrice;
}
