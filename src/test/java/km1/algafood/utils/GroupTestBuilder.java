package km1.algafood.utils;

import java.util.Collections;

import static km1.algafood.utils.PermissionTestBuilder.aPermission;

import km1.algafood.api.models.GroupInput;
import km1.algafood.api.models.GroupModel;
import km1.algafood.domain.models.Group;

public class GroupTestBuilder {
  private Group city =
      Group.builder().name("São Paulo").permissions(Collections.singletonList(aPermission().build())).build();

  public static GroupTestBuilder aGroup() {
    return new GroupTestBuilder();
  }

  public GroupTestBuilder withValidId() {
    this.city.setId(1l);
    return this;
  }

  public GroupTestBuilder withNullName() {
    this.city.setName(null);
    return this;
  }

  public GroupTestBuilder withBlankName() {
    this.city.setName("");
    return this;
  }
 
  public GroupTestBuilder withNullId(){
    this.city.setId(null);
    return this;
  }

  public GroupTestBuilder withNullIdPermission(){
      this.city.getPermissions().forEach(permission -> permission.setId(null));
      return this;
  }
  public Group build() {
    return this.city;
  }

  public GroupInput buildInput() {
    return GroupInput.builder()
        .name(this.city.getName())
        .build();
  }

  public GroupModel buildModel() {
    return GroupModel.builder()
        .name(this.city.getName())
        .build();
  }
}
