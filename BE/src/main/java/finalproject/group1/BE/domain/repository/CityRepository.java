package finalproject.group1.BE.domain.repository;

import finalproject.group1.BE.domain.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,Integer> {
}
