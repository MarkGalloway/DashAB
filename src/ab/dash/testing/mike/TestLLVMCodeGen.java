package ab.dash.testing.mike;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

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
	private static String readFile(String file) throws Exception {
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}
		reader.close();
		
		return stringBuilder.toString();
	}
	
	private static void inheritIO(final InputStream src, final PrintStream dest) {
	    new Thread(new Runnable() {
	        public void run() {
	            Scanner sc = new Scanner(src);
	            while (sc.hasNextLine()) {
	                dest.println(sc.nextLine());
	            }
	        }
	    }).start();
	}

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
	
	public static String parseFile(String name, String file) throws RecognitionException {
		Boolean debug = false;
		return parseFile(name, file, debug);
	}
	
	public static String parseFile(String name, String file, Boolean debug) throws RecognitionException {
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
        	return "";
        }
        
        if (parser.getErrorCount() > 0) {
        	return "";
        }

		DashAST tree = (DashAST) r.getTree();

		CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
		nodes.setTokenStream(tokens);

		SymbolTable symtab = new SymbolTable(tokens); // make global scope,
														// types
		Def def = new Def(nodes, symtab, debug); // use custom constructor
		def.downup(tree); // trigger symtab actions upon certain subtrees
		
		if (def.getErrorCount() > 0) {
			return "";
		}

		// RESOLVE SYMBOLS, COMPUTE EXPRESSION TYPES
		nodes.reset();
		Types typeComp = new Types(nodes, symtab);
		typeComp.downup(tree); // trigger resolve/type computation actions
		
		if (symtab.getErrorCount() > 0) {
			return "";
		}
		
		nodes.reset();
		DefineTupleTypes tupleTypeComp = new DefineTupleTypes(nodes, symtab);
		tupleTypeComp.debug_off();
		tupleTypeComp.downup(tree); // trigger resolve/type computation actions
		
		if (symtab.getErrorCount() > 0) {
			return "";
		}
				
		// Delete nodes that are no longer needed
		nodes.reset();
		CleanAST clean = new CleanAST();
		clean.clean(tree);
				
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
		
		if (debug)
			llvm.debug_on();
		else
			llvm.debug_off();
		
		nodes.reset();
    	tokens.reset();
		llvm.build(tree);
		
		Process p_mkdir;
		try {
			p_mkdir = Runtime.getRuntime().exec("mkdir LLVMIROutput");
			p_mkdir.waitFor();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(baos);
    	
    	PrintWriter out;
		try {
			String code = "LLVMIROutput/" + name + ".ll";
			out = new PrintWriter(code);
			out.println(llvm.toString());
			out.close();
			
			Process p_lli = Runtime.getRuntime().exec("lli " + code);
		    inheritIO(p_lli.getInputStream(), ps);
		    inheritIO(p_lli.getErrorStream(), ps);
		    p_lli.waitFor();
		    Thread.sleep(500);
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String llvm_out = "";
		try {
			llvm_out = baos.toString("UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return llvm_out;
	}
}
