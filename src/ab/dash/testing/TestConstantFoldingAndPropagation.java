package ab.dash.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeVisitor;
import org.antlr.runtime.tree.TreeVisitorAction;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.DefineTupleTypes;
import ab.dash.LLVMCodeGenerator;
import ab.dash.LLVMIRGenerator;
import ab.dash.Types;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.opt.ConstantFolding;
import ab.dash.opt.ConstantPropagation;


public class TestConstantFoldingAndPropagation {

	private static String SlurpFile(String f)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
			String l;

			while ((l = br.readLine()) != null)
				sb.append(l + System.getProperty("line.separator"));

			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		return sb.toString();
	}
	
	public static void parseFile(String name, String file) throws RecognitionException {
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
		
		if (lexer.getErrorCount() > 0) {
        	return;
        }
        
        if (parser.getErrorCount() > 0) {
        	return;
        }

		DashAST tree = (DashAST) r.getTree();

		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		nodes.setTokenStream(tokens);

		SymbolTable symtab = new SymbolTable(tokens); // make global scope,
														// types
		Def def = new Def(nodes, symtab); // use custom constructor
		def.debug_off();
		def.downup(tree); // trigger symtab actions upon certain subtrees
		

		// RESOLVE SYMBOLS, COMPUTE EXPRESSION TYPES
		nodes.reset();
		Types typeComp = new Types(nodes, symtab);
		typeComp.downup(tree); // trigger resolve/type computation actions
		
		if (symtab.getErrorCount() > 0) {
			return;
		}
		
		nodes.reset();
		DefineTupleTypes tupleTypeComp = new DefineTupleTypes(nodes, symtab);
		tupleTypeComp.debug_off();
		tupleTypeComp.downup(tree); // trigger resolve/type computation actions
		
		if (symtab.getErrorCount() > 0) {
			return;
		}
		
		System.out.println(tree.toStringTree());
		
		boolean fixed_state = false;
		
		ConstantFolding opt1 = new ConstantFolding(nodes, symtab);
		opt1.setTreeAdaptor(DashAST.dashAdaptor);

		ConstantPropagation opt2 = new ConstantPropagation(symtab);
		int iterations = 0;
		String tree_before = tree.toStringTree();
		
		while (!fixed_state) {
			iterations++;
			System.out.println("Iteration: " + iterations);
			
			nodes.reset();
			opt1.downup(tree); // trigger resolve/type computation actions
			
			nodes.reset();
	    	tokens.reset();
	    	opt2.optimize(tree);
			
			String tree_after = tree.toStringTree();
			fixed_state = tree_before.equals(tree_after);
			tree_before = tree_after;
		}
		
		System.out.println(tree.toStringTree());
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
	        	    
	        	    if (extension.equals("db")) {
		        	    System.out.println("File: " + file.getName());
			            parseFile(file.getName(), file.getPath());
	        	    }
	        	}
			}
		}
	}

	public static void main(String[] args) throws RecognitionException {
		File[] files = new File("TestConstantFoldingAndPropagation/").listFiles();
		showFiles(files);
	}
}
