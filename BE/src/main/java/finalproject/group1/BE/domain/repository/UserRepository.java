package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.web.dto.response.UserListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("select u.email as email " +
            "   ,u.id as id " +
            "   ,u.username as username " +
            "   ,u.birthday as birthday " +
            "   ,case when (o is null) then 0 else SUM(o.totalPrice) end as totalPrice " +
            " from User u left join u.orders as o " +
            " where (o is null or o is not null) and" +
            "   (:username is null or username = :username) and (:email is null or email = :email) " +
            "   and (:startDate is null or birthday >= :startDate) " +
            "   and (:endDate is null or birthday <= :endDate) " +
            "   and (:totalPrice is null or totalPrice >= :totalPrice)" +
            " group by u.id ")
    List<UserListResponse> findUserBySearchConditions(@Param("username") String username
            , @Param("email") String email, @Param("startDate") LocalDate startDate
            , @Param("endDate") LocalDate endDate, @Param("totalPrice") Float totalPrice
            , Pageable pageable);
}
