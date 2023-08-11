package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.embedded.FavoriteProductId;
import finalproject.group1.BE.domain.entities.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, FavoriteProductId> {
    List<FavoriteProduct> findByIdUserId(int userId);

    List<FavoriteProduct> findByIdProductId(int productId);
}
