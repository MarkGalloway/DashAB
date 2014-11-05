/** Actual LLVM tests. Given a Dash program,
 *  these tests check that the compiled and executed
 *  llvm code outputs what we expect. **/

package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestLLVM extends BaseTest {
    
    @Test 
    public void simpleMain() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/01SimpleMain/simpleMain.ds"};
        Runner.llvmMain(args);
        assertEquals("", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void variableDeclarationInMain() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/02VariableDeclarationInMain/variableDeclarationInMain.ds"};
        Runner.llvmMain(args);
        assertEquals("2", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void declareGlobals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/03DeclareGlobals/declareGlobals.ds"};
        Runner.llvmMain(args);
        assertEquals("5", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void simpleIfStatement() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/04SimpleIfStatement/simpleIfStatement.ds"};
        Runner.llvmMain(args);
        assertEquals("14", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void tuples() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/05Tuples/tuples.ds"};
        Runner.llvmMain(args);
        assertEquals("T\na\n2\n3.5", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void typeInference() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/06TypeInference/typeInference.ds"};
        Runner.llvmMain(args);
        assertEquals("T10.5a", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void integersTokens() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/07IntegersTokens/integersTokens.ds"};
        Runner.llvmMain(args);
        assertEquals("0\n145\n145", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void procedureWithNoReturnType() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/08ProcedureWithNoReturnType/procedureWithNoReturnType.ds"};
        Runner.llvmMain(args);
        assertEquals("f", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void typedef() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/09Typedef/typedef.ds"};
        Runner.llvmMain(args);
        assertEquals("5", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void functionWithArgs() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/10FunctionWithArgs/functionWithArgs.ds"};
        Runner.llvmMain(args);
        assertEquals("25", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void multiFunction() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/11MultiFunction/multiFunction.ds"};
        Runner.llvmMain(args);
        assertEquals("26", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void procedureWithArgs() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/12ProcedureWithArgs/procedureWithArgs.ds"};
        Runner.llvmMain(args);
        assertEquals("1", outErrIntercept.toString().trim());
    }

    @Test 
    public void ifelse() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/13IfElse/ifelse.ds"};
        Runner.llvmMain(args);
        assertEquals("1", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void tupleReturn() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/14TupleReturn/tupleReturn.ds"};
        Runner.llvmMain(args);
        assertEquals("b\na", outErrIntercept.toString().trim());
    }

    @Test 
    public void whileLoop() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/15WhileLoop/whileLoop.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<10; ++i) { sb.append(i + "\n"); }
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test 
    public void doWhileLoop() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/16DoWhileLoop/doWhileLoop.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<10; ++i) { sb.append(i + "\n"); }
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void infLoop() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/17InfLoop/infLoop.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<12; ++i) { sb.append(i + "\n"); }
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test 
    public void loopContinue() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/18LoopContinue/loopContinue.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        for(int i=1; i<11; ++i) { 
            if (i!=7) { sb.append(i + "\n");  }
        }
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void booleans() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/19Booleans/booleans.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("T\n");	// b0 -> out;	'\n' -> out;	// true
        sb.append("F\n");	// b1 -> out;	'\n' -> out;	// false
        
        sb.append("F\n");	// b2 -> out;	'\n' -> out;	// false
        sb.append("T\n");	// b3 -> out;	'\n' -> out;	// true
        
        sb.append("T\n");	// b4 -> out;	'\n' -> out;	// true
        sb.append("F\n");	// b5 -> out;	'\n' -> out;	// false
        sb.append("F\n");	// b6 -> out;	'\n' -> out;	// false
        sb.append("T\n");	// b7 -> out;	'\n' -> out;	// true
    	
    	
        sb.append("F\n");	// b8 -> out;	'\n' -> out;	// false
        sb.append("T\n");	// b9 -> out;	'\n' -> out;	// true
        sb.append("T\n");	// b10 -> out;	'\n' -> out;	// true
        sb.append("F\n");	// b11 -> out;	'\n' -> out;	// false
    	
        sb.append("F\n");	// b12 -> out;	'\n' -> out;	// false
    	sb.append("F\n");	// b13 -> out;	'\n' -> out;	// false
    	sb.append("F\n");	// b14 -> out;	'\n' -> out;	// false
    	sb.append("T\n");	// b15 -> out;	'\n' -> out;	// true
    	
    	sb.append("F\n");	// b16 -> out;	'\n' -> out;	// false
    	sb.append("T\n");	// b17 -> out;	'\n' -> out;	// true
    	sb.append("T\n");	// b18 -> out;	'\n' -> out;	// true
    	sb.append("T\n");	// b19 -> out;	'\n' -> out;	// true
    	
    	sb.append("F\n");	// b20 -> out;	'\n' -> out;	// false
    	sb.append("T\n");	// b21 -> out;	'\n' -> out;	// true
    	sb.append("T\n");	// b22 -> out;	'\n' -> out;	// true
    	sb.append("F\n");	// b23 -> out;	'\n' -> out;	// false
    	
    	sb.append("T\n");	// b24 -> out;	'\n' -> out;	// true
    	sb.append("F\n");	// b25 -> out;	'\n' -> out;	// false
    	
    	sb.append("T\n");	// l1 -> out;	'\n' -> out;	// true
    	sb.append("T\n");	// l2 -> out;	'\n' -> out;	// true
    	
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }  
    
    @Test 
    public void characters() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/20Characters/characters.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("a\n");	// c0 -> out; '\n' -> out;		// a
        sb.append("b\n");	// c1 -> out; '\n' -> out;		// b
    	
        sb.append("T\n");	// b0 -> out; '\n' -> out;		// true
        sb.append("F\n");	// b1 -> out; '\n' -> out;		// false

        sb.append("F\n");	// b2 -> out; '\n' -> out;		// false
        sb.append("T\n");	// b3 -> out; '\n' -> out;		// true
    	
        sb.append("T\n");	// b4 -> out; '\n' -> out;		// true
        sb.append("F\n");	// b5 -> out; '\n' -> out;		// false
        sb.append("F\n");	// b6 -> out; '\n' -> out;		// false
    	
        sb.append("F\n");	// b7 -> out; '\n' -> out;		// false
        sb.append("F\n");	// b8 -> out; '\n' -> out;		// false
        sb.append("T\n");	// b9 -> out; '\n' -> out;		// true
    	
        sb.append("T\n");	// b10 -> out; '\n' -> out;		// true
        sb.append("T\n");	// b11 -> out; '\n' -> out;		// true
        sb.append("F\n");	// b12 -> out; '\n' -> out;		// false
    	
        sb.append("F\n");	// b13 -> out; '\n' -> out;		// false
        sb.append("T\n");	// b14 -> out; '\n' -> out;		// true
        sb.append("T\n");	// b15 -> out; '\n' -> out;		// true
    	
        sb.append("T\n");	// l1 -> out; '\n' -> out;		// true
        sb.append("T\n");	// l2 -> out; '\n' -> out;		// true
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void integers() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/21Integers/integers.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("-5\n");	// i0 -> out; '\n' -> out;		// -5
        sb.append("7\n");	// i1 -> out; '\n' -> out;		// 7
        sb.append("3\n");	// i2 -> out; '\n' -> out;		// 3
        sb.append("10\n");	// i3 -> out; '\n' -> out;		// 10
        sb.append("5\n");	// i4 -> out; '\n' -> out;		// 5
        sb.append("1\n");	// i5 -> out; '\n' -> out;		// 1
        sb.append("8\n");	// i6 -> out; '\n' -> out;		// 8

        sb.append("11\n");	// e0 -> out; '\n' -> out;		// 11
        sb.append("15\n");	// e1 -> out; '\n' -> out;		// 15
        sb.append("20\n");	// e2 -> out; '\n' -> out;		// 20

        sb.append("T\n");	// b0 -> out; '\n' -> out;		// true;
        sb.append("F\n");	// b1 -> out; '\n' -> out;		// false

        sb.append("F\n");	// b2 -> out; '\n' -> out;		// false
        sb.append("T\n");	// b3 -> out; '\n' -> out;		// true

        sb.append("T\n");	// b4 -> out; '\n' -> out;		// true
        sb.append("F\n");	// b5 -> out; '\n' -> out;		// false
        sb.append("F\n");	// b6 -> out; '\n' -> out;		// false

        sb.append("F\n");	// b7 -> out; '\n' -> out;		// false
    	sb.append("F\n");	// b8 -> out; '\n' -> out;		// false
    	sb.append("T\n");	// b9 -> out; '\n' -> out;		// true

    	sb.append("T\n");	// b10 -> out; '\n' -> out;		// true
    	sb.append("T\n");	// b11 -> out; '\n' -> out;		// true
    	sb.append("F\n");	// b12 -> out; '\n' -> out;		// false

    	sb.append("F\n");	// b13 -> out; '\n' -> out;		// false
    	sb.append("T\n");	// b14 -> out; '\n' -> out;		// true
    	sb.append("T\n");	// b15 -> out; '\n' -> out;		// true
    	
    	sb.append("25\n");	// l1 -> out; '\n' -> out;		// 25
    	sb.append("30\n");	// l2 -> out; '\n' -> out;		// 30
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void reals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/22Reals/reals.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("-5.5\n");	// f0 -> out; '\n' -> out;		// -5.5
        sb.append("7.5\n");		// f1 -> out; '\n' -> out;		// 7.5
        sb.append("3.5\n");		// f2 -> out; '\n' -> out;		// 3.5
        sb.append("11\n");		// f3 -> out; '\n' -> out;		// 11
        sb.append("5.25\n");	// f4 -> out; '\n' -> out;		// 5.25
        sb.append("0.5\n");		// f5 -> out; '\n' -> out;		// 0.5
        sb.append("15.625\n");	// f6 -> out; '\n' -> out;		// 15.625

        sb.append("15.25\n");	// e0 -> out; '\n' -> out;		// 15.25
        sb.append("18.75\n");	// e1 -> out; '\n' -> out;		// 18.75
        sb.append("24.25\n");	// e2 -> out; '\n' -> out;		// 24.25

        sb.append("T\n");		// b0 -> out; '\n' -> out;		// true;
        sb.append("F\n");		// b1 -> out; '\n' -> out;		// false

        sb.append("F\n");		// b2 -> out; '\n' -> out;		// false
        sb.append("T\n");		// b3 -> out; '\n' -> out;		// true

        sb.append("T\n");		// b4 -> out; '\n' -> out;		// true
        sb.append("F\n");		// b5 -> out; '\n' -> out;		// false
        sb.append("F\n");		// b6 -> out; '\n' -> out;		// false

        sb.append("F\n");		// b7 -> out; '\n' -> out;		// false
    	sb.append("F\n");		// b8 -> out; '\n' -> out;		// false
    	sb.append("T\n");		// b9 -> out; '\n' -> out;		// true

    	sb.append("T\n");		// b10 -> out; '\n' -> out;		// true
    	sb.append("T\n");		// b11 -> out; '\n' -> out;		// true
    	sb.append("F\n");		// b12 -> out; '\n' -> out;		// false

    	sb.append("F\n");		// b13 -> out; '\n' -> out;		// false
    	sb.append("T\n");		// b14 -> out; '\n' -> out;		// true
    	sb.append("T\n");		// b15 -> out; '\n' -> out;		// true
    	
    	sb.append("29.75\n");	// l1 -> out; '\n' -> out;		// 29.75
    	sb.append("35.25\n");	// l2 -> out; '\n' -> out;		// 35.25
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void realTokens() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/23RealTokens/realTokens.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("0.42\n");
        sb.append("42\n");
        sb.append("4.2e+11\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void functionExpression() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/27FunctionExpression/functionExpression.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void promoteTupleTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/28PromoteTupleTypes/promoteTupleTypes.ds"};
        Runner.llvmMain(args);
        assertEquals("T", outErrIntercept.toString().trim());
    }
}