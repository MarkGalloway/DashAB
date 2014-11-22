/** AST Unit Tests. Compares output to expected AST **/
package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.Runner;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestAST extends BaseTest {

    @Test // Variable Declaration
    public void datatypeDeclarationTest() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/01datatypeDeclarationTest.ds"};
        Runner.astTestMain(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/01AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Comments and block comments
    public void commentTest() throws RecognitionException, LexerException, ParserException {
        
        String[] args = new String[] {"TestGrammarPrograms/02comments.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileSingleLineCommentTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileSingleLineComment.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Comments must not throw an error at end of file
    public void endOfFileMultiLineCommentTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/02endOfFileMultiLineComment.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/02AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Block comments must not nest!
    public void nestedCommentTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("line 17: comments cannot be nested");
        String[] args = new String[] {"TestGrammarPrograms/03nestedComments.ds"};
        Runner.astTestMain(args);
    }
    
    @Test  // Block comments must match up
    public void missingEndComment() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("error: Missing closing comment '*/'.");
        String[] args = new String[] {"TestGrammarPrograms/03missingEndComment.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // Block comments must match up
    public void missingStartComment() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(LexerException.class);
        expectedEx.expectMessage("line 17: missing opening comment '/*'");
        String[] args = new String[] {"TestGrammarPrograms/03missingStartComment.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // Reals
    public void realsTest() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/04Reals.ds"};
        Runner.astTestMain(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/04AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Tuples
    public void tuplesTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/05tuples.ds"};
        Runner.astTestMain(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/05AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Single Element Tuples are invalid
    public void singleElementTupleTestA() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples must have more than one element");
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_a.ds"};
        Runner.astTestMain(args);     
    }
    
    @Test // Single Element Tuples are invalid
    public void singleElementTupleTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples must have more than one element");
        String[] args = new String[] {"TestGrammarPrograms/06singleElementTupleFail_b.ds"};
        Runner.astTestMain(args);  
    }
    
    @Test // Empty Tuples are invalid
    public void emptyTupleTestA() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples cannot be empty");
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_a.ds"};
        Runner.astTestMain(args);     
    }
    
    @Test // Empty Tuples are invalid
    public void emptyTupleTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: tuples cannot be empty");
        String[] args = new String[] {"TestGrammarPrograms/07emptyTupleFail_b.ds"};
        Runner.astTestMain(args);        
    }
    
    @Test // Expressions test - null
    public void expressionsNull() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/09nullTests.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/09AST_output"), outErrIntercept.toString().trim());
    }

    @Test // Expressions test - identity
    public void expressionsIdentity() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/10identityTests.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/10AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // untyped Tuples test
    public void untypedTuplesTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/11untypedTuples.ds"};
        Runner.astTestMain(args);  
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/11AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Tuples test - null
    public void tuplesNullTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/13nullTuples.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/13AST_output"), outErrIntercept.toString().trim());
    }

    @Test // Tuples test - identity
    public void tuplesIdentityTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/12identityTuples.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/12AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Typedef test
    public void typedefTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/14typedef.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/14AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Stream Assign Test
    public void StreamAssignTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/15testStreamDeclaration.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/15AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Tuples test - unpacking
    public void tuplesUnpackingTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/16tupleUnpacking.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/16AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Block Statement Parse test
    public void blockStatementParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/17blockStatementValid.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/17AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // Block Statement invalid parse test
    public void blockStatementInvalidParseTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("In the block starting on line 5: Declarations can only appear at the start of this block.");
        String[] args = new String[] {"TestGrammarPrograms/18blockStatementInvalid.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // If/else Statement parse test
    public void ifStatementTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/19ifStatement.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/19AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // if Statement invalid parse test
    public void ifStatementInvalidDeclTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("In the block starting on line 7: Declarations can only appear at the start of this block.");
        String[] args = new String[] {"TestGrammarPrograms/20ifStatementInvalidDecl.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // else Statement invalid parse test
    public void ifStatementInvalidElseTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 13: else statement missing matching if.");
        String[] args = new String[] {"TestGrammarPrograms/21ifStatementInvalidElse.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // infinite loop parse test
    public void infiniteLoopTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/22infiniteLoop.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/22AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // pre-predicated loop parse test
    public void prePredicatedLoopTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/23prePredicatedLoop.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/23AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // post-predicated loop parse test
    public void postPredicatedLoopTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/24postPredicatedLoop.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/24AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // break statement parse test
    public void breakTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/25break.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/25AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // break statement outside of loop (invalid)
    public void breakStatementOutsideOfLoopTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 12: Break statements can only be used within loops.");
        String[] args = new String[] {"TestGrammarPrograms/26breakInvalid.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // nested break statements
    public void nestedBreakStatementTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/27nestedBreak.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/27AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // block statements nested
    public void nestedBlockStatementTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("In the block starting on line 1: Declarations can only appear at the start of this block.");
        String[] args = new String[] {"TestGrammarPrograms/28nestedBlockStatements.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // if condition missing left paren
    public void ifStatMissingLparenTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Missing left parenthesis.");
        String[] args = new String[] {"TestGrammarPrograms/29IfStatmissingLparen.ds"};
        Runner.astTestMain(args);
    }
    
    @Test //  condition missing right paren
    public void ifStatMissingRparenTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Missing right parenthesis.");
        String[] args = new String[] {"TestGrammarPrograms/30IfStatmissingRparen.ds"};
        Runner.astTestMain(args);
    }
    
    @Test //  condition missing left paren
    public void prePredLoopMissingLparenTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Missing left parenthesis.");
        String[] args = new String[] {"TestGrammarPrograms/32prePredLoopmissingLparen.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // if condition missing right paren
    public void prePredLoopMissingRparenTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Missing right parenthesis.");
        String[] args = new String[] {"TestGrammarPrograms/33prePredLoopmissingRparen.ds"};
        Runner.astTestMain(args);
    }

    @Test // Test with multiple functions
    public void multipleFunctions() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/31multipleFunctions.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/31AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test //  condition missing left paren
    public void postPredLoopMissingLparenTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Missing left parenthesis.");
        String[] args = new String[] {"TestGrammarPrograms/35postPredLoopmissingLparen.ds"};
        Runner.astTestMain(args);
    }
    
    @Test //  condition missing right paren
    public void postPredLoopMissingRparenTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Missing right parenthesis.");
        String[] args = new String[] {"TestGrammarPrograms/36postPredLoopmissingRparen.ds"};
        Runner.astTestMain(args);
    }

    @Test // Test with calling multiple functions
    public void callFunction() throws RecognitionException, LexerException, ParserException {        
        String[] args = new String[] {"TestGrammarPrograms/34callOutsideFunction.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/34AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // continue statement parse test
    public void continueTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/37continue.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/37AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // continue statement outside of loop (invalid)
    public void continueStatementOutsideOfLoopTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 12: Continue statements can only be used within loops.");
        String[] args = new String[] {"TestGrammarPrograms/38continueInvalid.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // continue break statements
    public void nestedContinueStatementTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/39nestedContinue.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/39AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // stream decl at start of block
    public void streamDeclTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/40streamDecl.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/40AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // block statements nested
    public void streamDeclInvalidTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("In the block starting on line 3: Declarations can only appear at the start of this block.");
        String[] args = new String[] {"TestGrammarPrograms/41streamDeclInvalid.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // globals must be declared const
    public void globalConstTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: Global variables must be declared with the const specifier.");
        String[] args = new String[] {"TestGrammarPrograms/42globalMissingConst.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // globals must be declared const
    public void globalConstTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: Global variables must be declared with the const specifier.");
        String[] args = new String[] {"TestGrammarPrograms/43globalMissingConstB.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // typecast parse test
    public void typecastParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/44typecasts.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/44AST_output"), outErrIntercept.toString().trim());
    }

//    @Test // tuple lists must be more than one element
//    public void invalidTupleSize() throws RecognitionException, LexerException, ParserException {        
//        expectedEx.expect(ParserException.class);
//        expectedEx.expectMessage("line 2: tuple lists must have more than one element");
//        String[] args = new String[] {"TestGrammarPrograms/45tupleSize.ds"};
//        Runner.astTestMain(args);
//    }
    
    @Test // globals must be declared const
    public void globalTupleConstTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: Global variables must be declared with the const specifier.");
        String[] args = new String[] {"TestGrammarPrograms/46globalTuplesConst.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // globals must be declared const
    public void globalTupleConstTestB() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 1: Global variables must be declared with the const specifier.");
        String[] args = new String[] {"TestGrammarPrograms/47globalTuplesConst.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // typedef that is not global must throw exception
    public void globalTypedefExceptionTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 4: Typedef must only be declared in global scope.");
        String[] args = new String[] {"TestGrammarPrograms/48localTypedef.ds"};
        Runner.astTestMain(args);
    }
    
    @Test 
    public void functionExpression() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/25FunctionExpression/functionExpression.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/49aAST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // function parse test
    public void functionParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/49functions.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/49AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // function parse test
    public void functionCallMultiTypeTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/52functionsArgOrdering.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/52AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // forward decl parse test
    public void forwardDeclTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/51forwardDecl.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/51AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // procedure parse test
    public void procedureParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/50procedures.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/50AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // tuple return parse test
    public void tupleReturnTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/53tupleReturns.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/53AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // unary positive test
    public void unaryPositiveTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/55unaryPositive.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/55AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // intervals
    public void intervalParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/56intervals.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/56AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // intervals invalid
    public void intervalNonIntegerException() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 2: Intervals only support integer base types.");
        String[] args = new String[] {"TestGrammarPrograms/57intervalNonIntegerException.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // vector parsing
    public void vectorParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/60vectorParsing.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/60AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // vector construction invalid
    public void vectorEmptyContructionTest() throws RecognitionException, LexerException, ParserException {
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 3: Empty vector construction is not allowed.");
        String[] args = new String[] {"TestGrammarPrograms/62emptyVectorConstruction.ds"};
        Runner.astTestMain(args);
    }
    
    @Test // By Expression parsing
    public void byExpressionParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/61byExpressions.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/61AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // length function parsing
    public void lengthFunctionParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/63lengthParsing.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/63AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // vector typedef parsing
    public void vectorTypedefParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/64vectorTypedef.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/64AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // vector concatenation parsing
    public void vectorConcatenationParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/65vectorConcatenationParsing.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/65AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // dot product parsing
    public void dotProductParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/66dotProduct.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/66AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // vector index parsing
    public void vectorIdnexParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/67vectorIndex.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/67AST_output"), outErrIntercept.toString().trim());
    }
    
    @Test // matrix parsing
    public void matrixParseTest() throws RecognitionException, LexerException, ParserException {
        String[] args = new String[] {"TestGrammarPrograms/68matrixParsing.ds"};
        Runner.astTestMain(args);
        SampleFileWriter.assertFileContent(new File("TestGrammarPrograms/68AST_output"), outErrIntercept.toString().trim());
    }

}