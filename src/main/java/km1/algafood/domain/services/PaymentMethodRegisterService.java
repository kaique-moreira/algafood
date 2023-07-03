package km1.algafood.domain.services;

import jakarta.transaction.Transactional;
import java.util.List;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.PaymentMethodHasDependents;
import km1.algafood.domain.exceptions.PaymentMethodNotFountException;
import km1.algafood.domain.models.PaymentMethod;
import km1.algafood.domain.repositories.PaymentMethodRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentMethodRegisterService{

  private final PaymentMethodRepository repository;


  @Transactional
  public PaymentMethod register(PaymentMethod entity) throws DomainException {
    try {
      entity = repository.save(entity);
    } catch (DataIntegrityViolationException e) {
      throw new DomainException(e.getMessage());
    }
    return entity;
  }

  public List<PaymentMethod> fetchAll() throws DomainException {
    List<PaymentMethod> cuisines = repository.findAll();
    return cuisines;
  }

  public PaymentMethod fetchByID(Long id) throws DomainException {
    PaymentMethod cuisine =
        repository.findById(id).orElseThrow(() -> new PaymentMethodNotFountException(id));
    return cuisine;
  }

  @Transactional
  public void remove(Long id) throws DomainException {
    try {
      repository.deleteById(id);
      repository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new PaymentMethodNotFountException(id);
    } catch (DataIntegrityViolationException e) {
      throw new PaymentMethodHasDependents(id);
    }
  }

  @Transactional
  public PaymentMethod update(Long id, PaymentMethod entity) throws DomainException {
    PaymentMethod cuisine = this.fetchByID(id);
    BeanUtils.copyProperties(entity, cuisine, "id");
    return this.register(cuisine);
  }
}
