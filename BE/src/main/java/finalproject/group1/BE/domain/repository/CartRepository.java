package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByOwnerId(int id);

    Optional<Cart> findByToken(String token);

}
