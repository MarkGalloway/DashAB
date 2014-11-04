/** Test Suite for running all Junit Tests at once **/
package ab.dash.testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
   TestAST.class,
   TestDef.class,
   TestType.class,
   TestLLVM.class,
})

public class FullTestSuite {   
	
}  