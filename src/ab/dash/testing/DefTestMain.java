package ab.dash.testing;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;

public class DefTestMain {
    
    public static SymbolTable main(String[] args) throws LexerException, ParserException, RecognitionException {
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
        System.out.println(tree.toStringTree());
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);

        // make global scope, types
        SymbolTable symtab = new SymbolTable(tokens); 
        Boolean debug = true;
        
        // make global scope, types
        Def def = new Def(nodes, symtab, debug);
        def.downup(tree); 
        return symtab;
    }

}
