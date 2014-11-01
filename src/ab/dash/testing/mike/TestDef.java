package ab.dash.testing.mike;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import ab.dash.DashLexer;
import ab.dash.DashParser;
import ab.dash.Def;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;


public class TestDef {
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
        System.out.println(tree.toStringTree());
        
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);

        SymbolTable symtab = new SymbolTable(tokens); // make global scope, types
        
        Boolean debug = true;
        Def def = new Def(nodes, symtab, debug); // use custom constructor
        def.downup(tree); // trigger symtab actions upon certain subtrees 
        System.out.println("globals: "+symtab.globals);
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
		File[] files = new File("TestPrograms/").listFiles();
		showFiles(files);
		
//		File[] invalid_files = new File("InvalidTestPrograms/").listFiles();
//		showFiles(invalid_files);
	}
}
