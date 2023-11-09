package km1.algafood.domain.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.StateHasDependents;
import km1.algafood.domain.exceptions.StateNotFountException;
import km1.algafood.domain.models.State;
import km1.algafood.domain.repositories.StateRepository;

@Service
public class StateRegisterService implements RegisterService<State> {

  private final StateRepository repository;

  public StateRegisterService(StateRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public State register(State entity) throws DomainException {
    try{
      entity = repository.save(entity);
    }catch (DataIntegrityViolationException e){
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  @Override
  @Transactional
  public List<State> fetchAll() throws DomainException {
    List<State> states = repository.findAll();
    return states;
  }

  @Override
  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);

    } catch (EmptyResultDataAccessException e) {
      throw new StateNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new StateHasDependents(id);
    }
  }

  @Override
  @Transactional
  public State update(Long id, State entity) throws DomainException {
    State state = this.fetchByID(id);
    BeanUtils.copyProperties(entity, state, "id");
    return this.register(state);
  }

  @Override
  @Transactional
  public State fetchByID(Long id) throws DomainException {
    State state = repository.findById(id).orElseThrow(() -> new StateNotFountException(id));
    return state;
  }
}
