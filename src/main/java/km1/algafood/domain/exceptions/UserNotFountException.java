package km1.algafood.domain.exceptions;

public class UserNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public UserNotFountException(String mensagem) {
    super(mensagem);
  }

  public UserNotFountException(Long cuisineId) {
    this(String.format("there is no cuisine with id %d", cuisineId));
  }  
}
