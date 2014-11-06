package ab.dash;
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
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeAdaptor;

import ab.dash.ast.DashAST;
import ab.dash.ast.DashErrorNode;
import ab.dash.ast.SymbolTable;
import ab.dash.exceptions.LexerException;
import ab.dash.exceptions.ParserException;
import ab.dash.exceptions.SymbolTableException;
import ab.dash.testing.Runner;

public class DashAB_Part1_Test {

    public static void main(String[] args) throws RecognitionException, LexerException, ParserException, SymbolTableException {

        String[] blah = {"TestPrograms/40UninitializedTuple/uninitializedTuple.ds"};
        Runner.typesTestMain(blah);

        
    }
}
