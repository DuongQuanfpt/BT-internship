package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.Product;
import finalproject.group1.BE.web.dto.response.product.ProductListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findBySku(String sku);

    @Query("SELECT p FROM Product p " +
            "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (LOWER(p.sku) LIKE LOWER(CONCAT('%', :skuKeyword, '%')) OR " +
            "     LOWER(p.name) LIKE LOWER(CONCAT('%', :nameKeyword, '%'))) ")
    List<ProductListResponse> searchProductByConditions(@Param("categoryId") Integer categoryId
            , @Param("skuKeyword") String skuKeyword
            , @Param("nameKeyword") String nameKeyword
            , Pageable pageable);

}
