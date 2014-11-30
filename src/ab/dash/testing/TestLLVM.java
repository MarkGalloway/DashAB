/** Actual LLVM tests. Given a Dash program,
 *  these tests check that the compiled and executed
 *  llvm code outputs what we expect. **/

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

public class TestLLVM extends BaseTest {
	
	@BeforeClass
    public static void oneTimeSetUp() {
		String[] cmd = {
				"/bin/sh",
				"-c",
				"make clean_runtime --always-make > /dev/null && ",
				"make runtime --always-make > /dev/null"
				};
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
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
        sb.append("0\n");   // e2 -> out; '\n' -> out;      // 24.25

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
    public void inputTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/24InputTest/inputTest.ds", "TestPrograms/24InputTest/input.in"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("Hello, World!\n");
        sb.append("T\n");
        sb.append("F\n");
       	sb.append("125\n");
       	sb.append("125\n");
       	sb.append("-125\n");
       	sb.append("1.25\n");
       	sb.append("1.25\n");
       	sb.append("-1.25\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test 
    public void functionExpression() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/25FunctionExpression/functionExpression.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("1\n");
        sb.append("2.82843\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void promoteTupleTypes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/26PromoteTupleTypes/promoteTupleTypes.ds"};
        Runner.llvmMain(args);
        assertEquals("T", outErrIntercept.toString().trim());
    }
    
    @Test 
    public void literalTuple() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/27LiteralTuple/literalTuple.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("0.5\n");
        sb.append("5\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void dataTypeDeclarationTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/28DataTypeDeclarationTest/datatypeDeclarationTest.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void endOfFileMultiLineComment() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/29EndOfFileMultiLineComment/endOfFileMultiLineComment.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void endOfFileSingleLineComment() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/30EndOfFileSingleLineComment/endOfFileSingleLineComment.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void tupleCompareEquals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/31TupleCompareEquals/tupleCompareEquals.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("T\n");
        sb.append("F\n");
        sb.append("F\n");
        sb.append("F\n");
        sb.append("F\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void tupleCompareNotEquals() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/32TupleCompareNotEquals/tupleCompareNotEquals.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("F\n");
        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void typeCast() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/33TypeCast/typeCast.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("T\n");		// as<boolean>(true) -> out; '\n' -> out;		\\ T
        sb.append("F\n");		// as<boolean>(false) -> out; '\n' -> out;		\\ F
        sb.append("T\n");		// as<boolean>('a') -> out; '\n' -> out;		\\ T
        sb.append("F\n");		// as<boolean>(0) -> out; '\n' -> out;			\\ F
        sb.append("T\n");		// as<boolean>(5) -> out; '\n' -> out;			\\ T
    	
        sb.append("b\n");		// as<character>('b') -> out;  '\n' -> out;		\\ b
        sb.append("a\n");		// as<character>(97) -> out;  '\n' -> out;		\\ a
        sb.append("c\n");		// as<character>(355) -> out;  '\n' -> out;		\\ c
    	
        sb.append("1\n");		// as<integer>(true) -> out;  '\n' -> out;		\\ 1
        sb.append("0\n");		// as<integer>(false) -> out;  '\n' -> out;		\\ 0
        sb.append("98\n");		// as<integer>('b') -> out;  '\n' -> out;		\\ 98
        sb.append("1024\n");	// as<integer>(1024) -> out;  '\n' -> out;		\\ 1024
        sb.append("1\n");		// as<integer>(1.7) -> out;  '\n' -> out;		\\ 1
        sb.append("52\n");		// as<integer>(52.2) -> out;  '\n' -> out;		\\ 52
    	
        sb.append("1\n");		// as<real>(true) -> out;  '\n' -> out;			\\ 1
        sb.append("0\n");		// as<real>(false) -> out;  '\n' -> out;		\\ 0
        sb.append("98\n");		// as<real>('b') -> out;  '\n' -> out;			\\ 98
        sb.append("1024\n");	// as<real>(1024) -> out;  '\n' -> out;			\\ 1024
        sb.append("1.7\n");		// as<real>(1.7) -> out;  '\n' -> out;			\\ 1.7
        sb.append("52.2\n");	// as<real>(52.2) -> out;  '\n' -> out;			\\ 52.2
    	
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void tupleTypeCast() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/34TupleTypeCast/tupleTypeCast.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("1\n");
        sb.append("2\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void promoteFunctionArguments() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/35PromoteFunctionArguments/promoteFunctionArguments.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("25\n");
        sb.append("25\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void tupleAsArgument() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/36TupleAsArgument/tupleAsArgument.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("1\n");
        sb.append("2\n");
        sb.append("1\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void returnedTupleAsArgument() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/37ReturnedTupleAsArgument/returnedTupleAsArgument.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("10\n");
        sb.append("7\n");
        sb.append("1\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void forwardDeclaration() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/38ForwardDeclaration/forwardDeclaration.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("a\n");
        sb.append("b\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testUnitializedTuple() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/40UninitializedTuple/uninitializedTuple.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("0\n0");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testUninitializedReal() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/41UninitializedReal/uninitializedReal.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("0\n0");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    
    @Test 
    public void testUninitializedBoolean() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/42UninitializedBoolean/uninitializedBoolean.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("F\nF");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    
    @Test 
    public void testUninitializedCharacter() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/43UninitializedCharacter/uninitializedCharacter.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    
    @Test 
    public void testUninitializedInteger() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/44UninitializedInteger/uninitializedInteger.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("0\n0");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testNullInteger() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/45NullInteger/nullInteger.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("02");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testIdentityPrimitives() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/46IdentityPrimitives/identityPrimitives.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("11T");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void legalNullTypeInferences() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/47LegalNullTypeInference/legalNullTypeInference.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("0\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("T\n");
        sb.append("F\n");
        sb.append("T\n");
        sb.append("F\n");
        sb.append("0\n");
        sb.append("0\n");
        sb.append("0\n");
        sb.append("T\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void legalIdentityTypeInferences() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/48LegalIdentityTypeInference/legalIdentityTypeInference.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("1\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("F\n");
        sb.append("F\n");
        sb.append("F\n");
        sb.append("T\n");
        sb.append("1\n");
        sb.append("-1\n");
        sb.append("1\n");
        sb.append("F\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void argNullIdentityTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/49ArgNullIdentityTest/argNullIdentityTest.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("T\n");
        sb.append("F\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void nullIdentityTupleComparison() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/50NullInferencePostDeclaration/nullInferencePostDeclaration.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("1\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void identity() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/51Identity/identity.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("F\n");
        sb.append("T\n");
        sb.append((char)0 + "\n");
        sb.append((char)1 + "\n");
        sb.append("0\n");
        sb.append("1\n");
        sb.append("0\n");
        sb.append("1\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void globalTuples() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/52GlobalTuples/globalTuples.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("1\n");
        sb.append("0.1\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void unpacking() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/54Unpacking/unpacking.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("1\n");
        sb.append("0.1\n");
        sb.append("5\n");
        sb.append("0.5\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void promotion() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/55Promotion/promotion.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("5.5\n");
        sb.append("4.4\n");
        sb.append("5.1\n");
        sb.append("1.5\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void nullTupleComparison() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/56NullTupleComparison/nullTupleComparison.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("TT\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void tupleTypeCastWithID() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/57TupleTypeCastWithID/tupleTypeCastWithID.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("1\n");
        sb.append("97\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void variableSwapping() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/58VariableSwapping/variableSwapping.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("5\n");
        sb.append("4\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void tupleTypedef() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/59TupleTypedef/tupleTypedef.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("1\n");
        sb.append("0.5\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void testPromotionInExpression() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/60TestPromotionInExpression/testPromotionInExpression.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("1.41421\n");
        sb.append("1.41421\n");
        sb.append("1.41421\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void validAliasing() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/61ValidAliasing/validAliasing.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void dashABStyleInput() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/62DashABStyleInput/dashABStyleInput.ds", "TestPrograms/62DashABStyleInput/input.in"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("1234\n");
        sb.append("1234\n");
        sb.append("1234\n");
        sb.append("0.42\n");
        sb.append("42\n");
        sb.append("4.2e+11\n");
        sb.append("0.42\n");
        sb.append("42\n");
        sb.append("4.2e-09\n");
        sb.append("4.2e+11\n");
        sb.append("4.2e-09\n");
        sb.append("4.2e+11\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void primes() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/63Primes/primes.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        int p = 1;
    	
    	while (p < 1000) {
    	   	int i = 1;
    	    boolean isPrime = true;
    	    p = p + 1;
    		
    	    while (i < p/2) {
    	        i = i+1;
    	
    	        if ((p/i) * i == p) {
    	            isPrime = false;
    	            i = p;
    	        }
    	    }
    	
    	    if (isPrime) {
    	        sb.append(p + "\n");
    	    }
    	}
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void tupleNull() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/64TupleNull/tupleNull.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("F\n");
        sb.append("\0\n");
        sb.append("0\n");
        sb.append("0\n");
        sb.append("T\n");
        sb.append("\n");
        sb.append("1\n");
        sb.append("1\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void streamState() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/65StreamState/streamState.ds", "TestPrograms/65StreamState/input.in"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        
        sb.append("T\n");
        sb.append("0\n");
        sb.append("1\n");
        sb.append("125\n");
        sb.append("0\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("1.25\n");
        sb.append("0\n");
        sb.append("1\n");
        sb.append("1\n");
        
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void vector() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/66Vector/vector.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("Vector\n");
        sb.append("\tinteger v1[1+3] = [1, 2, 3]\n");
        sb.append("\tinteger v2[*] =  [3, 4, 5]\n");
        sb.append("\tinteger v3[3] = [2, 2, 2]\n");
        sb.append("\tinteger v4[3] = zeros() = [0, 0, 0]\n");
        sb.append("\tinteger v5[*] = 1..3 + v1 + 2..4 = [4, 7, 10]\n");
        sb.append("Addition\n");
        sb.append("\t1 + v1 + 2 = [4, 5, 6]\n");
        sb.append("\tv1 + v2 = [4, 6, 8]\n");
        sb.append("Subtraction\n");
        sb.append("\tv2 - v1 = [2, 2, 2]\n");
        sb.append("\t(5 - v1) - 1) = [3, 2, 1]\n");
        sb.append("Multiplication\n");
        sb.append("\tv1 * v2 = [3, 8, 15]\n");
        sb.append("\t2 * v1 * 3 = [6, 12, 18]\n");
        sb.append("Division\n");
        sb.append("\t(2 * v1 * 3) / 3 = [2, 4, 6]\n");
        sb.append("\t18 / v1 = [18, 9, 6]\n");
        sb.append("\tv1 / v1 = [1, 1, 1]\n");
        sb.append("Modular\n");
        sb.append("\tv2 % v1 = [0, 0, 2]\n");
        sb.append("\tv1 % 2 = [1, 0, 1]\n");
        sb.append("\t2 % v1 = [0, 0, 2]\n");
        sb.append("Power\n");
        sb.append("\tv1 ^ v1 = [1, 4, 27]\n");
        sb.append("\tv1 ^ 2 = [1, 4, 9]\n");
        sb.append("\t2 ^ v1 = [2, 4, 8]\n");
        sb.append("Compare\n");
	    sb.append("\tv1 < v3 = [T, F, F]\n");
        sb.append("\tv1 <= v3 = [T, T, F]\n");
        sb.append("\tv1 > v3 = [F, F, T]\n");
        sb.append("\tv1 >= v3 = [F, T, T]\n");
        sb.append("Equality\n");
        sb.append("\tv1 == v1 = T\n");
        sb.append("\tv1 == v2 = F\n");
        sb.append("\tv1 != v1 = F\n");
        sb.append("\tv1 != v2 = T\n");
        sb.append("Built-in\n");
    	sb.append("\tv1 by 2 = [1, 3]\n");
    	sb.append("\tlength(v1) = 3\n");
    	sb.append("\tv1 || v2 = [1, 2, 3, 3, 4, 5]\n");
        sb.append("123\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void procedureAddOne() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/67ProcedureAddOne/procedureAddOne.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("2\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void gaussianElimination() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/68GaussianElimination/gaussianElimination.ds", "TestPrograms/68GaussianElimination/input.in"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void iterativeLoop() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/87IterativeLoop/iterativeLoop.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("1\n");
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4\n");
        sb.append("5\n");
        sb.append("6\n");
        sb.append("7\n");
        sb.append("8\n");
        sb.append("9\n");
        sb.append("10\n");
        sb.append("1\n");
        sb.append("2\n");
        sb.append("2\n");
        sb.append("4\n");
        sb.append("3\n");
        sb.append("6\n");
        sb.append("4\n");
        sb.append("8\n");
        sb.append("5\n");
        sb.append("10\n");
        sb.append("4\n");
        sb.append("8\n");
        sb.append("6\n");
        sb.append("12\n");
        sb.append("2\n");
        sb.append("4\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void vectorLiteralsAssignmentsAccesses() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/100VectorLiteralsAssignmentsAccesses/vectorLiteralsAssignmentsAccesses.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("8\n");
        sb.append("9\n");
        sb.append("10\n");
        sb.append("4\n");
        sb.append("5\n");
        sb.append("6\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void vectorPromotion() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/104VectorPromotion/vectorPromotion.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("[1, 2]\n");
        sb.append("[1, 2]\n");
        sb.append("[2, 4]\n");
        sb.append("[1.5, 2.5]\n");
        sb.append("[1.5, 2.5]\n");
        sb.append("[1.5, 2.5]\n");
        sb.append("[1.5, 2.5]\n");
        sb.append("[1.5, 2.5]\n");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void intervalAssign() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/105IntervalAssign/intervalAssign.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void memoryTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/106MemoryTest/memoryTest.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void vectorAddition() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/107VectorAddition/vectorAddition.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("5\n");
        sb.append("7\n");
        sb.append("9\n");
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4\n");
        sb.append("5\n");
        sb.append("6\n");
        sb.append("7\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void vectorSubtraction() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/108VectorSubtraction/vectorSubtraction.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("5\n");
        sb.append("3\n");
        sb.append("1\n");
        sb.append("0\n");
        sb.append("1\n");
        sb.append("2\n");
        sb.append("9\n");
        sb.append("8\n");
        sb.append("7\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void vectorMultiplicationDivision() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/109VectorMultiplicationDivision/vectorMultiplicationDivision.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("16\n");
        sb.append("27\n");
        sb.append("50\n");
        sb.append("4\n");
        sb.append("6\n");
        sb.append("10\n");
        sb.append("6\n");
        sb.append("9\n");
        sb.append("15\n");

        sb.append("4\n");
        sb.append("3\n");
        sb.append("2\n");
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4\n");
        sb.append("15\n");
        sb.append("10\n");
        sb.append("6\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void vectorModulus() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/110VectorModulus/vectorModulus.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("0\n");
        sb.append("1\n");
        sb.append("2\n");
        sb.append("1\n");
        sb.append("1\n");
        sb.append("3\n");
        sb.append("2\n");
        sb.append("1\n");
        sb.append("0\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void vectorAnd() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/111VectorAnd/vectorAnd.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("F\n");
        sb.append("F\n");
        sb.append("F\n");
        sb.append("T\n");

        sb.append("F\n");
        sb.append("T\n");
        sb.append("F\n");
        sb.append("T\n");

        sb.append("F\n");
        sb.append("T\n");
        sb.append("F\n");
        sb.append("T\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void vectorOr() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/112VectorOr/vectorOr.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("F\n");
        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");

        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");

        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");
        sb.append("T\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void vectorXor() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/113VectorXor/vectorXor.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("F\n");
        sb.append("T\n");
        sb.append("T\n");
        sb.append("F\n");

        sb.append("T\n");
        sb.append("F\n");
        sb.append("T\n");
        sb.append("F\n");

        sb.append("F\n");
        sb.append("T\n");
        sb.append("F\n");
        sb.append("T\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void vectorDeclLargerSizedVector() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/114VectorDeclLargerSizedVector/vectorDeclLargerSizedVector.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Right hand side vector must be smaller or equal to the declared size of left hand side vector.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void invalidIndexingLower() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/115InvalidIndexingLower/invalidIndexingLower.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vector indexing out of bounds.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void invalidIndexingUpper() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/116InvalidIndexingUpper/invalidIndexingUpper.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vector indexing out of bounds.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void invalidIndexingWithVectorLower() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/117InvalidIndexingWithVectorLower/invalidIndexingWithVectorLower.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vector indexing out of bounds.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test
    public void invalidIndexingWithVectorUpper() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/118InvalidIndexingWithVectorUpper/invalidIndexingWithVectorUpper.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vector indexing out of bounds.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
}
