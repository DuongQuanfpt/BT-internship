package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetail,Integer> {
    @Query("Select case when (SUM(cd.totalPrice) is null) then 0 else SUM(cd.totalPrice) end " +
            "from CartDetail cd where cd.cart.id = :cartId")
    float sumTotalPriceByCartId(int cartId);

    @Query("Select case when (SUM(cd.quantity) is null) then 0 else SUM(cd.quantity) end " +
            "from CartDetail cd where cd.cart.id = :cartId")
    int sumQuantityByCardId(int cartId);

    Optional<CartDetail> findByProductIdAndCartId(int productId , int cartId);

    List<CartDetail> findByCartId(int cartId);

    @Modifying
    @Query("DELETE FROM CartDetail cd WHERE cd.cart.id = :cartId")
    void deleteByCartId(int cartId);
}
