package km1.algafood.api.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.basePath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static km1.algafood.matchers.CityDtoMatcher.isCityDtoEqualTo;
import static km1.algafood.matchers.ProblemMatcher.isConflictProblem;
import static km1.algafood.matchers.ProblemMatcher.isNotFoundProblem;
import static km1.algafood.utils.CityTestBuilder.aCity;
import static km1.algafood.utils.JsonConversionUtils.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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
import km1.algafood.api.assemblers.CityDtoAssembler;
import km1.algafood.api.assemblers.CityInputDisassembler;
import km1.algafood.api.exceptionHandler.ApiExceptionHandler;
import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.api.models.CityDto;
import km1.algafood.api.models.CityInput;
import km1.algafood.domain.exceptions.CityHasDependents;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.services.CityRegisterService;

@ExtendWith(MockitoExtension.class)
public class CityControllerTests {
  private static final long INVALID_ID = 1l;
  private static final long VALID_ID = 1l;
  private static final String PATH_VALID_ID = String.format("/%d", VALID_ID);
  private static final String PATH_INVALID_ID = String.format("/%d", INVALID_ID);

  @Mock private CityRegisterService registerService;
  @Mock private CityInputDisassembler disassembler;
  @Mock private CityDtoAssembler assembler;
  @InjectMocks private CityController controller;
  @InjectMocks private ApiExceptionHandler exceptionHandler;

  private final String BASE_PATH = "/api/v1/cities";

  ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    standaloneSetup(controller, exceptionHandler);
    basePath = BASE_PATH;
  }

  @Test
  void shouldReturnNotFound_whenGetIsCalledWithUnregisteredId() {
    when(
      registerService.fetchByID(VALID_ID)
    ).thenThrow(CityNotFountException.class);

    given()
      .accept(ContentType.JSON)
    .when()
      .get(PATH_VALID_ID)
    .then()
      .status(HttpStatus.NOT_FOUND); }

  @Test
  void shouldReturnNotFoundProblemDetails_whenGetIsCalledWithUnregisteredId() {
    when(
      registerService.fetchByID(VALID_ID)
    ).thenThrow(CityNotFountException.class);

    Problem actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get(PATH_VALID_ID)
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isNotFoundProblem());
  }

  @Test
  void shouldReturnNotFound_whenDeleteIsCalledWitchUnregisteredId() {
    doThrow(
      CityNotFountException.class
    ).when(registerService).remove(VALID_ID);

    given()
      .accept(ContentType.JSON)
    .when()
      .delete(PATH_VALID_ID)
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
                      .delete(PATH_VALID_ID)
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
    .delete(PATH_VALID_ID)
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
                      .delete(PATH_VALID_ID)
                    .thenReturn()
                      .as(Problem.class);

    assertThat(actual, isConflictProblem());
  }

  @Test
  void shouldReturnNotFound_whenPutIsCalledWithInvalidId() {
    CityInput input = aCity().buildInput();
    City valid = aCity().build();

    when(
      disassembler.apply(input)
    ).thenReturn(
        valid
      );

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
          input
        )
      )
    .when()
      .put(PATH_VALID_ID)
    .then()
      .status(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundProblemDetails_whenPutIsCalledWithInvalidId() {
    CityInput input = aCity().buildInput();
    City valid = aCity().build();

    when(
      disassembler.apply(input)
    ).thenReturn(
        valid
      );

    when(
      registerService.update(
        INVALID_ID,
        valid
      )
    ).thenThrow(CityNotFountException.class);

  Problem actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          input
                        )
                      )
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
    CityDto expected = aCity().withValidId().buildDto();

    when(
      assembler.apply(registered)
    ).thenReturn(
        expected
      );

    when(
      disassembler.apply(input)
    ).thenReturn(
        valid
      );

    when(
      registerService.update(
        VALID_ID,
        valid
      )
    ).thenReturn(
        registered
      );

    CityDto actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          input
                        )
                      )
                    .when()
                      .put(PATH_VALID_ID)
                    .thenReturn()
                      .as(CityDto.class);

    assertThat(actual, isCityDtoEqualTo(expected));
  }

  @Test
  void shouldReturnOk_whenPutIsCalledWithValidCity() {
    CityInput input = aCity().buildInput();
    City valid = aCity().build();
    City registered = aCity().withValidId().build();

    when(
      assembler.apply(registered)
    ).thenReturn(
        any()
      );

    when(
      disassembler.apply(input)
    ).thenReturn(
        valid
      );


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
          input
        )
      )
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

   when(
      assembler.apply(registered)
    ).thenReturn(
        any()
      );

    when(
      disassembler.apply(input)
    ).thenReturn(
        valid
      );

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
          input
        )
      )
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
    CityDto expected = aCity().withValidId().buildDto();

     when(
      assembler.apply(registered)
    ).thenReturn(
        expected
      );

    when(
      disassembler.apply(input)
    ).thenReturn(
        valid
      );

    when(
      registerService.register(
        valid
      )
    ).thenReturn(
        registered
      );

    CityDto actual = given()
                      .contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(
                        toJson(
                          input
                        )
                      )
                    .when()
                      .post()
                    .thenReturn()
                      .as(CityDto.class);

    assertThat(actual, isCityDtoEqualTo(expected));
  }

  @Test
  void shouldReturnCity_whenGetIsCalledWithValidCityId() {
    City registered = aCity().withValidId().build();
    CityDto expected = aCity().withValidId().buildDto();

     when(
      assembler.apply(registered)
    ).thenReturn(
        expected
      );

    when(
      registerService.fetchByID(1l)
    ).thenReturn(
        registered
      );

    CityDto actual = given()
                      .accept(ContentType.JSON)
                    .when()
                      .get(PATH_VALID_ID)
                    .thenReturn()
                      .as(CityDto.class);

    assertThat(actual, isCityDtoEqualTo(expected));
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
    City registered = aCity().withValidId().build();
    when(
      assembler.apply(registered)
    ).thenReturn(
        aCity().withValidId().buildDto()
      );

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
      .delete(PATH_VALID_ID)
    .then()
      .status(HttpStatus.NO_CONTENT);
  }
}
