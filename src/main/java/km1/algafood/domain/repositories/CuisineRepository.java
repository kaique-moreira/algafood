package km1.algafood.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.Cuisine;

public interface CuisineRepository extends JpaRepository<Cuisine, Long>{
}
