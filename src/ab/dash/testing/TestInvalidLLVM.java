/** Dash programs who's LLVM should never run due errors. 
 *  DONT CHECK FOR JAVA EXCEPTIONS HERE, this should be the output the
 *  user sees when using our compiler - check actual output stream for expected error message instead.**/

package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.Runner;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestInvalidLLVM extends BaseTest {
    
    // Programs with Invalid Syntax: Put TestInvalidSyntaxPrograms Here
    
    @Test 
    public void invalidCharacter() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/01InvalidCharacter/invalidCharacter.ds"};
        Runner.llvmMain(args);
        assertEquals("line 5: expected single quotes for character", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleMemberListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/02TupleSizeMemberListOf1/TupleSizeMemberListOf1.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 2: tuples must have more than one element");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/03TupleSizeTupleListOf1/TupleSizeTupleListOf1.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: t2, (0):<integer> have incompatible types in tuple(integer, integer i, integer k) t2 = (0);", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void missingEndOfComment() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/04MissingEndOfComment/missingEndOfComment.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("Error: Missing closing comment '*/'.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void missingStartOfComment() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/05MissingStartOfComment/missingStartOfComment.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: missing opening comment '/*'");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void missingMainProcedure() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/06MissingMainProcedure/missingMainProcedure.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("error: missing main procedure");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void mainDeclaredAsFunction() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/07MainDeclaredAsFunction/mainDeclaredAsFunction.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("error: main must be a procedure not a function");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void mainWithArguments() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/08MainWithArguments/mainWithArguments.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: main procedure takes no arguments");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void mainReturnsNonInteger() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/09MainReturnsNonInteger/mainReturnsNonInteger.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: main procedure must return an integer");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void nestedComments() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/12NestedComments/nestedComments.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 17: comments cannot be nested");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void emptyTupleMemberList() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/13EmptyTupleMemberList/emptyTupleMemberList.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 3: Empty tuple lists are not allowed.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void emptyTupleTupleList() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/14EmptyTupleTupleList/emptyTupleTupleList.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 3: tuples cannot be empty");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void functionCallBeforeDecl() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/15FunctionCallBeforeDecl/functionCallBeforeDecl.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 3: unknown identifier foo");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void procedureCallBeforeDecl() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/16ProcedureCallBeforeDecl/procedureCallBeforeDecl.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 3: unknown identifier foo");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void tupleIndexExpression() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/17TupleIndexExpression/tupleIndexExpression.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 7: Only integers and identifiers are allowed to index tuples.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void varDeclAfterStartOfBlock() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/18VarDeclAfterStartOfBlock/varDeclAfterStartOfBlock.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("In the block starting on line 1: Declarations can only appear at the start of this block.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void varDeclAfterStartOfIfStat() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/19VarDeclAfterStartOfIfStat/varDeclAfterStartOfIfStat.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("In the block starting on line 7: Declarations can only appear at the start of this block.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void danglingElseStatement() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/20DanglingElseStatement/danglingElseStatement.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 15: else statement missing matching if.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void danglingBreakStatement() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/21DangingBreakStatement/danglingBreakStatement.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 15: Break statements can only be used within loops.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void danglingContinueStatement() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/22DangingContinueStatement/danglingContinueStatement.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 14: Continue statements can only be used within loops.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void varDeclAfterNestedBlock() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/23VarDeclAfterNestedBlock/varDeclAfterNestedBlock.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("In the block starting on line 3: Declarations can only appear at the start of this block.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void ifStatMissingLeftParen() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/24IfStatMissingLeftParen/ifStatMissingLeftParen.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 5: Missing left parenthesis.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void ifStatMissingRightParen() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/25IfStatMissingRightParen/ifStatMissingRightParen.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 5: Missing right parenthesis.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void prePredLoopMissingLeftParen() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/26PrePredLoopMissingLeftParen/prePredLoopMissingLeftParen.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 5: Missing left parenthesis.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void prePredLoopMissingRightParen() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/27PrePredLoopMissingRightParen/prePredLoopMissingRightParen.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 5: Missing right parenthesis.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void postPredLoopMissingLeftParen() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/28PostPredLoopMissingLeftParen/postPredLoopMissingLeftParen.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 5: Missing left parenthesis.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void postPredLoopMissingRightParen() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/29PostPredLoopMissingRightParen/postPredLoopMissingRightParen.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 5: Missing right parenthesis.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void globalVarMissingConst() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/30GlobalVarMissingConst/globalVarMissingConst.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: Global variables must be declared with the const specifier.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void globalVarNotConst() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/31GlobalVarNotConst/globalVarNotConst.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: Global variables must be declared with the const specifier.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void globalTupleMissingConst() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/32GlobalTupleMissingConst/globalTupleMissingConst.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: Global variables must be declared with the const specifier.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void globalTupleNotConst() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/33GlobalTupleNotConst/globalTupleNotConst.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: Global variables must be declared with the const specifier.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void typedefInLocalScope() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/34TypedefInLocalScope/typedefInLocalScope.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 8: Typedef must only be declared in global scope.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void danglingForwardDeclaration() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/35DanglingForwardDeclaration/danglingForwardDeclaration.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 1: Danging forward Declaration.");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidEscapeSequence() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/36InvalidEscapeSequence/invalidEscapeSequence.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 3: invalid escape sequence");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    // Programs with undeclared variables: Put TestUndefinedVariablePrograms Here
    
    @Test 
    public void undefined() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/01Undefined/undefined.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: unknown identifier e2", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidUndefinedButDefinedAfter() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {        
        String[] args = new String[] {"TestUndefinedVariablePrograms/02UndefinedButDefinedAfter/undefinedButDefinedAfter.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: unknown identifier e2", outErrIntercept.toString().trim());
    }
    
    // TODO: Fix this!
    @Test 
    public void doubleDeclaration() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {        
        String[] args = new String[] {"TestUndefinedVariablePrograms/03DoubleDeclaration/doubleDeclaration.ds"};
        Runner.llvmMain(args);
        assertEquals("line 3: Identifier e1 declared twice in the same scope.", outErrIntercept.toString().trim());
    }
    
    // TODO: Fix this!
    @Test 
    public void referToValueInInitialization() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {        
        String[] args = new String[] {"TestUndefinedVariablePrograms/04ReferToValueInInitialization/referToValueInInitialization.ds"};
        Runner.llvmMain(args);
        assertEquals("", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void undeclaredTupleMember() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/05UndeclaredTupleMember/undeclaredTupleMember.ds"};
        Runner.llvmMain(args);
        assertEquals("line 11: unknown member 'j' for tuple t", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void undeclaredTuple() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/06UndeclaredTuple/undeclaredTuple.ds"};
        Runner.llvmMain(args);
        assertEquals("line 7: unknown identifier k", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void undeclaredTupleIndex() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/07UndeclaredTupleIndex/undeclaredTupleIndex.ds"};
        Runner.llvmMain(args);
        assertEquals("line 7: unknown identifier k", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleIndex() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/08InvalidTupleIndex/invalidTupleIndex.ds"};
        Runner.llvmMain(args);
        assertEquals("line 7: t:<(<tuple.b:boolean>, <tuple.b:character>, <tuple.i:integer>, <tuple.r:real>) > tuple member not found t.5", outErrIntercept.toString().trim());
    }
    
    // Programs with invalid types: Put TestInvalidTypePrograms Here
    
    @Test 
    public void invalidTupleSmallerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {
        String[] args = new String[] {"TestInvalidTypePrograms/01TupleSizeSmallerMemberList/tupleSizeSmallerMemberList.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: Tuples have mismatched sizes in tuple(real, integer i) t1 = (0.0, 0, 1);", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleType() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {
        String[] args = new String[] {"TestInvalidTypePrograms/02TupleType/tupleType.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 2: Tuple argument at index 2 has incompatible types in tuple(real, integer i) t1 = (0.0, 0.0);");
        sb.append("\nline 3: Tuple argument at index 1 has incompatible types in tuple(real, boolean b) t2 = (true, 1.0);");
        sb.append("\nline 3: Tuple argument at index 2 has incompatible types in tuple(real, boolean b) t2 = (true, 1.0);");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleSizeLargerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidTypePrograms/03TupleSizeLargerMemberList/TupleSizeLargerMemberList.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: Tuples have mismatched sizes in tuple(real, integer i, integer k) t2 = (0.0, 2);", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void constError() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidTypePrograms/04ConstError/constError.ds"};
        Runner.llvmMain(args);
        assertEquals("line 5: The specifier for a:<integer> is constant and can not be reassigned in a = 1;", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void constArgumentError() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidTypePrograms/05ConstArgumentError/constArgumentError.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: The specifier for a:<integer> is constant and can not be reassigned in a = 5;", outErrIntercept.toString().trim());
    }
      
    @Test 
    public void invalidOutputTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {
        String[] args = new String[] {"TestInvalidTypePrograms/06InvalidOutputTypes/invalidOutputTypes"};
        Runner.llvmMain(args);
        assertEquals("line 20: invalid type (<tuple.a:boolean>, <tuple.b:boolean>)  sent to outstream", outErrIntercept.toString().trim());;
    }
    
    @Test
    public void forwardDeclarationError() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/39ForwardDeclarationError/forwardDeclarationError.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("line 1: unknown identifier f\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void procedureCallInFunction() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/53ProcedureCallInFunction/procedureCallInFunction.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("line 6: Can not call procedure inside function.\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void cantInferNullType() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/37CantInferNullType/cantInferNullType.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 2: type cannot be inferred");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void missingReturn() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/38MissingReturn/missingReturn.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("error: missing return value");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void cantInferIdentityType() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/39CantInferIdentityType/cantInferIdentityType.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 2: type cannot be inferred");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void cantCombineNullIdentity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/40CantCombineNullIdentity/cantCombineNullIdentity.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 2: identity:<identity>, null:<null> have incompatible types in identity + null");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void procedureCallWithFewManyArgs() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/41ProcedureCallWithTooFewArgs/procedureCallWithTooFewArgs.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("line 6: invalid number of args to 'test'");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void invalidAliasing() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidProceduresAndFunctions/01InvalidAliasing/invalidAliasing.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("Error about aliasing");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    
}