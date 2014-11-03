package ab.dash;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeAdaptor;

import ab.dash.ast.DashAST;
import ab.dash.ast.DashErrorNode;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.SymbolTableException;

public class DashAB_Part1_Test {

    public static void main(String[] args) throws RecognitionException {

        //TODO: add command line rejection
        
        ANTLRFileStream input = null;
        try {
            input = new ANTLRFileStream(args[0]);
        } catch (IOException e) {
            System.err.print("Invalid program filename: ");
            System.err.println(args[0]);
            System.exit(1);
        }
        
        DashLexer lexer = new DashLexer(input);
        final TokenRewriteStream tokens = new TokenRewriteStream(lexer);
        
        DashParser parser = new DashParser(tokens);
        parser.setTreeAdaptor(DashAST.dashAdaptor);
        DashParser.program_return entry = parser.program();
        
        // exit if we find lexer errors
        if (lexer.getErrorCount() > 0 || parser.getErrorCount() > 0) {
        	System.exit(1);
        }
        
        CommonTree ast = (CommonTree) entry.getTree();
        
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(ast);
        nodes.setTokenStream(tokens);

        // make global scope, types
        SymbolTable symtab = new SymbolTable(tokens); 
        
        // make global scope, types
        Def def = new Def(nodes, symtab, true);
        def.downup(ast); 
        
        if (symtab.getErrorCount() > 0) {
            System.exit(1);
        }
        
        /** TESTING FEATURE: REMOVE ON RELEASE */
        if(args.length > 1 && args[1].equals("astDebug")) {
            System.out.println(ast.toStringTree());
            return;
            
        }
        /** TESTING FEATURE: END REMOVE ON RELEASE */
        
    }
}
