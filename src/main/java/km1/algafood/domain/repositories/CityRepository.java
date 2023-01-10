package km1.algafood.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.City;

public interface CityRepository extends JpaRepository<City, Long>{
}
