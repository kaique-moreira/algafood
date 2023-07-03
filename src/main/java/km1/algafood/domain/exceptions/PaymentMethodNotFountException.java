package km1.algafood.domain.exceptions;

public class PaymentMethodNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public PaymentMethodNotFountException(String mensagem) {
    super(mensagem);
  }

  public PaymentMethodNotFountException(Long id) {
    this(String.format("there is no permission with id %d", id));
  }  
}
