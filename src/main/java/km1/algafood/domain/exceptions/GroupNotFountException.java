package km1.algafood.domain.exceptions;

public class GroupNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public GroupNotFountException(String mensagem) {
    super(mensagem);
  }

  public GroupNotFountException(Long cityId) {
    this(String.format("there is no city with id %d", cityId));
  }  
}
