package km1.algafood.domain.repositories;

import java.util.Optional;


import km1.algafood.domain.models.User;

public interface UserRepository extends CustomJpaRepository<User, Long>{
  Optional<User> findByEmail(String email);
}
