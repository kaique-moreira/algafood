package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import km1.algafood.domain.exceptions.PermissionHasDependents;
import km1.algafood.domain.exceptions.PermissionNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Permission;
import km1.algafood.domain.repositories.PermissionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PermissionRegisterService {

  private final PermissionRepository repository;


  @Transactional
  public Permission register(Permission entity) throws DomainException {
    return repository.save(entity);
  }

  @Transactional
  public List<Permission> fetchAll() throws DomainException {
    List<Permission> cities = repository.findAll();
    return cities;
  }

  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new PermissionNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new PermissionHasDependents(id);
    }
  }

  @Transactional
  public Permission update(Long id, Permission entity) throws DomainException {
    Permission cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(entity);
  }

  @Transactional
  public Permission fetchByID(Long id) throws DomainException {
    Permission city = repository.findById(id).orElseThrow(() -> new PermissionNotFountException(id));
    return city;
  }
}
