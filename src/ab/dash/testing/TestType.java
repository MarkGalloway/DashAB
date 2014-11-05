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
    public void invalidTupleSizeLargerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuples have mismatched sizes in tuple(real, integer i, integer k) t2 = (0.0, 2);");
        String[] args = new String[] {"TestInvalidTypePrograms/03TupleSizeLargerMemberList/TupleSizeLargerMemberList.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void constError() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 5: The specifier for a:<integer> is constant and can not be reassigned in a = 1;");
        String[] args = new String[] {"TestInvalidTypePrograms/04ConstError/constError.ds"};
        Runner.typesTestMain(args);
    }
    
    @Test 
    public void constArgumentError() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: The specifier for a:<integer> is constant and can not be reassigned in a = 5;");
        String[] args = new String[] {"TestInvalidTypePrograms/05ConstArgumentError/constArgumentError.ds"};
        Runner.typesTestMain(args);
    }
      
    @Test 
    public void invalidOutputTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 20: invalid type (<tuple.a:boolean>, <tuple.b:boolean>) sent to outstream");
        String[] args = new String[] {"TestInvalidTypePrograms/06InvalidOutputTypes/invalidOutputTypes"};
        Runner.typesTestMain(args);
    }
    

}
