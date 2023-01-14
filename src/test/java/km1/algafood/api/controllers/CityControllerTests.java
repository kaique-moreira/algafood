package km1.algafood.api.controllers;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.CityMatcher.isCityEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.CityBuilderFactory.*;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.domain.exceptions.CityHasDependents;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.services.CityRegisterService;

@ExtendWith(MockitoExtension.class)
public class CityControllerTests {
  @Mock private CityRegisterService registerService;
  @InjectMocks private CityController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;
  private final String BASE_PATH = "/api/v1/cities";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(CityNotFountException.class);

    given()
      .accept(ContentType.JSON)
    .when()
      .get("/1")
    .then()
      .status(HttpStatus.NOT_FOUND); 
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(
      registerService.fetchByID(1l)
    ).thenThrow(CityNotFountException.class);

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
      CityNotFountException.class
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
      CityNotFountException.class
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
      CityHasDependents.class
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
      CityHasDependents.class
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
    City valid = validCity().build();
    when( 
      registerService.update(
        1l,
        valid
      )
    ).thenThrow(CityNotFountException.class);

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
    City valid = validCity().build();
    when(
      registerService.update(
        1l,
        valid
      )
    ).thenThrow(CityNotFountException.class);

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
  void shouldReturnUpdatedCity_whenPutIsCalledWithValidCity() {
    City valid = validCity().build();
    City registered = registeredCity().build();
    when(
      registerService.update(
        1l,
        valid
      )
    ).thenReturn(
        registered
      );

    City actual = given()
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
                      .as(City.class);

    assertThat(actual, isCityEqualTo(registered));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidCity() {
    City valid = validCity().build();
    City registered = registeredCity().build();
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
  void shouldReturnCreated_whenPostIsCalledWithValidCity() {
    City valid = validCity().build();
    City registered = registeredCity().build();

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
  void shouldReturnSavedCity_whenPostIsCalledWithValidCity() {
    City valid = validCity().build();
    City registered = registeredCity().build();

    when(
      registerService.register(
        valid
      )
    ).thenReturn(
        registered
      );

    City actual = given()
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
                      .as(City.class);

    assertThat(actual, isCityEqualTo(registered));
  }

  @Test
  void shouldReturnCity_whenGetIsCalledWithValidCityId() {
    City registered = registeredCity().build();
  
    when(
      registerService.fetchByID(1l)
    ).thenReturn(
        registered
      );

    City actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get("/1")
                    .thenReturn()
                      .as(City.class);

    assertThat(actual, isCityEqualTo(registered));
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
  void shouldReturnCityList_whenGetIsCalled() {  
    City registered = registeredCity().build();
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
  void shouldReturnNoContent_whenDeleteIsCalledWithValidCityId() {
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
