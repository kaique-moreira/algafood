package km1.algafood.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import km1.algafood.domain.models.Order;

@Repository
public interface OrderRepository extends CustomJpaRepository<Order,Long>{
  @Query("from Order o join fetch o.client join fetch o.restaurant r join fetch r.cuisine")
  List<Order> findAll(); 
  Optional<Order> findByCode(String code);
}
