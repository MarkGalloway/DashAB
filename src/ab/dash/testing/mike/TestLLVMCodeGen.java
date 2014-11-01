package ab.dash.testing.mike;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Comparator;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.stringtemplate.StringTemplateGroup;

import ab.dash.CleanAST;
import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.DefineTupleTypes;
import ab.dash.LLVMIRGenerator;
import ab.dash.Types;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;


public class TestLLVMCodeGen {

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
		Boolean debug = false;
		Def def = new Def(nodes, symtab, debug); // use custom constructor
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
		
		// Delete nodes that are no longer needed
		nodes.reset();
		CleanAST clean = new CleanAST();
		clean.clean(tree);
		
		System.out.println(tree.toStringTree());
		
		StringBuilder sb;
		sb = new StringBuilder();

		String[] STGFiles = new String[] {
				"StringTemplate/LLVM.stg",
				"StringTemplate/LLVM_Bool.stg",
				"StringTemplate/LLVM_Char.stg",
				"StringTemplate/LLVM_Int.stg",
				"StringTemplate/LLVM_Real.stg",
				"StringTemplate/LLVM_Tuple.stg"
				};
		
		for (String s : STGFiles)
			sb.append(SlurpFile(s));
		
		StringTemplateGroup stg = new StringTemplateGroup(
				new StringReader(sb.toString()));
		
		LLVMIRGenerator llvm = new LLVMIRGenerator(stg, symtab);
		llvm.debug_on();
		nodes.reset();
    	tokens.reset();
		llvm.build(tree);
		System.out.println(llvm);
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
//		parseFile("simpleMain.db", "TestPrograms/05SimpleMain/simpleMain.db");
//		parseFile("variableDeclarationInMain.db", "TestPrograms/07VariableDeclarationInMain/variableDeclarationInMain.db");
		File[] files = new File("TestPrograms/").listFiles();
		showFiles(files);

		File[] invalid_files = new File("TestInvalidTypePrograms/").listFiles();
		showFiles(invalid_files);
	}
}
