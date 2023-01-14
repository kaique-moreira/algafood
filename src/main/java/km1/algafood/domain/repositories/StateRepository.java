package km1.algafood.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.State;

public interface StateRepository extends JpaRepository<State, Long>{
}
