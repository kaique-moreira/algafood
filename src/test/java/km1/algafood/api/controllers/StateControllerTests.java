package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.matchers.StateMatcher.isStateEqualTo;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static km1.algafood.utils.StateTestBuilder.aState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.domain.exceptions.StateHasDependents;
import km1.algafood.domain.exceptions.StateNotFountException;
import km1.algafood.domain.models.State;
import km1.algafood.domain.services.StateRegisterService;

@ExtendWith(MockitoExtension.class)
public class StateControllerTests {
  @Mock private StateRegisterService registerService;
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
    when(
      registerService.fetchByID(1l)
    ).thenThrow(StateNotFountException.class);

    given()
      .accept(ContentType.JSON)
    .when()
      .get("/1")
    .then()
      .status(HttpStatus.NOT_FOUND); }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(StateNotFountException.class);

    Problem actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(
      StateNotFountException.class
    ).when(registerService).remove(1l);

    given()
      .accept(ContentType.JSON)
    .when()
      .delete("/1")
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(
      StateNotFountException.class
    ).when(registerService).remove(1l);

    Problem actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .delete("/1")
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnConflict_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(
      StateHasDependents.class
    ).when(registerService).remove(1l);

    given()
      .accept(ContentType.JSON)
    .when()
    .delete("/1")
    .then()
      .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(
      StateHasDependents.class
    ).when(registerService).remove(1l);

    Problem actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .delete("/1")
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isConflictProblem());
  }

  @Test
  void shouldReturnNotFound_whenPutIsCalledWithInvalidId() {
    State valid = aState().build();
    when( 
      registerService.update(
        1l,
        valid
      )
    ).thenThrow(StateNotFountException.class);

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          valid
        )
      )
    .when()
      .put("/1")
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenPutIsCalledWithInvalidId() {
    State valid = aState().build();
    when(
      registerService.update(
        1l,
        valid
      )
    ).thenThrow(StateNotFountException.class);

  Problem actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          valid
                        )
                      )
                    .when()
                      .put("/1")
                    .thenReturn()
                      .as(Problem.class);
    
    assertThat(actual, isNotFoundProblem());
  }
   
  @Test
  void shouldReturnUpdatedState_whenPutIsCalledWithValidState() {
    State valid = aState().build();
    State registered = aState().withValidId().build();
    when(
      registerService.update(
        1l,
        valid
      )
    ).thenReturn(
        registered
      );

    State actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          valid
                        )
                      )
                    .when()
                      .put("/1")
                    .thenReturn()
                      .as(State.class);

    assertThat(actual, isStateEqualTo(registered));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidState() {
    State valid = aState().build();
    State registered = aState().withValidId().build();
    when(
      registerService.update(
        1l,
        valid
      )
    ).thenReturn(registered);

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          valid
        )
      )
    .when()
      .put("/1")
    .then()
      .status(HttpStatus.OK);
  }

  @Test
  void shouldReturnCreated_whenPostIsCalledWithValidState() {
    State valid = aState().build();
    State registered = aState().withValidId().build();

    when(
      registerService.register(
        valid
      )
    ).thenReturn(registered);

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          valid
        )
      )
    .when()
      .post()
    .then()
      .status(HttpStatus.CREATED);
  }

  @Test
  void shouldReturnSavedState_whenPostIsCalledWithValidState() {
    State valid = aState().build();
    State registered = aState().withValidId().build();

    when(
      registerService.register(
        valid
      )
    ).thenReturn(
        registered
      );

    State actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          valid
                        )
                      )
                    .when()
                      .post()
                    .thenReturn()
                      .as(State.class);

    assertThat(actual, isStateEqualTo(registered));
  }

  @Test
  void shouldReturnState_whenGetIsCalledWithValidStateId() {
    State registered = aState().withValidId().build();
  
    when(
      registerService.fetchByID(1l)
    ).thenReturn(
        registered
      );

    State actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(State.class);

    assertThat(actual, isStateEqualTo(registered));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given()
      .accept(ContentType.JSON)
    .when()
     .get()
    .then()
      .status(HttpStatus.OK); 
  }

  @Test
  void shouldReturnStateList_whenGetIsCalled() {  
    State registered = aState().withValidId().build();
    when(
      registerService.fetchAll()
    ).thenReturn(
       Collections.singletonList(
          registered
        )
      );

    given()
      .accept(ContentType.JSON)
    .when()
     .get()
    .then()
      .body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidStateId() {
    given()
      .accept(ContentType.JSON)
    .when()
      .delete("/1")
    .then()
      .status(HttpStatus.NO_CONTENT);
  }

}
