package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District,Integer> {
    Optional<District> findByIdAndCityId(int id , int cityId);
}
