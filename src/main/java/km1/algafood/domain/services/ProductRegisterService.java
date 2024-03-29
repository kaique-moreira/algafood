package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import km1.algafood.domain.exceptions.ProductNotFountException;
import km1.algafood.domain.models.Product;
import km1.algafood.domain.repositories.ProductRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductRegisterService {

  private final ProductRepository repository;
  private final RestaurantRegisterService rRegisterService;

  @Transactional
  public Product register(Long restaurantId, Product product) {
    product.setRestaurant(rRegisterService.tryFetch(restaurantId));
    return repository.save(product);
  }


  @Transactional
  public List<Product> fetchByRestaurantId(Long restaurantId) {
    var restaurant = rRegisterService.tryFetch(restaurantId);
    return repository.findByRestaurant(restaurant);
  }

  @Transactional
  public List<Product> fetchActivesByRestaurantId(Long restaurantId) {
    var restaurant = rRegisterService.tryFetch(restaurantId);
    return repository.findActivesByRestaurant(restaurant);
  }


  @Transactional
  public Product fetchById(Long restaurantId, Long productId) {
    return repository
        .findById(restaurantId, productId)
        .orElseThrow(() -> new ProductNotFountException(productId, restaurantId));
  }

public Product update(Long restaurantId, Long productId, Product entity) {
    var product = this.fetchById(restaurantId, productId);
    BeanUtils.copyProperties(entity,product, "id");
    return this.register(restaurantId, product);
}
}
