package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.matchers.UserModelMatcher.isUserModelEqualTo;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static km1.algafood.utils.UserTestBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import km1.algafood.api.assemblers.UserInputDisassembler;
import km1.algafood.api.assemblers.UserModelAssembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.PasswordInput;
import km1.algafood.api.models.UserInput;
import km1.algafood.api.models.UserModel;
import km1.algafood.api.models.UserWithPasswordInput;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.UserHasDependents;
import km1.algafood.domain.exceptions.UserNotFountException;
import km1.algafood.domain.models.User;
import km1.algafood.domain.services.UserRegisterService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private UserRegisterService registerService;
  @Mock private UserInputDisassembler disassembler;
  @Mock private UserModelAssembler assembler;
  @InjectMocks private UserController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/users";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(registerService.tryFetch(VALID_ID)).thenThrow(UserNotFountException.class);

    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.tryFetch(VALID_ID)).thenThrow(UserNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(UserNotFountException.class).when(registerService).tryRemove(VALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(UserNotFountException.class).when(registerService).tryRemove(1l);

    Problem actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .delete(PATH_VALID_ID)
            .thenReturn()
            .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnConflict_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(UserHasDependents.class).when(registerService).tryRemove(1l);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(UserHasDependents.class).when(registerService).tryRemove(1l);

    Problem actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .delete(PATH_VALID_ID)
            .thenReturn()
            .as(Problem.class);

    assertThat(actual, isConflictProblem());
  }

  @Test
  void shouldReturnNotFound_whenPutIsCalledWithInvalidId() {
    UserWithPasswordInput input = aUser().buildInputWithPassword();
    User valid = aUser().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenThrow(UserNotFountException.class);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenPutIsCalledWithInvalidId() {
    UserInput input = aUser().buildInput();
    User valid = aUser().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(UserNotFountException.class);

    Problem actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_INVALID_ID)
            .thenReturn()
            .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnUpdatedUser_whenPutIsCalledWithValidUser() {

    UserInput input = aUser().buildInput();
    User valid = aUser().build();
    User registered = aUser().withValidId().build();
    UserModel expected = aUser().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    UserModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(UserModel.class);

    assertThat(actual, isUserModelEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidUser() {
    UserInput input = aUser().buildInput();
    User valid = aUser().build();
    User registered = aUser().withValidId().build();

    when(assembler.apply(registered)).thenReturn(any());

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(1l, valid)).thenReturn(registered);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID)
        .then()
        .status(HttpStatus.OK);
  }

  @Test
  void shouldReturnCreated_whenPostIsCalledWithValidUser() {
    UserInput input = aUser().buildInput();
    User valid = aUser().build();
    User registered = aUser().withValidId().build();

    when(assembler.apply(registered)).thenReturn(any());

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .post()
        .then()
        .status(HttpStatus.CREATED);
  }

  @Test
  void shouldReturnSavedUser_whenPostIsCalledWithValidUser() {
    UserInput input = aUser().buildInput();
    User valid = aUser().build();
    User registered = aUser().withValidId().build();
    UserModel expected = aUser().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    UserModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(UserModel.class);

    assertThat(actual, isUserModelEqualTo(expected));
  }

  @Test
  void shouldReturnUser_whenGetIsCalledWithValidUserId() {
    User registered = aUser().withValidId().build();
    UserModel expected = aUser().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.tryFetch(1l)).thenReturn(registered);

    UserModel actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .get(PATH_VALID_ID)
            .thenReturn()
            .as(UserModel.class);

    assertThat(actual, isUserModelEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnUserList_whenGetIsCalled() {
    User registered = aUser().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aUser().withValidId().buildModel());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidUserId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnBadRequest_whenPostIsCalledWithBlankNameUser() {
    UserInput input = aUser().withBlankName().buildInput();

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .post()
        .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenPutIsCalledWithBlankNameUser() {
    UserInput input = aUser().withBlankName().buildInput();

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID)
        .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenPutPasswordIsCalledWithBlankCurrentPassword() {
    PasswordInput input = PasswordInput.builder().currentPassword("").newPassword("123").build();

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID.concat("/password"))
        .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenPutPasswordIsCalledWithBlankNewPassword() {
    PasswordInput input = PasswordInput.builder().currentPassword("123").newPassword("").build();

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID.concat("/password"))
        .then()
        .status(HttpStatus.BAD_REQUEST);
  }

  @Test
  void shouldReturnBadRequest_whenPutPasswordIsCalledWithNoMatchesCurrentPassword() {
    PasswordInput input = PasswordInput.builder().currentPassword("123").newPassword("123").build();
    
    doThrow(DomainException.class).when(registerService).updatePassword(VALID_ID, input.getCurrentPassword(), input.getNewPassword());

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID.concat("/password"))
        .then()
        .status(HttpStatus.BAD_REQUEST);
  }
}
