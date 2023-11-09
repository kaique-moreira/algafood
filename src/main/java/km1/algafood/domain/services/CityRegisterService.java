package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import km1.algafood.domain.exceptions.CityHasDependents;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.repositories.CityRepository;

@Service
public class CityRegisterService implements RegisterService<City> {

  private final CityRepository repository;

  public CityRegisterService(CityRepository repository) {
    this.repository = repository;
  }

  @Override
  public City register(City entity) throws DomainException {
    try{
      entity = repository.save(entity);
    }catch(DataIntegrityViolationException e){
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  @Override
  public List<City> fetchAll() throws DomainException {
    List<City> cities = repository.findAll();
    return cities;
  }

  @Override
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new CityNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new CityHasDependents(id);
    }
  }

  @Override
  public City update(Long id, City entity) throws DomainException {
    City cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(entity);
  }

  @Override
  public City fetchByID(Long id) throws DomainException {
    City city = repository.findById(id).orElseThrow(() -> new CityNotFountException(id));
    return city;
  }
}
