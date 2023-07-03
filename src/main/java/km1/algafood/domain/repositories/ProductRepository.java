package km1.algafood.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import km1.algafood.domain.models.Product;
import km1.algafood.domain.models.Restaurant;

public interface ProductRepository extends CustomJpaRepository<Product, Long> {
  List<Product> findByRestaurantId(Long restaurantId);

  @Query("from Product where restaurant.id = :restaurant and id = :product")
  Optional<Product> findById(
      @Param("restaurant") Long restaurantId, @Param("product") Long productId);

  @Query("from Product p where p.active = true and p.restaurant = :restaurant")
  List<Product> findActivesByRestaurant(Restaurant restaurant);

  @Query("from Product  where  restaurant = :restaurant")
  List<Product> findByRestaurant(Restaurant restaurant);
}
