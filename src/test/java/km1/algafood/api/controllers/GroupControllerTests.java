package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.GroupModelMatcher.isGroupModelEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.GroupTestBuilder.aGroup;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import km1.algafood.api.assemblers.GroupInputDisassembler;
import km1.algafood.api.assemblers.GroupModelAssembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.GroupInput;
import km1.algafood.api.models.GroupModel;
import km1.algafood.core.validation.ValidationConfig;
import km1.algafood.domain.exceptions.GroupHasDependents;
import km1.algafood.domain.exceptions.GroupNotFountException;
import km1.algafood.domain.models.Group;
import km1.algafood.domain.services.GroupRegisterService;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private GroupRegisterService registerService;
  @Mock private GroupInputDisassembler disassembler;
  @Mock private GroupModelAssembler assembler;
  @InjectMocks private GroupController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/groups";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    ValidationConfig validationConfig = new ValidationConfig();
    exceptionHandler.setMessageSource(validationConfig.messageSource());
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(GroupNotFountException.class);

    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(GroupNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(GroupNotFountException.class).when(registerService).remove(VALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(GroupNotFountException.class).when(registerService).remove(INVALID_ID);

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
    doThrow(GroupHasDependents.class).when(registerService).remove(VALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(GroupHasDependents.class).when(registerService).remove(VALID_ID);

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
  void shouldReturnNotFound_whenPutIsCalledWithInvalidId() throws JSONException {
    GroupInput input = aGroup().buildInput();
    Group valid = aGroup().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(GroupNotFountException.class);

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
    GroupInput input = aGroup().buildInput();
    Group valid = aGroup().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(GroupNotFountException.class);

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
  void shouldReturnUpdatedGroup_whenPutIsCalledWithValidGroup() {

    GroupInput input = aGroup().buildInput();
    Group valid = aGroup().build();
    Group registered = aGroup().withValidId().build();
    GroupModel expected = aGroup().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    GroupModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(GroupModel.class);

    assertThat(actual, isGroupModelEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidGroup() {
    GroupInput input = aGroup().buildInput();
    Group valid = aGroup().build();
    Group registered = aGroup().withValidId().build();

    when(assembler.apply(registered)).thenReturn(any());

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

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
  void shouldReturnCreated_whenPostIsCalledWithValidGroup() {
    GroupInput input = aGroup().buildInput();
    Group valid = aGroup().build();
    Group registered = aGroup().withValidId().build();

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
  void shouldReturnSavedGroup_whenPostIsCalledWithValidGroup() {
    GroupInput input = aGroup().buildInput();
    Group valid = aGroup().build();
    Group registered = aGroup().withValidId().build();
    GroupModel expected = aGroup().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    GroupModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(GroupModel.class);

    assertThat(actual, isGroupModelEqualTo(expected));
  }

  @Test
  void shouldReturnGroup_whenGetIsCalledWithValidGroupId() {
    Group registered = aGroup().withValidId().build();
    GroupModel expected = aGroup().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.fetchByID(VALID_ID)).thenReturn(registered);

    GroupModel actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(GroupModel.class);

    assertThat(actual, isGroupModelEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnGroupList_whenGetIsCalled() {
    Group registered = aGroup().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aGroup().withValidId().buildModel());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidGroupId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnBadRequest_whenPostIsCalledWithBlankNameGroup(){
    GroupInput input = aGroup().withBlankName().buildInput();

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
  void shouldReturnBadRequest_whenPutIsCalledWithBlankNameGroup(){
    GroupInput input = aGroup().withBlankName().buildInput();

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
  void shouldReturnBadRequest_whenPostIsCalledWithGroupInputPermissionsContainsNullId(){
    var input = aGroup().withNullIdPermission().build();
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
  void shouldReturnBadRequest_whenPutIsCalledWithGroupInputPermissionsContainsNullId(){
    var input = aGroup().withNullIdPermission().build();
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(toJson(input))
        .when()
        .put(PATH_VALID_ID)
        .then()
        .status(HttpStatus.BAD_REQUEST);
  }
}
