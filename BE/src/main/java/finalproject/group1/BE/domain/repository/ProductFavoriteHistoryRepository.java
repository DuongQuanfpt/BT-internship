package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.ProductFavoriteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProductFavoriteHistoryRepository extends JpaRepository<ProductFavoriteHistory, Integer> {
    long countByProductIdAndIsFavorite(int productId, boolean isFavorite);

    long countByProductIdAndIsFavoriteAndDateAfter(int productId, boolean isFavorite, LocalDateTime date);
}
