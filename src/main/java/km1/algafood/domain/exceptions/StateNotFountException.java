package km1.algafood.domain.exceptions;

public class StateNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public StateNotFountException(String mensagem) {
    super(mensagem);
  }

  public StateNotFountException(Long stateId) {
    this(String.format("there is no state with id %d", stateId));
  }  
}
