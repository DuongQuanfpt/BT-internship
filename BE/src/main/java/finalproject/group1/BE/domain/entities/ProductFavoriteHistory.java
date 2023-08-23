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
@Table(name = "Product_favorite_history_tbl")
public class ProductFavoriteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private boolean isFavorite;

    @Column(nullable = false)
    private LocalDateTime date;
}
