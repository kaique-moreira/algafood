package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import km1.algafood.domain.exceptions.GroupHasDependents;
import km1.algafood.domain.exceptions.GroupNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Group;
import km1.algafood.domain.repositories.GroupRepository;

@Service
public class GroupRegisterService implements RegisterService<Group> {

  private final GroupRepository repository;

  public GroupRegisterService(GroupRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Group register(Group entity) throws DomainException {
    try{
      entity = repository.save(entity);
    }catch(DataIntegrityViolationException e){
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  @Override
  @Transactional
  public List<Group> fetchAll() throws DomainException {
    List<Group> cities = repository.findAll();
    return cities;
  }

  @Override
  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new GroupNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new GroupHasDependents(id);
    }
  }

  @Override
  @Transactional
  public Group update(Long id, Group entity) throws DomainException {
    Group cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(entity);
  }

  @Override
  @Transactional
  public Group fetchByID(Long id) throws DomainException {
    Group city = repository.findById(id).orElseThrow(() -> new GroupNotFountException(id));
    return city;
  }
}
