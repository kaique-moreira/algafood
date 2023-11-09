package km1.algafood.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long>{
}
