package km1.algafood.domain.exceptions;

public class OrderNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public OrderNotFountException(String code) {
    super(String.format("there is no order with id %s", code));
  }  
}
