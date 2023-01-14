package km1.algafood.domain.exceptions;

public class EntityNotFoundException extends DomainException {
  private static final long serialVersionUID = 1L;

  public EntityNotFoundException(String mensagem) {
    super(mensagem);
  }
}
