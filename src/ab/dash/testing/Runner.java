/**  Contains logic and static methods used to execute the program from Junit Tests **/
package ab.dash.testing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.antlr.runtime.ANTLRFileStream;
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
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;

public class Runner {
	
    // needed to return tokens from lexer/parser pass
    private static TokenRewriteStream tokens;

    // grabs the input file name from args
    private static ANTLRFileStream getInputStream(String[] args) {
        ANTLRFileStream input = null;
        
        try {
            input = new ANTLRFileStream(args[0]);
        } catch (IOException e) {
            throw new RuntimeException("Invalid program filename: " + args[0]);
        }
        
        return input;
    }
    
    // builds the AST in the lexer/parser, aborts if errors are found
    private static DashAST runLexerParser(ANTLRFileStream input) throws LexerException, ParserException, RecognitionException {
        
        DashLexer lexer = new DashLexer(input);
        tokens = new TokenRewriteStream(lexer);
        DashParser parser = new DashParser(tokens);
        parser.setTreeAdaptor(DashAST.dashAdaptor);
        DashParser.program_return entry = parser.program();
        
        // lexer errors are constructed after parser.program() executes
        if(lexer.inComment) {
            throw new LexerException("Error: Missing closing comment '*/'.");
        }
        if (lexer.getErrorCount() > 0) {
            throw new LexerException(lexer.getErrors());
        }
        
        if (parser.getErrorCount() > 0) {
            throw new ParserException(parser.getErrors());
        }
  
        return (DashAST)entry.getTree();
    }
    
    // runs Def.g treewalker, aborts if errors are found
    private static void runDef(CommonTreeNodeStream nodes, SymbolTable symtab, DashAST tree) throws SymbolTableException {
        Def def = new Def(nodes, symtab, false);
        def.downup(tree); 
        if (symtab.getErrorCount() > 0) {
            throw new SymbolTableException(symtab.getErrors());
        }
    }
    
    // runs Types.g treewalker, aborts if errors are found
    private static void runTypes(CommonTreeNodeStream nodes, SymbolTable symtab, DashAST tree) throws SymbolTableException {
        nodes.reset();
        Types typeComp = new Types(nodes, symtab);
        typeComp.downup(tree);
        
        if (symtab.getErrorCount() > 0) {
            throw new SymbolTableException(symtab.getErrors());
        }
    }
    
    // runs Types.g DefineTupleTypes.g treewalker, aborts if errors are found
    private static void runDefineTupleTypes(CommonTreeNodeStream nodes, SymbolTable symtab, DashAST tree) throws SymbolTableException {
        nodes.reset();
        DefineTupleTypes tupleTypeComp = new DefineTupleTypes(nodes, symtab);
        tupleTypeComp.debug_off();
        tupleTypeComp.downup(tree);
        
        if (symtab.getErrorCount() > 0) {
            throw new SymbolTableException(symtab.getErrors());
        }
    }
    
    // generates LLVM code
    private static String runLLVMIRgenerator(CommonTreeNodeStream nodes, SymbolTable symtab, DashAST tree, TokenRewriteStream tokens) {
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
       
       nodes.reset();
       tokens.reset();
       llvm.build(tree);
       return llvm.toString();
    }
    
    private static String SlurpFile(String f) {
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
    
    private static void deleteNoLongerNeeded(CommonTreeNodeStream nodes, DashAST tree) {
        nodes.reset();
        CleanAST clean = new CleanAST();
        clean.clean(tree);
    }
    
    // used by ASTtest
    public static void astTestMain(String[] args) throws LexerException, ParserException, RecognitionException {
        ANTLRFileStream input = getInputStream(args);
        DashAST tree = runLexerParser(input);
        System.out.println(tree.toStringTree());
    }
    
    // used by DefTest
    public static SymbolTable defTestMain(String[] args) throws LexerException, ParserException, RecognitionException, SymbolTableException {
        ANTLRFileStream input = getInputStream(args);
        DashAST tree = runLexerParser(input);
        
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);
        SymbolTable symtab = new SymbolTable(tokens); 
        runDef(nodes, symtab, tree);
        return symtab;
    }
    
    // used by TypeTest
    public static void typesTestMain(String[] args) throws LexerException, ParserException, RecognitionException, SymbolTableException {
        ANTLRFileStream input = getInputStream(args);
        DashAST tree = runLexerParser(input);
        
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);
        SymbolTable symtab = new SymbolTable(tokens); 
        runDef(nodes, symtab, tree);
        runTypes(nodes, symtab, tree);
        runDefineTupleTypes(nodes, symtab, tree);
    }
    
    // used by LLVMtest
    public static void llvmMain(String[] args) throws IOException, InterruptedException {
        
        // build the AST
        
        ANTLRFileStream input = getInputStream(args);
        

        DashAST tree;
        try {
            tree = runLexerParser(input);
        } catch (LexerException | ParserException | RecognitionException e) {
            return;
        }

        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(tokens);
        SymbolTable symtab = new SymbolTable(tokens);
        
        // run tree walker passes
        
        try {
            runDef(nodes, symtab, tree);
            runTypes(nodes, symtab, tree);
            runDefineTupleTypes(nodes, symtab, tree);
        } catch (SymbolTableException e) {
            return;
        }
;
        
        // generate llvm
        
        deleteNoLongerNeeded(nodes, tree);
        String llvm = runLLVMIRgenerator(nodes, symtab, tree, tokens);
        String llvm_file = "LLVMIROutput/" + args[0].substring((args[0].lastIndexOf('/')+1)) + ".ll";
        SampleFileWriter.createFile(llvm_file, llvm);
        
        // execute llvm and print it in stdout/stderr
    	Process p = null;
    	if (args.length > 1) {
    		String[] cmd = {
    				"/bin/sh",
    				"-c",
    				"cat " + args[1] + " | lli " + llvm_file
    				};
    		p = Runtime.getRuntime().exec(cmd);
        } else {
        	String[] cmd = {
        			"/bin/sh",
    				"-c",
    				"lli " + llvm_file
    				};
        	p = Runtime.getRuntime().exec(cmd);
        }
        
        p.waitFor();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        StringBuffer output = new StringBuffer();
        StringBuffer errors = new StringBuffer();
        String line = null;
        
        while ((line = reader.readLine()) != null) {output.append(line); output.append("\n");}
        while ((line = errorReader.readLine()) != null) {errors.append(line); errors.append("\n");}
        
        if (reader != null) reader.close();
        if (errorReader != null) errorReader.close();
        
        System.out.println(output.toString().trim());
        System.err.println(errors.toString().trim());
    }

}
