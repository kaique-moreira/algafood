package km1.algafood.domain.services;

import jakarta.transaction.Transactional;
import java.util.List;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.UserHasDependents;
import km1.algafood.domain.exceptions.UserNotFountException;
import km1.algafood.domain.models.User;
import km1.algafood.domain.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService implements RegisterService<User> {

  private final UserRepository repository;

  public UserRegisterService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public User register(User entity) throws DomainException {
    try {
      entity = repository.save(entity);
    } catch (DataIntegrityViolationException e) {
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  @Override
  @Transactional
  public List<User> fetchAll() throws DomainException {
    List<User> cuisines = repository.findAll();
    return cuisines;
  }

  @Override
  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new UserNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new UserHasDependents(id);
    }
  }

  @Override
  @Transactional
  public User update(Long id, User entity) throws DomainException {
    User cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(cuisine);
  }

  @Override
  @Transactional
  public User fetchByID(Long id) throws DomainException {
    User cuisine = repository.findById(id).orElseThrow(() -> new UserNotFountException(id));
    return cuisine;
  }

  @Transactional
  public void updatePassword(Long id, String currentPassword, String newPassword) {
    User user = this.fetchByID(id);

    if (user.passwordNotMatchesWith(currentPassword)) {
      throw new DomainException("Password informed does not matches with user passworod");
    }

    user.setPassword(newPassword);
  }
}
