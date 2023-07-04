package km1.algafood.domain.services;

import java.util.List;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.RestaurantHasDependents;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.repositories.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RestaurantRegisterService {

  private final RestaurantRepository repository;
  private final CuisineRegisterService cuisineRegisterService;
  private final CityRegisterService cityRegisterService;
  private final PaymentMethodRegisterService paymentMethodRegisterService;
  private final UserRegisterService userRegisterService;

  @Transactional
  public Restaurant register(Restaurant restaurant) throws DomainException {
    Long cuisineId = restaurant.getCuisine().getId();
    Cuisine cuisine = cuisineRegisterService.tryFetch(cuisineId);

    Long cityId = restaurant.getAddres().getCity().getId();
    City city = cityRegisterService.tryFetch(cityId);

    restaurant.setCuisine(cuisine);
    restaurant.getAddres().setCity(city);

    restaurant = repository.save(restaurant);
    return restaurant;
  }

  @Transactional
  public void tryRemove(Long restaurantId) throws DomainException {
    try {
      repository.deleteById(restaurantId);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new RestaurantNotFountException(restaurantId);
    } catch (DataIntegrityViolationException e) {
      throw new RestaurantHasDependents(restaurantId);
    }
  }

  public Restaurant tryFetch(Long restaurantId) throws DomainException {
    var restaurant =
        repository
            .findById(restaurantId)
            .orElseThrow(() -> new RestaurantNotFountException(restaurantId));
    return restaurant;
  }

  @Transactional
  public void desassociatePaymentMethod(Long restaurantId, Long paymentMethodId)
      throws DomainException {
    var restaurant = this.tryFetch(restaurantId);
    var toRemove = paymentMethodRegisterService.fetchByID(paymentMethodId);
    restaurant.removePaymentMethod(toRemove);
  }

  @Transactional
  public void associatePaymentMethod(Long restaurantId, Long paymentMethodId)
      throws DomainException {
    var restaurant = this.tryFetch(restaurantId);
    var toAdd = paymentMethodRegisterService.fetchByID(paymentMethodId);
    restaurant.addPaymentMethod(toAdd);
  }

  @Transactional
  public void associateUser(Long restaurantId, Long userId) {
    var restaurant = this.tryFetch(restaurantId);
    var toAdd = userRegisterService.tryFetch(userId);
    restaurant.addMember(toAdd);
  }

  @Transactional
  public void desassociateUser(Long restaurantId, Long userId) {
    var restaurant = this.tryFetch(restaurantId);
    var toRemove = userRegisterService.tryFetch(userId);
    restaurant.removeMember(toRemove);
  }

  @Transactional
  public void active(Long id) throws DomainException {
    var restaurant = this.tryFetch(id);
    restaurant.active();
  }

  @Transactional
  public void active(List<Long> ids) throws DomainException {
    try {
      ids.forEach(this::active);

    } catch (RestaurantNotFountException e) {
      throw new DomainException(e.getMessage(), e);
    }
  }

  @Transactional
  public void disactive(Long id) throws DomainException {
    var restaurant = this.tryFetch(id);
    restaurant.disactive();
  }

  @Transactional
  public void disactive(List<Long> ids) throws DomainException {
    try {
      ids.forEach(this::disactive);

    } catch (RestaurantNotFountException e) {
      throw new DomainException(e.getMessage(), e);
    }
  }

  @Transactional
  public void opening(Long id) throws DomainException {
    var restaurant = this.tryFetch(id);
    restaurant.open();
  }

  @Transactional
  public void closure(Long id) throws DomainException {
    var restaurant = this.tryFetch(id);
    restaurant.close();
  }
}
