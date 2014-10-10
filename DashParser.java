// $ANTLR 3.3 Nov 30, 2010 12:50:56 /Users/markgalloway/Documents/workspace/DashAB/Dash.g 2014-10-09 13:53:03

  import ab.dash.ast.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class DashParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PROGRAM", "In", "By", "As", "Var", "Const", "Matrix", "Vector", "Interval", "Integer", "Boolean", "True", "False", "Real", "String", "Procedure", "Function", "Returns", "Typedef", "If", "Else", "Loop", "While", "Break", "Continue", "Return", "Filter", "Not", "And", "Or", "Xor", "Rows", "Columns", "Length", "Out", "Inp", "Tuple", "Stream_state", "Revserse", "UNDERSCORE", "LETTER", "DIGIT", "ID", "INTEGER", "WS", "NL"
    };
    public static final int EOF=-1;
    public static final int PROGRAM=4;
    public static final int In=5;
    public static final int By=6;
    public static final int As=7;
    public static final int Var=8;
    public static final int Const=9;
    public static final int Matrix=10;
    public static final int Vector=11;
    public static final int Interval=12;
    public static final int Integer=13;
    public static final int Boolean=14;
    public static final int True=15;
    public static final int False=16;
    public static final int Real=17;
    public static final int String=18;
    public static final int Procedure=19;
    public static final int Function=20;
    public static final int Returns=21;
    public static final int Typedef=22;
    public static final int If=23;
    public static final int Else=24;
    public static final int Loop=25;
    public static final int While=26;
    public static final int Break=27;
    public static final int Continue=28;
    public static final int Return=29;
    public static final int Filter=30;
    public static final int Not=31;
    public static final int And=32;
    public static final int Or=33;
    public static final int Xor=34;
    public static final int Rows=35;
    public static final int Columns=36;
    public static final int Length=37;
    public static final int Out=38;
    public static final int Inp=39;
    public static final int Tuple=40;
    public static final int Stream_state=41;
    public static final int Revserse=42;
    public static final int UNDERSCORE=43;
    public static final int LETTER=44;
    public static final int DIGIT=45;
    public static final int ID=46;
    public static final int INTEGER=47;
    public static final int WS=48;
    public static final int NL=49;

    // delegates
    // delegators


        public DashParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public DashParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return DashParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/markgalloway/Documents/workspace/DashAB/Dash.g"; }


    public static class program_return extends ParserRuleReturnScope {
        DashAST tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:17:1: program : ( . )* EOF -> ^( PROGRAM ) ;
    public final DashParser.program_return program() throws RecognitionException {
        DashParser.program_return retval = new DashParser.program_return();
        retval.start = input.LT(1);

        DashAST root_0 = null;

        Token wildcard1=null;
        Token EOF2=null;

        DashAST wildcard1_tree=null;
        DashAST EOF2_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");

        try {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:18:3: ( ( . )* EOF -> ^( PROGRAM ) )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:18:5: ( . )* EOF
            {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:18:5: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=PROGRAM && LA1_0<=NL)) ) {
                    alt1=1;
                }
                else if ( (LA1_0==EOF) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:18:5: .
            	    {
            	    wildcard1=(Token)input.LT(1);
            	    matchAny(input); 
            	    wildcard1_tree = (DashAST)adaptor.create(wildcard1);
            	    adaptor.addChild(root_0, wildcard1_tree);


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_program64);  
            stream_EOF.add(EOF2);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (DashAST)adaptor.nil();
            // 18:12: -> ^( PROGRAM )
            {
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:18:15: ^( PROGRAM )
                {
                DashAST root_1 = (DashAST)adaptor.nil();
                root_1 = (DashAST)adaptor.becomeRoot((DashAST)adaptor.create(PROGRAM, "PROGRAM"), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (DashAST)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (DashAST)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    // Delegated rules


 

    public static final BitSet FOLLOW_EOF_in_program64 = new BitSet(new long[]{0x0000000000000002L});

}