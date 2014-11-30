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
    public void testIntervals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/69TestIntervals/testIntervals.ds"};
        Runner.llvmMain(args);
        assertEquals("", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testVectorsFromIntervals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/70TestVectorsFromIntervals/testVectorsFromIntervals.ds"};
        Runner.llvmMain(args);
        assertEquals("12345", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testStrings() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/71TestStrings/testStrings.ds"};
        Runner.llvmMain(args);
        assertEquals("hello", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testMatrices() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/72TestMatrices/testMatrices.ds"};
        Runner.llvmMain(args);
        assertEquals("123456", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void intervalNullTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/73IntervalNullTest/intervalNullTest.ds"};
        Runner.llvmMain(args);
        assertEquals("0", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void intervalIdentityTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/74IntervalIdentityTest/intervalIdentityTest.ds"};
        Runner.llvmMain(args);
        assertEquals("1", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testIntervalAddition() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/75TestIntervalAddition/testIntervalAddition.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4\n");
        sb.append("5\n");
        sb.append("6\n");
        sb.append("7\n");
        sb.append("8\n");
        sb.append("9\n");
        sb.append("10");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testIntervalSubtraction() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/76TestIntervalSubtraction/testIntervalSubtraction.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("5\n");
        sb.append("6\n");
        sb.append("7\n");
        sb.append("8\n");
        sb.append("9\n");
        sb.append("10");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testIntervalMultiplication() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/77TestIntervalMultiplication/testIntervalMultiplication.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4\n");
        sb.append("5\n");
        sb.append("6\n");
        sb.append("7\n");
        sb.append("8");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
        
     @Test 
     public void testIntervalDivision() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/78TestIntervalDivision/testIntervalDivision.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("1\n");
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     @Test 
     public void testIntervalNegation() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/79TestIntervalNegation/testIntervalNegation.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("-2\n");
        sb.append("-1\n");
        sb.append("0\n");
        sb.append("1");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     @Test 
     public void testIntervalUnaryPostive() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/80TestIntervalUnaryPlus/testIntervalUnaryPlus.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("-1\n");
        sb.append("0\n");
        sb.append("1\n");
        sb.append("2");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     @Test 
     public void testIntervalEquals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/81TestIntervalEquals/testIntervalEquals.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("T\n");
        sb.append("F");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     @Test 
     public void testIntervalNotEquals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/82TestIntervalNotEquals/testIntervalNotEquals.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("F\n");
        sb.append("T");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     
     @Test 
     public void testVectorInitializedWithVector() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/83TestVectorInitializedWithVector/testVectorInitializedWithVector.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("0");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     @Test 
     public void testNullVector() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/84TestNullVector/testNullVector.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("F\n");
        sb.append("F\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     @Test 
     public void testIdentityVector() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/85TestIdentityVector/testIdentityVector.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("T\n");
        sb.append("T\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

     @Test 
     public void testIntervalBy() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/86TestIntervalBy/testIntervalBy.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("3\n");
        sb.append("6");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
     
     // Invalid Syntax Test
     @Test 
     public void testInvalidByInterval() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/48InvalidIntervalBy/invalidIntervalBy.ds"};
        Runner.llvmMain(args);
        assertEquals("RuntimeError: Right hand side of by operator must be an integer greater than 0.", outErrIntercept.toString().trim());
    }
     
    // Invalid Syntax Test
     @Test 
     public void inferredVectorWithNull() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestInvalidSyntaxPrograms/45InferredVectorWithNull/inferredVectorWithNull.ds"};
        Runner.llvmMain(args);
        assertEquals("line 5: cannot use null to instantiate an un-sized vector", outErrIntercept.toString().trim());
    }
    
     // Invalid Syntax Test
    @Test 
    public void inferredVectorWithIdentity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/46InferredVectorWithIdentity/inferredVectorWithIdentity.ds"};
       Runner.llvmMain(args);
       assertEquals("line 5: cannot use identity to instantiate an un-sized vector", outErrIntercept.toString().trim());
   }
    
    // Invalid Syntax Test
    @Test 
    public void inferredVectorWithScalar() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/47InferredVectorWithScalar.ds/inferredVectorWithScalar.ds"};
       Runner.llvmMain(args);
       assertEquals("line 4: cannot use scalar or none-vector type to instantiate an un-sized vector integer vector v1 = 1;", outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorConcatenation() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/88TestVectorConcatenation/testVectorConcatenation.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("1\n");
       sb.append("2\n");
       sb.append("3\n");
       sb.append("4\n");
       sb.append("5\n");
       sb.append("1\n");
       sb.append("2\n");
       sb.append("3\n");
       sb.append("4\n");
       sb.append("1\n");
       sb.append("2\n");
       sb.append("3\n");
       sb.append("4.1\n");
       sb.append("0\n");
       sb.append("1\n");
       sb.append("2\n");
       sb.append("3\n");
       sb.append("4.1\n");
       sb.append("4\n");
       sb.append("5\n");
       sb.append("1\n");
       sb.append("2\n");
       sb.append("3\n");
       sb.append("4.1\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorIndexing() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/89TestVectorIndexing/testVectorIndexing.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("5\n");
       sb.append("5\n");
       sb.append("6\n");
       sb.append("6\n");
       sb.append("4\n");
       sb.append("5\n");
       sb.append("T\n");
       sb.append("TTF\n");
       sb.append("c\n");
       sb.append("tac\n");
       sb.append("4.5\n");
       sb.append("6.55.54.5\n");
       sb.append("406\n");
       sb.append("410\n");
       sb.append("220\n");
       sb.append("212\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    // Invalid Runtime Test
    @Test 
    public void testVectorOutOfRange() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/49VectorOutOfRange/vectorOutOfRange.ds"};
       Runner.llvmMain(args);
       assertEquals("RuntimeError: Vector indexing out of bounds.", outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorLength() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/90TestVectorLength/testVectorLength.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("1\n");
       sb.append("2\n");
       sb.append("3\n");
       sb.append("4\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorUnaryOperations() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/91TestVectorUnaryOperations/testVectorUnaryOperations.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("8\n");
       sb.append("9\n");
       sb.append("-8\n");
       sb.append("-9\n");
       sb.append("F\n");
       sb.append("T\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorBinaryOperations() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/92TestVectorBinaryOperations/testVectorBinaryOperations.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       
       // exponents
       sb.append("1\n");
       sb.append("2\n");
       
       // addition
       sb.append("3\n");
       sb.append("3\n");
       
       // subtraction
       sb.append("-1\n");
       sb.append("1\n");
       
       // multiplication
       sb.append("2\n");
       sb.append("2\n");
       
       // division
       sb.append("0\n");
       sb.append("2\n");
       
       // modulus
       sb.append("1\n");
       sb.append("0\n");
       
       // less than
       sb.append("T\n");
       sb.append("F\n");
       
       // less than or equal to
       sb.append("T\n");
       sb.append("F\n");
       
       // greater than
       sb.append("F\n");
       sb.append("T\n");
       
       // greater than or equal to
       sb.append("F\n");
       sb.append("T\n");
       
       // or
       sb.append("T\n");
       sb.append("T\n");
       
       // xor
       sb.append("T\n");
       sb.append("T\n");
       
       // and
       sb.append("F\n");
       sb.append("F\n");
       
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorEqualities() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/93TestVectorEqualities/testVectorEqualities.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("F\n");
       sb.append("T\n");
       sb.append("F\n");
       sb.append("T\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testStringNull() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/94TestStringNull/testStringNull.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append((char)0 + "\n");
       sb.append((char)0 + "\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testStringIdentity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/94TestStringNull/testStringNull.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append((char)1 + "\n");
       sb.append((char)1 + "\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    // Invalid Syntax Test
    @Test 
    public void testInferredStringLengthWithNull() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/50InferredStringLengthWithNull/inferredStringLengthWithNull.ds"};
       Runner.llvmMain(args);
       // strings are vectors so we could use the same error message
       assertEquals("line 6: cannot use null to instantiate an un-sized vector", outErrIntercept.toString().trim());
   }
    
    // Invalid Syntax Test
    @Test 
    public void testInferredStringLengthWithIdentity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/51InferredStringLengthWithIdentity/inferredStringLengthWithIdentity.ds"};
       Runner.llvmMain(args);
       assertEquals("line 6: cannot use identity to instantiate an un-sized vector", outErrIntercept.toString().trim());
   }
    
    // Invalid Syntax Test
    @Test 
    public void testStringLiteral() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/96TestStringLiteral/testStringLiteral.ds"};
       Runner.llvmMain(args);
       assertEquals("\"World!\"", outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testStringEqualities() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/97TestStringEqualities/testStringEqualities.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("T\n");
       sb.append("T\n");
       sb.append("T\n");
       sb.append("T\n");
       sb.append("F\n");
       sb.append("F\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testStringBy() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/98TestStringBy/testStringBy.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("hlo");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testVectorBy() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/99TestVectorBy/testVectorBy.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("12345\n");
       sb.append("12345\n");
       sb.append("135\n");
       sb.append("14\n");
       sb.append("TF\n");
       sb.append("14\n");
       sb.append("1.54.5\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
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
    
    @Test 
    public void testMatrixDeclaration() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/101TestMatrixDeclaration/testMatrixDeclaration.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("1234\n");
       sb.append("1234\n");
       sb.append("1234\n");
       sb.append("1234\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testMatrixIdentity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/102TestMatrixIdentity/testMatrixIdentity.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("1111\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    @Test 
    public void testMatrixNull() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestPrograms/103TestMatrixNull/testMatrixNull.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("0000\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    
    // Invalid Syntax Test
    @Test 
    public void testInferredMatrixLengthWithNull() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/54InferredMatrixLengthWithNull/testInferredMatrixLengthWithNull.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("line 6: cannot use null to instantiate an un-sized matrix\n");
       sb.append("line 7: cannot use null to instantiate an un-sized matrix\n");
       sb.append("line 8: cannot use null to instantiate an un-sized matrix\n");
       sb.append("line 9: cannot use null to instantiate an un-sized matrix\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
    
    // Invalid Syntax Test
    @Test 
    public void testInferredMatrixLengthWithIdentity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
       String[] args = new String[] {"TestInvalidSyntaxPrograms/55InferredMatrixLengthWithIdentity/testInferredMatrixLengthWithIdentity.ds"};
       Runner.llvmMain(args);
       StringBuffer sb = new StringBuffer();
       sb.append("line 6: cannot use identity to instantiate an un-sized matrix\n");
       sb.append("line 7: cannot use identity to instantiate an un-sized matrix\n");
       sb.append("line 8: cannot use identity to instantiate an un-sized matrix\n");
       sb.append("line 9: cannot use identity to instantiate an un-sized matrix\n");
       assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
   }
}
