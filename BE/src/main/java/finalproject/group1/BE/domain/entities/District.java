package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "District_tbl")
@Data
public class District {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;


    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "district")
    private OrderShippingDetail shippingDetail;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
