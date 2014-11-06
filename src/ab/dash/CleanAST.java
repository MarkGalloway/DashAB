package ab.dash;

import ab.dash.DashLexer;
import ab.dash.ast.DashAST;

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
				case DashLexer.PROCEDURE_DECL:
				case DashLexer.FUNCTION_DECL:
				{
					if (child.getChildCount() <= 1) {
						delete = true;
						break;
					}
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
