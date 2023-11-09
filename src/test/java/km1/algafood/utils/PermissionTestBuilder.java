package km1.algafood.utils;

import km1.algafood.domain.models.Permission;

public class PermissionTestBuilder {
  private Permission permission =
  Permission.builder().name("").description("").build();

  public static PermissionTestBuilder aPermission() {
    return new PermissionTestBuilder();
  }

  public PermissionTestBuilder withValidId() {
    this.permission.setId(1l);
    return this;
  }

  public PermissionTestBuilder withNullName() {
    this.permission.setName(null);
    return this;
  }

  public PermissionTestBuilder withNullId(){
    this.permission.setId(null);
    return this;
  }

  public Permission build() {
    return this.permission;
  }
}
