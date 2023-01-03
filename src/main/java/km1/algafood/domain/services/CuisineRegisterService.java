package km1.algafood.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Cuisine;

@Service
public class CuisineRegisterService implements RegisterService<Cuisine>{

  @Override
  public Cuisine register(Cuisine entity) throws DomainException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Cuisine> fetchAll() throws DomainException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void remove(Long id) throws DomainException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Cuisine update(Long id, Cuisine entity) throws DomainException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cuisine fetchByID(Long id) throws DomainException {
    // TODO Auto-generated method stub
    return null;
  }
  
}
