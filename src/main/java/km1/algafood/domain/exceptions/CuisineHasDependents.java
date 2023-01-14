package km1.algafood.domain.exceptions;


public class CuisineHasDependents extends EntityHasDependents{

  public CuisineHasDependents(String mensagem) {
    super(mensagem);
  }
  public CuisineHasDependents(Long id) {
    super(String.format("Cuisine with id: %d id 1 has dependent entities in the system", id));
  }
}
