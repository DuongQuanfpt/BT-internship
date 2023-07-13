package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailsRepository extends JpaRepository<CartDetail,Integer> {
}
