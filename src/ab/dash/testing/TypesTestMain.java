package ab.dash.testing;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeVisitor;
import org.antlr.runtime.tree.TreeVisitorAction;

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
        System.out.println(tree.toStringTree());
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);

        // make global scope, types
        SymbolTable symtab = new SymbolTable(tokens); 
        Boolean debug = true;
        
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

		System.err.flush();
		System.out.flush();
    }
    
	static void showTypesAndPromotions(DashAST t, TokenRewriteStream tokens) {
		if (t.evalType != null && t.getType() != DashParser.EXPR) {
			System.out.printf(
					"%-17s",
					tokens.toString(t.getTokenStartIndex(),
							t.getTokenStopIndex()));
			String ts = t.evalType.toString();
			System.out.printf(" type %-8s", ts);
			if (t.promoteToType != null) {
				System.out.print(" promoted to " + t.promoteToType);
			}
			System.out.println();
		}
	}
	
	/** Insert a cast before tokens from which this node was created. */
	static void insertCast(DashAST t, TokenRewriteStream tokens) {
		String cast = "(" + t.promoteToType + ")";
		int left = t.getTokenStartIndex(); // location in token buffer
		int right = t.getTokenStopIndex();
		CommonToken tok = (CommonToken) t.token; // tok is node's token payload
		if (tok.getType() == DashParser.EXPR) {
			tok = (CommonToken) ((DashAST) t.getChild(0)).token;
		}
		if (left == right) {
			// it's a single atom
			// or a[i]
			tokens.insertBefore(left, cast);
		} else { // need parens
			String original = tokens.toString(left, right);
			tokens.replace(left, right, cast + "(" + original + ")");
		}
	}

}