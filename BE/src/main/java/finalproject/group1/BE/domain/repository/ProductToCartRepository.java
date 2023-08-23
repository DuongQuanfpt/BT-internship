package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.ProductToCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductToCartRepository extends JpaRepository<ProductToCart,Integer> {
}
