package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cart_tbl")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token", nullable = true)
    private String token;

    @Column(name = "version_no", nullable = false)
    private int versionNo;

    @Column(name = "total_price", nullable = false)
    private Float totalPrice;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true, referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetail> cartDetails = new ArrayList<>();
}
