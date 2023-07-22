package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetail,Integer> {
    @Query("Select COALESCE(SUM(cd.totalPrice),0) " +
            "from CartDetail cd where cd.cart.id = :cartId")
    float sumTotalPriceByCartId(int cartId);

    @Query("Select COALESCE(SUM(cd.quantity),0) " +
            "from CartDetail cd where cd.cart.id = :cartId")
    int sumQuantityByCardId(int cartId);

    Optional<CartDetail> findByProductIdAndCartId(int productId , int cartId);

    List<CartDetail> findByCartId(int cartId);

}
