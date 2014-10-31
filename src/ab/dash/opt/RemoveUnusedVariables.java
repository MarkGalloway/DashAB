package ab.dash.opt;

import java.util.TreeSet;
import ab.dash.DashLexer;
import ab.dash.ast.*;

public class RemoveUnusedVariables {
	TreeSet<Integer> refs;
	boolean debug_mode;
	
	public RemoveUnusedVariables(TreeSet<Integer> refs) {
        this.refs = refs;
        this.debug_mode = false;
    }
    
    public RemoveUnusedVariables(TreeSet<Integer> refs, boolean debug) {
        this.refs = refs;
        this.debug_mode = debug;
    }
    
    private void debug(String msg) {
    	if (debug_mode)
    		System.out.println(msg);
    }
    
    public void optimize(DashAST tree) {
		exec(tree);
	}
	
	protected void exec(DashAST t) {
		switch (t.getToken().getType()) {
		case DashLexer.VAR_DECL: {
			DashAST ID = (DashAST) t.getChild(0);
			if (ID != null) {
				if(ID.symbol != null) {
					if (ID.symbol instanceof VariableSymbol) {
						VariableSymbol s = (VariableSymbol)ID.symbol;
						if (!refs.contains(new Integer(s.id))) {
							debug("line " + ID.getLine() + ": unused " + ID.getText());
							int index = t.getChildIndex();
							DashAST parent = (DashAST) t.getParent();
							if (parent != null) {
								debug("Removing unused variable " + ID.getText() + " : index " + index);
								parent.deleteChild(index);
							}
						}
					}
				}
			}
		}
		default: {
			for (int i = 0; i < t.getChildCount(); i++)
				exec((DashAST) t.getChild(i));
		}
		}
	}
}
