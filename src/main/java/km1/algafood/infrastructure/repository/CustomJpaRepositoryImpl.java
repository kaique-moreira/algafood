package km1.algafood.infrastructure.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import km1.algafood.domain.repositories.CustomJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
	implements CustomJpaRepository<T, ID> {

	private EntityManager manager;
	
	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, 
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		
		this.manager = entityManager;
	}

	@Override
	public void detach(T entity) {
		manager.detach(entity);
	}

  @Override
  public Optional<T> findFirst() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findFirst'");
  }

}
