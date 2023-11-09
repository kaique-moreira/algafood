package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.ProblemMatcher.*;
import static km1.algafood.utils.CuisineBuilderFactory.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;

import static km1.algafood.matchers.CuisineMatcher.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.domain.exceptions.CuisineHasDependents;
import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.services.CuisineRegisterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class CuisineControllerTests {
  @Mock private CuisineRegisterService registerService;
  @InjectMocks private CuisineController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;
  private final String BASE_PATH = "/api/v1/cuisines";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredCuisineId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(CuisineNotFountException.class);

    given()
      .accept(ContentType.JSON)
    .when()
      .get("/1")
    .then()
      .status(HttpStatus.NOT_FOUND); }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredCuisineId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(CuisineNotFountException.class);

    Problem actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredCuisineId() {
    doThrow(
      CuisineNotFountException.class
    ).when(registerService).remove(1l);

    given()
      .accept(ContentType.JSON)
    .when()
      .delete("/1")
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredCuisineId() {
    doThrow(
      CuisineNotFountException.class
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
      CuisineHasDependents.class
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
      CuisineHasDependents.class
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
  void shouldReturnNotFound_whenPutIsCalledWithInvalidCuisineId() {
    when( 
      registerService.update(
        1l,
        validCuisine()
        .build()
      )
    ).thenThrow(CuisineNotFountException.class);

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          validCuisine()
          .build()
        )
      )
    .when()
      .put("/1")
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenPutIsCalledWithInvalidCuisineId() {
    when(
      registerService.update(
        1l,
        validCuisine()
        .build()
      )
    ).thenThrow(CuisineNotFountException.class);

  Problem actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          validCuisine()
                          .build()
                        )
                      )
                    .when()
                      .put("/1")
                    .thenReturn()
                      .as(Problem.class);
    
    assertThat(actual, isNotFoundProblem());
  }
   
  @Test
  void shouldReturnUpdatedCuisine_whenPutIsCalledWithValidCuisine() {
    when(
      registerService.update(
        1l,
        validCuisine()
        .build()
      )
    ).thenReturn(
        registeredCuisine().build()
      );

    Cuisine actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          validCuisine()
                          .build()
                        )
                      )
                    .when()
                      .put("/1")
                    .thenReturn()
                      .as(Cuisine.class);

    assertThat(actual, isCuisineEqualTo(registeredCuisine().build()));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidCuisine() {
    when(
      registerService.update(
        1l,
        validCuisine()
        .build()
      )
    ).thenReturn(registeredCuisine().build());

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          validCuisine()
          .build()
        )
      )
    .when()
      .put("/1")
    .then()
      .status(HttpStatus.OK);
  }

  @Test
  void shouldReturnCreated_whenPostIsCalledWithValidCuisine() {
    when(
      registerService.register(
        validCuisine()
        .build()
      )
    ).thenReturn(registeredCuisine().build());

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          validCuisine()
          .build()
        )
      )
    .when()
      .post()
    .then()
      .status(HttpStatus.CREATED);
  }

  @Test
  void shouldReturnSavedCuisine_whenPostIsCalledWithValidCuisine() {
    when(
      registerService.register(
        validCuisine()
        .build()
      )
    ).thenReturn(
        registeredCuisine().build()
      );

    Cuisine actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          validCuisine()
                          .build()
                        )
                      )
                    .when()
                      .post()
                    .thenReturn()
                      .as(Cuisine.class);

    assertThat(actual, isCuisineEqualTo(registeredCuisine().build()));
  }

  @Test
  void shouldReturnCuisine_whenGetIsCalledWithValidCuisineId() {
    when(
      registerService.fetchByID(1l)
    ).thenReturn(
        registeredCuisine().build()
      );

    Cuisine actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(Cuisine.class);

    assertThat(actual, isCuisineEqualTo(registeredCuisine().build()));
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
  void shouldReturnCuisineList_whenGetIsCalled() {
    when(
      registerService.fetchAll()
    ).thenReturn(
       Collections.singletonList(
          registeredCuisine()
          .build()
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
  void shouldReturnNoContent_whenDeleteIsCalledWithValidCuisineId() {
    given()
      .accept(ContentType.JSON)
    .when()
      .delete("/1")
    .then()
      .status(HttpStatus.NO_CONTENT);
  }

  public String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
