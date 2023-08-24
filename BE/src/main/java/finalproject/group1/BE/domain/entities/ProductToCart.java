package finalproject.group1.BE.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product_to_cart_tbl")
public class ProductToCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime date;
}
