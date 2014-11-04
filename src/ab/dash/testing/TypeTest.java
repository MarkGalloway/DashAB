package ab.dash.testing;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TypeTest extends BaseTest {
    
    @Test 
    public void invalidTupleSmallerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuples have mismatched sizes in tuple(real, integer i) t1 = (0.0, 0, 1);");
        String[] args = new String[] {"TestInvalidTypePrograms/01TupleSizeSmallerMemberList/tupleSizeSmallerMemberList.ds"};
        TypesTestMain.main(args);
    }
    
    @Test 
    public void invalidTupleType() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuple argument at index 2 has incompatible types in tuple(real, integer i) t1 = (0.0, 0.0);");
        String[] args = new String[] {"TestInvalidTypePrograms/02TupleType/tupleType.ds"};
        TypesTestMain.main(args);
    }
    
    @Test 
    public void undefined() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: e2 is not defined in the program.");
        String[] args = new String[] {"TestInvalidTypePrograms/03Undefined/undefined.ds"};
        TypesTestMain.main(args);
    }
    
    
    @Test 
    public void invalidTupleSizeLargerMemberList() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuples have mismatched sizes in tuple(real, integer i, integer k) t2 = (0.0, 2);");
        String[] args = new String[] {"TestInvalidTypePrograms/04TupleSizeLargerMemberList/TupleSizeLargerMemberList.ds"};
        TypesTestMain.main(args);
    }
    
    @Test 
    public void invalidTupleMemberListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 2: tuples must have more than one element");
        String[] args = new String[] {"TestInvalidTypePrograms/05TupleSizeMemberListOf1/TupleSizeMemberListOf1.ds"};
        TypesTestMain.main(args);
    }
    
    @Test 
    public void invalidTupleListOf1() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(ParserException.class);
        expectedEx.expectMessage("line 2: tuple lists must have more than one element");
        String[] args = new String[] {"TestInvalidTypePrograms/06TupleSizeTupleListOf1/TupleSizeTupleListOf1.ds"};
        TypesTestMain.main(args);
    }
    
    
    // TODO: Fix this!
    @Test 
    public void invalidUndefinedButDefinedAfter() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: Tuples have mismatched sizes in tuple(real, integer i, integer k) t2 = (0.0, 2);");
        String[] args = new String[] {"TestInvalidTypePrograms/07UndefinedButDefinedAfter/undefinedButDefinedAfter.ds"};
        TypesTestMain.main(args);
    }
    
    // TODO: Fix this!
    @Test 
    public void doubleDeclaration() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("");
        String[] args = new String[] {"TestInvalidTypePrograms/08DoubleDeclaration/doubleDeclaration.ds"};
        TypesTestMain.main(args);
    }
    
    // TODO: Fix this!
    @Test 
    public void referToValueInInitialization() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("");
        String[] args = new String[] {"TestInvalidTypePrograms/09ReferToValueInInitialization/referToValueInInitialization.ds"};
        TypesTestMain.main(args);
    }
    
    
    
}
