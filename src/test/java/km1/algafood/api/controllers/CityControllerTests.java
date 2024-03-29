package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.CityModelMatcher.isCityModelEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.CityTestBuilder.aCity;
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
import km1.algafood.api.assemblers.CityModelAssembler;
import km1.algafood.api.assemblers.CityInputDisassembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.CityInput;
import km1.algafood.api.models.CityModel;
import km1.algafood.core.validation.ValidationConfig;
import km1.algafood.domain.exceptions.CityHasDependents;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.services.CityRegisterService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class CityControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private CityRegisterService registerService;
  @Mock private CityInputDisassembler disassembler;
  @Mock private CityModelAssembler assembler;
  @InjectMocks private CityController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/cities";

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
    when(registerService.tryFetch(VALID_ID)).thenThrow(CityNotFountException.class);
    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.tryFetch(VALID_ID)).thenThrow(CityNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(CityNotFountException.class).when(registerService).retryRemoveALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(CityNotFountException.class).when(registerService).retryRemoveNVALID_ID);

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
    doThrow(CityHasDependents.class).when(registerService).retryRemoveALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(CityHasDependents.class).when(registerService).retryRemoveALID_ID);

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
    CityInput input = aCity().buildInput();
    City valid = aCity().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(CityNotFountException.class);

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
    CityInput input = aCity().buildInput();
    City valid = aCity().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(CityNotFountException.class);

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
  void shouldReturnUpdatedCity_whenPutIsCalledWithValidCity() {

    CityInput input = aCity().buildInput();
    City valid = aCity().build();
    City registered = aCity().withValidId().build();
    CityModel expected = aCity().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    CityModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(CityModel.class);

    assertThat(actual, isCityModelEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidCity() {
    CityInput input = aCity().buildInput();
    City valid = aCity().build();
    City registered = aCity().withValidId().build();

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
  void shouldReturnCreated_whenPostIsCalledWithValidCity() {
    CityInput input = aCity().buildInput();
    City valid = aCity().build();
    City registered = aCity().withValidId().build();

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
  void shouldReturnSavedCity_whenPostIsCalledWithValidCity() {
    CityInput input = aCity().buildInput();
    City valid = aCity().build();
    City registered = aCity().withValidId().build();
    CityModel expected = aCity().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    CityModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(CityModel.class);

    assertThat(actual, isCityModelEqualTo(expected));
  }

  @Test
  void shouldReturnCity_whenGetIsCalledWithValidCityId() {
    City registered = aCity().withValidId().build();
    CityModel expected = aCity().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.tryFetch(VALID_ID)).thenReturn(registered);

    CityModel actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(CityModel.class);

    assertThat(actual, isCityModelEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnCityList_whenGetIsCalled() {
    City registered = aCity().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aCity().withValidId().buildModel());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidCityId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnBadRequest_whenPostIsCalledWithBlankNameCity(){
    CityInput input = aCity().withBlankName().buildInput();

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
  void shouldReturnBadRequest_whenPostIsCalledWithNullStateIDCityInput(){
    CityInput input = aCity().withState(aState().withNullId().build()).buildInput();

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
  void shouldReturnBadRequest_whenPutIsCalledWithBlankNameCity(){
    CityInput input = aCity().withBlankName().buildInput();

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
  void shouldReturnBadRequest_whenPutIsCalledWithNullStateIDCityInput(){
    CityInput input = aCity().withState(aState().withNullId().build()).buildInput();

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
