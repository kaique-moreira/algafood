package km1.algafood.domain.exceptions;


public class RestaurantHasDependents extends EntityHasDependents{

  public RestaurantHasDependents(String mensagem) {
    super(mensagem);
  }
  public RestaurantHasDependents(Long id) {
    super(String.format("Restaurant with id: %d id 1 has dependent entities in the system", id));
  }
}
