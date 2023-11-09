package km1.algafood.domain.exceptions;


public class GroupHasDependents extends EntityHasDependents{

  public GroupHasDependents(String mensagem) {
    super(mensagem);
  }
  public GroupHasDependents(Long id) {
    super(String.format("Group with id: %d id 1 has dependent entities in the system", id));
  }
}
