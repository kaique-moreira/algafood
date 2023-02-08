package km1.algafood.domain.exceptions;


public class UserHasDependents extends EntityHasDependents{

  public UserHasDependents(String mensagem) {
    super(mensagem);
  }
  public UserHasDependents(Long id) {
    super(String.format("User with id: %d, has dependent entities in the system", id));
  }
}
