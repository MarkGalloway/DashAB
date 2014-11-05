/** Types.g Unit Tests. 
 *  Currently just checks that our Symbol Table throws the required errors
 *  when encountering undefined or invalid types. **/
package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestType extends BaseTest {
    
    @Test 
    public void invalidTupleSmallerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuples have mismatched sizes in tuple(real, integer i) t1 = (0.0, 0, 1);");
        String[] args = new String[] {"TestInvalidTypePrograms/01TupleSizeSmallerMemberList/tupleSizeSmallerMemberList.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void invalidTupleType() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuple argument at index 2 has incompatible types in tuple(real, integer i) t1 = (0.0, 0.0);");
        String[] args = new String[] {"TestInvalidTypePrograms/02TupleType/tupleType.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void undefined() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: unknown variable e2");
        String[] args = new String[] {"TestInvalidTypePrograms/03Undefined/undefined.ds"};
        Runner.typesTestMain(args);
    }
    
    
    @Test 
    public void invalidTupleSizeLargerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuples have mismatched sizes in tuple(real, integer i, integer k) t2 = (0.0, 2);");
        String[] args = new String[] {"TestInvalidTypePrograms/04TupleSizeLargerMemberList/TupleSizeLargerMemberList.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void invalidTupleMemberListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 2: tuples must have more than one element");
        String[] args = new String[] {"TestInvalidTypePrograms/05TupleSizeMemberListOf1/TupleSizeMemberListOf1.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void invalidTupleListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 2: tuple lists must have more than one element");
        String[] args = new String[] {"TestInvalidTypePrograms/06TupleSizeTupleListOf1/TupleSizeTupleListOf1.ds"};
        Runner.typesTestMain(args);
    }
    
    
    // TODO: Fix this!
    @Test 
    public void invalidUndefinedButDefinedAfter() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: unknown variable e2");
        String[] args = new String[] {"TestInvalidTypePrograms/07UndefinedButDefinedAfter/undefinedButDefinedAfter.ds"};
        Runner.typesTestMain(args);
    }
    
    // TODO: Fix this!
    @Test 
    public void doubleDeclaration() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("");
        String[] args = new String[] {"TestInvalidTypePrograms/08DoubleDeclaration/doubleDeclaration.ds"};
        Runner.typesTestMain(args);
    }
    
    // TODO: Fix this!
    @Test 
    public void referToValueInInitialization() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: reference to undefined variable");
        String[] args = new String[] {"TestInvalidTypePrograms/09ReferToValueInInitialization/referToValueInInitialization.ds"};
        Runner.typesTestMain(args);
    }
    

    @Test 
    public void constError() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 5: The specifier for a:<integer> is constant and can not be reassigned in a = 1;");
        String[] args = new String[] {"TestPrograms/24ConstError/constError.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void constArgumentError() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: The specifier for a:<integer> is constant and can not be reassigned in a = 5;");
        String[] args = new String[] {"TestPrograms/25ConstArgumentError/constArgumentError.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void promoteTupleTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/28PromoteTupleTypes/promoteTupleTypes.ds"};
        Runner.typesTestMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidOutputTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 20: invalid type (<tuple.a:boolean>, <tuple.b:boolean>) sent to outstream");
        String[] args = new String[] {"TestInvalidTypePrograms/10InvalidOutputTypes/invalidOutputTypes"};
        Runner.typesTestMain(args);
    }
}
