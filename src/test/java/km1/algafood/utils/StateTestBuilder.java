package km1.algafood.utils;

import km1.algafood.api.models.StateModel;
import km1.algafood.api.models.StateInput;
import km1.algafood.domain.models.State;

public class StateTestBuilder {
  private State state = State.builder().name("").build();
  public static StateTestBuilder aState(){
    return new StateTestBuilder();
  }

  public  StateTestBuilder withValidId(){
    this.state.setId(1l);
    return this;
  }

  public StateTestBuilder withNullName(){
    this.state.setName(null);
    return this;
  }

  public State build(){
    return this.state;
  }

  public StateModel buildModel(){
    return StateModel.builder().name(this.state.getName()).id(this.state.getId()).build();
  }

  public StateInput buildInput(){
    return StateInput.builder().name(this.state.getName()).build();
  }
}
