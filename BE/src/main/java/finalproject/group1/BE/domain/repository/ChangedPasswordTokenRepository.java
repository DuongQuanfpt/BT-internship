package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.ChangedPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ChangedPasswordTokenRepository extends JpaRepository<ChangedPasswordToken,String> {

    @Query("Select cpt from ChangedPasswordToken cpt " +
            "where cpt.token = :token and cpt.expireDate >= :date")
    Optional<ChangedPasswordToken>findByTokenAndNotExpired(@Param(value = "token") String token,
            @Param(value = "date") LocalDateTime date);
}
