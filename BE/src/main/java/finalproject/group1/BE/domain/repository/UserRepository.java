package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
     Optional<User> findByEmail(String email);

     @Query("select u from User u left join u.orders as o ")
     List<User> findUserBySearchConditions();
}
