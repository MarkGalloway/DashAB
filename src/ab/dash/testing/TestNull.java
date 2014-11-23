/** Def.g Unit Tests. Compares symbol table globals to expected globals. **/
/** TODO: Add Error Tests **/
package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.Runner;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestNull extends BaseTest {
    
    
    @Test 
    public void testUnitializedTuple() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/40UninitializedTuple/uninitializedTuple.ds"};
        Runner.nullTestMain(args);
        String expected = "(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL (tuple (FIELD_DECL var integer a) (FIELD_DECL var integer b)) t2<(<tuple.a:integer>, <tuple.b:integer>) > (EXPR<(<tuple.null:integer>, <tuple.null:integer>) > (TUPLE_LIST<(<tuple.null:integer>, <tuple.null:integer>) > (EXPR<integer> 0<integer>) (EXPR<integer> 0<integer>)))) (PRINT (EXPR<integer> (.<integer> t2<(<tuple.a:integer>, <tuple.b:integer>) > 1<integer>))) (PRINT (EXPR<character> '\\n'<character>)) (PRINT (EXPR<integer> (.<integer> t2<(<tuple.a:integer>, <tuple.b:integer>) > 2<integer>))) (return<integer> (EXPR<integer> 0<integer>)))))";
        assertEquals(expected, outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testUnitializedReal() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/41UninitializedReal/uninitializedReal.ds"};
        Runner.nullTestMain(args);
        String expected = "(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (VAR_DECL real j<real> (EXPR<real> 0.0<real>)) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL real i<real> (EXPR<real> 0.0<real>)) (PRINT (EXPR<real> i<real>)) (PRINT (EXPR<character> '\\n'<character>)) (PRINT (EXPR<real> j<real>)) (return<integer> (EXPR<integer> 0<integer>)))))";
        assertEquals(expected, outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testUnitializedBoolean() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/42UninitializedBoolean/uninitializedBoolean.ds"};
        Runner.nullTestMain(args);
        String expected = "(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (VAR_DECL boolean j<boolean> (EXPR<boolean> false<boolean>)) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL boolean i<boolean> (EXPR<boolean> false<boolean>)) (PRINT (EXPR<boolean> i<boolean>)) (PRINT (EXPR<character> '\\n'<character>)) (PRINT (EXPR<boolean> j<boolean>)) (return<integer> (EXPR<integer> 0<integer>)))))";
        assertEquals(expected, outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testUnitializedCharacter() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/43UninitializedCharacter/uninitializedCharacter.ds"};
        Runner.nullTestMain(args);
        String expected = "(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (VAR_DECL character j<character> (EXPR<character> '\\0'<character>)) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL character i<character> (EXPR<character> '\\0'<character>)) (PRINT (EXPR<character> i<character>)) (PRINT (EXPR<character> '\\n'<character>)) (PRINT (EXPR<character> j<character>)) (return<integer> (EXPR<integer> 0<integer>)))))";
        assertEquals(expected, outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testUnitializedInteger() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/44UninitializedInteger/uninitializedInteger.ds"};
        Runner.nullTestMain(args);
        String expected = "(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (VAR_DECL integer j<integer> (EXPR<integer> 0<integer>)) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL integer i<integer> (EXPR<integer> 0<integer>)) (PRINT (EXPR<integer> i<integer>)) (PRINT (EXPR<character> '\\n'<character>)) (PRINT (EXPR<integer> j<integer>)) (return<integer> (EXPR<integer> 0<integer>)))))";
        assertEquals(expected, outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testNullInteger() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/45NullInteger/nullInteger.ds"};
        Runner.nullTestMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL integer i<integer> (EXPR<integer> 0<integer>)) (VAR_DECL integer j<integer> (EXPR<integer> (+<integer> 2<integer> 0<integer:integer>))) (PRINT (EXPR<integer> i<integer>)) (PRINT (EXPR<integer> j<integer>)) (return<integer> (EXPR<integer> 0<integer>)))))");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void testIdentityPrimitives() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/46IdentityPrimitives/identityPrimitives.ds"};
        Runner.nullTestMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL integer j<integer> (EXPR<integer> 1<integer>)) (VAR_DECL real k<real> (EXPR<real> 1.0<real>)) (VAR_DECL character c<character> (EXPR<character> ''<character>)) (VAR_DECL boolean t<boolean> (EXPR<boolean> true<boolean>)) (PRINT (EXPR<integer> j<integer>)) (PRINT (EXPR<real> k<real>)) (PRINT (EXPR<character> c<character>)) (PRINT (EXPR<boolean> t<boolean>)) (return<integer> (EXPR<integer> 0<integer>)))))");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
    
    @Test 
    public void argNullIdentityTest() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/49ArgNullIdentityTest/argNullIdentityTest.ds"};
        Runner.nullTestMain(args);
        StringBuffer sb = new StringBuffer();
        sb.append("(PROGRAM (DECL_OUTSTREAM const out<std_output()> std_output()) (FUNCTION_DECL test2<boolean> (ARG_DECL boolean b<boolean>) (BLOCK (return<boolean> (EXPR<boolean> b<boolean>)))) (PROCEDURE_DECL test<boolean> (ARG_DECL boolean a<boolean>) (ARG_DECL boolean b<boolean>) (BLOCK (return<boolean> (EXPR<boolean> b<boolean>)))) (PROCEDURE_DECL main<integer> (BLOCK (VAR_DECL boolean a1<boolean> (EXPR<boolean> (CALL<boolean> test<boolean> (ELIST (EXPR<boolean> false<boolean>) (EXPR<boolean> true<boolean>))))) (VAR_DECL boolean a2<boolean> (EXPR<boolean> (CALL<boolean> test2<boolean> (ELIST (EXPR<boolean> false<boolean>))))) (PRINT (EXPR<boolean> a1<boolean>)) (PRINT (EXPR<character> '\\n'<character>)) (PRINT (EXPR<boolean> a2<boolean>)) (PRINT (EXPR<character> '\\n'<character>)) (return<integer> (EXPR<integer> 0<integer>)))))");
        assertEquals(sb.toString().trim(), outErrIntercept.toString().trim());
    }
}

