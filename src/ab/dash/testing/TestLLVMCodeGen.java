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
import ab.dash.LLVMCodeGenerator;
import ab.dash.LLVMIRGenerator;
import ab.dash.Types;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;


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

		StringBuilder sb;
		sb = new StringBuilder();

		String[] STGFiles = new String[] {
				"StringTemplate/LLVM.stg",
				"StringTemplate/LLVM_Bool.stg",
				"StringTemplate/LLVM_Int.stg"
				};
		
		for (String s : STGFiles)
			sb.append(SlurpFile(s));
		
		StringTemplateGroup stg = new StringTemplateGroup(
				new StringReader(sb.toString()));
		
		LLVMIRGenerator llvm = new LLVMIRGenerator(stg);
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
	        	    
	        	    if (extension.equals("db")) {
		        	    System.out.println("File: " + file.getName());
			            parseFile(file.getName(), file.getPath());
	        	    }
	        	}
			}
		}
	}

	public static void main(String[] args) throws RecognitionException {
		parseFile("simpleMain.db", "TestPrograms/05SimpleMain/simpleMain.db");
		parseFile("variableDeclarationInMain.db", "TestPrograms/07VariableDeclarationInMain/variableDeclarationInMain.db");
//		File[] files = new File("TestPrograms/").listFiles();
//		showFiles(files);
//
//		File[] invalid_files = new File("TestInvalidTypePrograms/").listFiles();
//		showFiles(invalid_files);
	}
}
