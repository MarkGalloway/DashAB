package ab.dash.testing;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class FullTest {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(FullTestSuite.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      
	System.out.print("Failures: " + result.getFailureCount());
	System.out.println(" out of " + result.getRunCount());
   }
}  	