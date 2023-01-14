package km1.algafood.domain.exceptions;

public class CityNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public CityNotFountException(String mensagem) {
    super(mensagem);
  }

  public CityNotFountException(Long cityId) {
    this(String.format("there is no city with id %d", cityId));
  }  
}
