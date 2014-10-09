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
import org.antlr.runtime.tree.TreeAdaptor;

import ab.dash.ast.DashAST;
import ab.dash.ast.DashErrorNode;

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
        parser.setTreeAdaptor(DashAdaptor);
        DashParser.program_return entry = parser.program();
  
        
        CommonTree ast = (CommonTree) entry.getTree();
        
        if(args.length > 1 && args[1].equals("astDebug")) {
            System.out.println(ast.toStringTree());
            return;
        }
        
    }
    
    
    /** An adaptor that tells ANTLR to build FashAST nodes */
    public static TreeAdaptor DashAdaptor = new CommonTreeAdaptor() {
        public Object create(Token token) {
            return new DashAST(token);
        }
        public Object dupNode(Object t) {
            if ( t==null ) {
                return null;
            }
            return create(((DashAST)t).token);
        }

        public Object errorNode(TokenStream input,
                                Token start,
                                Token stop,
                                RecognitionException e)
        {
            return new DashErrorNode(input,start,stop,e);
        }
    };

}
