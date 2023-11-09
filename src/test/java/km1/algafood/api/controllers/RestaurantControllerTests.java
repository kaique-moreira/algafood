package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static km1.algafood.matchers.ProblemMatcher.*;
import static km1.algafood.matchers.RestaurantDtoMatcher.isRestaurantDtoEqualTo;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static km1.algafood.utils.RestaurantTestBuilder.aRestaurant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Collections;
import km1.algafood.api.assemblers.RestaurantDtoAssembler;
import km1.algafood.api.assemblers.RestaurantInputDisassembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.api.models.RestaurantModel;
import km1.algafood.domain.exceptions.RestaurantHasDependents;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.services.RestaurantRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private RestaurantRegisterService registerService;
  @Mock private RestaurantInputDisassembler disassembler;
  @Mock private RestaurantDtoAssembler assembler;
  @InjectMocks private RestaurantController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/restaurants";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    exceptionHandler.setMessageSource(messageSource);
    standaloneSetup(controller, exceptionHandler, messageSource);
    basePath = BASE_PATH;
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(RestaurantNotFountException.class);

    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(RestaurantNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(RestaurantNotFountException.class).when(registerService).remove(VALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(RestaurantNotFountException.class).when(registerService).remove(1l);

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
    doThrow(RestaurantHasDependents.class).when(registerService).remove(1l);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(RestaurantHasDependents.class).when(registerService).remove(1l);

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
    RestaurantInput input = aRestaurant().buildInput();
    Restaurant valid = aRestaurant().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenThrow(RestaurantNotFountException.class);

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
    RestaurantInput input = aRestaurant().buildInput();
    Restaurant valid = aRestaurant().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(RestaurantNotFountException.class);

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
  void shouldReturnUpdatedRestaurant_whenPutIsCalledWithValidRestaurant() {

    RestaurantInput input = aRestaurant().buildInput();
    Restaurant valid = aRestaurant().build();
    Restaurant registered = aRestaurant().withValidId().build();
    RestaurantModel expected = aRestaurant().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    RestaurantModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(RestaurantModel.class);

    assertThat(actual, isRestaurantDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidRestaurant() {
    RestaurantInput input = aRestaurant().buildInput();
    Restaurant valid = aRestaurant().build();
    Restaurant registered = aRestaurant().withValidId().build();

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
  void shouldReturnCreated_whenPostIsCalledWithValidRestaurant() {
    RestaurantInput input = aRestaurant().buildInput();
    Restaurant valid = aRestaurant().build();
    Restaurant registered = aRestaurant().withValidId().build();
    RestaurantModel model = aRestaurant().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(model);

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
  void shouldReturnCreated_whenPostIsCalledWithValidRestaurant1() {

    RestaurantInput input = aRestaurant().withBlankName().buildInput();

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
  void shouldReturnSavedRestaurant_whenPostIsCalledWithValidRestaurant() {
    RestaurantInput input = aRestaurant().buildInput();
    Restaurant valid = aRestaurant().build();
    Restaurant registered = aRestaurant().withValidId().build();
    RestaurantModel expected = aRestaurant().withValidId().buildModel();
    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    RestaurantModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(RestaurantModel.class);

    assertThat(actual, isRestaurantDtoEqualTo(expected));
  }

  @Test
  void shouldReturnRestaurant_whenGetIsCalledWithValidRestaurantId() {
    Restaurant registered = aRestaurant().withValidId().build();
    RestaurantModel expected = aRestaurant().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.fetchByID(1l)).thenReturn(registered);

    RestaurantModel actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .get(PATH_VALID_ID)
            .thenReturn()
            .as(RestaurantModel.class);

    assertThat(actual, isRestaurantDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnRestaurantList_whenGetIsCalled() {
    Restaurant registered = aRestaurant().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aRestaurant().withValidId().buildModel());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidRestaurantId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnNoContent_whenPutActiveIsCalledWithValidRestaurantId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .put(PATH_VALID_ID.concat("/active"))
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnNoContent_whenDeleteActiveIsCalledWithValidRestaurantId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID.concat("/active"))
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnNotFound_whenPutActiveIsCalledWithInValidRestaurantId() {
    doThrow(RestaurantNotFountException.class).when(registerService).active(VALID_ID);
    given()
        .accept(ContentType.JSON)
        .when()
        .put(PATH_VALID_ID.concat("/active"))
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFound_whenDeleteActiveIsCalledWithInvalidRestaurantId() {
    doThrow(RestaurantNotFountException.class).when(registerService).disactive(VALID_ID);
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID.concat("/active"))
        .then()
        .status(HttpStatus.NOT_FOUND);
  }
}
