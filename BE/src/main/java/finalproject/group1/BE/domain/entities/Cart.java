package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cart_tbl")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "token", nullable = false)
    private String token;

//    @Column(name = "user_id", nullable = false)
//    private int userId;

    @Column(name = "version_no", nullable = false)
    private int versionNo;

    @Column(name = "total_price", nullable = false)
    private Float totalPrice;
}
