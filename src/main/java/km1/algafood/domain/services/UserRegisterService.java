package km1.algafood.domain.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.UserHasDependents;
import km1.algafood.domain.exceptions.UserNotFountException;
import km1.algafood.domain.models.User;
import km1.algafood.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRegisterService {

  private final UserRepository repository;
  private final GroupRegisterService groupRegisterService;

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

  @Transactional
  public void tryRemove(Long userId) throws DomainException {
    try {
      repository.deleteById(userId);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new UserNotFountException(userId);
    } catch (DataIntegrityViolationException e) {
      throw new UserHasDependents(userId);
    }
  }

  @Transactional
  public User tryFetch(Long userId) throws DomainException {
    User user = repository.findById(userId).orElseThrow(() -> new UserNotFountException(userId));
    return user;
  }

  @Transactional
  public void updatePassword(Long id, String currentPassword, String newPassword) {
    User user = this.tryFetch(id);

    if (user.passwordNotMatchesWith(currentPassword)) {
      throw new DomainException("Password informed does not matches with user passworod");
    }

    user.setPassword(newPassword);
  }

  @Transactional
  public void associateGroup(Long userId, Long groupId) {
    var user = this.tryFetch(userId);
    var toAdd = groupRegisterService.fetchByID(groupId);
    user.addGroup(toAdd);
  }

  @Transactional
  public void desassociateGroup(Long userId, Long groupId) {
    var user = this.tryFetch(userId);
    var toRemove = groupRegisterService.fetchByID(groupId);
    user.removeGroup(toRemove);
  }
}
