package ab.dash;

import org.antlr.runtime.CommonToken;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;
import ab.dash.ast.SymbolTable;
import ab.dash.ast.VariableSymbol;

public class CleanAST {
    public CleanAST() {
    }
    
    public void clean(DashAST tree) {
		exec(tree);
	}
	
	protected void exec(DashAST t) {
		for (int i = t.getChildCount() - 1; i >= 0; i--) {
			DashAST child = (DashAST) t.getChild(i);
			boolean delete = false;
			switch (child.getToken().getType()) {
				case DashLexer.TYPEDEF:
				case DashLexer.DECL_INSTREAM:
				case DashLexer.DECL_OUTSTREAM: {
					delete = true;
					break;
				}
			}
			
			if (!delete)
				exec(child);
			else {
				t.deleteChild(i);
			}
		}
	}
}
