package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
  @Transactional
  public City register(City entity) throws DomainException {
    return repository.save(entity);
  }

  @Override
  @Transactional
  public List<City> fetchAll() throws DomainException {
    List<City> cities = repository.findAll();
    return cities;
  }

  @Override
  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new CityNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new CityHasDependents(id);
    }
  }

  @Override
  @Transactional
  public City update(Long id, City entity) throws DomainException {
    City cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(entity);
  }

  @Override
  @Transactional
  public City fetchByID(Long id) throws DomainException {
    City city = repository.findById(id).orElseThrow(() -> new CityNotFountException(id));
    return city;
  }
}
