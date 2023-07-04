package km1.algafood.domain.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.StateHasDependents;
import km1.algafood.domain.exceptions.StateNotFountException;
import km1.algafood.domain.models.State;
import km1.algafood.domain.repositories.StateRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StateRegisterService {

  private final StateRepository repository;


  @Transactional
  public State register(State state) throws DomainException {
    try{
      state = repository.save(state);
    }catch (DataIntegrityViolationException e){
      throw new DomainException(e.getMessage());
    }
    return state;
  }

  @Transactional
  public void tryRemove(Long stateId) throws DomainException {
    try {
      repository.deleteById(stateId);

    } catch (EmptyResultDataAccessException e) {
      throw new StateNotFountException(stateId);
    } catch (DataIntegrityViolationException e) {
      throw new StateHasDependents(stateId);
    }
  }


  @Transactional
  public State tryFetch(Long stateId) throws DomainException {
    State state = repository.findById(stateId).orElseThrow(() -> new StateNotFountException(stateId));
    return state;
  }
}
