package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.ProductFavoriteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFavoriteHistoryRepository extends JpaRepository<ProductFavoriteHistory,Integer> {
}
