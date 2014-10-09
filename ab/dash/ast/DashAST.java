package ab.dash.ast;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.Token;

public class DashAST extends CommonTree { 

    public DashAST() { }
    
    public DashAST(Token t) { 
        super(t);
    }

}
