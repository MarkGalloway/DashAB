package ab.dash.testing;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.ast.DashAST;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;

public class AstTestMain {

    public static void main(String[] args) throws LexerException, ParserException, RecognitionException {
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
        
        if (lexer.getErrorCount() > 0) {
        	throw new LexerException(lexer.getErrors());
        }
        
        if (parser.getErrorCount() > 0) {
        	throw new ParserException(parser.getErrors());
        }
  
        CommonTree ast = (CommonTree) entry.getTree();
        
        // Print the tree for testing
        System.out.println(ast.toStringTree());
    }

}
