package ab.dash.testing;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.ast.DashAST;

public class AstTestMain {

    public static void main(String[] args) throws RecognitionException {
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
  
        
        CommonTree ast = (CommonTree) entry.getTree();
        
        // Print the tree for testing
        System.out.println(ast.toStringTree());
    }

}
