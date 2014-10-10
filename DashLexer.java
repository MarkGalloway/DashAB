// $ANTLR 3.3 Nov 30, 2010 12:50:56 /Users/markgalloway/Documents/workspace/DashAB/Dash.g 2014-10-09 13:53:04

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DashLexer extends Lexer {
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

    public DashLexer() {;} 
    public DashLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DashLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/markgalloway/Documents/workspace/DashAB/Dash.g"; }

    // $ANTLR start "In"
    public final void mIn() throws RecognitionException {
        try {
            int _type = In;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:23:4: ( 'in' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:23:6: 'in'
            {
            match("in"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "In"

    // $ANTLR start "By"
    public final void mBy() throws RecognitionException {
        try {
            int _type = By;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:24:4: ( 'by' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:24:6: 'by'
            {
            match("by"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "By"

    // $ANTLR start "As"
    public final void mAs() throws RecognitionException {
        try {
            int _type = As;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:25:4: ( 'as' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:25:6: 'as'
            {
            match("as"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "As"

    // $ANTLR start "Var"
    public final void mVar() throws RecognitionException {
        try {
            int _type = Var;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:26:5: ( 'var' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:26:7: 'var'
            {
            match("var"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Var"

    // $ANTLR start "Const"
    public final void mConst() throws RecognitionException {
        try {
            int _type = Const;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:27:7: ( 'const' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:27:9: 'const'
            {
            match("const"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Const"

    // $ANTLR start "Matrix"
    public final void mMatrix() throws RecognitionException {
        try {
            int _type = Matrix;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:28:8: ( 'matrix' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:28:10: 'matrix'
            {
            match("matrix"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Matrix"

    // $ANTLR start "Vector"
    public final void mVector() throws RecognitionException {
        try {
            int _type = Vector;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:29:8: ( 'vector' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:29:10: 'vector'
            {
            match("vector"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Vector"

    // $ANTLR start "Interval"
    public final void mInterval() throws RecognitionException {
        try {
            int _type = Interval;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:30:10: ( 'interval' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:30:12: 'interval'
            {
            match("interval"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Interval"

    // $ANTLR start "Integer"
    public final void mInteger() throws RecognitionException {
        try {
            int _type = Integer;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:31:9: ( 'integer' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:31:11: 'integer'
            {
            match("integer"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Integer"

    // $ANTLR start "Boolean"
    public final void mBoolean() throws RecognitionException {
        try {
            int _type = Boolean;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:32:9: ( 'boolean' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:32:11: 'boolean'
            {
            match("boolean"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Boolean"

    // $ANTLR start "True"
    public final void mTrue() throws RecognitionException {
        try {
            int _type = True;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:33:6: ( 'true' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:33:8: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "True"

    // $ANTLR start "False"
    public final void mFalse() throws RecognitionException {
        try {
            int _type = False;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:34:7: ( 'false' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:34:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "False"

    // $ANTLR start "Real"
    public final void mReal() throws RecognitionException {
        try {
            int _type = Real;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:35:6: ( 'real' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:35:8: 'real'
            {
            match("real"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Real"

    // $ANTLR start "String"
    public final void mString() throws RecognitionException {
        try {
            int _type = String;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:36:8: ( 'string' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:36:10: 'string'
            {
            match("string"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "String"

    // $ANTLR start "Procedure"
    public final void mProcedure() throws RecognitionException {
        try {
            int _type = Procedure;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:37:11: ( 'procedure' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:37:13: 'procedure'
            {
            match("procedure"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Procedure"

    // $ANTLR start "Function"
    public final void mFunction() throws RecognitionException {
        try {
            int _type = Function;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:38:10: ( 'function' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:38:12: 'function'
            {
            match("function"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Function"

    // $ANTLR start "Returns"
    public final void mReturns() throws RecognitionException {
        try {
            int _type = Returns;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:39:9: ( 'returns' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:39:11: 'returns'
            {
            match("returns"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Returns"

    // $ANTLR start "Typedef"
    public final void mTypedef() throws RecognitionException {
        try {
            int _type = Typedef;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:40:9: ( 'typedef' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:40:11: 'typedef'
            {
            match("typedef"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Typedef"

    // $ANTLR start "If"
    public final void mIf() throws RecognitionException {
        try {
            int _type = If;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:41:4: ( 'if' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:41:6: 'if'
            {
            match("if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "If"

    // $ANTLR start "Else"
    public final void mElse() throws RecognitionException {
        try {
            int _type = Else;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:42:6: ( 'else' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:42:8: 'else'
            {
            match("else"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Else"

    // $ANTLR start "Loop"
    public final void mLoop() throws RecognitionException {
        try {
            int _type = Loop;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:43:6: ( 'loop' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:43:8: 'loop'
            {
            match("loop"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Loop"

    // $ANTLR start "While"
    public final void mWhile() throws RecognitionException {
        try {
            int _type = While;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:44:7: ( 'while' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:44:9: 'while'
            {
            match("while"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "While"

    // $ANTLR start "Break"
    public final void mBreak() throws RecognitionException {
        try {
            int _type = Break;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:45:7: ( 'break' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:45:9: 'break'
            {
            match("break"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Break"

    // $ANTLR start "Continue"
    public final void mContinue() throws RecognitionException {
        try {
            int _type = Continue;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:46:10: ( 'continue' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:46:12: 'continue'
            {
            match("continue"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Continue"

    // $ANTLR start "Return"
    public final void mReturn() throws RecognitionException {
        try {
            int _type = Return;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:47:8: ( 'return' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:47:10: 'return'
            {
            match("return"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Return"

    // $ANTLR start "Filter"
    public final void mFilter() throws RecognitionException {
        try {
            int _type = Filter;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:48:8: ( 'filter' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:48:10: 'filter'
            {
            match("filter"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Filter"

    // $ANTLR start "Not"
    public final void mNot() throws RecognitionException {
        try {
            int _type = Not;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:49:5: ( 'not' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:49:7: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Not"

    // $ANTLR start "And"
    public final void mAnd() throws RecognitionException {
        try {
            int _type = And;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:50:5: ( 'and' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:50:7: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "And"

    // $ANTLR start "Or"
    public final void mOr() throws RecognitionException {
        try {
            int _type = Or;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:51:4: ( 'or' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:51:6: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Or"

    // $ANTLR start "Xor"
    public final void mXor() throws RecognitionException {
        try {
            int _type = Xor;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:52:5: ( 'xor' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:52:7: 'xor'
            {
            match("xor"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Xor"

    // $ANTLR start "Rows"
    public final void mRows() throws RecognitionException {
        try {
            int _type = Rows;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:53:6: ( 'rows' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:53:8: 'rows'
            {
            match("rows"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Rows"

    // $ANTLR start "Columns"
    public final void mColumns() throws RecognitionException {
        try {
            int _type = Columns;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:54:9: ( 'columns' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:54:11: 'columns'
            {
            match("columns"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Columns"

    // $ANTLR start "Length"
    public final void mLength() throws RecognitionException {
        try {
            int _type = Length;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:55:8: ( 'length' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:55:10: 'length'
            {
            match("length"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Length"

    // $ANTLR start "Out"
    public final void mOut() throws RecognitionException {
        try {
            int _type = Out;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:56:5: ( 'out' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:56:7: 'out'
            {
            match("out"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Out"

    // $ANTLR start "Inp"
    public final void mInp() throws RecognitionException {
        try {
            int _type = Inp;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:57:5: ( 'inp' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:57:7: 'inp'
            {
            match("inp"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Inp"

    // $ANTLR start "Tuple"
    public final void mTuple() throws RecognitionException {
        try {
            int _type = Tuple;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:58:7: ( 'tuple' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:58:9: 'tuple'
            {
            match("tuple"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Tuple"

    // $ANTLR start "Stream_state"
    public final void mStream_state() throws RecognitionException {
        try {
            int _type = Stream_state;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:59:14: ( 'stream_state' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:59:16: 'stream_state'
            {
            match("stream_state"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Stream_state"

    // $ANTLR start "Revserse"
    public final void mRevserse() throws RecognitionException {
        try {
            int _type = Revserse;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:60:10: ( 'reverse' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:60:12: 'reverse'
            {
            match("reverse"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Revserse"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:63:4: ( ( UNDERSCORE | LETTER ) ( ( UNDERSCORE | LETTER | DIGIT ) )* )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:63:6: ( UNDERSCORE | LETTER ) ( ( UNDERSCORE | LETTER | DIGIT ) )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:63:28: ( ( UNDERSCORE | LETTER | DIGIT ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:63:29: ( UNDERSCORE | LETTER | DIGIT )
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:64:9: ( ( DIGIT )+ )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:64:11: ( DIGIT )+
            {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:64:11: ( DIGIT )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:64:11: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:66:4: ( ( ' ' | '\\t' | '\\f' )+ )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:66:6: ( ' ' | '\\t' | '\\f' )+
            {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:66:6: ( ' ' | '\\t' | '\\f' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\t'||LA3_0=='\f'||LA3_0==' ') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "NL"
    public final void mNL() throws RecognitionException {
        try {
            int _type = NL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:4: ( ( '\\r' '\\n' | '\\r' | '\\n' | EOF ) )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:6: ( '\\r' '\\n' | '\\r' | '\\n' | EOF )
            {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:6: ( '\\r' '\\n' | '\\r' | '\\n' | EOF )
            int alt4=4;
            switch ( input.LA(1) ) {
            case '\r':
                {
                int LA4_1 = input.LA(2);

                if ( (LA4_1=='\n') ) {
                    alt4=1;
                }
                else {
                    alt4=2;}
                }
                break;
            case '\n':
                {
                alt4=3;
                }
                break;
            default:
                alt4=4;}

            switch (alt4) {
                case 1 :
                    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:7: '\\r' '\\n'
                    {
                    match('\r'); 
                    match('\n'); 

                    }
                    break;
                case 2 :
                    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:19: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 3 :
                    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:26: '\\n'
                    {
                    match('\n'); 

                    }
                    break;
                case 4 :
                    // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:67:33: EOF
                    {
                    match(EOF); 

                    }
                    break;

            }

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NL"

    // $ANTLR start "UNDERSCORE"
    public final void mUNDERSCORE() throws RecognitionException {
        try {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:69:21: ( '_' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:69:23: '_'
            {
            match('_'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UNDERSCORE"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:70:16: ( '0' .. '9' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:70:18: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:71:17: ( 'a' .. 'z' | 'A' .. 'Z' )
            // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    public void mTokens() throws RecognitionException {
        // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:8: ( In | By | As | Var | Const | Matrix | Vector | Interval | Integer | Boolean | True | False | Real | String | Procedure | Function | Returns | Typedef | If | Else | Loop | While | Break | Continue | Return | Filter | Not | And | Or | Xor | Rows | Columns | Length | Out | Inp | Tuple | Stream_state | Revserse | ID | INTEGER | WS | NL )
        int alt5=42;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:10: In
                {
                mIn(); 

                }
                break;
            case 2 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:13: By
                {
                mBy(); 

                }
                break;
            case 3 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:16: As
                {
                mAs(); 

                }
                break;
            case 4 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:19: Var
                {
                mVar(); 

                }
                break;
            case 5 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:23: Const
                {
                mConst(); 

                }
                break;
            case 6 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:29: Matrix
                {
                mMatrix(); 

                }
                break;
            case 7 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:36: Vector
                {
                mVector(); 

                }
                break;
            case 8 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:43: Interval
                {
                mInterval(); 

                }
                break;
            case 9 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:52: Integer
                {
                mInteger(); 

                }
                break;
            case 10 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:60: Boolean
                {
                mBoolean(); 

                }
                break;
            case 11 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:68: True
                {
                mTrue(); 

                }
                break;
            case 12 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:73: False
                {
                mFalse(); 

                }
                break;
            case 13 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:79: Real
                {
                mReal(); 

                }
                break;
            case 14 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:84: String
                {
                mString(); 

                }
                break;
            case 15 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:91: Procedure
                {
                mProcedure(); 

                }
                break;
            case 16 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:101: Function
                {
                mFunction(); 

                }
                break;
            case 17 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:110: Returns
                {
                mReturns(); 

                }
                break;
            case 18 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:118: Typedef
                {
                mTypedef(); 

                }
                break;
            case 19 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:126: If
                {
                mIf(); 

                }
                break;
            case 20 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:129: Else
                {
                mElse(); 

                }
                break;
            case 21 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:134: Loop
                {
                mLoop(); 

                }
                break;
            case 22 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:139: While
                {
                mWhile(); 

                }
                break;
            case 23 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:145: Break
                {
                mBreak(); 

                }
                break;
            case 24 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:151: Continue
                {
                mContinue(); 

                }
                break;
            case 25 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:160: Return
                {
                mReturn(); 

                }
                break;
            case 26 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:167: Filter
                {
                mFilter(); 

                }
                break;
            case 27 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:174: Not
                {
                mNot(); 

                }
                break;
            case 28 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:178: And
                {
                mAnd(); 

                }
                break;
            case 29 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:182: Or
                {
                mOr(); 

                }
                break;
            case 30 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:185: Xor
                {
                mXor(); 

                }
                break;
            case 31 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:189: Rows
                {
                mRows(); 

                }
                break;
            case 32 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:194: Columns
                {
                mColumns(); 

                }
                break;
            case 33 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:202: Length
                {
                mLength(); 

                }
                break;
            case 34 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:209: Out
                {
                mOut(); 

                }
                break;
            case 35 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:213: Inp
                {
                mInp(); 

                }
                break;
            case 36 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:217: Tuple
                {
                mTuple(); 

                }
                break;
            case 37 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:223: Stream_state
                {
                mStream_state(); 

                }
                break;
            case 38 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:236: Revserse
                {
                mRevserse(); 

                }
                break;
            case 39 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:245: ID
                {
                mID(); 

                }
                break;
            case 40 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:248: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 41 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:256: WS
                {
                mWS(); 

                }
                break;
            case 42 :
                // /Users/markgalloway/Documents/workspace/DashAB/Dash.g:1:259: NL
                {
                mNL(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\1\25\21\22\4\uffff\1\65\1\66\1\67\2\22\1\72\24\22\1\122\3\22\1"+
        "\126\3\uffff\2\22\1\uffff\1\131\1\132\24\22\1\161\1\uffff\1\162"+
        "\1\163\1\22\1\uffff\2\22\2\uffff\5\22\1\175\5\22\1\u0083\2\22\1"+
        "\u0086\3\22\1\u008a\1\u008b\2\22\3\uffff\3\22\1\u0091\1\22\1\u0093"+
        "\3\22\1\uffff\1\22\1\u0098\1\u0099\2\22\1\uffff\2\22\1\uffff\3\22"+
        "\2\uffff\1\22\1\u00a2\3\22\1\uffff\1\u00a6\1\uffff\2\22\1\u00a9"+
        "\1\22\2\uffff\1\22\1\u00ac\1\u00ae\1\22\1\u00b0\2\22\1\u00b3\1\uffff"+
        "\1\22\1\u00b5\1\u00b6\1\uffff\1\22\1\u00b8\1\uffff\1\u00b9\1\22"+
        "\1\uffff\1\u00bb\1\uffff\1\u00bc\1\uffff\2\22\1\uffff\1\u00bf\2"+
        "\uffff\1\u00c0\2\uffff\1\u00c1\2\uffff\2\22\3\uffff\1\22\1\u00c5"+
        "\1\22\1\uffff\1\22\1\u00c8\1\uffff";
    static final String DFA5_eofS =
        "\u00c9\uffff";
    static final String DFA5_minS =
        "\1\11\1\146\1\157\1\156\1\141\1\157\1\141\1\162\1\141\1\145\1\164"+
        "\1\162\1\154\1\145\1\150\1\157\1\162\1\157\4\uffff\3\60\1\157\1"+
        "\145\1\60\1\144\1\162\1\143\1\154\1\164\1\165\2\160\1\154\1\156"+
        "\1\154\1\141\1\167\1\162\1\157\1\163\1\157\1\156\1\151\1\164\1\60"+
        "\1\164\1\162\1\145\1\60\3\uffff\1\154\1\141\1\uffff\2\60\1\164\1"+
        "\163\1\165\1\162\2\145\1\154\1\163\1\143\1\164\1\154\1\165\1\145"+
        "\1\163\1\145\1\143\1\145\1\160\1\147\1\154\1\60\1\uffff\2\60\1\147"+
        "\1\uffff\1\145\1\153\2\uffff\1\157\1\164\1\151\1\155\1\151\1\60"+
        "\1\144\2\145\1\164\1\145\1\60\2\162\1\60\1\156\1\141\1\145\2\60"+
        "\1\164\1\145\3\uffff\1\166\1\145\1\141\1\60\1\162\1\60\2\156\1\170"+
        "\1\uffff\1\145\2\60\1\151\1\162\1\uffff\1\156\1\163\1\uffff\1\147"+
        "\1\155\1\144\2\uffff\1\150\1\60\1\141\1\162\1\156\1\uffff\1\60\1"+
        "\uffff\1\165\1\163\1\60\1\146\2\uffff\1\157\2\60\1\145\1\60\1\137"+
        "\1\165\1\60\1\uffff\1\154\2\60\1\uffff\1\145\1\60\1\uffff\1\60\1"+
        "\156\1\uffff\1\60\1\uffff\1\60\1\uffff\1\163\1\162\1\uffff\1\60"+
        "\2\uffff\1\60\2\uffff\1\60\2\uffff\1\164\1\145\3\uffff\1\141\1\60"+
        "\1\164\1\uffff\1\145\1\60\1\uffff";
    static final String DFA5_maxS =
        "\1\172\1\156\1\171\1\163\1\145\1\157\1\141\1\171\1\165\1\157\1\164"+
        "\1\162\1\154\1\157\1\150\1\157\1\165\1\157\4\uffff\3\172\1\157\1"+
        "\145\1\172\1\144\1\162\1\143\1\156\1\164\1\165\2\160\1\154\1\156"+
        "\1\154\1\166\1\167\1\162\1\157\1\163\1\157\1\156\1\151\1\164\1\172"+
        "\1\164\1\162\1\145\1\172\3\uffff\1\154\1\141\1\uffff\2\172\2\164"+
        "\1\165\1\162\2\145\1\154\1\163\1\143\1\164\1\154\1\165\1\145\1\163"+
        "\1\151\1\143\1\145\1\160\1\147\1\154\1\172\1\uffff\2\172\1\162\1"+
        "\uffff\1\145\1\153\2\uffff\1\157\1\164\1\151\1\155\1\151\1\172\1"+
        "\144\2\145\1\164\1\145\1\172\2\162\1\172\1\156\1\141\1\145\2\172"+
        "\1\164\1\145\3\uffff\1\166\1\145\1\141\1\172\1\162\1\172\2\156\1"+
        "\170\1\uffff\1\145\2\172\1\151\1\162\1\uffff\1\156\1\163\1\uffff"+
        "\1\147\1\155\1\144\2\uffff\1\150\1\172\1\141\1\162\1\156\1\uffff"+
        "\1\172\1\uffff\1\165\1\163\1\172\1\146\2\uffff\1\157\2\172\1\145"+
        "\1\172\1\137\1\165\1\172\1\uffff\1\154\2\172\1\uffff\1\145\1\172"+
        "\1\uffff\1\172\1\156\1\uffff\1\172\1\uffff\1\172\1\uffff\1\163\1"+
        "\162\1\uffff\1\172\2\uffff\1\172\2\uffff\1\172\2\uffff\1\164\1\145"+
        "\3\uffff\1\141\1\172\1\164\1\uffff\1\145\1\172\1\uffff";
    static final String DFA5_acceptS =
        "\22\uffff\1\47\1\50\1\51\1\52\37\uffff\1\1\1\23\1\2\2\uffff\1\3"+
        "\27\uffff\1\35\3\uffff\1\43\2\uffff\1\34\1\4\26\uffff\1\33\1\42"+
        "\1\36\11\uffff\1\13\5\uffff\1\15\2\uffff\1\37\3\uffff\1\24\1\25"+
        "\5\uffff\1\27\1\uffff\1\5\4\uffff\1\44\1\14\10\uffff\1\26\3\uffff"+
        "\1\7\2\uffff\1\6\2\uffff\1\32\1\uffff\1\31\1\uffff\1\16\2\uffff"+
        "\1\41\1\uffff\1\11\1\12\1\uffff\1\40\1\22\1\uffff\1\21\1\46\2\uffff"+
        "\1\10\1\30\1\20\3\uffff\1\17\2\uffff\1\45";
    static final String DFA5_specialS =
        "\u00c9\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\24\2\uffff\1\24\23\uffff\1\24\17\uffff\12\23\7\uffff\32\22"+
            "\4\uffff\1\22\1\uffff\1\3\1\2\1\5\1\22\1\14\1\10\2\22\1\1\2"+
            "\22\1\15\1\6\1\17\1\20\1\13\1\22\1\11\1\12\1\7\1\22\1\4\1\16"+
            "\1\21\2\22",
            "\1\27\7\uffff\1\26",
            "\1\31\2\uffff\1\32\6\uffff\1\30",
            "\1\34\4\uffff\1\33",
            "\1\35\3\uffff\1\36",
            "\1\37",
            "\1\40",
            "\1\41\2\uffff\1\43\3\uffff\1\42",
            "\1\44\7\uffff\1\46\13\uffff\1\45",
            "\1\47\11\uffff\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\55\11\uffff\1\54",
            "\1\56",
            "\1\57",
            "\1\60\2\uffff\1\61",
            "\1\62",
            "",
            "",
            "",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\17\22\1\64\3\22\1"+
            "\63\6\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\70",
            "\1\71",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\77\1\uffff\1\76",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107\22\uffff\1\110\1\uffff\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\123",
            "\1\124",
            "\1\125",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "",
            "\1\127",
            "\1\130",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\133",
            "\1\134\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\153\3\uffff\1\152",
            "\1\154",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\165\12\uffff\1\164",
            "",
            "\1\166",
            "\1\167",
            "",
            "",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u0084",
            "\1\u0085",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u008c",
            "\1\u008d",
            "",
            "",
            "",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u0092",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "",
            "\1\u0097",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u009a",
            "\1\u009b",
            "",
            "\1\u009c",
            "\1\u009d",
            "",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "",
            "",
            "\1\u00a1",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\u00a7",
            "\1\u00a8",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u00aa",
            "",
            "",
            "\1\u00ab",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\22\22\1\u00ad\7\22",
            "\1\u00af",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u00b1",
            "\1\u00b2",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\u00b4",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\u00b7",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u00ba",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\u00bd",
            "\1\u00be",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "\1\u00c2",
            "\1\u00c3",
            "",
            "",
            "",
            "\1\u00c4",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\u00c6",
            "",
            "\1\u00c7",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( In | By | As | Var | Const | Matrix | Vector | Interval | Integer | Boolean | True | False | Real | String | Procedure | Function | Returns | Typedef | If | Else | Loop | While | Break | Continue | Return | Filter | Not | And | Or | Xor | Rows | Columns | Length | Out | Inp | Tuple | Stream_state | Revserse | ID | INTEGER | WS | NL );";
        }
    }
 

}