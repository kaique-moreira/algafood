package km1.algafood.domain.exceptions;


public class CityHasDependents extends EntityHasDependents{

  public CityHasDependents(String mensagem) {
    super(mensagem);
  }
  public CityHasDependents(Long id) {
    super(String.format("City with id: %d id 1 has dependent entities in the system", id));
  }
}
