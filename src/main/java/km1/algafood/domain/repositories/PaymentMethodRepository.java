package km1.algafood.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import km1.algafood.domain.models.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>{
  Optional<PaymentMethod> findFirstByOrderById();
}
