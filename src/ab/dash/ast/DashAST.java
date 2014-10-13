package ab.dash.ast;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.Token;
import org.antlr.runtime.RecognitionException;

public class DashAST extends CommonTree {
    private static int LLVMIR_ID_COUNTER = 0;

    public Scope scope;
    public Symbol symbol;
    public Type evalType;
    public Type promoteToType;
    public int llvmResultID;

    public DashAST () {
        init();
    }

    public DashAST(Token t) { 
        super(t); 
        init();
    }

   private void init() {
       LLVMIR_ID_COUNTER++;
       llvmResultID = LLVMIR_ID_COUNTER;
   }

    public String toString() {
        String s = super.toString();
        if ( evalType !=null ) {
            String annot = evalType.getName();
            if ( promoteToType!=null ) {
                annot += ":"+promoteToType.getName();
            }
            return s+'<'+annot+'>';
        }
        return s;
    }

    public static TreeAdaptor dashAdaptor = new CommonTreeAdaptor() {
        public Object create(Token token) {
            return new DashAST(token);
        }
        public Object dupNode(Object t) {
            if ( t==null ) {
                return null;
            }
            return create(((DashAST)t).token);
        }
        public Object errorNode(TokenStream input, Token start, Token stop,
                                RecognitionException e)
        {
            DashErrorNode t = new DashErrorNode(input, start, stop, e);
            return t;
        }
    };
}
