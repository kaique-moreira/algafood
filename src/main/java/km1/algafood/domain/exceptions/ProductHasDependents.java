package km1.algafood.domain.exceptions;


public class ProductHasDependents extends EntityHasDependents{

  public ProductHasDependents(String mensagem) {
    super(mensagem);
  }
  public ProductHasDependents(Long id) {
    super(String.format("Product with id: %d id 1 has dependent entities in the system", id));
  }
}
