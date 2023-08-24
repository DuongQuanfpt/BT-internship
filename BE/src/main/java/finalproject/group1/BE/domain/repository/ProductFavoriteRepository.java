package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.embedded.FavoriteProductId;
import finalproject.group1.BE.domain.entities.ProductFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, FavoriteProductId> {
    List<ProductFavorite> findByIdUserId(int userId);

    List<ProductFavorite> findByIdProductId(int productId);
}
