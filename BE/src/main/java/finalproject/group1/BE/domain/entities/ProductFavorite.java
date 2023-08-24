package finalproject.group1.BE.domain.entities;

import finalproject.group1.BE.domain.embedded.FavoriteProductId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_product_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavorite {
    @EmbeddedId
    private FavoriteProductId id;
}
