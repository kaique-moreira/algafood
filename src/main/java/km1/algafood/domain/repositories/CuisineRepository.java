package km1.algafood.domain.repositories;

import java.util.Optional;

import km1.algafood.domain.models.Cuisine;

public interface CuisineRepository extends CustomJpaRepository<Cuisine, Long>{
  Optional<Cuisine> findFirstByOrderById();
}
