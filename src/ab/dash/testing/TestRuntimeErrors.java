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

public class TestRuntimeErrors extends BaseTest {
	
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
    public void addDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/001AddDifferentLengthVectors/addDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void subtractDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/002SubtractDifferentLengthVectors/subtractDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void multiplyDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/003MultiplyDifferentLengthVectors/multiplyDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void divideDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/004DivideDifferentLengthVectors/divideDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void andDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/005AndDifferentLengthVectors/andDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void orDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/006OrDifferentLengthVectors/orDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void xorDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/007XorDifferentLengthVectors/xorDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void ltDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/008LTDifferentLengthVectors/ltDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void leDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/009LEDifferentLengthVectors/leDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void gtDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/010GTDifferentLengthVectors/gtDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void geDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/011GEDifferentLengthVectors/geDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void modDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/012ModDifferentLengthVectors/modDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }

    @Test
    public void powerDifferentLengthVectors() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestRuntimeErrors/013PowerDifferentLengthVectors/powerDifferentLengthVectors.ds"};
        Runner.llvmMain(args);
        StringBuffer sb = new StringBuffer();

        sb.append("RuntimeError: Vectors are not of same length.\n");

        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
}
