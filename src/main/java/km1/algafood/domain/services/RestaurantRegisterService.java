package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.RestaurantHasDependents;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.repositories.RestaurantRepository;

@Service
public class RestaurantRegisterService implements RegisterService<Restaurant> {

  private final RestaurantRepository repository;

  public RestaurantRegisterService(RestaurantRepository repository) {
    this.repository = repository;
  }

  @Override
  public Restaurant register(Restaurant entity) throws DomainException {
    try {
      entity = repository.save(entity);
    } catch (DataIntegrityViolationException e){
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  @Override
  public List<Restaurant> fetchAll() throws DomainException {
    List<Restaurant> restaurants = repository.findAll();
    return restaurants;
  }

  @Override
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new RestaurantNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new RestaurantHasDependents(id);
    }
  }

  @Override
  public Restaurant update(Long id, Restaurant entity) throws DomainException {
    Restaurant restaurant = this.fetchByID(id);
    BeanUtils.copyProperties(entity, restaurant, "id");
    return this.register(restaurant);
  }

  @Override
  public Restaurant fetchByID(Long id) throws DomainException {
    Restaurant restaurant = repository.findById(id).orElseThrow(() -> new RestaurantNotFountException(id));
    return restaurant;
  }
}
