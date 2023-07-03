package km1.algafood.domain.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.UserHasDependents;
import km1.algafood.domain.exceptions.UserNotFountException;
import km1.algafood.domain.models.User;
import km1.algafood.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegisterService implements RegisterService<User> {

  private final UserRepository repository;
  private final GroupRegisterService groupRegisterService;

  @Override
  @Transactional
  public User register(User user) throws DomainException {
    repository.detach(user);
    Optional<User> existedUser = repository.findByEmail(user.getEmail());
    if (existedUser.isPresent() && !existedUser.get().equals(user)) {
      throw new DomainException("");
    }
    try {
      user = repository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new DomainException(e.getMessage());
    }
    return user;
  }

  @Override
  @Transactional
  public List<User> fetchAll() throws DomainException {
    List<User> users = repository.findAll();
    return users;
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
    User user = this.fetchByID(id);
    BeanUtils.copyProperties(entity, user, "id");
    return this.register(user);
  }

  @Override
  @Transactional
  public User fetchByID(Long id) throws DomainException {
    User user = repository.findById(id).orElseThrow(() -> new UserNotFountException(id));
    return user;
  }

  @Transactional
  public void updatePassword(Long id, String currentPassword, String newPassword) {
    User user = this.fetchByID(id);

    if (user.passwordNotMatchesWith(currentPassword)) {
      throw new DomainException("Password informed does not matches with user passworod");
    }

    user.setPassword(newPassword);
  }

  @Transactional
  public void associateGroup(Long userId, Long groupId) {
    var user = this.fetchByID(userId);
    var toAdd = groupRegisterService.fetchByID(groupId);
    user.addGroup(toAdd);
  }

  @Transactional
  public void desassociateGroup(Long userId, Long groupId) {
    var user = this.fetchByID(userId);
    var toRemove = groupRegisterService.fetchByID(groupId);
    user.removeGroup(toRemove);
  }
}
