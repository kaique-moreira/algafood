package km1.algafood.domain.exceptions;


public class OrderHasDependents extends EntityHasDependents{

  public OrderHasDependents(String mensagem) {
    super(mensagem);
  }
  public OrderHasDependents(Long id) {
    super(String.format("Order with id: %d id 1 has dependent entities in the system", id));
  }
}
