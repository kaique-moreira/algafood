package km1.algafood.api.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserWithPasswordInput extends UserInput{
  @NotBlank
  private String password;

  public UserWithPasswordInput(String name, String email, @NotBlank String password) {
    super(name, email);
    this.password = password;
  }

}
