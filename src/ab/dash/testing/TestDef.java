/** Def.g Unit Tests. Compares symbol table globals to expected globals. **/
/** TODO: Add Error Tests **/
package ab.dash.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.Runner;
import ab.dash.ast.DashAST;
import ab.dash.ast.MethodSymbol;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestDef extends BaseTest {
    
    @Test 
    public void simpleMain() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/01SimpleMain/simpleMain.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        assertEquals(base_globals, symtab.globals.keys());
    }
    
    @Test 
    public void localVariables() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/02VariableDeclarationInMain/variableDeclarationInMain.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());
    }
    
    @Test 
    public void ifStatement() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/04SimpleIfStatement/simpleIfStatement.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());    
    }

    @Test 
    public void tuples() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/05Tuples/tuples.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());    
    }

    @Test 
    public void typeInference() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/06TypeInference/typeInference.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("b");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test 
    public void integers() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/07IntegersTokens/integersTokens.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());    
    }

    @Test
    public void procedures() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/08ProcedureWithNoReturnType/procedureWithNoReturnType.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("no_return");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test 
    public void proceduresWithArgs() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/12ProcedureWithArgs/procedureWithArgs.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("no_return");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test 
    public void typedef() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/09Typedef/typedef.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("x");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test 
    public void multiFunction() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/11MultiFunction/multiFunction.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("squared");
        base_globals.add("squaredPlus1");
        base_globals.add("blah");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test
    public void functionWithArgs() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/10FunctionWithArgs/functionWithArgs.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("squared");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    // TODO: Why is this failing here T_T
    @Test 
    public void tupleReturn() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/14TupleReturn/tupleReturn.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        assertEquals("", outErrIntercept.toString().trim());
    }
    
    // Tests with undefined values
    
    @Test 
    public void undefined() throws RecognitionException, LexerException, ParserException, SymbolTableException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: unknown identifier e2");
        String[] args = new String[] {"TestUndefinedVariablePrograms/01Undefined/undefined.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    @Test 
    public void invalidUndefinedButDefinedAfter() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: unknown identifier e2");
        String[] args = new String[] {"TestUndefinedVariablePrograms/02UndefinedButDefinedAfter/undefinedButDefinedAfter.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    // TODO: Fix this!
    @Test 
    public void doubleDeclaration() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 3: Identifier e1 declared twice in the same scope.");
        String[] args = new String[] {"TestUndefinedVariablePrograms/03DoubleDeclaration/doubleDeclaration.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    // TODO: Fix this!
    @Test 
    public void referToValueInInitialization() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 2: reference to undefined variable");
        String[] args = new String[] {"TestUndefinedVariablePrograms/04ReferToValueInInitialization/referToValueInInitialization.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    @Test 
    public void undeclaredTupleMember() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 11: unknown member 'j' for tuple t");
        String[] args = new String[] {"TestUndefinedVariablePrograms/05UndeclaredTupleMember/undeclaredTupleMember.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    @Test 
    public void undeclaredTuple() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 7: unknown identifier k");
        String[] args = new String[] {"TestUndefinedVariablePrograms/06UndeclaredTuple/undeclaredTuple.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    @Test 
    public void undeclaredTupleIndex() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 7: unknown identifier k");
        String[] args = new String[] {"TestUndefinedVariablePrograms/07UndeclaredTupleIndex/undeclaredTupleIndex.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    @Test 
    public void invalidTupleIndex() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        expectedEx.expect(SymbolTableException.class);
        expectedEx.expectMessage("line 7: invalid index for tuple t");
        String[] args = new String[] {"TestUndefinedVariablePrograms/08InvalidTupleIndex/invalidTupleIndex.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
    }
    
    @Test
    public void vector() throws IOException, RecognitionException, LexerException, ParserException, SymbolTableException, InterruptedException {
        String[] args = new String[] {"TestPrograms/66Vector/vector.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("inp");
        assertEquals(base_globals, symtab.globals.keys());
        
        MethodSymbol main = (MethodSymbol) symtab.globals.resolve("main");
        if (main == null)
        	fail("main method not found in globals.");
        
        DashAST main_node = main.def;
        
        if (main_node == null)
        	fail("main method not attached to AST node.");
        
        DashAST block = (DashAST) main_node.parent.getChild(1);
        
        Set<String> main_locals = new LinkedHashSet<String>();
        main_locals.add("v1");
        main_locals.add("v2");
        main_locals.add("v3");
        main_locals.add("v4");
        main_locals.add("v5");
        main_locals.add("v6");
     	assertEquals(main_locals.toString(), block.scope.toString());
    }
}

