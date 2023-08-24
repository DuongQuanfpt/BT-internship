package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView,Integer> {
    long countByProductId(int productId);

    long countByProductIdAndDateAfter(int productId, LocalDateTime date);
}
