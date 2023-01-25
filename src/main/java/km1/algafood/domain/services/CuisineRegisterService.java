package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import km1.algafood.domain.exceptions.CuisineHasDependents;
import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.repositories.CuisineRepository;

@Service
public class CuisineRegisterService implements RegisterService<Cuisine> {

  private final CuisineRepository repository;

  public CuisineRegisterService(CuisineRepository repository) {
    this.repository = repository;
  }

  @Override
  public Cuisine register(Cuisine entity) throws DomainException {
    try {
      entity = repository.save(entity);    
    } catch (DataIntegrityViolationException e) {
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  @Override
  public List<Cuisine> fetchAll() throws DomainException {
    List<Cuisine> cuisines = repository.findAll();
    return cuisines;
  }

  @Override
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new CuisineNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new CuisineHasDependents(id);
    }
  }

  @Override
  public Cuisine update(Long id, Cuisine entity) throws DomainException {
    Cuisine cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(cuisine);
  }

  @Override
  public Cuisine fetchByID(Long id) throws DomainException {
    Cuisine cuisine = repository.findById(id).orElseThrow(() -> new CuisineNotFountException(id));
    return cuisine;
  }
}
