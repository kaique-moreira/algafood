package km1.algafood.domain.exceptions;

public class ProductNotFountException extends EntityNotFoundException{
  private static final long serialVersionUID = 1L;

  public ProductNotFountException(String mensagem) {
    super(mensagem);
  }

  public ProductNotFountException(Long productId, Long restaurantId) {
    this(String.format("there is no product with id %d in restaurant with id %d", productId, restaurantId));
  }  
}
