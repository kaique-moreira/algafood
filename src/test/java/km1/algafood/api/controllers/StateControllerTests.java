package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.StateDtoMatcher.isStateDtoEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.StateTestBuilder.aState;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.util.Collections;
import km1.algafood.api.assemblers.StateDtoAssembler;
import km1.algafood.api.assemblers.StateInputDisassembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.StateDto;
import km1.algafood.api.models.StateInput;
import km1.algafood.domain.exceptions.StateHasDependents;
import km1.algafood.domain.exceptions.StateNotFountException;
import km1.algafood.domain.models.State;
import km1.algafood.domain.services.StateRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class StateControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private StateRegisterService registerService;
  @Mock private StateInputDisassembler disassembler;
  @Mock private StateDtoAssembler assembler;
  @InjectMocks private StateController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/states";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(StateNotFountException.class);

    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(StateNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(StateNotFountException.class).when(registerService).remove(VALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(StateNotFountException.class).when(registerService).remove(1l);

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
    doThrow(StateHasDependents.class).when(registerService).remove(1l);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(StateHasDependents.class).when(registerService).remove(1l);

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
    StateInput input = aState().buildInput();
    State valid = aState().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenThrow(StateNotFountException.class);

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
    StateInput input = aState().buildInput();
    State valid = aState().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(StateNotFountException.class);

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
  void shouldReturnUpdatedState_whenPutIsCalledWithValidState() {

    StateInput input = aState().buildInput();
    State valid = aState().build();
    State registered = aState().withValidId().build();
    StateDto expected = aState().withValidId().buildDto();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    StateDto actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(StateDto.class);

    assertThat(actual, isStateDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidState() {
    StateInput input = aState().buildInput();
    State valid = aState().build();
    State registered = aState().withValidId().build();

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
  void shouldReturnCreated_whenPostIsCalledWithValidState() {
    StateInput input = aState().buildInput();
    State valid = aState().build();
    State registered = aState().withValidId().build();

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
  void shouldReturnSavedState_whenPostIsCalledWithValidState() {
    StateInput input = aState().buildInput();
    State valid = aState().build();
    State registered = aState().withValidId().build();
    StateDto expected = aState().withValidId().buildDto();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    StateDto actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(StateDto.class);

    assertThat(actual, isStateDtoEqualTo(expected));
  }

  @Test
  void shouldReturnState_whenGetIsCalledWithValidStateId() {
    State registered = aState().withValidId().build();
    StateDto expected = aState().withValidId().buildDto();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.fetchByID(1l)).thenReturn(registered);

    StateDto actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .get(PATH_VALID_ID)
            .thenReturn()
            .as(StateDto.class);

    assertThat(actual, isStateDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnStateList_whenGetIsCalled() {
    State registered = aState().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aState().withValidId().buildDto());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidStateId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }
}
