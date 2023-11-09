package km1.algafood.utils;

import km1.algafood.api.models.UserInput;
import km1.algafood.api.models.UserModel;
import km1.algafood.api.models.UserWithPasswordInput;
import km1.algafood.domain.models.User;

public class UserTestBuilder {
  private static final long VALID_ID = 1l;
  private User user =
      User.builder().name("test").email("test@gmail.com").password("senhaforte123").build();

  public static UserTestBuilder aUser() {
    return new UserTestBuilder();
  }

  public UserTestBuilder withValidId() {
    this.user.setId(VALID_ID);
    return this;
  }

  public UserTestBuilder withNullId() {
    this.user.setId(null);
    return this;
  }

  public UserTestBuilder withNullName() {
    this.user.setName(null);
    return this;
  }

  public UserTestBuilder withBlankName() {
    this.user.setName("");
    return this;
  }

  public UserTestBuilder withNullEmail() {
    this.user.setEmail(null);
    return this;
  }

  public UserTestBuilder withBlankEmail() {
    this.user.setEmail("");
    return this;
  }

  public UserTestBuilder withNullPassword() {
    this.user.setPassword(null);
    return this;
  }

  public UserTestBuilder withBlankPassword() {
    this.user.setPassword("");
    return this;
  }

  public User build() {
    return this.user;
  }

  public UserInput buildInput() {
    return UserInput.builder().name(this.user.getName()).email(this.user.getEmail()).build();
  }

  public UserWithPasswordInput buildInputWithPassword() {
    return new UserWithPasswordInput(
        this.user.getName(), this.user.getEmail(), this.user.getPassword());
  }

  public UserModel buildModel() {
    return UserModel.builder().name(this.user.getName()).email(this.user.getEmail()).id(this.user.getId()).build();
  }
}
