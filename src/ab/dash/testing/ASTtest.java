package ab.dash.testing;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.antlr.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;

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
    
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    
    @Test // Dummy Test
    public void dummyTest() throws RecognitionException, LexerException, ParserException {
        SampleFileWriter.createFile("Tests/00dummytest.db", "");
        
        String[] args = new String[] {"Tests/00dummytest.db"};
        AstTestMain.main(args);
        
        assertEquals("PROGRAM", outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Variable Declaration
    public void datatypeDeclarationTest() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/01datatypeDeclarationTest.db"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/01AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Comments and block comments
    public void commentTest() throws RecognitionException, LexerException, ParserException {
        
        String[] args = new String[] {"TestGrammarPrograms/02comments.db"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileSingleLineCommentTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileSingleLineComment.db"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileMultiLineCommentTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileMultiLineComment.db"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outIntercept.toString().trim());
    }
    
    @Test // Block comments must not nest!
    public void nestedCommentTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("Error: Comments cannot be nested.");
        String[] args = new String[] {"TestGrammarPrograms/03nestedComments.db"};
        AstTestMain.main(args);
    }
    
    @Test  // Block comments must match up
    public void missingEndComment() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("Error: Missing closing comment '*/'.");
        String[] args = new String[] {"TestGrammarPrograms/03missingEndComment.db"};
        AstTestMain.main(args);
    }
    
    @Test // Block comments must match up
    public void missingStartComment() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("Error: Missing opening comment '/*'.");
        String[] args = new String[] {"TestGrammarPrograms/03missingStartComment.db"};
        AstTestMain.main(args);
    }
    
    @Test // Reals
    public void realsTest() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/04Reals.db"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/04AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Tuples
    public void tuplesTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/05tuples.db"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/05AST_output"), outIntercept.toString().trim());
        assertEquals("", errIntercept.toString().trim());
    }
    
    @Test // Single Element Tuples are invalid
    public void singleElementTupleTestA() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("Error: Tuples must have more than one element.");
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_a.db"};
        AstTestMain.main(args);     
    }
    
    @Test // Single Element Tuples are invalid
    public void singleElementTupleTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("Error: Tuples must have more than one element.");
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_b.db"};
        AstTestMain.main(args);  
    }
    
    @Test // Empty Tuples are invalid
    public void emptyTupleTestA() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("Error: Tuples cannot be empty.");
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_a.db"};
        AstTestMain.main(args);     
    }
    
    @Test // Empty Tuples are invalid
    public void emptyTupleTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("Error: Tuples cannot be empty.");
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_b.db"};
        AstTestMain.main(args);        
    }

    
    
    
}