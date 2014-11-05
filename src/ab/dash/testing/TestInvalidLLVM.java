/** Dash programs who's LLVM should never run due errors. 
 *  DONT CHECK FOR JAVA EXCEPTIONS HERE, this should be the output the
 *  user sees when using our compiler - check actual output stream for expected error message instead.**/

package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestInvalidLLVM extends BaseTest {
    
    // Programs with Invalid Syntax
    
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
        sb.append("\nline 2: tuple lists must have more than one element");
        assertEquals(sb.toString(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException, IOException {        
        String[] args = new String[] {"TestInvalidSyntaxPrograms/03TupleSizeTupleListOf1/TupleSizeTupleListOf1.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: tuple lists must have more than one element", outErrIntercept.toString().trim());
    }
    
    // Programs with undeclared variables
    
    @Test 
    public void undefined() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/01Undefined/undefined.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: unknown variable e2", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidUndefinedButDefinedAfter() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {        
        String[] args = new String[] {"TestUndefinedVariablePrograms/02UndefinedButDefinedAfter/undefinedButDefinedAfter.ds"};
        Runner.llvmMain(args);
        assertEquals("line 2: unknown variable e2", outErrIntercept.toString().trim());
    }
    
    // TODO: Fix this!
    @Test 
    public void doubleDeclaration() throws RecognitionException, LexerException, ParserException, SymbolTableException, IOException, InterruptedException {        
        String[] args = new String[] {"TestUndefinedVariablePrograms/03DoubleDeclaration/doubleDeclaration.ds"};
        Runner.llvmMain(args);
        assertEquals("", outErrIntercept.toString().trim());
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
        assertEquals("line 7: unknown variable k", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void undeclaredTupleIndex() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/07UndeclaredTupleIndex/undeclaredTupleIndex.ds"};
        Runner.llvmMain(args);
        assertEquals("line 7: unknown variable k", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidTupleIndex() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestUndefinedVariablePrograms/08InvalidTupleIndex/invalidTupleIndex.ds"};
        Runner.llvmMain(args);
        assertEquals("line 7: invalid index for tuple t", outErrIntercept.toString().trim());
    }
    
    // Programs with invalid types
    
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
        assertEquals("line 20: invalid type (<tuple.a:boolean>, <tuple.b:boolean>) sent to outstream", outErrIntercept.toString().trim());;
    }
    
}