package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_tbl")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_id", nullable = false)
    private String displayId;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "total_price", nullable = false)
    private Float totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private OrderShippingDetail shippingDetail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;
}
