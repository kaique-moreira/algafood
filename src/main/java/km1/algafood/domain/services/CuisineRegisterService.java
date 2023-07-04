package km1.algafood.domain.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import km1.algafood.domain.exceptions.CuisineHasDependents;
import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.repositories.CuisineRepository;

@Service
public class CuisineRegisterService {

  private final CuisineRepository repository;

  public CuisineRegisterService(CuisineRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public Cuisine register(Cuisine cuisine) throws DomainException {
    try {
      cuisine = repository.save(cuisine);    
    } catch (DataIntegrityViolationException e) {
      throw new DomainException(e.getMessage());
    }
    return cuisine;
  }

  @Transactional
  public void tryRemove(Long cuisineId) throws DomainException {
    try {
      repository.deleteById(cuisineId);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new CuisineNotFountException(cuisineId);
    } catch (DataIntegrityViolationException e) {
      throw new CuisineHasDependents(cuisineId);
    }
  }

  @Transactional
  public Cuisine tryFetch(Long cuisineId) throws DomainException {
    Cuisine cuisine = repository.findById(cuisineId).orElseThrow(() -> new CuisineNotFountException(cuisineId));
    return cuisine;
  }
}
