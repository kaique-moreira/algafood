package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.matchers.RestaurantMatcher.isRestaurantEqualTo;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static km1.algafood.utils.RestaurantTestBuilder.aRestaurant;
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
import km1.algafood.domain.exceptions.RestaurantHasDependents;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.services.RestaurantRegisterService;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTests {
  @Mock private RestaurantRegisterService registerService;
  @InjectMocks private RestaurantController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;
  private final String BASE_PATH = "/api/v1/restaurants";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredRestaurantId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(RestaurantNotFountException.class);

    given()
      .accept(ContentType.JSON)
    .when()
      .get("/1")
    .then()
      .status(HttpStatus.NOT_FOUND); }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredRestaurantId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(RestaurantNotFountException.class);

    Problem actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredRestaurantId() {
    doThrow(
      RestaurantNotFountException.class
    ).when(registerService).remove(1l);

    given()
      .accept(ContentType.JSON)
    .when()
      .delete("/1")
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredRestaurantId() {
    doThrow(
      RestaurantNotFountException.class
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
      RestaurantHasDependents.class
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
      RestaurantHasDependents.class
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
  void shouldReturnNotFound_whenPutIsCalledWithInvalidRestaurantId() {
    when( 
      registerService.update(
        1l,
        aRestaurant()
        .build()
      )
    ).thenThrow(RestaurantNotFountException.class);

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
        aRestaurant()
          .build()
        )
      )
    .when()
      .put("/1")
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenPutIsCalledWithInvalidRestaurantId() {
    when(
      registerService.update(
        1l,
        aRestaurant()
        .build()
      )
    ).thenThrow(RestaurantNotFountException.class);

  Problem actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          aRestaurant()
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
  void shouldReturnUpdatedRestaurant_whenPutIsCalledWithValidRestaurant() {
    when(
      registerService.update(
        1l,
        aRestaurant()
        .build()
      )
    ).thenReturn(
        aRestaurant().withValidId().build()
      );

    Restaurant actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          aRestaurant()
                          .build()
                        )
                      )
                    .when()
                      .put("/1")
                    .thenReturn()
                      .as(Restaurant.class);

    assertThat(actual, isRestaurantEqualTo(aRestaurant().withValidId().build()));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidRestaurant() {
    when(
      registerService.update(
        1l,
        aRestaurant()
        .build()
      )
    ).thenReturn(aRestaurant().withValidId().build());

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          aRestaurant()
          .build()
        )
      )
    .when()
      .put("/1")
    .then()
      .status(HttpStatus.OK);
  }

  @Test
  void shouldReturnCreated_whenPostIsCalledWithValidRestaurant() {
    when(
      registerService.register(
        aRestaurant()
        .build()
      )
    ).thenReturn(aRestaurant().withValidId().build());

    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(
        toJson(
          aRestaurant()
          .build()
        )
      )
    .when()
      .post()
    .then()
      .status(HttpStatus.CREATED);
  }

  @Test
  void shouldReturnSavedRestaurant_whenPostIsCalledWithValidRestaurant() {
    when(
      registerService.register(
        aRestaurant()
        .build()
      )
    ).thenReturn(
        aRestaurant().withValidId().build()
      );

    Restaurant actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          aRestaurant()
                          .build()
                        )
                      )
                    .when()
                      .post()
                    .thenReturn()
                      .as(Restaurant.class);

    assertThat(actual, isRestaurantEqualTo(aRestaurant().withValidId().build()));
  }

  @Test
  void shouldReturnRestaurant_whenGetIsCalledWithValidRestaurantId() {
    when(
      registerService.fetchByID(1l)
    ).thenReturn(
        aRestaurant().withValidId().build()
      );

    Restaurant actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(Restaurant.class);

    assertThat(actual, isRestaurantEqualTo(aRestaurant().withValidId().build()));
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
  void shouldReturnRestaurantList_whenGetIsCalled() {
    when(
      registerService.fetchAll()
    ).thenReturn(
       Collections.singletonList(
          aRestaurant().withValidId().build()
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
  void shouldReturnNoContent_whenDeleteIsCalledWithValidRestaurantId() {
    given()
      .accept(ContentType.JSON)
    .when()
      .delete("/1")
    .then()
      .status(HttpStatus.NO_CONTENT);
  }

  
}
