package ab.dash.testing;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.stringtemplate.StringTemplate;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.ast.DashAST;

public class TestGrammarParser {
	
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
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DashParser parser = new DashParser(tokens);
        parser.setTreeAdaptor(DashAST.dashAdaptor);
        DashParser.program_return r = parser.program();

        DashAST tree = (DashAST)r.getTree();
        System.err.println(tree.toStringTree());

        DOTTreeGenerator gen = new DOTTreeGenerator();
        StringTemplate st = gen.toDOT(tree);
        System.out.println(st);
        
        PrintWriter writer_AST;
		try {
			writer_AST = new PrintWriter("ASTOutput/" + name + ".dot", "UTF-8");
			writer_AST.println(st);
			writer_AST.close();
			
			Runtime.getRuntime().exec("dot -Tpng ASTOutput/" + name + ".dot -o ASTOutput/" + name + ".png");
			//Runtime.getRuntime().exec("xdg-open ASTOutput/AST.png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void showFiles(File[] files) throws RecognitionException {
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

	public static void main(String[] args) throws RecognitionException, IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec("mkdir ASTOutput");
		p.waitFor();
		
		File[] files = new File("TestGrammarPrograms/").listFiles();
		showFiles(files);
		
		files = new File("TestPrograms/").listFiles();
		showFiles(files);
	}

}
