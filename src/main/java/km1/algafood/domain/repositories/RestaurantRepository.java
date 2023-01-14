package km1.algafood.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
}
