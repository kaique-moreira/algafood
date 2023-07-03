package km1.algafood.domain.exceptions;

public class PermissionNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public PermissionNotFountException(String mensagem) {
    super(mensagem);
  }

  public PermissionNotFountException(Long permissionId) {
    this(String.format("there is no permission with id %d", permissionId));
  }  
}
