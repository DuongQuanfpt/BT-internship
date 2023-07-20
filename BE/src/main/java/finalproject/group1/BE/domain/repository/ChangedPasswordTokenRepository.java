package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.ChangedPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangedPasswordTokenRepository extends JpaRepository<ChangedPasswordToken,String> {
}
