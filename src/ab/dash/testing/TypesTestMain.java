package ab.dash.testing;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.Types;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class TypesTestMain {
    
    public static void main(String[] args) throws LexerException, ParserException, RecognitionException, SymbolTableException {
        ANTLRFileStream input = null;
        try {
            input = new ANTLRFileStream(args[0]);
        } catch (IOException e) {
            throw new RuntimeException("Invalid program filename: " + args[0]);
        }
        
        DashLexer lexer = new DashLexer(input);
        final TokenRewriteStream tokens = new TokenRewriteStream(lexer);
        DashParser parser = new DashParser(tokens);
        parser.setTreeAdaptor(DashAST.dashAdaptor);
        DashParser.program_return entry = parser.program();
        
        if (lexer.getErrorCount() > 0) {
            throw new LexerException(lexer.getErrors());
        }
        
        if (parser.getErrorCount() > 0) {
            throw new ParserException(parser.getErrors());
        }
  
        DashAST tree = (DashAST)entry.getTree();
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);

        // make global scope, types
        SymbolTable symtab = new SymbolTable(tokens); 
        Boolean debug = false;
        
        // make global scope, types
        Def def = new Def(nodes, symtab, debug);
        def.downup(tree); 
        
        if (symtab.getErrorCount() > 0) {
            throw new SymbolTableException(symtab.getErrors());
        }
        
		// RESOLVE SYMBOLS, COMPUTE EXPRESSION TYPES
		nodes.reset();
		Types typeComp = new Types(nodes, symtab);
		typeComp.downup(tree); // trigger resolve/type computation actions

        if (symtab.getErrorCount() > 0) {
            throw new SymbolTableException(symtab.getErrors());
        }
        
        return;
    }

}