/** Dash Spec 2 Tests 
 * 
 * These Tests ARE NOT part of the Full Test Suite
 * To run these tests run this file separately.
 * 
 * **/

package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.Test;

import ab.dash.Runner;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestUnimplemented extends BaseTest {
    
    
    @Test 
    public void testBinaryOpsDifferentSizeVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/52BinaryOperationsWithVectorsOfDifferentSizes/binaryOperationsWithVectorsOfDifferentSizes.ds"};
       Runner.llvmMain(args);
       assertEquals("Runtime Error: vector size mismatch", outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testEqualityOpsDifferentSizeVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/53EqualityOperationsWithVectorsOfDifferentSizes/equalityOperationsWithVectorsOfDifferentSizes.ds"};
       Runner.llvmMain(args);
       assertEquals("Runtime Error: vector size mismatch", outErrIntercept.toString().trim());
   }
   
}
