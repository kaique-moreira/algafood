package km1.algafood.domain.services;

import java.util.List;

import km1.algafood.domain.exceptions.DomainException;

public interface RegisterService<T>{
  T register(T entity) throws DomainException;
  List<T> fetchAll()  throws DomainException;
  T fetchByID(Long id)  throws DomainException;
  void remove(Long id)  throws DomainException;
  T update(Long id, T entity)  throws DomainException;
}
