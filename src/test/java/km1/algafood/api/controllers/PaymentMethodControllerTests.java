package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.PaymentMethodDtoMatcher.isPaymentMethodDtoEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.PaymentMethodTestBuilder.aPaymentMethod;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.util.Collections;
import km1.algafood.api.assemblers.PaymentMethodDtoAssembler;
import km1.algafood.api.assemblers.PaymentMethodInputDisassembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.PaymentMethodDto;
import km1.algafood.api.models.PaymentMethodInput;
import km1.algafood.domain.exceptions.PaymentMethodHasDependents;
import km1.algafood.domain.exceptions.PaymentMethodNotFountException;
import km1.algafood.domain.models.PaymentMethod;
import km1.algafood.domain.services.PaymentMethodRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private PaymentMethodRegisterService registerService;
  @Mock private PaymentMethodInputDisassembler disassembler;
  @Mock private PaymentMethodDtoAssembler assembler;
  @InjectMocks private PaymentMethodController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/payment-methods";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(PaymentMethodNotFountException.class);

    given().accept(ContentType.JSON).when().get(PATH_VALID_ID).then().status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(registerService.fetchByID(VALID_ID)).thenThrow(PaymentMethodNotFountException.class);

    Problem actual =
        given().accept(ContentType.JSON).when().get(PATH_VALID_ID).thenReturn().as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(PaymentMethodNotFountException.class).when(registerService).remove(VALID_ID);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenDeleteIsCalledWithUnregisteredId() {
    doThrow(PaymentMethodNotFountException.class).when(registerService).remove(1l);

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
    doThrow(PaymentMethodHasDependents.class).when(registerService).remove(1l);

    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.CONFLICT);
  }

  @Test
  void shouldReturnConflictProblemDetails_whenDeleteIsCalledWhileEntityHasDependents() {
    doThrow(PaymentMethodHasDependents.class).when(registerService).remove(1l);

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
    PaymentMethodInput input = aPaymentMethod().buildInput();
    PaymentMethod valid = aPaymentMethod().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenThrow(PaymentMethodNotFountException.class);

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
    PaymentMethodInput input = aPaymentMethod().buildInput();
    PaymentMethod valid = aPaymentMethod().build();

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(INVALID_ID, valid)).thenThrow(PaymentMethodNotFountException.class);

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
  void shouldReturnUpdatedPaymentMethod_whenPutIsCalledWithValidPaymentMethod() {

    PaymentMethodInput input = aPaymentMethod().buildInput();
    PaymentMethod valid = aPaymentMethod().build();
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    PaymentMethodDto expected = aPaymentMethod().withValidId().buildDto();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.update(VALID_ID, valid)).thenReturn(registered);

    PaymentMethodDto actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .put(PATH_VALID_ID)
            .thenReturn()
            .as(PaymentMethodDto.class);

    assertThat(actual, isPaymentMethodDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidPaymentMethod() {
    PaymentMethodInput input = aPaymentMethod().buildInput();
    PaymentMethod valid = aPaymentMethod().build();
    PaymentMethod registered = aPaymentMethod().withValidId().build();

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
  void shouldReturnCreated_whenPostIsCalledWithValidPaymentMethod() {
    PaymentMethodInput input = aPaymentMethod().buildInput();
    PaymentMethod valid = aPaymentMethod().build();
    PaymentMethod registered = aPaymentMethod().withValidId().build();

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
  void shouldReturnSavedPaymentMethod_whenPostIsCalledWithValidPaymentMethod() {
    PaymentMethodInput input = aPaymentMethod().buildInput();
    PaymentMethod valid = aPaymentMethod().build();
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    PaymentMethodDto expected = aPaymentMethod().withValidId().buildDto();

    when(assembler.apply(registered)).thenReturn(expected);

    when(disassembler.apply(input)).thenReturn(valid);

    when(registerService.register(valid)).thenReturn(registered);

    PaymentMethodDto actual =
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(toJson(input))
            .when()
            .post()
            .thenReturn()
            .as(PaymentMethodDto.class);

    assertThat(actual, isPaymentMethodDtoEqualTo(expected));
  }

  @Test
  void shouldReturnPaymentMethod_whenGetIsCalledWithValidPaymentMethodId() {
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    PaymentMethodDto expected = aPaymentMethod().withValidId().buildDto();

    when(assembler.apply(registered)).thenReturn(expected);

    when(registerService.fetchByID(1l)).thenReturn(registered);

    PaymentMethodDto actual =
        given()
            .accept(ContentType.JSON)
            .when()
            .get(PATH_VALID_ID)
            .thenReturn()
            .as(PaymentMethodDto.class);

    assertThat(actual, isPaymentMethodDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given().accept(ContentType.JSON).when().get().then().status(HttpStatus.OK);
  }

  @Test
  void shouldReturnPaymentMethodList_whenGetIsCalled() {
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    when(assembler.apply(registered)).thenReturn(aPaymentMethod().withValidId().buildDto());

    when(registerService.fetchAll()).thenReturn(Collections.singletonList(registered));

    given().accept(ContentType.JSON).when().get().then().body("size()", is(1));
  }

  @Test
  void shouldReturnNoContent_whenDeleteIsCalledWithValidPaymentMethodId() {
    given()
        .accept(ContentType.JSON)
        .when()
        .delete(PATH_VALID_ID)
        .then()
        .status(HttpStatus.NO_CONTENT);
  }
}
