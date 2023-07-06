package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
     Optional<User> findByEmail(String email);
}
