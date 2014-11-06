/** Base Test for Project Junit Test Classes. 
 *  Performs Setup/Teardown. Inherit from this for new Test Classes. **/

package ab.dash.testing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class BaseTest {

    protected PrintStream out_backup;
    protected PrintStream err_backup;
    protected ByteArrayOutputStream outErrIntercept;
    public Set<String> base_globals;

    @Before
    public void setUp() throws Exception {
        out_backup = System.out;
        err_backup = System.err;
        
        outErrIntercept = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outErrIntercept));
        System.setErr(new PrintStream(outErrIntercept));
        
        base_globals = new HashSet<String>();
        base_globals.add("boolean");
        base_globals.add("character");
        base_globals.add("const");
        base_globals.add("integer");
        base_globals.add("real");
        base_globals.add("std_output()");
        base_globals.add("std_input()");
        base_globals.add("tuple");
        base_globals.add("var");
        base_globals.add("null");
        base_globals.add("identity");
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(out_backup);
        System.setErr(err_backup);
    }
    
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    
}
