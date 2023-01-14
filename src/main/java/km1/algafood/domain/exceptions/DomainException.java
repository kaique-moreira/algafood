package km1.algafood.domain.exceptions;

public class DomainException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DomainException(String mensagem) {
    super(mensagem);
  }

  public DomainException(String mensagem, Throwable cause) {
    super(mensagem, cause );
  }
}
