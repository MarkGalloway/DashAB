package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;

public class ASTtest extends BaseTest {


    @Test // Dummy Test
    public void dummyTest() throws RecognitionException, LexerException, ParserException {
        SampleFileWriter.createFile("Tests/00dummytest.ds", "");
        
        String[] args = new String[] {"Tests/00dummytest.ds"};
        AstTestMain.main(args);
        
        assertEquals("PROGRAM", outErrIntercept.toString().trim());
        SampleFileWriter.destroy("Tests/00dummytest.ds");
    }
    
    @Test // Variable Declaration
    public void datatypeDeclarationTest() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/01datatypeDeclarationTest.ds"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/01AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Comments and block comments
    public void commentTest() throws RecognitionException, LexerException, ParserException {
        
        String[] args = new String[] {"TestGrammarPrograms/02comments.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileSingleLineCommentTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileSingleLineComment.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileMultiLineCommentTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileMultiLineComment.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Block comments must not nest!
    public void nestedCommentTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("line 17: comments cannot be nested");
        String[] args = new String[] {"TestGrammarPrograms/03nestedComments.ds"};
        AstTestMain.main(args);
    }
    
    @Test  // Block comments must match up
    public void missingEndComment() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("Error: Missing closing comment '*/'.");
        String[] args = new String[] {"TestGrammarPrograms/03missingEndComment.ds"};
        AstTestMain.main(args);
    }
    
    @Test // Block comments must match up
    public void missingStartComment() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("line 17: missing opening comment '/*'");
        String[] args = new String[] {"TestGrammarPrograms/03missingStartComment.ds"};
        AstTestMain.main(args);
    }
    
    @Test // Reals
    public void realsTest() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/04Reals.ds"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/04AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Tuples
    public void tuplesTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/05tuples.ds"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/05AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Single Element Tuples are invalid
    public void singleElementTupleTestA() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples must have more than one element");
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_a.ds"};
        AstTestMain.main(args);     
    }
    
    @Test // Single Element Tuples are invalid
    public void singleElementTupleTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples must have more than one element");
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_b.ds"};
        AstTestMain.main(args);  
    }
    
    @Test // Empty Tuples are invalid
    public void emptyTupleTestA() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples cannot be empty");
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_a.ds"};
        AstTestMain.main(args);     
    }
    
    @Test // Empty Tuples are invalid
    public void emptyTupleTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples cannot be empty");
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_b.ds"};
        AstTestMain.main(args);        
    }
    
    @Test // Expressions test - null
    public void expressionsNull() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/09nullTests.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/09AST_output"), outErrIntercept.toString().trim());
    }

    @Test // Expressions test - identity
    public void expressionsIdentity() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/10identityTests.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/10AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // untyped Tuples test
    public void untypedTuplesTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/11untypedTuples.ds"};
        AstTestMain.main(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/11AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Tuples test - null
    public void tuplesNullTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/13nullTuples.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/13AST_output"), outErrIntercept.toString().trim());
    }

    @Test // Tuples test - identity
    public void tuplesIdentityTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/12identityTuples.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/12AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Typedef test
    public void typedefTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/14typedef.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/14AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Stream Assign Test
    public void StreamAssignTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/15testStreamDeclaration.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/15AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Tuples test - unpacking
    public void tuplesUnpackingTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/16tupleUnpacking.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/16AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Block Statement Parse test
    public void blockStatementParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/17blockStatementValid.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/17AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Block Statement invalid parse test
    public void blockStatementInvalidParseTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 8: Declarations can only appear at the start of a block.");
        String[] args = new String[] {"TestGrammarPrograms/18blockStatementInvalid.ds"};
        AstTestMain.main(args);
    }
    
    @Test // If/else Statement parse test
    public void ifStatementTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/19ifStatement.ds"};
        AstTestMain.main(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/19AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // if Statement invalid parse test
    public void ifStatementInvalidDeclTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 10: Declarations can only appear at the start of a block.");
        String[] args = new String[] {"TestGrammarPrograms/20ifStatementInvalidDecl.ds"};
        AstTestMain.main(args);
    }
    
    @Test // else Statement invalid parse test
    public void ifStatementInvalidElseTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 13: else statement missing matching if.");
        String[] args = new String[] {"TestGrammarPrograms/21ifStatementInvalidElse.ds"};
        AstTestMain.main(args);
    }
    
    
}