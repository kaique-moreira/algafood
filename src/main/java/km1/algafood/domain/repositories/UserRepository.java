package km1.algafood.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findFirstByOrderById();
}
