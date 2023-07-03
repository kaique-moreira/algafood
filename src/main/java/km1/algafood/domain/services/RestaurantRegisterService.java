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
import org.springframework.beans.BeanUtils;
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

  @Transactional
  public Restaurant register(Restaurant entity) throws DomainException {
    Long cuisineId = entity.getCuisine().getId();
    Cuisine cuisine = cuisineRegisterService.fetchByID(cuisineId);

    Long cityId = entity.getAddres().getCity().getId();
    City city = cityRegisterService.fetchByID(cityId);

    entity.setCuisine(cuisine);
    entity.getAddres().setCity(city);

    entity = repository.save(entity);
    return entity;
  }

  public List<Restaurant> fetchAll() throws DomainException {
    List<Restaurant> restaurants = repository.findAll();
    return restaurants;
  }

  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new RestaurantNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new RestaurantHasDependents(id);
    }
  }

  @Transactional
  public Restaurant update(Long id, Restaurant entity) throws DomainException {
    var restaurant = this.fetchByID(id);
    BeanUtils.copyProperties(entity, restaurant, "id");
    return this.register(restaurant);
  }

  public Restaurant fetchByID(Long id) throws DomainException {
    var restaurant = repository.findById(id).orElseThrow(() -> new RestaurantNotFountException(id));
    return restaurant;
  }

  @Transactional
  public void desassociatePaymentMethod(Long restaurantId, Long paymentMethodId)
      throws DomainException {
    var restaurant = this.fetchByID(restaurantId);
    var toRemove = paymentMethodRegisterService.fetchByID(paymentMethodId);
    restaurant.removePaymentMethod(toRemove);
  }

  @Transactional
  public void associatePaymentMethod(Long restaurantId, Long paymentMethodId)
      throws DomainException {
    var restaurant = this.fetchByID(restaurantId);
    var toAdd = paymentMethodRegisterService.fetchByID(paymentMethodId);
    restaurant.addPaymentMethod(toAdd);
  }

  @Transactional
  public void active(Long id) throws DomainException {
    var restaurant = this.fetchByID(id);
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
    var restaurant = this.fetchByID(id);
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
    var restaurant = this.fetchByID(id);
    restaurant.open();
  }

  @Transactional
  public void closure(Long id) throws DomainException {
    var restaurant = this.fetchByID(id);
    restaurant.close();
  }
}
