/** Def.g Unit Tests. Compares symbol table globals to expected globals. **/
/** TODO: Add Error Tests **/
package ab.dash.testing;

import static org.junit.Assert.assertEquals;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TestDef extends BaseTest {
    
    @Test // Check that the globals are what we expect for a program that just defines main
    public void simpleMain() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/01SimpleMain/simpleMain.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        assertEquals(base_globals, symtab.globals.keys());
    }
    
    
    @Test // Check that the globals are declared correctly when local variables are added
    public void localVariables() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/02VariableDeclarationInMain/variableDeclarationInMain.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());
    }
    
    @Test // Test with if statements
    public void ifStatement() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/04SimpleIfStatement/simpleIfStatement.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());    
    }

    @Test // Test with tuples
    public void tuples() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/05Tuples/tuples.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());    
    }

    @Test // Test with type inference
    public void typeInference() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/06TypeInference/typeInference.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("b");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test // Test with integers
    public void integers() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/07Integers/integers.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        assertEquals(base_globals, symtab.globals.keys());    
    }

    @Test // Test with procedures
    public void procedures() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/08ProcedureWithNoReturnType/procedureWithNoReturnType.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("no_return");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test // Test with procedures with args
    public void proceduresWithArgs() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/12ProcedureWithArgs/procedureWithArgs.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("no_return");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test // Test with typedefs
    public void typedef() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/09Typedef/typedef.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("out");
        base_globals.add("x");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test // Test with main using a function
    public void useFunction() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/11UseFunction/useFunction.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("squared");
        assertEquals(base_globals, symtab.globals.keys());    
    }
    
    @Test // Test with multiple functions
    public void functionWithArgs() throws RecognitionException, LexerException, ParserException, SymbolTableException {        
        String[] args = new String[] {"TestPrograms/10FunctionWithArgs/functionWithArgs.ds"};
        SymbolTable symtab = Runner.defTestMain(args);
        base_globals.add("main");
        base_globals.add("squared");
        assertEquals(base_globals, symtab.globals.keys());    
    }
}
