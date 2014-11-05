/** Dash programs who's LLVM should never run due errors. 
 *  DONT CHECK FOR JAVA EXCEPTIONS HERE, this should be the output the
 *  user sees when using our compiler - check actual output stream for expected error message instead.**/

package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestInvalidLLVM extends BaseTest {
    
    @Test 
    public void invalidCharacter() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/01InvalidCharacter/invalidCharacter.ds"};
        Runner.llvmMain(args);
        assertEquals("line 5: expected single quotes for character", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void invalidOutputTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidTypePrograms/10InvalidOutputTypes/invalidOutputTypes"};
        Runner.llvmMain(args);
        assertEquals("line 20: invalid type (<tuple.a:boolean>, <tuple.b:boolean>) sent to outstream", outErrIntercept.toString().trim());
    }
}