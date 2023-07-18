package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findBySku(String sku);

    @Query("SELECT p FROM Product p " +
            "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
            "     LOWER(p.name) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR :searchKey IS NULL) " +
            "AND p.deleteFlag = finalproject.group1.BE.domain.enums.DeleteFlag.NORMAL")
    Page<Product> searchProductByConditions(@Param("categoryId") Integer categoryId
            , @Param("searchKey") String searchKey
            , Pageable pageable);
}
