package km1.algafood.domain.exceptions;

public class RestaurantNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public RestaurantNotFountException(String mensagem) {
    super(mensagem);
  }

  public RestaurantNotFountException(Long id) {
    this(String.format("there is no restaurant with id %d", id));
  }  
}
