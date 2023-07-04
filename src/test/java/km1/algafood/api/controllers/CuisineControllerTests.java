package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.CuisineModelMatcher.isCuisineModelEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.CuisineTestBuilder.aCuisine;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.util.Collections;
import km1.algafood.api.assemblers.CuisineInputDisassembler;
import km1.algafood.api.assemblers.CuisineModelAssembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.CuisineInput;
import km1.algafood.api.models.CuisineModel;
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
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private CuisineRegisterService registerService;
  @Mock private CuisineInputDisassembler disassembler;
  @Mock private CuisineModelAssembler assembler;
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
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(registerService.tryFetch(VALID_ID)).thenThrow(CuisineNotFountException.class);

    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.tryFetch(VALID_ID)).thenThrow(CuisineNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(CuisineNotFountException.class).when(registerService).retryRemoveALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(CuisineNotFountException.class).when(registerService).retryRemovel);

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
    doThrow(CuisineHasDependents.class).when(registerService).retryRemovel);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(CuisineHasDependents.class).when(registerService).retryRemovel);

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
    CuisineInput input = aCuisine().buildInput();
    Cuisine valid = aCuisine().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenThrow(CuisineNotFountException.class);

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
    CuisineInput input = aCuisine().buildInput();
    Cuisine valid = aCuisine().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(CuisineNotFountException.class);

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
  void shouldReturnUpdatedCuisine_whenPutIsCalledWithValidCuisine() {

    CuisineInput input = aCuisine().buildInput();
    Cuisine valid = aCuisine().build();
    Cuisine registered = aCuisine().withValidId().build();
    CuisineModel expected = aCuisine().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    CuisineModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(CuisineModel.class);

    assertThat(actual, isCuisineModelEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidCuisine() {
    CuisineInput input = aCuisine().buildInput();
    Cuisine valid = aCuisine().build();
    Cuisine registered = aCuisine().withValidId().build();

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
  void shouldReturnCreated_whenPostIsCalledWithValidCuisine() {
    CuisineInput input = aCuisine().buildInput();
    Cuisine valid = aCuisine().build();
    Cuisine registered = aCuisine().withValidId().build();

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
  void shouldReturnSavedCuisine_whenPostIsCalledWithValidCuisine() {
    CuisineInput input = aCuisine().buildInput();
    Cuisine valid = aCuisine().build();
    Cuisine registered = aCuisine().withValidId().build();
    CuisineModel expected = aCuisine().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    CuisineModel actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(CuisineModel.class);

    assertThat(actual, isCuisineModelEqualTo(expected));
  }

  @Test
  void shouldReturnCuisine_whenGetIsCalledWithValidCuisineId() {
    Cuisine registered = aCuisine().withValidId().build();
    CuisineModel expected = aCuisine().withValidId().buildModel();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.tryFetch(1l)).thenReturn(registered);

    CuisineModel actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .get(PATH_VALID_ID)
            .thenReturn()
            .as(CuisineModel.class);

    assertThat(actual, isCuisineModelEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnCuisineList_whenGetIsCalled() {
    Cuisine registered = aCuisine().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aCuisine().withValidId().buildModel());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidCuisineId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldReturnBadRequest_whenPostIsCalledWithBlankNameCuisine() {
    CuisineInput input = aCuisine().withBlankName().buildInput();

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
  void shouldReturnBadRequest_whenPutIsCalledWithBlankNameCuisine() {
    CuisineInput input = aCuisine().withBlankName().buildInput();

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
