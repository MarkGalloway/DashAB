package ab.dash.testing;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ab.dash.DashAB_Part1_Test;
import ab.dash.exceptions.InvalidAssignmentException;
import ab.dash.exceptions.ParserException;

public class ASTtest {

    private PrintStream out_backup;
    private PrintStream err_backup;
    private ByteArrayOutputStream outErrIntercept;

    @Before
    public void setUp() throws Exception {
        out_backup = System.out;
        err_backup = System.err;
        outErrIntercept = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outErrIntercept));
        System.setErr(new PrintStream(outErrIntercept));     
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(out_backup);
        System.setErr(err_backup);
        SampleFileWriter.destroy("Tests/00dummytest.dash");
    }
    
    @Test // Dummy Test
    public void dummyTest() throws IOException, RecognitionException, ParserException, InvalidAssignmentException {
        SampleFileWriter.createFile("Tests/00dummytest.dash", "");
        
        String[] args = new String[] {"Tests/00dummytest.dash","astDebug"};
        DashAB_Part1_Test.main(args);
        
        assertEquals("PROGRAM", outErrIntercept.toString().trim());
    }
    
}