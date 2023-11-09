package km1.algafood.api.exeptionHandler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import km1.algafood.api.exceptionHandler.Problem;
import km1.algafood.utils.ProblemBuilderFactory;

public class ProblemBuilderTest {
@Test 
 void test (){
    Problem p = ProblemBuilderFactory.notFoundProblem().build();
    assertTrue(p instanceof Problem);
  }
}
