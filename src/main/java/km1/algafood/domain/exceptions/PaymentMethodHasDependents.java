package km1.algafood.domain.exceptions;


public class PaymentMethodHasDependents extends EntityHasDependents{

  public PaymentMethodHasDependents(String mensagem) {
    super(mensagem);
  }
  public PaymentMethodHasDependents(Long id) {
    super(String.format("PaymentMethod with id: %d, has dependent entities in the system", id));
  }
}
