package km1.algafood;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.Matchers.hasSize;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Sql({"/test_data.sql"})
public class CuisineRegisterApiTests {
  
  @LocalServerPort
  private Integer localPort;

  @BeforeEach
  void setup(){
    enableLoggingOfRequestAndResponseIfValidationFails();
    basePath = "/api/v1/cuisines";
   port = localPort;
  }

  @Test
  void test(){
    System.out.println(ResourceUtils.getContentFromResource("/testData/t.json"));
  }
  @Test
  void shouldReturnOK_whenGetIsCalled() {
    given()
      .accept(ContentType.JSON)
    .when()
      .get()
    .then()
      .statusCode(HttpStatus.SC_OK)  ;
  }

  @Test
  void shouldReturnOK_whenGetIsCalle() {
    given()
      .accept(ContentType.JSON)
    .when()
      .get()
    .then()
      .body("", hasSize(4));
  }
}
