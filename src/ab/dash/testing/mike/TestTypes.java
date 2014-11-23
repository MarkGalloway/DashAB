package ab.dash.testing.mike;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeVisitor;
import org.antlr.runtime.tree.TreeVisitorAction;

import org.antlr.runtime.CommonToken;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.Types;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;

public class TestTypes {
	public static void parseFile(String name, String file)
			throws RecognitionException {
		CharStream input = null;
		try {
			input = new ANTLRFileStream(file);
		} catch (IOException e) {
			System.err.print("Invalid program filename: ");
			System.err.println(file);
			System.exit(1);
		}
		DashLexer lexer = new DashLexer(input);
		final TokenRewriteStream tokens = new TokenRewriteStream(lexer);
		DashParser parser = new DashParser(tokens);
		parser.setTreeAdaptor(DashAST.dashAdaptor);
		DashParser.program_return r = parser.program();

		DashAST tree = (DashAST) r.getTree();

		System.out.println("\nTree:");
		System.out.println(tree.toStringTree());
		System.out.println();

		System.out.println("Def Step:");

		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		nodes.setTokenStream(tokens);

		SymbolTable symtab = new SymbolTable(tokens); // make global scope,
														// types
		Boolean debug = true;
		Def def = new Def(nodes, symtab, debug); // use custom constructor
		def.downup(tree); // trigger symtab actions upon certain subtrees
		System.out.println("globals: " + symtab.globals);
		System.out.println();
		
		System.out.println("\nTree:");
		System.out.println(tree.toStringTree());
		System.out.println();

		System.out.println("Type Step:");
		System.out.flush();
		
		// RESOLVE SYMBOLS, COMPUTE EXPRESSION TYPES
		nodes.reset();
		Types typeComp = new Types(nodes, symtab);
		typeComp.downup(tree); // trigger resolve/type computation actions

		System.err.flush();
		System.out.flush();

		if (symtab.getErrorCount() == 0) {
			// WALK TREE TO DUMP SUBTREE TYPES
			TreeVisitor v = new TreeVisitor(new CommonTreeAdaptor());
			TreeVisitorAction actions = new TreeVisitorAction() {
				public Object pre(Object t) {
					return t;
				}

				public Object post(Object t) {
					showTypesAndPromotions((DashAST) t, tokens);
					return t;
				}
			};
			v.visit(tree, actions); // walk in postorder, showing types

			TreeVisitorAction actions2 = new TreeVisitorAction() {
				public Object pre(Object t) {
					return t;
				}

				public Object post(Object t) {
					DashAST u = (DashAST) t;
					if (u.promoteToType != null)
						insertCast(u, tokens);
					return t;
				}
			};
			v.visit(tree, actions2);
		}

		System.out.println("\nTree:");
		System.out.println(tree.toStringTree());
		System.out.println();
		
		System.out.println("\nCode:");
		System.out.println(tokens);
		System.out.println("\n\n");

		System.out.flush();
		System.err.flush();
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

	public static void showFiles(File[] files) throws RecognitionException {
		Arrays.sort(files, new Comparator<File>() {
		    public int compare(File f1, File f2) {
		        return f1.getName().compareTo(f2.getName());
		    }
		});
		
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.println("Directory: " + file.getName());
				showFiles(file.listFiles()); // Calls same method again.
			} else {
				int i = file.getName().lastIndexOf('.');
	        	if (i > 0) {
	        	    String extension = file.getName().substring(i+1);
	        	    
	        	    if (extension.equals("ds")) {
		        	    System.out.println("File: " + file.getName());
			            parseFile(file.getName(), file.getPath());
	        	    }
	        	}
			}
		}
	}

	public static void main(String[] args) throws RecognitionException {
//		parseFile("tuples.ds", "TestPrograms/05Tuples/tuples.ds");
//		parseFile("typeCast.ds", "TestPrograms/33TypeCast/typeCast.ds");
//		parseFile("tupleTypeCast.ds", "TestPrograms/34TupleTypeCast/tupleTypeCast.ds");
//		parseFile("forwardDeclaration.ds", "TestPrograms/38ForwardDeclaration/forwardDeclaration.ds");
//		parseFile("functionWithArgs.ds", "TestPrograms/10FunctionWithArgs/functionWithArgs.ds");
//		parseFile("functionExpression.ds", "TestPrograms/25FunctionExpression/functionExpression.ds");
//		parseFile("promoteFunctionArguments.ds", "TestPrograms/35PromoteFunctionArguments/promoteFunctionArguments.ds");
//		parseFile("uninitializedTuple.ds", "TestPrograms/40UninitializedTuple/uninitializedTuple.ds");
//		parseFile("promoteTupleTypes.ds", "TestPrograms/26PromoteTupleTypes/promoteTupleTypes.ds");

		File[] files = new File("TestPrograms/").listFiles();
		showFiles(files);

//		File[] invalid_files = new File("TestInvalidTypePrograms/").listFiles();
//		showFiles(invalid_files);
	}
}
