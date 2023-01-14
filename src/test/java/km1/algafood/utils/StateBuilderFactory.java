package km1.algafood.utils;

import km1.algafood.domain.models.State;
import km1.algafood.domain.models.State.StateBuilder;

public class StateBuilderFactory {
  public static StateBuilder validState(){
    return State.builder().id(null).name("");
  }
  public static StateBuilder registeredState(){
    return State.builder().id(null).name("");
  }
}
