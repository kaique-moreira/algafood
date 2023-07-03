package km1.algafood.domain.exceptions;


public class PermissionHasDependents extends EntityHasDependents{

  public PermissionHasDependents(String mensagem) {
    super(mensagem);
  }
  public PermissionHasDependents(Long id) {
    super(String.format("Permission with id: %d id 1 has dependent entities in the system", id));
  }
}
