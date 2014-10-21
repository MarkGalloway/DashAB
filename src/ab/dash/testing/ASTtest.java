package ab.dash.testing;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.antlr.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ASTtest {

    private PrintStream out_backup;
    private PrintStream err_backup;
    private ByteArrayOutputStream errIntercept;
    private ByteArrayOutputStream outIntercept;

    @Before
    public void setUp() throws Exception {
        out_backup = System.out;
        err_backup = System.err;
        
        errIntercept = new ByteArrayOutputStream();
        outIntercept = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outIntercept));
        System.setErr(new PrintStream(errIntercept));     
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(out_backup);
        System.setErr(err_backup);
        SampleFileWriter.destroy("Tests/00dummytest.db");
    }
    
    @Test // Dummy Test
    public void dummyTest() throws RecognitionException {
        SampleFileWriter.createFile("Tests/00dummytest.db", "");
        
        String[] args = new String[] {"Tests/00dummytest.db"};
        AstTestMain.main(args);
        
        assertEquals("PROGRAM", outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Variable Declaration
    public void datatypeDeclarationTest() throws RecognitionException {        
        String[] args = new String[] {"TestGrammarPrograms/01datatypeDeclarationTest.db"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/01AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Comments and block comments
    public void commentTest() throws RecognitionException {
        
        String[] args = new String[] {"TestGrammarPrograms/02comments.db"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileSingleLineCommentTest() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileSingleLineComment.db"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileMultiLineCommentTest() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileMultiLineComment.db"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outIntercept.toString().trim());
    }
    
    @Test  //(expected=RecognitionException.class)  // Block comments must not nest!
    public void nestedCommentTest() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/03nestedComments.db"};
        AstTestMain.main(args);
        assertEquals("Not allowed to have nested comments.", errIntercept.toString().trim());
    }
    
    @Test  //(expected=RecognitionException.class)  // Block comments must match up
    public void missingEndComment() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/03missingEndComment.db"};
        AstTestMain.main(args);
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test  //(expected=RecognitionException.class)  // Block comments must match up
    public void missingStartComment() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/03missingStartComment.db"};
        AstTestMain.main(args);
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Reals
    public void realsTest() throws RecognitionException {        
        String[] args = new String[] {"TestGrammarPrograms/04Reals.db"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/04AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Tuples
    public void tuplesTest() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/05tuples.db"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/05AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test (expected=RecognitionException.class) // Single Element Tuples are invalid
    public void singleElementTupleTestA() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_a.db"};
        AstTestMain.main(args);     

        //assertEquals("", outIntercept.toString().trim());
        
    }
    
    @Test (expected=RecognitionException.class) // Single Element Tuples are invalid
    public void singleElementTupleTestB() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_b.db"};
        AstTestMain.main(args);  
        
        //assertEquals("", outIntercept.toString().trim());
    }
    
    @Test (expected=RecognitionException.class) // Empty Tuples are invalid
    public void emptyTupleTestA() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_a.db"};
        AstTestMain.main(args);     
        //assertEquals("", outIntercept.toString().trim());
    }
    
    @Test (expected=RecognitionException.class) // Empty Tuples are invalid
    public void emptyTupleTestB() throws RecognitionException {
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_b.db"};
        AstTestMain.main(args);        
        assertEquals("", outIntercept.toString().trim());
    }
    
    

    
    
    
}