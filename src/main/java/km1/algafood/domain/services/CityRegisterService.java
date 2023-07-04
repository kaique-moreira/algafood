package km1.algafood.domain.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import km1.algafood.domain.exceptions.CityHasDependents;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.repositories.CityRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CityRegisterService {

  private final CityRepository repository;

  @Transactional
  public City register(City city) throws DomainException {
    return repository.save(city);
  }


  @Transactional
  public void tryRemove(Long cityId) throws DomainException {
    try {
      repository.deleteById(cityId);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new CityNotFountException(cityId);
    } catch (DataIntegrityViolationException e) {
      throw new CityHasDependents(cityId);
    }
  }

  @Transactional
  public City tryFetch(Long cityId) throws DomainException {
    City city = repository.findById(cityId).orElseThrow(() -> new CityNotFountException(cityId));
    return city;
  }
}
