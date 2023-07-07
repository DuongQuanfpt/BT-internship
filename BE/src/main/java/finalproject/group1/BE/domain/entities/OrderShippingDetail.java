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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

//    @Column(name = "order_id", nullable = false)
//    private int orderId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

//    @Column(name = "district_id", nullable = false)
//    private int districtId;
//
//    @Column(name = "city_id", nullable = false)
//    private int cityId;
}
