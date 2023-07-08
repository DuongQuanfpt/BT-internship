package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderShippingDetail_tbl")
@Data
public class OrderShippingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

    @OneToOne
    @JoinColumn(name = "city_id",nullable = false,referencedColumnName = "id")
    private City city;

    @OneToOne
    @JoinColumn(name = "district_id",nullable = false)
    private District district;

}
