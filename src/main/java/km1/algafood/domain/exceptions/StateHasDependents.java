package km1.algafood.domain.exceptions;


public class StateHasDependents extends EntityHasDependents{

  public StateHasDependents(String mensagem) {
    super(mensagem);
  }
  public StateHasDependents(Long id) {
    super(String.format("State with id: %d id 1 has dependent entities in the system", id));
  }
}
